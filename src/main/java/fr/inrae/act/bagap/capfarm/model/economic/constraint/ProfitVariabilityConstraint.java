package fr.inrae.act.bagap.capfarm.model.economic.constraint;

import java.util.Set;

import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.constraint.ConstraintMode;
import fr.inrae.act.bagap.capfarm.model.constraint.ConstraintType;
import fr.inrae.act.bagap.capfarm.model.constraint.CoverAllocationConstraint;
import fr.inrae.act.bagap.capfarm.model.domain.Domain;
import fr.inrae.act.bagap.capfarm.model.economic.EconomicProfil;
import fr.inrae.act.bagap.capfarm.model.economic.csp.EconomicCoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class ProfitVariabilityConstraint extends CoverAllocationConstraint<Integer, Integer>{

	private static final long serialVersionUID = 1L;
	
	private EconomicProfil ep;
	
	private int paramDiv = 10000;
	
	// coef de domaine positionn� � 100
	
	public ProfitVariabilityConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> parcels, Domain<Integer, Integer> domain) {
		super(code, checkOnly, ConstraintType.ProfitVariability, mode, covers, parcels, domain);
	}
	
	@Override
	public void post(CoverAllocationProblem cap) {
		
		EconomicCoverAllocationProblem ecap = (EconomicCoverAllocationProblem) cap;
		
		ep = ecap.getEconomicProfil();
		
		IntVar profit = VF.bounded("profit", 0, 100000000, ecap.solver()); 
		ecap.solver().post(ICF.scalar(ecap.coverAreas(), ep.profits(), profit));
		/*
		int[] epp = ep.profits();
		System.out.println("profit");
		for(int i=0; i<9; i++){
			System.out.println(epp[i]);
		}*/
		
		int[][] historicalProfits = ep.getHistoricalProfits();
		int nbCombinations = historicalProfits[0].length;
		int nbCovers = ecap.coverAreas().length;
		
		IntVar[] profits = new IntVar[nbCombinations+1];
		
		IntVar profitdiv10000 = VF.bounded("profitdiv10000", 0, 10000, ecap.solver()); 
		IntVar divisor = VF.bounded("divisor", paramDiv, paramDiv, ecap.solver()); 
		ecap.solver().post(ICF.eucl_div(profit, divisor, profitdiv10000));
		
		//IntVar profitdiv1000 = VF.bounded("profitdiv1000", 0, 90000000, ecap.solver()); 
		//ecap.solver().post(ICF.times(profitdiv1000, paramDiv, profit));
		
		profits[0] = profitdiv10000;
		
		IntVar[] profitcarres = new IntVar[nbCombinations+1];
		IntVar profitcarre = VF.bounded("profitcarre", 0, 10000000, ecap.solver()); 
		ecap.solver().post(ICF.square(profitcarre, profitdiv10000));
		profitcarres[0] = profitcarre;
		
		IntVar profith, profithdiv10000, profithcarre;
		for(int h=0; h<nbCombinations; h++){
			
			profith = VF.bounded("profit"+h, 0, 100000000, ecap.solver());
			
			int[] hp = new int[nbCovers];
			for(int s=0; s<nbCovers; s++){
				hp[s] = historicalProfits[s][h];
			}
			/*
			System.out.println("profit"+h);
			for(int i=0; i<9; i++){
				System.out.println(hp[i]);
			}*/
			
			ecap.solver().post(ICF.scalar(ecap.coverAreas(), hp, profith));
			
			profithdiv10000 = VF.bounded("profit"+h+"div10000", 0, 10000, ecap.solver()); 
			//ecap.solver().post(ICF.times(profithdiv1000, paramDiv, profith));
			ecap.solver().post(ICF.eucl_div(profith, divisor, profithdiv10000));
			
			profits[h+1] = profithdiv10000;
			
			profithcarre = VF.bounded("profit"+h+"carre", 0, 10000000, ecap.solver());
			ecap.solver().post(ICF.square(profithcarre, profithdiv10000));
			profitcarres[h+1] = profithcarre;
		}
			
		IntVar sum = VF.bounded("sum", 0, 100000, ecap.solver());
		ecap.solver().post(ICF.sum(profits, sum));
		
		IntVar nbcombinationsplusone = VF.bounded("nbcover", nbCombinations+1, nbCombinations+1, ecap.solver());
		
		IntVar moyenne = VF.bounded("moyenne", 0, 10000, ecap.solver()); 
		//ecap.solver().post(ICF.times(moyenne, nbCombinations+1, sum));
		ecap.solver().post(ICF.eucl_div(sum, nbcombinationsplusone, moyenne));
		
		IntVar moyennecarre = VF.bounded("moyennecarre", 0, 10000000, ecap.solver()); 
		ecap.solver().post(ICF.square(moyennecarre, moyenne));
		
		IntVar sumprofitcarres = VF.bounded("sumprofitcarres", 0, 100000000, ecap.solver());
		ecap.solver().post(ICF.sum(profitcarres, sumprofitcarres));
		
		IntVar sumprofitcarressurtaille = VF.bounded("sumprofitcarressurtaille", 0, 10000000, ecap.solver()); 
		ecap.solver().post(ICF.eucl_div(sumprofitcarres, nbcombinationsplusone, sumprofitcarressurtaille));
		
		IntVar variance = VF.bounded("variance", 0, 100000000, ecap.solver()); 
		ecap.solver().post(ICF.distance(sumprofitcarressurtaille, moyennecarre, "=", variance));
		
		//IntVar racinevariance = VF.bounded("racinevariance", 0, 10000, ecap.solver()); 
		IntVar powecarttype = VF.bounded("powecarttype", 0, 100000000, ecap.solver()); 
		ecap.solver().post(ICF.distance(powecarttype, variance, "<", 1000));
		
		IntVar ecarttype = VF.bounded("ecarttype", 0, 10000, ecap.solver()); 
		//ecap.solver().post(ICF.distance(racinevariance, ecarttype, "<", 10));
		
		//ecap.solver().post(ICF.square(variance, ecarttype));
		//ecap.solver().post(ICF.eucl_div(variance, racinevariance, ecarttype));
		ecap.solver().post(ICF.square(powecarttype, ecarttype));		
		
		IntVar ecarttype10000 = VF.bounded("ecarttype10000", 0, 100000000, ecap.solver());
		ecap.solver().post(ICF.times(ecarttype, 10000, ecarttype10000));
		
		IntVar coeffvar = VF.bounded("coeffvar", 0, 100000, ecap.solver());
		//ecap.solver().post(ICF.times(coeffvar, moyenne, ecarttype10000));
		ecap.solver().post(ICF.eucl_div(ecarttype10000, moyenne, coeffvar));
		/*
		switch(mode()){
		case ONLY :
			post(ecap, domain(), coeffvar);
			break;
		case NEVER : 
			post(ecap, domain().inverse(), coeffvar);
			break;
		default : throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());
		}
		*/
	}
	
	private void post(CoverAllocationProblem cap, Domain<Integer, Integer> domain, IntVar coeffvar){
		cap.solver().post(domain.postIntVar(coeffvar));
	}

	@Override
	public boolean check(Instant start, Instant end, boolean verbose){
		boolean ok = true;
		/*
		StringBuilder sb = new StringBuilder();
		double supermin = Double.MAX_VALUE;
		double supermax = Double.MIN_VALUE;
		Delay d = new YearDelay(1);
		CoverUnit c;
		for(Instant t=start; t.isBefore(end) || t.equals(end); t=d.next(t)){
			double profit = 0;
			for(Parcel p : location()){
				c = (CoverUnit) p.getAttribute("cover").getValue(t);
				if(covers().contains(c)){
					System.out.println(p.getId()+";"+c+";"+p.getArea()+";"+ep.profit(c, p.getArea())+" euros");
					//System.out.println(ep.profit(c, p.getArea()));
					profit += ep.profit(c, p.getArea());
				}
			}
			
			System.out.println(t.year()+" : profit = "+new Double(profit).intValue()+" euros");
			supermin = Math.min(supermin, profit);
			supermax = Math.max(supermax, profit);
			
			switch(mode()){
			case ONLY :
				if(!domain().accept(new Double(profit*100.0).intValue())){
					ok = false;
					if(verbose){
						sb.append("BAD : profit  = "+profit+" not in domain "+domain()+"\n");
					}else{
						return ok;
					}
				}
				break;
			case NEVER : 
				if(domain().accept(new Double(profit*100.0).intValue())){
					ok = false;
					if(verbose){
						sb.append("BAD : profit = "+profit+" not in domain "+domain()+"\n");
					}else{
						return ok;
					}
				}
				break;
			default : 
				throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());
			}
		}
		if(verbose){
			if(ok){
				if(supermin == supermax){
					sb.append("GOOD : cover "+covers().toString()+" has profit = "+new Double(supermin).intValue()+" euros");
				}else{
					sb.append("GOOD : cover "+covers().toString()+" has profit between min = "+new Double(supermin).intValue()+" and max = "+new Double(supermax).intValue());
				}
			}
			System.out.println(sb.toString());
		}
		*/
		return ok;	
	}
	
	
}

