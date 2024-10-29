package fr.inrae.act.bagap.capfarm.model.economic.csp;

import java.util.Set;

import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.loop.monitors.SMF;
import org.chocosolver.solver.search.strategy.ISF;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.search.strategy.strategy.StrategiesSequencer;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.solver.variables.VariableFactory;
import org.chocosolver.util.tools.ArrayUtils;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.csp.CoverAllocator;
import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.economic.EconomicProfil;
import fr.inrae.act.bagap.capfarm.model.economic.MaeliaManagmentProfil;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class OptimizeEconomicCoverAllocationProblem extends CoverAllocationProblem {

	private EconomicProfil ep;
	
	private MaeliaManagmentProfil mp;
	
	private IntVar profit, unprofit, work, sumDiffProfitsDiv1000AuCarre, nbcombinations, variance, distance;
	
	private IntVar[] coverAreas, profits, diffProfitsDiv1000AuCarre;
	
	private BoolVar[] allocatedCovers;
	
	public OptimizeEconomicCoverAllocationProblem(CoverAllocator allocator, Instant t, EconomicProfil ep, MaeliaManagmentProfil mp) {
		super(allocator, t);
		this.ep = ep;
		this.mp = mp;
	}

	public EconomicProfil getEconomicProfil(){
		return ep;
	}
	
	public IntVar[] coverAreas(){
		return coverAreas;
	}
	
	@Override
	protected void buildVariables(){
		
		super.buildVariables();
	
		profit = VF.bounded("profit", 0, 2000000000, solver); 
		unprofit = VF.bounded("unprofit", -2000000000, 0, solver);
		
		work = VF.bounded("work", 0, VariableFactory.MAX_INT_BOUND, solver);
		
		coverAreas = new IntVar[covers().size()];
		
	}
	
	@Override
	protected void structureInitialisation(){
		
		super.structureInitialisation();
		
		int totarea = allocator().totalParcelsArea();
		System.out.println("total area "+totarea);
		int[] areas = new int[allocator().parcels().size()];
		for(int i=0; i<areas.length; i++){
			areas[i] = 0;
		}
		
		for(Parcel p : parcels().keySet()){
			//areas[parcels().get(p)] = p.getArea();  // surface en m�
			areas[parcels().get(p)] = p.getArea()/100;  // surface en ares
			//areas[parcels().get(p)] = p.getArea()/10000;  // surface en hectares
		}			
		for(CoverUnit c : covers().keySet()){
			int ic = covers().get(c);
			coverAreas[ic] = VF.bounded("a_cvs_"+ic, 0, totarea, solver());
			solver().post(ICF.scalar(coversAndParcels(ic), areas, coverAreas[ic])); 
		}
		
		// ajout du profit
		solver().post(ICF.arithm(profit, "+", unprofit, "=", 0));
		solver().post(ICF.scalar(coverAreas, ep.profits(), profit));
		
		
		// ajout du travail
		solver().post(ICF.scalar(coverAreas, mp.works(), work));
		
		
		// ajout variabilite du profit
		int[][] historicalProfits = ep.getHistoricalProfits();
		int nbCombinations = historicalProfits[0].length;
		int nbCovers = coverAreas().length;
		
		profits = new IntVar[nbCombinations];
		
		diffProfitsDiv1000AuCarre = new IntVar[nbCombinations];
		
		IntVar divisor = VF.fixed(1000, solver());
		IntVar profith, diffProfith, diffProfithDiv1000, diffProfithDiv1000AuCarre;
		for(int h=0; h<nbCombinations; h++){
			
			profith = VF.bounded("profit"+h, 0, VariableFactory.MAX_INT_BOUND, solver());
			
			int[] hp = new int[nbCovers];
			for(int s=0; s<nbCovers; s++){
				hp[s] = historicalProfits[s][h];
			}
			
			solver().post(ICF.scalar(coverAreas(), hp, profith));
			
			profits[h] = profith;
			
			diffProfith = VF.bounded("diffprofit"+h, 0, 100000000, solver());
			
			solver().post(ICF.distance(profit, profith, "=", diffProfith));
			
			diffProfithDiv1000 = VF.bounded("diffprofit"+h+"div1000", 0, 100000000, solver());
			
			solver().post(ICF.eucl_div(diffProfith, divisor, diffProfithDiv1000));
			
			
			diffProfithDiv1000AuCarre = VF.bounded("diffprofit"+h+"div1000aucarre", 0, VariableFactory.MAX_INT_BOUND, solver());
			
			solver().post(ICF.square(diffProfithDiv1000AuCarre, diffProfithDiv1000));
			
			diffProfitsDiv1000AuCarre[h] = diffProfithDiv1000AuCarre;	
		}
		
		sumDiffProfitsDiv1000AuCarre = VF.bounded("sumdiffprofitdiv1000aucarre", 0, VariableFactory.MAX_INT_BOUND, solver());
		solver().post(ICF.sum(diffProfitsDiv1000AuCarre, sumDiffProfitsDiv1000AuCarre));
		
		nbcombinations = VF.bounded("nbcombinations", nbCombinations, nbCombinations, solver());
		
		variance = VF.bounded("variance", 0, 10000000, solver()); 
		solver().post(ICF.eucl_div(sumDiffProfitsDiv1000AuCarre, nbcombinations, variance));
		/*
		//ecarttype = VF.bounded("ecarttype", 0, 10000, solver()); 
		
		//solver().post(ICF.square(variance, ecarttype)); // marche pas car ne g�r eque des entiers e tle r�sultat doit �tre exactement �gal
		*/
		
		//ajout distance entre cultures
		Set<CoverUnit> historicalCovers = historicalCovers();
		
		//Map<CoverUnit, Map<CoverUnit, Integer>> distanceCovers = mp.getDistanceCovers();
		int[][] distanceCovers = mp.getDistanceCovers();
		
		if(distanceCovers != null){
			int[] expCovers = new int[covers().size()];
			
			for(CoverUnit cu1  : covers().keySet()){
				int min = 10;
				for(CoverUnit cu2  : covers().keySet()){
					if(historicalCovers.contains(cu2)){
						//min = Math.min(min, distanceCovers.get(cu2).get(cu1));
						min = Math.min(min, distanceCovers[covers().get(cu2)][covers().get(cu1)]);
					}
				}
				expCovers[covers().get(cu1)] = min;
				
				//System.out.println(cu1+" "+min);
			}
			
			allocatedCovers = new BoolVar[covers().size()];
			for(int ic=0; ic<covers().size(); ic++){
				BoolVar ac = VF.bool("alloc"+ic, solver());
				Constraint ct = ICF.arithm(coverAreas[ic], ">", 0);
				solver().post(ICF.arithm(ac , "=", ct.reif()));
				allocatedCovers[ic] = ac;
			}
			
			distance = VF.bounded("distancecovers", 0, covers().size()*10, solver());
			solver().post(ICF.scalar(allocatedCovers, expCovers, distance));
		}
		
	}
	
	@Override
	protected void configureSearch() {
		long r = new Double(Math.random() * 1000000000.0).longValue();
		
		AbstractStrategy<?> as1 = ISF.random(ArrayUtils.append(coversAndParcels), r);
		AbstractStrategy<?> as2 = ISF.random(parcelsImplantedCoverContinue, r);
		solver.set(ISF.lastConflict(solver, new StrategiesSequencer(as1, as2)));

		//SMF.limitFail(solver, 100000);
		SMF.limitTime(solver, 1000);
	}
	
	@Override
	protected boolean solve() {
		int ip, ic;
				
		//solver.findOptimalSolution(ResolutionPolicy.MAXIMIZE, profit);
		//solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, unprofit);
		//solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, work);
		//solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, variance);
		//solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, distance);
		solver.findParetoFront(ResolutionPolicy.MINIMIZE, unprofit, work, variance);
		//solver.findParetoFront(ResolutionPolicy.MINIMIZE, unprofit, work, variance, distance);
		if(solver.findSolution()){
			try {
				solver.restoreLastSolution();
				//System.out.println(time().year());
				System.out.println("profit = "+profit.getValue()/100+" euros"); // profit total de l'EA, 
																				// division par 100 pour revenir � l'hectare
				//allocator().getTerritory().getAttribute("profit").setValue(time(), profit.getValue());
				
				//System.out.println("work = "+work.getValue()/1200+" jours"); 	// quantit� de travail en jours, 
																				// division par 100 pour revenir � l'hectare, 
																				// division par 12 pour revenir au nombre d'heures de travail par jour
				
				//System.out.println("work = "+work.getValue()/72000+" jours"); 	// quantit� de travail en jours, 
																				// division par 100 pour revenir � l'hectare, 
																				// division par 720 (60 minutes * 12 heures) au nombre d'heures de travail par jour
				
				//allocator().getTerritory().getAttribute("work").setValue(time(), work.getValue());
				
				System.out.println("coeff var profit = "+1000.0*Math.sqrt(variance.getValue())/(profit.getValue()/100.0)); // coefficient de variation du profit,
																															// multipli� pour r�int�grer le divisor (ici 1000)
				
				//System.out.println("difficult� � int�grer les couverts = "+distance.getValue());
				/*
				System.out.println(allocatedCovers[0]);
				System.out.println(allocatedCovers[1]);
				System.out.println(allocatedCovers[2]);
				System.out.println(allocatedCovers[3]);
				System.out.println(allocatedCovers[4]);
				System.out.println(allocatedCovers[5]);
				*/
				
				/*
				for(CoverUnit c : covers().keySet()){
					int icc = covers().get(c);
					System.out.println(c+" "+coverAreas[icc].getValue());
				}
				*/
				
				//System.out.println("travail = "+work.getValue());
				//int[] areas = new int[coverAreas.length];
				//int index = 0;
				//for(CoverUnit c : covers().keySet()){
				//	int it = covers().get(c);
				//	System.out.println(c+" "+coverAreas[it].getValue()+" "+coverCounts[it].getValue());
				//	areas[index++] = coverAreas[it].getValue();
				//}
				//ep.display(areas);
				for(Parcel p : parcels().keySet()){
					ip = parcels().get(p);
					if(((IntVar) parcelsImplantedCoverContinue[ip]).getValue() == 0){
						for(Cover c : covers().keySet()){
							ic = covers().get(c);	
							if(((IntVar) coversAndParcels(ic, ip)).getValue() == 1){
								p.getAttribute("cover").setValue(time(), c);
								//p.getAttribute("cov").setValue(t, c.getCode());
								break;
							}
						}
					}
				}
				return true;
			} catch (ContradictionException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
}
