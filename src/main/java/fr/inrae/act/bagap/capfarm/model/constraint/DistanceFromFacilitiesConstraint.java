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

public class DistanceFromFacilitiesConstraint extends CoverAllocationConstraint<Integer, Integer> {

	private static final long serialVersionUID = 1L;
	
	private String facilities;
	
	public DistanceFromFacilitiesConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> location, Domain<Integer, Integer> domain, String facilities) {
		super(code, checkOnly, ConstraintType.DistanceFromFacilities, mode, covers, location, domain);
		this.facilities = facilities;
	}
	
	@Override
	public void post(CoverAllocationProblem cap){
		if(hasSingleCover()){
			CoverUnit c = getSingleCover();
			int ic = cap.covers().get(c);
			for(Parcel p : location()){
				int ip = cap.parcels().get(p);
				int distance = cap.allocator().getDistanceFromFacilitiesToParcel(facilities, p);
				if(distance != -1){
					switch(mode()){
					case ALWAYS : 
						if(domain().accept(distance)){
							cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 1));
						}
						break;
					case NEVER : 
						if(domain().accept(distance)){
							cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0));
						}
						break;
					case AROUND : 
						if(!domain().accept(distance)){
							cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 1));
						}
						break;
					case ONLY : 
						if(!domain().accept(distance)){
							cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0));
						}
						break;
					}	
				}
			}
		}else{
			for(Parcel p : location()){
				int ip = cap.parcels().get(p);
				int distance = cap.allocator().getDistanceFromFacilitiesToParcel(facilities, p);
				if(distance != -1){
					switch(mode()){
					case ALWAYS : 
						if(domain().accept(distance)){
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
						if(domain().accept(distance)){
							for(CoverUnit c : covers()){
								int ic = cap.covers().get(c);
								cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0));
							}
						}
						break;
					case AROUND : 
						if(!domain().accept(distance)){
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
						if(!domain().accept(distance)){
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
	}

	@Override
	public boolean check(Instant start, Instant end, boolean verbose){
		boolean ok = true;
		StringBuilder sb = new StringBuilder();
		sb.append("distance from facilities ");
		Delay d = new YearDelay(1);
		switch(mode()){
		case ALWAYS : 
			for(Parcel p : location()){
				int distance = getAllocator().getDistanceFromFacilitiesToParcel(facilities, p);
				if(domain().accept(distance)){
					for(Instant t=start; t.isBefore(end) || t.equals(end); t=d.next(t)){
						Cover c = (Cover) p.getAttribute("cover").getValue(t);
						if(!covers().contains(c)){
							ok = false;
							if(verbose){
								sb.append("BAD Distance "+mode()+" : parcel "+p.getId()+" area = "+p.getArea()+" not in domain "+domain()+"\n");
							}else{
								return ok;
							}
						}
					}
				}
			}
			break;
		case NEVER : 
			for(Parcel p : location()){
				int distance = getAllocator().getDistanceFromFacilitiesToParcel(facilities, p);
				if(domain().accept(distance)){
					for(Instant t=start; t.isBefore(end) || t.equals(end); t=d.next(t)){
						Cover c = (Cover) p.getAttribute("cover").getValue(t);
						if(covers().contains(c)){
							ok = false;
							if(verbose){
								sb.append("BAD Distance "+mode()+" : parcel "+p.getId()+" area = "+p.getArea()+" not in domain "+domain()+"\n");
							}else{
								return ok;
							}
						}
					}
				}
			}
			break;
		case ONLY : 
			for(Parcel p : location()){
				int distance = getAllocator().getDistanceFromFacilitiesToParcel(facilities, p);
				if(!domain().accept(distance)){
					for(Instant t=start; t.isBefore(end) || t.equals(end); t=d.next(t)){
						Cover c = (Cover) p.getAttribute("cover").getValue(t);
						if(covers().contains(c)){
							ok = false;
							if(verbose){
								sb.append("BAD Distance "+mode()+" : parcel "+p.getId()+" area = "+p.getArea()+" not in domain "+domain()+"\n");
							}else{
								return ok;
							}
						}
					}
				}
			}
			break;
		case AROUND : 
			for(Parcel p : location()){
				int distance = getAllocator().getDistanceFromFacilitiesToParcel(facilities, p);
				if(!domain().accept(distance)){
					for(Instant t=start; t.isBefore(end) || t.equals(end); t=d.next(t)){
						Cover c = (Cover) p.getAttribute("cover").getValue(t);
						if(!covers().contains(c)){
							ok = false;
							if(verbose){
								sb.append("BAD Distance "+mode()+" : parcel "+p.getId()+" area = "+p.getArea()+" not in domain "+domain()+"\n");
							}else{
								return ok;
							}
						}
					}
				}
			}
			break;
		}
		if(verbose){
			System.out.println(sb.toString());
		}
		return ok;
	}
	
}
