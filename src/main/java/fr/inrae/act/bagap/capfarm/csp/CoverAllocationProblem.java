package fr.inrae.act.bagap.capfarm.csp;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.LCF;
import org.chocosolver.solver.search.loop.monitors.SMF;
import org.chocosolver.solver.search.strategy.ISF;
import org.chocosolver.solver.search.strategy.selectors.VariableSelectorWithTies;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMin;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.search.strategy.strategy.StrategiesSequencer;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.util.tools.ArrayUtils;

import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverGroup;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.constraint.CoverAllocationConstraint;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.composition.DynamicAttribute;
import fr.inrae.act.bagap.apiland.core.composition.TemporalValue;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class CoverAllocationProblem extends AbstractProblem {

	private CoverAllocator allocator;

	private Instant t;
	
	private Instant[] starts;
	
	protected BoolVar[][] coversAndParcels;
	
	private BoolVar[][] parcelsAndCovers;
	
	//private BoolVar[][] groupsAndParcels;
	
	protected BoolVar[] parcelsImplantedCoverContinue;
	
	private Map<CoverUnit, Integer> covers;
	
	private Map<Parcel, Integer> parcels;
	
	private Map<CoverGroup, Integer> groups;
	
	private TemporalValue<CoverUnit>[] previous, first;
	
	public CoverAllocationProblem(CoverAllocator allocator, Instant t) {
		this.allocator = allocator;
		this.t = t;
	}
	
	protected Instant time(){
		return t;
	}

	@Override
	protected void createSolver() {
		solver = new Solver("allocateur de cultures annuelles");	
	}
	
	@Override
	protected void buildModel() {
		buildVariables(); 			// construction des variables
		structureInitialisation();	// initialisation de la structure de donn�es
		postConstraints();
	}

	@SuppressWarnings("unchecked")
	protected void buildVariables(){
		
		// les cultures et identifiants attaches
		covers = new TreeMap<CoverUnit, Integer>();
		int ic = 0;
		for (CoverUnit c : allocator.coverUnits()) {
			covers.put(c, ic++);
		}
		
		groups = new TreeMap<CoverGroup, Integer>();
		int ig = 0;
		for (CoverGroup g : allocator.coverGroups()) {
			groups.put(g, ig++);
		}
		
		// les parcelles et identifiants attaches
		parcels = new TreeMap<Parcel, Integer>();
		int ip = 0;
		for (Parcel p : allocator.parcels()) {
			parcels.put(p, ip++);
		}
		
		// les cultures par parcelle
		parcelsAndCovers = new BoolVar[allocator.parcels().size()][allocator.coverUnits().size()];
		// les parcelles par culture
		coversAndParcels = new BoolVar[allocator.coverUnits().size()][allocator.parcels().size()];
		// les parcelles par groupe de culture
		//groupsAndParcels = new BoolVar[farm.coverGroups().size()][farm.parcels().size()];		
		// les durees d'implantation par parcelle
		parcelsImplantedCoverContinue = new BoolVar[allocator.parcels().size()];
		
		previous = new TemporalValue[allocator.parcels().size()];
		first = new TemporalValue[allocator.parcels().size()];
		starts = new Instant[allocator.parcels().size()];
		for (Parcel p : parcels.keySet()) {
			ip = parcels.get(p);
			previous[ip] = ((DynamicAttribute<CoverUnit>) p.getAttribute("cover")).getLast();
			first[ip] = ((DynamicAttribute<CoverUnit>) p.getAttribute("cover")).getFirst();
			starts[ip] = ((DynamicAttribute<CoverUnit>) p.getAttribute("cover")).getTime().start();
		}
	}
	
	protected void structureInitialisation(){
		for (Parcel p : parcels.keySet()) {
			int ip = parcels.get(p);
			
			// gestion des CoverUnit
			for(CoverUnit c : covers.keySet()){
				int ic = covers.get(c);
				coversAndParcels[ic][ip] = (BoolVar) VF.bool("c_"+ic+"_p_"+ip, solver);
				parcelsAndCovers[ip][ic] = coversAndParcels[ic][ip];
			}
			
			// contrainte generale "chaque parcelle a une et une seule culture allouee"
			solver.post(ICF.sum(parcelsAndCovers[ip], VF.one(solver)));
			
			// gestion des CoverGroup
			for(CoverGroup g : groups.keySet()){
				//int ig = groups.get(g);
				//groupsAndParcels[ig][ip] = VF.bool("g_"+ig+"_p_"+ip, solver);
				BoolVar[] max = new BoolVar[g.size()];
				int i = 0;
				for(CoverUnit c : g){
					if(covers.containsKey(c)){
						max[i] = coversAndParcels[covers.get(c)][ip];
					}else{
						max[i] = VF.zero(solver);
					}
					i++;
				}
				//solver.post(ICF.maximum(groupsAndParcels[ig][ip], max));
			}
			
			// gestion de la continuit� des cultures
			parcelsImplantedCoverContinue[ip] = VF.bool("time_p_"+ip, solver);
			if(previous[ip] != null){
				LCF.ifThen(parcelsImplantedCoverContinue[ip], 
						ICF.arithm(coversAndParcels[covers.get(previous[ip].getValue())][ip], "=", 1));
			}else{
				solver.post(ICF.arithm(parcelsImplantedCoverContinue[ip], "=", 0));
			}
		}
	}
	
	protected void postConstraints() {
		for(CoverAllocationConstraint<?,?> cst : allocator.getConstraints()){
			if(!cst.checkOnly()){
				cst.post(this);
			}
		}
	}
	
	@Override
	protected void configureSearch() {
		
		long r = new Double(Math.random() * 1000000000.0).longValue();
		//System.out.println(farm.isMemory()+" "+t.year()+" "+r);
		AbstractStrategy<?> as1 = ISF.random(ArrayUtils.append(coversAndParcels), r);
		AbstractStrategy<?> as2 = ISF.random(parcelsImplantedCoverContinue, r);
		
		solver.set(ISF.lastConflict(solver, new StrategiesSequencer(as1, as2)));
		/*
		solver.set(ISF.custom(
			    new VariableSelectorWithTies(new FirstFail()),
			    new IntDomainMin(), ArrayUtils.append(ArrayUtils.append(coversAndParcels))));
		 */
		//SearchMonitorFactory.limitTime(solver, 500);
		SMF.limitFail(solver, 500);
	}
	
	@Override
	protected boolean solve() {
		int ip, ic;
		
		if(solver.findSolution()) {
			for(Parcel p : parcels.keySet()){
				ip = parcels.get(p);
				if(((IntVar) parcelsImplantedCoverContinue[ip]).getValue() == 0){
					for(Cover c : covers.keySet()){
						ic = covers.get(c);	
						if(((IntVar) coversAndParcels[ic][ip]).getValue() == 1){
							//System.out.println("1 "+p.getId()+";"+c+";"+p.getArea());
							p.getAttribute("cover").setValue(t, c);
							//System.out.println(p.getAttribute("cover").getValue(t));
							//p.getAttribute("cov").setValue(t, c.getCode());
							break;
						}
					}
				}
			}
			return true;
		}
		return false;
	}
	
	public Solver solver(){
		return solver;
	}
	
	public CoverAllocator allocator(){
		return allocator;
	}

	public Map<Parcel, Integer> parcels(){
		return parcels;
	}
	
	public Map<CoverUnit, Integer> covers(){
		return covers;
	}
	
	public Set<CoverUnit> historicalCovers(){
		return allocator.historicalCovers();
	}
	
	public Map<CoverGroup, Integer> groups(){
		return groups;
	}
	
	public BoolVar[] coversAndParcels(int ic){
		return coversAndParcels[ic];
	}
	
	public BoolVar coversAndParcels(int ic, int ip){
		return coversAndParcels[ic][ip];
	}

	/*public IntVar groupsAndParcels(int ig, int ip) {
		return groupsAndParcels[ig][ip];
	}*/

	public BoolVar parcelsImplantedCoverContinue(int ip){
		return parcelsImplantedCoverContinue[ip];
	}
	
	public Instant getTime(){
		return t;
	}
	
	public TemporalValue<CoverUnit> previous(int ip){
		return previous[ip];
	}
	
	public TemporalValue<CoverUnit> first(int ip){
		return first[ip];
	}
	
	public Instant starts(int ip){
		return starts[ip];
	}

}
