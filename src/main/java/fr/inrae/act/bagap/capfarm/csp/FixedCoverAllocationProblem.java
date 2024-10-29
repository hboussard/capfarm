package fr.inrae.act.bagap.capfarm.csp;

import java.util.Map;
import java.util.Map.Entry;

import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.variables.IntVar;

import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class FixedCoverAllocationProblem extends CoverAllocationProblem {

	private Map<Parcel, CoverUnit> fixed;
	
	public FixedCoverAllocationProblem(CoverAllocator allocator, Instant t, Map<Parcel, CoverUnit> fixed) {
		super(allocator, t);
		this.fixed = fixed;
	}

	@Override
	protected void postConstraints() {
		
		super.postConstraints();
		/*for(CoverAllocationConstraint<?,?> cst : allocator().getConstraints()){
			cst.setCheckOnly(true);
		}*/
		
		int ic;
		for(Entry<Parcel, Integer> p : parcels().entrySet()){
			//System.out.println(p.getKey()+" "+fixed.get(p.getKey())+" "+covers().size());
			ic = covers().get(fixed.get(p.getKey()));
			solver().post(ICF.arithm(coversAndParcels(ic, p.getValue()), "=", 1));
		}
	}
	
	@Override
	protected boolean solve() {
		int ip, ic;
		
		if(solver.findSolution()) {
			/*
			for(Variable v : solver.getVars()){
				if(v.getName().equalsIgnoreCase("a_cv_C05")){
					//System.out.println(v.getName()+" : "+((IntVar) v).getValue());
					allocator().getTerritory().getAttribute("prairies").setValue(time(), ((IntVar) v).getValue());
				}
			}*/
			for(Parcel p : parcels().keySet()){
				ip = parcels().get(p);
				if(((IntVar) parcelsImplantedCoverContinue[ip]).getValue() == 0){
					for(Cover c : covers().keySet()){
						ic = covers().get(c);	
						if(((IntVar) coversAndParcels[ic][ip]).getValue() == 1){
							//System.out.println("1 "+p.getId()+";"+c+";"+p.getArea());
							p.getAttribute("cover").setValue(time(), c);
							//System.out.println(p.getAttribute("cover").getValue(t));
							//p.getAttribute("cov").setValue(t, c.getCode());
							break;
						}
					}
				}
			}
			return true;
		}
		return false;
	}
	
}
