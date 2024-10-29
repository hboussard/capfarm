package fr.inrae.act.bagap.capfarm.model.economic.csp;

import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.search.loop.monitors.SMF;
import org.chocosolver.solver.search.strategy.ISF;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.search.strategy.strategy.StrategiesSequencer;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.solver.variables.Variable;
import org.chocosolver.util.tools.ArrayUtils;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.csp.CoverAllocator;
import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.economic.EconomicProfil;
import fr.inrae.act.bagap.capfarm.model.economic.ManagmentProfil;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class EconomicCoverAllocationProblem extends CoverAllocationProblem {
	
	private IntVar[] coverAreas;
	
	private EconomicProfil economicProfil;
	
	private ManagmentProfil managmentProfil;
	
	public EconomicCoverAllocationProblem(CoverAllocator allocator, Instant t, EconomicProfil ep, ManagmentProfil mp) {
		super(allocator, t);
		this.economicProfil = ep;
		this.managmentProfil = mp;
	}
	
	public EconomicProfil getEconomicProfil(){
		return economicProfil;
	}
	
	public ManagmentProfil getManagmentProfil(){
		return managmentProfil;
	}
	
	public IntVar[] coverAreas(){
		return coverAreas;
	}
	
	@Override
	protected void buildVariables(){
		
		super.buildVariables();
		
		coverAreas = new IntVar[covers().size()];
	}
	
	@Override
	protected void structureInitialisation(){
		
		super.structureInitialisation();
		
		int totarea = allocator().totalParcelsArea();
		int[] areas = new int[allocator().parcels().size()];
		int[] counts = new int[allocator().parcels().size()];
		for(int i=0; i<areas.length; i++){
			areas[i] = 0;
			counts[i] = 1;
		}
		for(Parcel p : parcels().keySet()){
			//System.out.println(p.getArea()+" "+p.getArea()/100);
			areas[parcels().get(p)] = p.getArea()/100;  // surface en ares
			//areas[parcels().get(p)] = p.getArea();  // surface en m�
		}
					
		for(CoverUnit c : covers().keySet()){
			int ic = covers().get(c);
			coverAreas[ic] = VF.bounded("a_cvs_"+ic, 0, totarea, solver());
			solver().post(ICF.scalar(coversAndParcels(ic), areas, coverAreas[ic]));
		}
		
	}
	
	@Override
	protected void configureSearch() {
		long r = new Double(Math.random() * 1000000000.0).longValue();
		
		AbstractStrategy<?> as1 = ISF.random(ArrayUtils.append(coversAndParcels), r);
		AbstractStrategy<?> as2 = ISF.random(parcelsImplantedCoverContinue, r);
		solver.set(ISF.lastConflict(solver, new StrategiesSequencer(as1, as2)));

		//SMF.limitFail(solver, 1000);
		SMF.limitTime(solver, 100);
	}
	
	@Override
	protected boolean solve() {
		if(solver.findSolution()) {
			for(Variable v : solver.getVars()){
				if(v.getName().equalsIgnoreCase("profit")){
					allocator().getTerritory().getAttribute("profit").setValue(time(), ((IntVar) v).getValue());
					//System.out.println("profit = "+((IntVar) v).getValue());
				}
				if(v.getName().equalsIgnoreCase("sum")){
					//allocator().getTerritory().getAttribute("profit").setValue(time(), ((IntVar) v).getValue());
					//System.out.println("sum = "+((IntVar) v).getValue()/100.0+" euros");
				}
				for(CoverUnit c : covers().keySet()){
					int ic = covers().get(c);
					if(v.getName().equalsIgnoreCase("a_cvs_"+ic)){
						//System.out.println("a_cvs_"+ic+" = "+((IntVar) v).getValue());
						//System.out.println(((IntVar) v).getValue());
					}
				}
				if(v.getName().equalsIgnoreCase("coeffvar")){
					//allocator().getTerritory().getAttribute("profit").setValue(time(), ((IntVar) v).getValue());
					//System.out.println("coeffvar = "+((IntVar) v).getValue()/10000.0);
					//System.out.println("coeffvar = "+((IntVar) v).getValue());
				}
				if(v.getName().equalsIgnoreCase("profit")){
					//System.out.println("profit = "+((IntVar) v).getValue());
				}
				
				for(int h=0; h<10; h++){
					if(v.getName().equalsIgnoreCase("profit"+h)){
						//System.out.println("profit"+h+" = "+((IntVar) v).getValue());
					}
					if(v.getName().equalsIgnoreCase("profit"+h+"div10000")){
						//System.out.println("profit"+h+"div10000 = "+((IntVar) v).getValue());
					}
					if(v.getName().equalsIgnoreCase("profit"+h+"carre")){
						//System.out.println("profit"+h+"carre = "+((IntVar) v).getValue());
					}
				}
				
				if(v.getName().equalsIgnoreCase("profitdiv1000")){
					//System.out.println("profitdiv1000 = "+((IntVar) v).getValue());
				}
				if(v.getName().equalsIgnoreCase("profitcarre")){
					//System.out.println("profitcarre = "+((IntVar) v).getValue());
				}
				if(v.getName().equalsIgnoreCase("sum")){
					//System.out.println("sum = "+((IntVar) v).getValue());
				}
				if(v.getName().equalsIgnoreCase("moyenne")){
					//System.out.println("moyenne = "+((IntVar) v).getValue());
				}
				if(v.getName().equalsIgnoreCase("moyennecarre")){
					//System.out.println("moyennecarre = "+((IntVar) v).getValue());
				}
				if(v.getName().equalsIgnoreCase("sumprofitcarres")){
					//System.out.println("sumprofitcarres = "+((IntVar) v).getValue());
				}
				if(v.getName().equalsIgnoreCase("sumprofitcarressurtaille")){
					//System.out.println("sumprofitcarressurtaille = "+((IntVar) v).getValue());
				}
				if(v.getName().equalsIgnoreCase("variance")){
					//System.out.println("variance = "+((IntVar) v).getValue());
				}
				if(v.getName().equalsIgnoreCase("racinevariance")){
					//System.out.println("racinevariance = "+((IntVar) v).getValue());
				}
				if(v.getName().equalsIgnoreCase("ecarttype")){
					//System.out.println("ecarttype = "+((IntVar) v).getValue());
				}
				if(v.getName().equalsIgnoreCase("ecarttype10000")){
					//System.out.println("ecarttype10000 = "+((IntVar) v).getValue());
				}
				
			}
			for(Parcel p : parcels().keySet()){
				int ip = parcels().get(p);
				if(((IntVar) parcelsImplantedCoverContinue[ip]).getValue() == 0){
					for(Cover c : covers().keySet()){
						int ic = covers().get(c);	
						if(((IntVar) coversAndParcels[ic][ip]).getValue() == 1){
							p.getAttribute("cover").setValue(time(), c);
							//System.out.println(p.getId()+";"+c+";"+p.getArea());
							break;
						}
					}
				}
			}
			//System.out.println(((IntVar) solver.profit).getValue());
			return true;
		}
		return false;
	}
	
}
