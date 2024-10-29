package fr.inrae.act.bagap.capfarm.model.economic.constraint;

import java.util.Set;

import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.constraint.ConstraintMode;
import fr.inrae.act.bagap.capfarm.model.constraint.ConstraintType;
import fr.inrae.act.bagap.capfarm.model.constraint.CoverAllocationConstraint;
import fr.inrae.act.bagap.capfarm.model.domain.Domain;
import fr.inrae.act.bagap.capfarm.model.economic.EconomicProfil;
import fr.inrae.act.bagap.capfarm.model.economic.csp.EconomicCoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.core.time.delay.Delay;
import fr.inrae.act.bagap.apiland.core.time.delay.YearDelay;

public class ProfitConstraint extends CoverAllocationConstraint<Integer, Integer>{

	private static final long serialVersionUID = 1L;
	
	private EconomicProfil ep;
	
	// coef de domaine positionn� � 100
	
	public ProfitConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> parcels, Domain<Integer, Integer> domain) {
		super(code, checkOnly, ConstraintType.Profit, mode, covers, parcels, domain);
	}
	
	@Override
	public void post(CoverAllocationProblem cap) {
		
		EconomicCoverAllocationProblem ecap = (EconomicCoverAllocationProblem) cap;
		
		ep = ecap.getEconomicProfil();
		
		IntVar profit = VF.bounded("profit", 0, 200000000, ecap.solver()); 
		
		//ecap.solver().post(ICF.scalar(ecap.coverAreas(), ep.profits(covers()), profit));
		ecap.solver().post(ICF.scalar(ecap.coverAreas(), ep.profits(), profit));
		
		switch(mode()){
		case ONLY :
			post(ecap, domain(), profit);
			break;
		case NEVER : 
			post(ecap, domain().inverse(), profit);
			break;
		default : throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());
		}
	}
	
	private void post(CoverAllocationProblem cap, Domain<Integer, Integer> domain, IntVar profit){
		//System.out.println(domain.getClass());
		cap.solver().post(domain.postIntVar(profit));
	}

	@Override
	public boolean check(Instant start, Instant end, boolean verbose){
		boolean ok = true;
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
					//System.out.println(p.getId()+";"+c+";"+p.getArea()+";"+ep.profit(c, p.getArea())+" euros");
					//System.out.println(ep.profit(c, p.getArea()));
					profit += ep.profit(c, p.getArea());
				}
			}
			
			//System.out.println(t.year()+" : profit = "+new Double(profit).intValue()+" euros");
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
			//System.out.println(sb.toString());
		}
		return ok;	
	}
	
	
}
