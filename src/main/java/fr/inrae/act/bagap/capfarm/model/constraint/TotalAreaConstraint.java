package fr.inrae.act.bagap.capfarm.model.constraint;

import java.util.Set;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.domain.Domain;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.core.time.delay.Delay;
import fr.inrae.act.bagap.apiland.core.time.delay.YearDelay;

public class TotalAreaConstraint extends CoverAllocationConstraint<Integer, Integer> {

	private static final long serialVersionUID = 1L;
	
	public TotalAreaConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> location, Domain<Integer, Integer> domain) {
		super(code, checkOnly, ConstraintType.TotalArea, mode, covers, location, domain);
	}
	
	@Override
	public void post(CoverAllocationProblem cap){
		int totarea = cap.allocator().totalParcelsArea();
		int[] areas = new int[cap.allocator().parcels().size()];
		for(int i=0; i<areas.length; i++){
			areas[i] = 0;
		}
		for(Parcel p : location()){
			areas[cap.parcels().get(p)] = p.getArea();
		}
		IntVar coverArea = null;
		if(hasSingleCover()){
			CoverUnit c = getSingleCover();
			int ic = cap.covers().get(c);
			coverArea = VF.bounded("a_cv_"+code(), 0, totarea, cap.solver());
			cap.solver().post(ICF.scalar(cap.coversAndParcels(ic), areas, coverArea));
		}else{
			coverArea = VF.bounded("a_cv_"+code(), 0, totarea, cap.solver());
			IntVar[] coverAreas = new IntVar[covers().size()];
			int index = 0;
			for(CoverUnit c : covers()){
				coverAreas[index] = VF.bounded("a_cv_"+code()+"_cv_"+cap.covers().get(c), 0, totarea, cap.solver());
				if(cap.covers().containsKey(c)){
					cap.solver().post(ICF.scalar(cap.coversAndParcels(cap.covers().get(c)), areas, coverAreas[index]));	
				}
				index++;
			}
			cap.solver().post(ICF.sum(coverAreas, coverArea));
		}
		switch(mode()){
		case ONLY :
			post(cap, domain(), coverArea);
			break;
		case NEVER : 
			post(cap, domain().inverse(), coverArea);
			break;
		default : throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());
		}
	}
	
	private void post(CoverAllocationProblem cap, Domain<Integer, Integer> domain, IntVar coverArea){
		cap.solver().post(domain.postIntVar(coverArea));
	}
	
	@Override
	public boolean check(Instant start, Instant end, boolean verbose){
		boolean ok = true;
		StringBuilder sb = new StringBuilder();
		//sb.append("total area ");
		int area;
		Delay d = new YearDelay(1);
		int supermin = Integer.MAX_VALUE;
		int supermax = Integer.MIN_VALUE;
		for(Instant t=start; t.isBefore(end) || t.equals(end); t=d.next(t)){
			area = 0;
			for(Parcel p : location()){
				if(covers().contains(p.getAttribute("cover").getValue(t))){
					area += p.getArea();
				}
			}
			
			supermin = Math.min(supermin, area);
			supermax = Math.max(supermax, area);
			
			switch(mode()){
			case ONLY :
				if(!domain().accept(area)){
					ok = false;
					if(verbose){
						sb.append("BAD  - Total Area : area = "+area+" not in domain "+domain()+"\n");
					}else{
						return ok;
					}
				}
				break;
			case NEVER : 
				if(domain().accept(area)){
					ok = false;
					if(verbose){
						sb.append("BAD  - Total Area : area = "+area+" not in domain "+domain()+"\n");
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
					sb.append("GOOD - cover "+covers().toString()+" has total area = "+supermin/10000.0);
				}else{
					sb.append("GOOD - cover "+covers().toString()+" has total area between min = "+supermin/10000.0+" and max = "+supermax/10000.0);
				}
			}
			System.out.println(sb.toString());
		}
		return ok;	
	}
	
}

