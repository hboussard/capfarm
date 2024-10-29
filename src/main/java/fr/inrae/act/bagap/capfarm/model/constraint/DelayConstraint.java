package fr.inrae.act.bagap.capfarm.model.constraint;

import java.io.Serializable;
import java.util.Set;
import org.chocosolver.solver.constraints.ICF;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.domain.Domain;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.composition.Attribute;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.core.time.delay.YearDelay;

public class DelayConstraint extends CoverAllocationConstraint<Integer, Integer> {

	private static final long serialVersionUID = 1L;

	private Set<CoverUnit> targets;
	
	public DelayConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> parcels, Domain<Integer, Integer> domain, Set<Cover> targets) {
		super(code, checkOnly, ConstraintType.Delay, mode, covers, parcels, domain);
		this.targets = getCoverUnits(targets);
	}

	@Override
	public void post(CoverAllocationProblem cap) {
		for(Parcel p : location()){
			int ip = cap.parcels().get(p);
			Attribute<Cover> attribute = (Attribute<Cover>) p.getAttribute("cover");
			Instant last = null;
			for(Cover c : covers()){
				Instant lastc = attribute.getLastOccurence(c);
				if(last == null || (lastc != null && lastc.isAfter(last))){
					last = lastc;
				}
			}
			
			for(Cover ct : targets){
				int ict = cap.covers().get(ct);
				switch(mode()){
				case NEVER : 
					for(Cover c : covers()){
						if(c.equals(ct)){ // m�me cultures
							if(attribute.getLast() != null && !attribute.getLast().getValue().equals(c)){
								if(last != null && domain().accept(cap.getTime().year()-last.year()+1)){
									cap.solver().post(ICF.arithm(cap.coversAndParcels(ict, ip), "=", 0));
								}
							}
						}else{// cultures diff�rentes
							if(last != null && domain().accept(cap.getTime().year()-last.year()+1)){
								cap.solver().post(ICF.arithm(cap.coversAndParcels(ict, ip), "=", 0));
							}
						}
					}
					break;
				case ONLY :
					for(Cover c : covers()){
						if(c.equals(ct)){ // m�me cultures
							if(attribute.getLast() != null && !attribute.getLast().getValue().equals(c)){
								if(last != null && !domain().accept(cap.getTime().year()-last.year()+1)){
									cap.solver().post(ICF.arithm(cap.coversAndParcels(ict, ip), "=", 0));
								}
								if(last != null && domain().accept(cap.getTime().year()-last.year()+1) 
										&& !domain().accept(cap.getTime().year()-last.year()+2)){
									cap.solver().post(ICF.arithm(cap.coversAndParcels(ict, ip), "=", 1));
								}
							}
							break;
						}else{ // cultures diff�rentes
							if(last != null && !domain().accept(cap.getTime().year()-last.year()+1)){
								cap.solver().post(ICF.arithm(cap.coversAndParcels(ict, ip), "=", 0));
							}
							if(last != null && domain().accept(cap.getTime().year()-last.year()+1) 
									&& !domain().accept(cap.getTime().year()-last.year()+2)){
								cap.solver().post(ICF.arithm(cap.coversAndParcels(ict, ip), "=", 1));
							}
							break;
						}
					}
					break;
				default : throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());
			}
		}
		
		
		/*
		 					if(c.equals(ct)){ // m�me cultures
		 						if(attribute.getLast() != null && !attribute.getLast().getValue().equals(c)){
									if(attribute.hasValue(c, Time.getIntervalYear_N(cap.getTime(), returnTime(ct)-1))){
										cap.solver().post(ICF.arithm(cap.coversAndParcels(ict, ip), "=", 0));
									}
								}
							}else{ // cultures diff�rentes
								if(attribute.hasValue(c, Time.getIntervalYear_N(cap.getTime(), returnTime(ct)-1))){
									cap.solver().post(ICF.arithm(cap.coversAndParcels(ict, ip), "=", 0));
								}
							}
						}else{ // cover group
							int irg = cap.groups().get(ct);
							if(((CoverGroup) ct).contains(c)){ // culture dans le groupe
								if(attribute.getLast() != null && !((CoverGroup) ct).contains(attribute.getLast().getValue())){
									if(attribute.hasValue(c, Time.getIntervalYear_N(cap.getTime(), returnTime(ct)-1))){
										cap.solver().post(ICF.arithm(cap.groupsAndParcels(irg,ip), "=", 0));
									}
								}
							}else{ // culture non inclue dans le groupe
								if(attribute.hasValue(c, Time.getIntervalYear_N(cap.getTime(), returnTime(ct)-1))){
									cap.solver().post(ICF.arithm(cap.groupsAndParcels(irg,ip), "=", 0));
								}
							}
						}
					}	
				}
			}else{  // group cover
				for(Parcel p : cst.location()){
					int ip = parcels.get(p);
					Attribute<Cover> attribute = (Attribute<Cover>) p.getAttribute("cover");
					for(Cover rc : ((MinimumReturnTimeConstraint) cst).returnCovers()){
						if(rc instanceof CoverUnit){
							int irc = covers.get(rc);
							if(((CoverGroup) cst.cover()).contains(rc)){ // culture dans le groupe
								if(attribute.getLast() != null && !attribute.getLast().getValue().equals(rc)){
									for(Cover c : (CoverGroup) cst.cover()){
										if(attribute.hasValue(c, Time.getIntervalYear_N(t, ((MinimumReturnTimeConstraint) cst).returnTime(rc)-1))){
											solver.post(IntConstraintFactory.arithm(coversAndParcels[irc][ip], "=", 0));
											break;
										}
									}
								}
							}else{  // culture non inclue dans le groupe
								for(Cover c : (CoverGroup) cst.cover()){
									if(attribute.hasValue(c, Time.getIntervalYear_N(t, ((MinimumReturnTimeConstraint) cst).returnTime(rc)-1))){
										solver.post(IntConstraintFactory.arithm(coversAndParcels[irc][ip], "=", 0));
										break;
									}
								}
							}
						}else{ // group cover
							int irg = groups.get(rc);
							if(cst.cover().equals(rc)){ // m�me groupe
								if(attribute.getLast() != null && !((CoverGroup) rc).contains(attribute.getLast().getValue())){
									for(Cover c : (CoverGroup) cst.cover()){
										if(attribute.hasValue(c, Time.getIntervalYear_N(t, ((MinimumReturnTimeConstraint) cst).returnTime(rc)-1))){
											solver.post(IntConstraintFactory.arithm(groupsAndParcels[irg][ip], "=", 0));
											break;
										}
									}
								}
							}else{ // groupes diff�rents
								for(Cover c : (CoverGroup) cst.cover()){
									if(attribute.hasValue(c, Time.getIntervalYear_N(t, ((MinimumReturnTimeConstraint) cst).returnTime(rc)-1))){
										solver.post(IntConstraintFactory.arithm(groupsAndParcels[irg][ip], "=", 0));
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		*/
	}

	/*
	@Override
	public boolean check(Instant start, Instant end, boolean verbose){
		boolean ok = true;
		StringBuilder sb = new StringBuilder();
		Delay d = new YearDelay(1);
		boolean nothing = true;
		boolean repeated = false;
		boolean waiting = false;
		Instant begin = null;
		for(Parcel p : location()){
			for(Cover c : covers()){
				for(Cover ct : targets){
					for(Instant t=start; t.isBefore(end) || t.equals(end); t=d.next(t)){
						Cover cp = (Cover) p.getAttribute("cover").getValue(t);
						if(nothing){
							if(c.equals(cp)){
								nothing = false;
								repeated = true;
								begin = t;
							}
						}else if(repeated){
								if(c.equals(cp)){
									begin = t;
								}else{
									repeated = false;
									waiting = true;
								}
						}else if(waiting){
							if(ct.equals(cp)){
								waiting = false;
								nothing = true;
								
							}	
						}else{
							System.err.println("error ");
						}
					}
				}
			}
		}
			
			
				Cover c = (Cover) p.getAttribute("cover").getValue(t);
				if(covers().contains(c)){
					if(!started){
						started = true;
						begin = t;
					}else if(repeated){
						begin = t;
					}else if(waiting){
						
					}
				}
				
				
				for(Cover ct : targets){
					switch(mode()){
					case NEVER : 
						if(.equals(c)){
							
						}
					if(attribute.getLast() != null && !attribute.getLast().getValue().equals(c)){
								if(last != null && domain().accept(t.year()-last.year()+1)){
									cap.solver().post(ICF.arithm(cap.coversAndParcels(ict, ip), "=", 0));
								}
							}
							break;
						}
					}
				}
			}
		
		
		if(verbose){
			System.out.println(sb.toString());
		}
		return ok;	
	}*/
	}

	@Override
	public boolean check(Instant start, Instant end, boolean verbose){

		boolean ok = true;
		StringBuilder sb = new StringBuilder();
		sb.append("delay ");
		
		int supermin = Integer.MAX_VALUE;
		int supermax = Integer.MIN_VALUE;
		
		for(Parcel p : location()){
			int from = -1;
			Serializable[] s = (Serializable[]) p.getAttribute("cover").split(new YearDelay(1));
			for(int i=0; i<s.length; i++){
				CoverUnit cu = (CoverUnit) s[i];
				if(from != -1 && targets.contains(cu)){
					int delay = i - from;
					if(delay > 1){
						switch(mode()){
						case NEVER :
							if(domain().accept(delay)){
								ok = false;
								if(verbose){
									sb.append("BAD : delay "+delay+"\n");
								}else{
									return ok;
								}
								return false;
							}else{
								supermin = Math.min(supermin, delay);
								supermax = Math.max(supermax, delay);
							}
							break;
						case ONLY :
							if(!domain().accept(delay)){
								ok = false;
								if(verbose){
									sb.append("BAD : delay "+delay+"\n");
								}else{
									return ok;
								}
								return false;
							}else{
								supermin = Math.min(supermin, delay);
								supermax = Math.max(supermax, delay);
							}
							break;
						default : throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());
						}
						from = -1;
					}else if(covers().contains(cu)){
						from = i;
					}
				}else if(covers().contains(cu)){
					from = i;
				}
			}
		}
		if(verbose){
			if(supermin == supermax){
				sb.append("GOOD : cover "+covers().toString()+" has delay time = "+supermin);
			}else{
				sb.append("GOOD : cover "+covers().toString()+" has delay time between min = "+supermin+" and max = "+supermax);
			}
			System.out.println(sb.toString());
		}
		return ok;
	}
	
}
