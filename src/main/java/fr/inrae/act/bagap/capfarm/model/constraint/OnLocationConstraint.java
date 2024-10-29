package fr.inrae.act.bagap.capfarm.model.constraint;

import java.util.Set;
import org.chocosolver.solver.constraints.ICF;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class OnLocationConstraint extends CoverAllocationConstraint<Integer, Integer> {

	private static final long serialVersionUID = 1L;

	public OnLocationConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> parcels) {
		super(code, checkOnly, ConstraintType.OnLocation, mode, covers, parcels, null);
	}

	@Override
	public void post(CoverAllocationProblem cap){
		
		switch(mode()){
		case ONLY : // les couverts s�lectionn�s sont seulement sur ces parcelles
					// = les couverts s�lectionn�s ne peuvent pas �tre ailleurs
			for(Parcel p : cap.allocator().parcels()){
				if(!location().contains(p)){
					int ip = cap.parcels().get(p);
					for(CoverUnit c : covers()){
						int ic = cap.covers().get(c);
						cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0));
					}
				}
			}
			break;
		case NEVER : // les couverts s�lectionn�s ne sont jamais sur ces parcelles
			for(Parcel p : location()){
				int ip = cap.parcels().get(p);
				for(CoverUnit c : covers()){
					int ic = cap.covers().get(c);
					cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0));
				}
			}
			break;
		case ALWAYS : // les couverts s�lectionn�s sont les seuls sur ces parcelles
					// = les autres couverts ne sont pas sur ces parcelles
					// les couverts s�lectionn�s peuvent �tre aussi ailleurs
			for(Parcel p : location()){
				int ip = cap.parcels().get(p);
				for(CoverUnit cu : cap.allocator().coverUnits()){
					if(!covers().contains(cu)){
						int ic = cap.covers().get(cu);
						cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0));
					}
				}
			}
			break;
		case AROUND : // les couverts s�lectionn�s sont partout � l'ext�rieur de ces parcelles
					// = les autres couverts ne sont pas sur les autres parcelles
			for(Parcel p : cap.allocator().parcels()){
				if(!location().contains(p)){
					int ip = cap.parcels().get(p);
					for(CoverUnit cu : cap.allocator().coverUnits()){
						if(!covers().contains(cu)){
							int ic = cap.covers().get(cu);
							cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0));
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
		sb.append("on location ");	
		if(verbose){
			if(ok){
				sb.append("GOOD : cover "+covers().toString()+" is "+mode()+" on "+domain());
			}
			System.out.println(sb.toString());
		}
		return ok;
	}
}
