package fr.inrae.act.bagap.capfarm.model.constraint;

import java.util.Set;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.LCF;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.domain.Domain;
import fr.inrae.act.bagap.capfarm.model.domain.VariableValueDomain;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.core.time.delay.Delay;
import fr.inrae.act.bagap.apiland.core.time.delay.YearDelay;

public class OnNumericConditionConstraint extends CoverAllocationConstraint<Double, Parcel> {

	private static final long serialVersionUID = 1L;
	
	public OnNumericConditionConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> location, Domain<Double, Parcel> domain) {
		super(code, checkOnly, ConstraintType.OnNumericCondition, mode, covers, location, domain);
	}
	
	protected boolean condition(Parcel p){
		return ((VariableValueDomain<Double>) domain()).accept(p);
	}
	
	@Override
	public void post(CoverAllocationProblem cap){
		if(hasSingleCover()){
			CoverUnit c = getSingleCover();
			int ic = cap.covers().get(c);
			for(Parcel p : location()){
				int ip = cap.parcels().get(p);
				switch(mode()){
				case ALWAYS : 
					if(condition(p)){
						cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 1));
					}
					break;
				case NEVER : 
					if(condition(p)){
						cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0));
					}
					break;
				case AROUND : 
					if(!condition(p)){
						cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 1));
					}
					break;
				case ONLY : 
					if(!condition(p)){
						cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0));
					}
					break;
				}
			}
		}else{
			for(Parcel p : location()){
				int ip = cap.parcels().get(p);
				switch(mode()){
				case ALWAYS : 
					if(condition(p)){
						Constraint[] ct = new Constraint[covers().size()];
						int i = 0;
						for(CoverUnit c : covers()){
							int ic = cap.covers().get(c);
							ct[i++] = ICF.arithm(cap.coversAndParcels(ic, ip), "=", 1);
						}
						cap.solver().post(LCF.or(ct));
					}
					break;
				case NEVER : 
					if(condition(p)){
						for(CoverUnit c : covers()){
							int ic = cap.covers().get(c);
							cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0));
						}
					}
					break;
				case AROUND : 
					if(!condition(p)){
						Constraint[] ct = new Constraint[covers().size()];
						int i = 0;
						for(CoverUnit c : covers()){
							int ic = cap.covers().get(c);
							ct[i++] = ICF.arithm(cap.coversAndParcels(ic, ip), "=", 1);
						}
						cap.solver().post(LCF.or(ct));
					}
					break;
				case ONLY : 
					if(!condition(p)){
						for(CoverUnit c : covers()){
							int ic = cap.covers().get(c);
							cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0));
						}
					}
					break;
				}
			}
		}
	}

	@Override
	public boolean check(Instant start, Instant end, boolean verbose){
		boolean ok = true;
		StringBuilder sb = new StringBuilder();
		sb.append("on numeric condition ");
		Delay d = new YearDelay(1);
		for(Parcel p : location()) {
			for(Instant t=start; t.isBefore(end) || t.equals(end); t=d.next(t)){
				switch(mode()){
				case ALWAYS :
					if(condition(p)){
						if(!covers().contains(p.getAttribute("cover").getValue(t))){
							ok = false;
							if(verbose){
								sb.append("BAD \n");
							}else{
								return ok;
							}
						}
					}
					break;
				case NEVER : 
					if(condition(p)){
						if(covers().contains(p.getAttribute("cover").getValue(t))){
							ok = false;
							if(verbose){
								sb.append("BAD \n");
							}else{
								return ok;
							}
						}
					}
					break;
				case AROUND : 
					if(!condition(p)){
						if(!covers().contains(p.getAttribute("cover").getValue(t))){
							ok = false;
							if(verbose){
								sb.append("BAD \n");
							}else{
								return ok;
							}
						}
					}
					break;
				case ONLY : 
					if(!condition(p)){
						if(covers().contains(p.getAttribute("cover").getValue(t))){
							ok = false;
							if(verbose){
								sb.append("BAD \n");
							}else{
								return ok;
							}
						}
					}
					break;	
				}
			}
		}		
		if(verbose){
			if(ok){
				sb.append("GOOD : cover "+covers().toString()+" is "+mode()+" on "+domain());
			}
			System.out.println(sb.toString());
		}
		return ok;
	}
	
}