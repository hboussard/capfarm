package fr.inrae.act.bagap.capfarm.model.constraint;

import java.util.Set;

import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.LCF;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.domain.Domain;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.core.time.delay.Delay;
import fr.inrae.act.bagap.apiland.core.time.delay.YearDelay;

public class DistanceBetweenCoversConstraint extends CoverAllocationConstraint<Integer, Integer> {

	private static final long serialVersionUID = 1L;
	
	private Set<CoverUnit> targets;
	
	public DistanceBetweenCoversConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> location, Domain<Integer, Integer> domain, Set<Cover> targets) {
		super(code, checkOnly, ConstraintType.DistanceBetweenCovers, mode, covers, location, domain);
		this.targets = getCoverUnits(targets);
	}
	
	public boolean targetHasSingleCover(){
		return targets.size() == 1;
	}
	
	public CoverUnit getTargetSingleCover(){
		return (CoverUnit) targets.iterator().next();
	}
	
	@Override
	public void post(CoverAllocationProblem cap){	
		for(Parcel p1 : location()){
			int ip1 = cap.parcels().get(p1);
			for(Parcel p2 : location()){
				int ip2 = cap.parcels().get(p2);
				if(p1 != p2){
					int distance = p1.getDistance(p2);
					
					for(CoverUnit c : covers()){
						int ic1 = cap.covers().get(c);
						if(targetHasSingleCover()){
							int ic2 = cap.covers().get(getTargetSingleCover());
							switch(mode()){
							case ALWAYS : 
								if(domain().accept(distance)){
									LCF.ifThen(cap.coversAndParcels(ic1,ip1), 
											ICF.arithm(cap.coversAndParcels(ic2, ip2), "=", 1));
								}
								break;
							case NEVER : 
								if(domain().accept(distance)){
									LCF.ifThen(cap.coversAndParcels(ic1,ip1), 
											ICF.arithm(cap.coversAndParcels(ic2, ip2), "=", 0));
								}
								break;
							case AROUND : 
								if(!domain().accept(distance)){
									LCF.ifThen(cap.coversAndParcels(ic1,ip1), 
											ICF.arithm(cap.coversAndParcels(ic2, ip2), "=", 1));
								}
								break;
							case ONLY : 
								if(!domain().accept(distance)){
									LCF.ifThen(cap.coversAndParcels(ic1,ip1), 
											ICF.arithm(cap.coversAndParcels(ic2, ip2), "=", 0));
								}
								break;
							}
						}else{
							switch(mode()){
							case ALWAYS : 
								if(domain().accept(distance)){
									Constraint[] ct = new Constraint[targets.size()];
									int i = 0;
									for(CoverUnit c2 : targets){
										int ic2 = cap.covers().get(c2);
										ct[i++] = ICF.arithm(cap.coversAndParcels(ic2, ip2), "=", 1);
									}
									LCF.ifThen(cap.coversAndParcels(ic1,ip1), LCF.or(ct));
								}
								break;
							case NEVER : 
								if(domain().accept(distance)){
									for(CoverUnit c2 : targets){
										int ic2 = cap.covers().get(c2);
										LCF.ifThen(cap.coversAndParcels(ic1,ip1), 
												ICF.arithm(cap.coversAndParcels(ic2, ip2), "=", 0));
									}
								}
								break;
							case AROUND : 
								if(!domain().accept(distance)){
									Constraint[] ct = new Constraint[targets.size()];
									int i = 0;
									for(CoverUnit c2 : targets){
										int ic2 = cap.covers().get(c2);
										ct[i++] = ICF.arithm(cap.coversAndParcels(ic2, ip2), "=", 1);
									}
									LCF.ifThen(cap.coversAndParcels(ic1,ip1), LCF.or(ct));
								}
								break;
							case ONLY : 
								if(!domain().accept(distance)){
									for(CoverUnit c2 : targets){
										int ic2 = cap.covers().get(c2);
										LCF.ifThen(cap.coversAndParcels(ic1,ip1), 
												ICF.arithm(cap.coversAndParcels(ic2, ip2), "=", 0));
									}
								}
								break;
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public boolean check(Instant start, Instant end, boolean verbose){
		boolean ok = true;
		StringBuilder sb = new StringBuilder();
		sb.append("distance between covers ");
		Delay d = new YearDelay(1);
		for(Instant t=start; t.isBefore(end) || t.equals(end); t=d.next(t)){
			for(Parcel p1 : location()){
				Cover c1 = (Cover) p1.getAttribute("cover").getValue(t);
				if(covers().contains(c1)){
					for(Parcel p2 : location()){
						if(!p1.equals(p2)){
							int distance = p1.getDistance(p2);
							Cover c2 = (Cover) p2.getAttribute("cover").getValue(t);
							switch(mode()){
							case ALWAYS : 
								if(domain().accept(distance)){
									if(!targets.contains(c2)){
										ok = false;
										if(verbose){
											sb.append("BAD Distance "+mode()+" : "+distance+"\n");
										}else{
											return ok;
										}
									}
								}
								break;
							case NEVER :
								if(domain().accept(distance)){
									if(targets.contains(c2)){
										ok = false;
										if(verbose){
											sb.append("BAD Distance "+mode()+" : "+distance+"\n");
										}else{
											return ok;
										}
									}
								}
								break;
							case ONLY :
								if(!domain().accept(distance)){
									if(targets.contains(c2)){
										ok = false;
										if(verbose){
											sb.append("BAD Distance "+mode()+" : "+distance+"\n");
										}else{
											return ok;
										}
									}
								}
								break;
							case AROUND :
								if(!domain().accept(distance)){
									if(!targets.contains(c2)){
										ok = false;
										if(verbose){
											sb.append("BAD Distance "+mode()+" : "+distance+"\n");
										}else{
											return ok;
										}
									}
								}
								break;
							}
						}
					}
				}
			}
		}
		if(verbose){
			System.out.println(sb.toString());
		}
		return ok;
	}

}
