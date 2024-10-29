package fr.inrae.act.bagap.capfarm.model.constraint;

import java.util.Set;
import org.chocosolver.solver.constraints.ICF;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.csp.ProbaTimeManager;
import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.domain.Domain;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.composition.DynamicAttribute;
import fr.inrae.act.bagap.apiland.core.composition.TemporalValue;
import fr.inrae.act.bagap.apiland.core.time.Future;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.core.time.Interval;
import fr.inrae.act.bagap.apiland.core.time.MultiInterval;
import fr.inrae.act.bagap.apiland.core.time.Time;

public class DurationConstraint extends CoverAllocationConstraint<Integer, Integer> {

	private static final long serialVersionUID = 1L;
	
	private String durationMode;
	
	private boolean hasMax;
	
	public DurationConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> parcels, Domain<Integer, Integer> domain, String durationMode, boolean hasMax) {
		super(code, checkOnly, ConstraintType.Duration, mode, covers, parcels, domain);
		this.durationMode = durationMode;
		this.hasMax = hasMax;
	}

	@Override
	public void post(CoverAllocationProblem cap) {
		
		int min = domain().minimum();
		int max = domain().maximum();
		for(CoverUnit c : covers()){
			for(Parcel p : location()){
				int ip = cap.parcels().get(p);
				if(cap.previous(ip) != null && c.equals(cap.previous(ip).getValue())){
					int count = cap.getTime().year() - cap.previous(ip).getTime().start().year();
					
					switch(mode()){
					case ONLY :
						if(count < min){
							if(!cap.previous(ip).equals(cap.first(ip))){
								cap.solver().post(ICF.arithm(cap.parcelsImplantedCoverContinue(ip), "=", 1));
							}else if(count < (min*Math.random())/2){
								cap.solver().post(ICF.arithm(cap.parcelsImplantedCoverContinue(ip), "=", 1));
							}
						}else if(hasMax){
							if(count >= max){
								cap.solver().post(ICF.arithm(cap.parcelsImplantedCoverContinue(ip), "=", 0));
							}else{
								int rp = (int) (Math.random() * 100);
								int nbyear = count - min;
								int duration = max - min + 1;
								if(rp > (ProbaTimeManager.getProba(durationMode, duration, nbyear)
										/ (100.0-ProbaTimeManager.getCumul(durationMode, duration, nbyear))
										* 100.0)){
									cap.solver().post(ICF.arithm(cap.parcelsImplantedCoverContinue(ip), "=", 1));
								}
							}
						}
						
						break;
					case NEVER :
						if(domain().accept(count)){
							cap.solver().post(ICF.arithm(cap.parcelsImplantedCoverContinue(ip), "=", 1));
						}
						break;
					default : throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());					
					}
				}
			}
		}
	}

	@Override
	public boolean check(Instant start, Instant end, boolean verbose){
		Interval actif = new Interval(start, end);
		Time t;
		boolean ok = true;
		StringBuilder sb = new StringBuilder();
		//sb.append("duration ");
		int supermin = Integer.MAX_VALUE;
		int supermax = Integer.MIN_VALUE;
		int d;
		for(Parcel p : location()){
			for(TemporalValue<CoverUnit> tv : (DynamicAttribute<CoverUnit>) p.getAttribute("cover")){
				int min = Integer.MAX_VALUE;
				int max = Integer.MIN_VALUE;
				if(covers().contains(tv.getValue()) && actif.intersects(tv.getTime())){
					if(!tv.equals(p.getAttribute("cover").getFirst()) && !tv.equals(p.getAttribute("cover").getLast())){
						t = tv.getTime();
						if(t instanceof Interval){
							if(!(((Interval) t).end() instanceof Future)){
								d = ((Interval) t).yearInterval();
								min = Math.min(min, d);
							}
						}else{
							for(Interval i : (MultiInterval) t){
								if(!(i.end() instanceof Future)){
									d = i.yearInterval();
									min = Math.min(min, d);
								}
							}
						}
					}
					t = tv.getTime();
					if(t instanceof Interval){
						if(!(((Interval) t).end() instanceof Future)){
							d = ((Interval) t).yearInterval();
							max = Math.max(max, d);
						}
					}else{
						for(Interval i : (MultiInterval) t){
							if(!(i.end() instanceof Future)){
								d = i.yearInterval();
								max = Math.max(max, d);
							}
						}
					}
				}
				
				supermin = Math.min(supermin, min);
				supermax = Math.max(supermax, max);
				
				switch(mode()){
				case ONLY :
					if(min != Integer.MAX_VALUE){
						if(!domain().accept(min) || !domain().accept(max)){
							ok = false;
							if(verbose){
								sb.append("BAD  - cover "+tv.getValue()+" has duration time between min = "+min+" and max = "+max+"\n");
							}else{
								return ok;
							}
						}
					}
					break;
				case NEVER :
					if(min != Integer.MAX_VALUE){
						if(domain().accept(min) || domain().accept(max)){
							ok = false;
							if(verbose){
								sb.append("BAD  - cover "+tv.getValue()+" has duration time between min = "+min+" and max = "+max+"\n");
							}else{
								return ok;
							}
						}
					}
					break;
				default : throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());	
				}
			}
		}
		
		if(verbose){
			if(ok){
				if(supermin == Integer.MAX_VALUE){
					sb.append("GOOD - Duration "+mode()+" cover "+covers().toString()+" has good duration");
				}else if(supermin == supermax){
					sb.append("GOOD - Duration "+mode()+" cover "+covers().toString()+" has duration time = "+supermin);
				}else{
					sb.append("GOOD - Duration "+mode()+" cover "+covers().toString()+" has duration time between min = "+supermin+" and max = "+supermax);
				}
			}
			System.out.println(sb.toString());	
		}
		
		return ok;
	}

}

