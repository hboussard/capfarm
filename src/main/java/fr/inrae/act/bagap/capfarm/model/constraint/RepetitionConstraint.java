package fr.inrae.act.bagap.capfarm.model.constraint;

import java.util.Set;

import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.LCF;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.csp.ProbaTimeManager;
import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.domain.Domain;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.composition.DynamicAttribute;
import fr.inrae.act.bagap.apiland.core.composition.TemporalValue;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class RepetitionConstraint extends CoverAllocationConstraint<Integer, Integer> {

	private static final long serialVersionUID = 1L;
	
	private String durationMode;
	
	private boolean hasMax;
	
	public RepetitionConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> parcels, Domain<Integer, Integer> domain, String durationMode, boolean hasMax) {
		super(code, checkOnly, ConstraintType.Repetition, mode, covers, parcels, domain);
		this.durationMode = durationMode;
		this.hasMax = hasMax;
	}

	@Override
	public void post(CoverAllocationProblem cap) {
		int min = domain().minimum();
		int max = domain().maximum();
		for(Parcel p : location()){
			int ip = cap.parcels().get(p);
			if(cap.previous(ip) != null && covers().contains(cap.previous(ip).getValue())){
				int count = countLastOccurencesOfCoverUnits((DynamicAttribute<CoverUnit>) p.getAttribute("cover"), covers()) - 1;
				switch(mode()){
				case ONLY :
					if(count < min){
						if(!((DynamicAttribute<CoverUnit>) p.getAttribute("cover")).hasOnlyForValue(cap.starts(ip), cap.getTime(), covers())){
							if(hasSingleCover()){
								
								int ic = cap.covers().get(getSingleCover());
								cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 1));
							}else{
								Constraint[] cons = new Constraint[covers().size()];
								int i = 0;
								for(CoverUnit cu : covers()){
									int ic = cap.covers().get(cu);
									cons[i++] = ICF.arithm(cap.coversAndParcels(ic, ip), "=", 1);
								}
								cap.solver().post(LCF.or(cons));
							}
						}
					}else if(hasMax){
						if(count >= max){
							if(hasSingleCover()){
								int ic = cap.covers().get(getSingleCover());
								LCF.ifThen(cap.parcelsImplantedCoverContinue(ip).not(), 
										ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0));
							}else{
								Constraint[] cons = new Constraint[covers().size()];
								int i = 0;
								for(CoverUnit cu : covers()){
									int ic = cap.covers().get(cu);
									cons[i++] = ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0);
								}
								LCF.ifThen(cap.parcelsImplantedCoverContinue(ip).not(), LCF.and(cons));
							}
						}else{
							int rp = (int) (Math.random() * 100);
							
							int nbyear = count - min;
							int duration = max - min + 1;
							
							if(rp > (ProbaTimeManager.getProba(durationMode, duration, nbyear)
									/ (100.0-ProbaTimeManager.getCumul(durationMode, duration, nbyear))
									* 100.0)){
								if(hasSingleCover()){
									int ic = cap.covers().get(getSingleCover());
									cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 1));
								}else{
									Constraint[] cons = new Constraint[covers().size()];
									int i = 0;
									for(CoverUnit cu : covers()){
										int ic = cap.covers().get(cu);
										cons[i++] = ICF.arithm(cap.coversAndParcels(ic, ip), "=", 1);
									}
									cap.solver().post(LCF.or(cons));
								}
							}
						}
					}
					
					break;
				case NEVER :
					if(domain().accept(count)){
						if(hasSingleCover()){
							int ic = cap.covers().get(getSingleCover());
							cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 1));
						}else{
							Constraint[] cons = new Constraint[covers().size()];
							int i = 0;
							for(CoverUnit cu : covers()){
								int ic = cap.covers().get(cu);
								cons[i++] = ICF.arithm(cap.coversAndParcels(ic, ip), "=", 1);
							}
							cap.solver().post(LCF.or(cons));
						}
					}
					break;
				default : throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());					
				}
			}
		}
	}

	private int countLastOccurencesOfCoverUnits(DynamicAttribute<CoverUnit> daCover, Set<CoverUnit> u){
		int count = 0;
		for(int i=daCover.size()-1; i>=0; i--){
			if(u.contains(daCover.get(i).getValue())){
				count++;
			}else{
				break;
			}
		}
		return count;
	}
	
	@Override
	public boolean check(Instant start, Instant end, boolean verbose){
		boolean ok = true;
		StringBuilder sb = new StringBuilder();
		//sb.append("repetition ");
		int supermin = Integer.MAX_VALUE;
		int supermax = Integer.MIN_VALUE;
		for(Parcel p : location()){
			
			int min = Integer.MAX_VALUE;
			int max = Integer.MIN_VALUE;
			int repet = -1;
			boolean begin = false;
			for(TemporalValue<CoverUnit> tv : (DynamicAttribute<CoverUnit>) p.getAttribute("cover")){
				if(covers().contains(tv.getValue())){
					repet++;
					if(tv.equals(p.getAttribute("cover").getFirst())){
						begin = true;
					}
				}else{
					if(begin){
						begin = false;
						if(repet > -1){
							max = Math.max(max,  repet);
							repet = -1;
						}
					}else{
						if(repet > -1){
							min = Math.min(min,  repet);
							max = Math.max(max,  repet);
							repet = -1;
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
						//System.out.println(p.getId()+" "+min+" "+max);
						if(verbose){
							sb.append("BAD  : cover has bad repetition time min = "+min+" and max = "+max+"\n");
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
						//System.out.println(p.getId()+" "+min+" "+max);
						if(verbose){
							sb.append("BAD  : cover has bad repetition time min = "+min+" and max = "+max+"\n");
						}else{
							return ok;
						}
					}
				}
				break;
			default : throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());	
			}
		}
		
		if(verbose){
			if(ok){
				if(supermin == Integer.MAX_VALUE){
					sb.append("GOOD - repetition "+mode()+" cover "+covers().toString()+" has no repetition");
				}else{
					sb.append("GOOD - repetition "+mode()+" cover "+covers().toString()+" has repetition between min = "+supermin+" and max = "+supermax);
				}
				
			}
			System.out.println(sb.toString());
		}
		return ok;
	}

}
