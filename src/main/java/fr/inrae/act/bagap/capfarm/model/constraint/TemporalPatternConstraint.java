package fr.inrae.act.bagap.capfarm.model.constraint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.LCF;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.composition.Attribute;
import fr.inrae.act.bagap.apiland.core.composition.DynamicAttribute;
import fr.inrae.act.bagap.apiland.core.composition.TemporalValue;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class TemporalPatternConstraint extends CoverAllocationConstraint<Cover, Cover> {

	private static final long serialVersionUID = 1L;
	
	private Map<Integer, Cover> pattern;

	public TemporalPatternConstraint(String code, boolean checkOnly, ConstraintMode mode, Set<Cover> covers, Set<Parcel> parcels, Map<Integer, Cover> pattern) {
		super(code, checkOnly, ConstraintType.TemporalPattern, mode, covers, parcels, null);
		this.pattern = pattern;
	}

	@Override
	public void post(CoverAllocationProblem cap) {
		for(Parcel p : location()){
			int ip = cap.parcels().get(p);
			//Attribute<Cover> attribute = (Attribute<Cover>) p.getAttribute("cover");
			boolean hasPattern = true;
			
			if(((DynamicAttribute<CoverUnit>) p.getAttribute("cover")).getDynamics().size() >= pattern.size()){
				Iterator<TemporalValue<CoverUnit>> ite = ((DynamicAttribute<CoverUnit>) p.getAttribute("cover")).getDynamics().iteratorInverse();
				int ind = 0;
				while(ite.hasNext() && ind < pattern.size()){
					CoverUnit cu = ite.next().getValue();
					if(!cu.equals(pattern.get(ind++))){
						hasPattern = false;
						break;
					}
				}
			}else{
				hasPattern = false;
			}
			
			if(hasPattern){
				switch(mode()){
				case NEVER : 
					for(CoverUnit c : covers()){
						int ic = cap.covers().get(c);
						cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 0));
					}
					
					break;
				case ONLY :
					if(hasSingleCover()){
						int ic = cap.covers().get(getSingleCover());
						cap.solver().post(ICF.arithm(cap.coversAndParcels(ic, ip), "=", 1));
					}else{
						Constraint[] cons = new Constraint[covers().size()];
						int i = 0;
						for(CoverUnit c : covers()){
							int ic = cap.covers().get(c);
							cons[i++] = ICF.arithm(cap.coversAndParcels(ic, ip), "=", 1);
						}
						cap.solver().post(LCF.or(cons));
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
		List<List<CoverUnit>> sequences = new ArrayList<List<CoverUnit>>();
		for(Parcel p : location()){
			sequences.clear();
			for(TemporalValue<CoverUnit> tv : (DynamicAttribute<CoverUnit>) p.getAttribute("cover")){
				CoverUnit cu = tv.getValue();
				
				for(int i=0; i<sequences.size(); i++){
					List<CoverUnit> seq = sequences.get(i);
					int size = seq.size();
					
					if(size == pattern.size()){
						
						sequences.remove(i);
						i--;
						
						switch(mode()){
						case ONLY :
							if(!covers().contains(cu)){
								ok = false;
								if(verbose){
									sb.append("BAD : TemporalPattern \n");
								}else{
									return ok;
								}
							}
							break;
						case NEVER : 
							if(covers().contains(cu)){
								ok = false;
								if(verbose){
									sb.append("BAD : TemporalPattern \n");
								}else{
									return ok;
								}
							}
							break;
						default : 
							throw new IllegalArgumentException("mode "+mode()+" is not supported for constraint type "+type());
						}
					}else{
						
						if(pattern.get(pattern.size()-size-1).equals(cu)){
							
							List<CoverUnit> seq2 = new ArrayList<CoverUnit>();
							seq2.addAll(seq);
							seq2.add(cu);
							sequences.set(i, seq2);
							
						}else{
							sequences.remove(i);
							i--;
						}
					}
					
				}
				if(pattern.get(1).equals(cu)){
					List<CoverUnit> seq = new ArrayList<CoverUnit>();
					seq.add(cu);
					sequences.add(seq);
				}
			}
		}
		
		if(verbose){
			System.err.println(sb.toString());
		}
		return ok;	
	}
	
}