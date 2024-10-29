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
import fr.inrae.act.bagap.capfarm.model.economic.ManagmentProfil;
import fr.inrae.act.bagap.capfarm.model.economic.csp.EconomicCoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.core.time.delay.Delay;
import fr.inrae.act.bagap.apiland.core.time.delay.YearDelay;

public class WorkConstraint extends CoverAllocationConstraint<Integer, Integer>{

	private static final long serialVersionUID = 1L;
	
	private ManagmentProfil mp;
	
	public WorkConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> parcels, Domain<Integer, Integer> domain) {
		super(code, checkOnly, ConstraintType.Work, mode, covers, parcels, domain);
	}
	
	@Override
	public void post(CoverAllocationProblem cap) {
		
		EconomicCoverAllocationProblem ecap = (EconomicCoverAllocationProblem) cap;
		
		mp = ecap.getManagmentProfil();
		
		IntVar work = VF.bounded("work", 0, 2000000000, ecap.solver()); 
		
		//ecap.solver().post(ICF.scalar(ecap.coverAreas(), ep.profits(covers()), profit));
		ecap.solver().post(ICF.scalar(ecap.coverAreas(), mp.works(), work));
		
		switch(mode()){
		case ONLY :
			post(ecap, domain(), work);
			break;
		case NEVER : 
			post(ecap, domain().inverse(), work);
			break;
		default : throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());
		}
	}
	
	private void post(CoverAllocationProblem cap, Domain<Integer, Integer> domain, IntVar work){
		//System.out.println(domain.getClass());
		cap.solver().post(domain.postIntVar(work));
	}

	@Override
	public boolean check(Instant start, Instant end, boolean verbose){
		boolean ok = true;
		StringBuilder sb = new StringBuilder();
		int supermin = Integer.MAX_VALUE;
		int supermax = Integer.MIN_VALUE;
		Delay d = new YearDelay(1);
		CoverUnit c;
		for(Instant t=start; t.isBefore(end) || t.equals(end); t=d.next(t)){
			int work = 0;
			for(Parcel p : location()){
				c = (CoverUnit) p.getAttribute("cover").getValue(t);
				if(covers().contains(c)){
					//System.out.println(p.getId()+";"+c+";"+p.getArea()+";"+ep.profit(c, p.getArea()));
					//System.out.println(ep.profit(c, p.getArea()));
					work += mp.work(c, p.getArea());
				}
			}
			
			//work /= 10.0; 
			
			//System.out.println(t.year()+" : profit "+profit+" "+supermin+" "+supermax);
			supermin = Math.min(supermin, work);
			supermax = Math.max(supermax, work);
			
			switch(mode()){
			case ONLY :
				if(!domain().accept(work)){
					ok = false;
					if(verbose){
						sb.append("BAD : work  = "+work+" not in domain "+domain()+"\n");
					}else{
						return ok;
					}
				}
				break;
			case NEVER : 
				if(domain().accept(work)){
					ok = false;
					if(verbose){
						sb.append("BAD : profit = "+work+" not in domain "+domain()+"\n");
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
					sb.append("GOOD : cover "+covers().toString()+" has work = "+supermin);
				}else{
					sb.append("GOOD : cover "+covers().toString()+" has work between min = "+supermin+" and max = "+supermax);
				}
			}
			System.out.println(sb.toString());
		}
		return ok;		
	}
	
	
}
