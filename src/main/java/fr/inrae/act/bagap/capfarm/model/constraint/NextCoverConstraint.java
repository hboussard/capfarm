package fr.inrae.act.bagap.capfarm.model.constraint;

import java.util.HashSet;
import java.util.Set;

import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.LCF;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.domain.Domain;
import fr.inrae.act.bagap.capfarm.model.domain.SetDomain;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.composition.DynamicAttribute;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.core.time.Interval;

public class NextCoverConstraint extends CoverAllocationConstraint<CoverUnit, CoverUnit> {

	private static final long serialVersionUID = 1L;
	
	public NextCoverConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> parcels, Domain<CoverUnit, CoverUnit> domain) {
		super(code, checkOnly, ConstraintType.NextCover, mode, covers, parcels, domain);
	}

	@Override
	public void post(CoverAllocationProblem cap) {
		for(Parcel p : location()){
			int ip = cap.parcels().get(p);
			if(cap.previous(ip) != null && covers().contains(cap.previous(ip).getValue())){
				Constraint[] cons;
				int i;
				switch(mode()){
				case ONLY :
					if(((SetDomain<CoverUnit>) domain()).set().size() > 0){
						cons = new Constraint[((SetDomain<CoverUnit>) domain()).set().size()];
						i = 0;
						for(CoverUnit cu : ((SetDomain<CoverUnit>) domain()).set()){
							cons[i++] = ICF.arithm(cap.coversAndParcels(cap.covers().get(cu), ip), "=", 1);
						}
						if(cons.length > 0){
							LCF.ifThen(cap.parcelsImplantedCoverContinue(ip).not(), LCF.or(cons));
						}
					}else{ // pas de suivant, le couvert continue
						cap.solver().post(ICF.arithm(cap.parcelsImplantedCoverContinue(ip), "=", 1));
					}
					break;
				case NEVER : 
					cons = new Constraint[((SetDomain<CoverUnit>) domain()).set().size()];
					i = 0;
					for(CoverUnit cu : ((SetDomain<CoverUnit>) domain()).set()){
						//System.out.println(covers()+" "+cu);
						cons[i++] = ICF.arithm(cap.coversAndParcels(cap.covers().get(cu), ip), "=", 0);
					}
					if(cons.length > 0){
						LCF.ifThen(cap.parcelsImplantedCoverContinue(ip).not(), LCF.and(cons));
					}
					break;
				default : throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());
				}
			}
		}
	}
		
	@Override
	public boolean check(Instant start, Instant end, boolean verbose){
		boolean ok = true;
		StringBuilder sb = new StringBuilder();
		//sb.append("next cover ");
		Set<CoverUnit> next = new HashSet<CoverUnit>();
		CoverUnit prec;
		Interval t = new Interval(start, end);
		for(Parcel p : location()){
			prec = null;
			for(CoverUnit c : ((DynamicAttribute<CoverUnit>) p.getAttribute("cover")).values(t)){
				if(covers().contains(prec)){
					
					switch(mode()){
					case ONLY :
						if(!domain().accept(c)){
							ok = false;
							if(verbose){
								sb.append(covers().toString()+" BAD : Next Cover "+c+"\n");
							}else{
								return ok;
							}
						}else{
							next.add(c);
						}
						break;
					case NEVER : 
						if(domain().accept(c)){
							ok = false;
							if(verbose){
								sb.append(covers().toString()+" BAD : Next Cover "+c+"\n");
							}else{
								return ok;
							}
						}else{
							next.add(c);
						}
						break;
					default : throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());
					}
				}
				prec = c;
			}
		}
		
		if(verbose){
			if(ok){
				sb.append("GOOD : cover "+covers().toString()+" has next transitions in "+next);
			}
			System.out.println(sb.toString());
		}
		return ok;
	}

}
