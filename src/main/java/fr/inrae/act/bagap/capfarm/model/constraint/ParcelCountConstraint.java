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

public class ParcelCountConstraint extends CoverAllocationConstraint<Integer, Integer> {

	private static final long serialVersionUID = 1L;
	
	public ParcelCountConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> location, Domain<Integer, Integer> domain) {
		super(code, checkOnly, ConstraintType.ParcelCount, mode, covers, location, domain);
	}
	
	@Override
	public void post(CoverAllocationProblem cap){
		int totcount = location().size();
		int[] hasCover = new int[cap.allocator().parcels().size()];
		for(int i=0; i<hasCover.length; i++){
			hasCover[i] = 0;
		}
		for(Parcel p : location()){
			hasCover[cap.parcels().get(p)] = 1;
		}
		IntVar nbCover = null;
		if(hasSingleCover()){
			CoverUnit c = getSingleCover();
			int ic = cap.covers().get(c);
			nbCover = VF.bounded("c_nb_"+code(), 0, totcount, cap.solver());
			cap.solver().post(ICF.scalar(cap.coversAndParcels(ic), hasCover, nbCover));
		}else{
			nbCover = VF.bounded("c_nb_"+code(), 0, totcount, cap.solver());
			IntVar[] nbCovers = new IntVar[covers().size()];
			int index = 0;
			for(CoverUnit c : covers()){
				nbCovers[index] = VF.bounded("c_nb_"+code()+"_cv_"+cap.covers().get(c), 0, totcount, cap.solver());
				if(cap.covers().containsKey(c)){
					cap.solver().post(ICF.scalar(cap.coversAndParcels(cap.covers().get(c)), hasCover, nbCovers[index]));	
				}
				index++;
			}
			cap.solver().post(ICF.sum(nbCovers, nbCover));
		}
		switch(mode()){
		case ONLY :
			post(cap, domain(), nbCover);
			break;
		case NEVER : 
			post(cap, domain().inverse(), nbCover);
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
		int nb;
		Delay d = new YearDelay(1);
		int supermin = Integer.MAX_VALUE;
		int supermax = Integer.MIN_VALUE;
		for(Instant t=start; t.isBefore(end) || t.equals(end); t=d.next(t)){
			nb = 0;
			for(Parcel p : location()){
				if(covers().contains(p.getAttribute("cover").getValue(t))){
					nb++;
				}
			}
			
			supermin = Math.min(supermin, nb);
			supermax = Math.max(supermax, nb);
			
			switch(mode()){
			case ONLY :
				if(!domain().accept(nb)){
					ok = false;
					if(verbose){
						sb.append("BAD  - Parcel Count : count = "+nb+" not in domain "+domain()+"\n");
					}else{
						return ok;
					}
				}
				break;
			case NEVER : 
				if(domain().accept(nb)){
					ok = false;
					if(verbose){
						sb.append("BAD  - Parcel Count : count = "+nb+" not in domain "+domain()+"\n");
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
					sb.append("GOOD - cover "+covers().toString()+" has count = "+supermin/10000.0);
				}else{
					sb.append("GOOD - cover "+covers().toString()+" has count between min = "+supermin/10000.0+" and max = "+supermax/10000.0);
				}
			}
			System.out.println(sb.toString());
		}
		return ok;	
	}
	
}
