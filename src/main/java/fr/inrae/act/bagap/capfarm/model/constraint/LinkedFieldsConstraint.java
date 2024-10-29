package fr.inrae.act.bagap.capfarm.model.constraint;

import java.util.Set;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.LCF;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class LinkedFieldsConstraint extends CoverAllocationConstraint<Integer, Integer> {

	private static final long serialVersionUID = 1L;

	public LinkedFieldsConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> parcels) {
		super(code, checkOnly, ConstraintType.LinkedFields, mode, covers, parcels, null);
	}

	@Override
	public void post(CoverAllocationProblem cap) {
		switch(mode()){
		case NEVER : //  les couverts sur ces parcelles nes sont jamais les m�mes 
			for(Parcel p1 : location()){
				int ip1 = cap.parcels().get(p1);
				for(Parcel p2 : location()){
					int ip2 = cap.parcels().get(p2);
					if(p1 != p2){
						for(CoverUnit c : covers()){
							int ic = cap.covers().get(c);
							LCF.ifThen(cap.coversAndParcels(ic,ip1), 
									ICF.arithm(cap.coversAndParcels(ic, ip2), "=", 0));
						}
					}
				}
			}
			break;
		case ALWAYS : // les parcelles sont toujours li�s 
			for(Parcel p1 : location()){
				int ip1 = cap.parcels().get(p1);
				for(Parcel p2 : location()){
					int ip2 = cap.parcels().get(p2);
					if(p1 != p2){
						for(CoverUnit c : covers()){
							int ic = cap.covers().get(c);
							LCF.ifThen(cap.coversAndParcels(ic,ip1), 
									ICF.arithm(cap.coversAndParcels(ic, ip2), "=", 1));
						}
					}
				}
			}
			break;
		
		default : throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());	
		}
	}
	
	@Override
	public boolean check(Instant start, Instant end, boolean verbose){
		boolean ok = true;
		StringBuilder sb = new StringBuilder();
		sb.append("linkedfields ");
		if(verbose){
			if(ok){
			}
			System.out.println(sb.toString());	
		}
		
		return ok;
	}

}
