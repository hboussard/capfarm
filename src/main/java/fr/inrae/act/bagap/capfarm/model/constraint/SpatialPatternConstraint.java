package fr.inrae.act.bagap.capfarm.model.constraint;

import java.util.HashSet;
import java.util.Set;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.LCF;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.solver.variables.VariableFactory;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.domain.Domain;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;

public class SpatialPatternConstraint extends CoverAllocationConstraint<Integer, Integer> {

	private static final long serialVersionUID = 1L;

	public SpatialPatternConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> parcels, Domain<Integer, Integer> domain) {
		super(code, checkOnly, ConstraintType.SpatialPattern, mode, covers, parcels, domain);
	}

	@Override
	public void post(CoverAllocationProblem cap) {
		int totlength = cap.allocator().totalEdgesLength();
		int[] edges = cap.allocator().edgesLength();
			
		Cover c1 = null, c2;
		int ic1 = -1, ic2 = -1;
		for(Cover c : covers()){
			if(c1 == null){
				c1 = c;
				ic1 = cap.covers().get(c1);
			}else{
				c2 = c;
				ic2 = cap.covers().get(c2);
			}
		}
		if(ic2 == -1){
			c2 = c1;
			ic2 = ic1;
		}
		
		BoolVar[] vars = new BoolVar[edges.length];
		Set<Parcel> ever = new HashSet<Parcel>();
		int i=0;
		for(Parcel p1 : cap.allocator().parcels()){
			ever.add(p1);
			int ip1 = cap.parcels().get(p1);
			for(Parcel p2 : cap.allocator().parcels()){
				if(!ever.contains(p2)){
					if(location().contains(p1) && location().contains(p2)){
						int ip2 = cap.parcels().get(p2);
						vars[i] = (BoolVar) VF.bool("c_"+ic1+"_c_"+ic2+"_p_"+ip1+"_p_"+ip2, cap.solver());
						LCF.ifThenElse(
								LCF.or(
										LCF.and(cap.coversAndParcels(ic1, ip1), cap.coversAndParcels(ic2, ip2)),
										LCF.and(cap.coversAndParcels(ic1, ip2), cap.coversAndParcels(ic2, ip1))),  
								ICF.arithm(vars[i], "=", 1),
								ICF.arithm(vars[i], "=", 0));
					}else{
						vars[i] = VF.zero(cap.solver());
					}
					i++;
				}
			}
		}
		
		IntVar edgeLength = VariableFactory.bounded("e_c_"+ic1+"_c_"+ic1, 0, totlength, cap.solver());
		cap.solver().post(ICF.scalar(vars, edges, edgeLength));
		
		switch(mode()){
		case ONLY :
			post(cap, domain(), edgeLength);
			break;
		case NEVER : 
			post(cap, domain().inverse(), edgeLength);
			break;
		default : throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());
		}
	}

	private void post(CoverAllocationProblem cap, Domain<Integer, Integer> domain, IntVar edgeLength) {
		cap.solver().post(domain.postSpatialPattern(edgeLength));
	}

}
