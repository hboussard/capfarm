package fr.inrae.act.bagap.capfarm.model.constraint;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.csp.CoverAllocator;
import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverGroup;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.domain.Domain;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public abstract class CoverAllocationConstraint<D, E> implements Serializable, Comparable<CoverAllocationConstraint<?,?>> {

	private static final long serialVersionUID = 1L;

	private CoverAllocator ca;
	
	private String code;
	
	private Set<CoverUnit> covers;
	
	private Set<Parcel> location; 
	
	private ConstraintType type;
	
	private ConstraintMode mode;
	
	private Domain<D, E> domain;
	
	private boolean checkOnly = false;
	
	public CoverAllocationConstraint(String code, boolean checkOnly, ConstraintType type, ConstraintMode mode, Set<Cover> covers, Set<Parcel> parcels, Domain<D, E> domain){
		this.code = code;
		this.checkOnly = checkOnly;
		this.type = type;
		this.mode = mode;
		this.covers = getCoverUnits(covers);
		this.location = new HashSet<Parcel>();
		this.location.addAll(parcels);
		this.domain = domain;
	}
	
	public void setCheckOnly(boolean checkOnly){
		this.checkOnly = checkOnly;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(Cover c : covers){
			sb.append(c.getCode()+", ");
		}
		String covs = sb.substring(0, sb.length()-2);
		
		StringBuilder sb2 = new StringBuilder();
		for(Parcel p : location){
			sb2.append(p+", ");
		}
		String parcels =  null;
		if(sb2.length() > 0){
			parcels = sb2.substring(0, sb2.length()-2);
		}
			
		//return "constraint '"+code+"' for "+covs+" in parcels "+parcels+" : "+type+" "+mode+" in domain "+domain;
		return "constraint '"+code+"' for "+covs+" : "+type+" "+mode+" in domain "+domain;
	}
		
	@Override
	public boolean equals(Object o) {
		if(o instanceof CoverAllocationConstraint){
			return code.equals(((CoverAllocationConstraint<?, ?>) o).code);	
		}
		return false;
	}

	public String code(){
		return code;
	}
	
	public boolean checkOnly(){
		return checkOnly;
	}
	
	public ConstraintType type() {
		return type;
	}
	
	public ConstraintMode mode() {
		return mode;
	}
	
	public Set<CoverUnit> covers() {
		return covers;
	}
	
	public boolean hasSingleCover(){
		return covers().size() == 1;
	}
	
	public CoverUnit getSingleCover(){
		return (CoverUnit) covers.iterator().next();
	}
	
	protected Set<CoverUnit> getCoverUnits(Set<Cover> covers){
		Set<CoverUnit> coverunits = new HashSet<CoverUnit>();
		for(Cover c : covers){
			if(c instanceof CoverUnit){
				coverunits.add((CoverUnit) c);
			}else{
				for(CoverUnit cu : (CoverGroup) c){
					coverunits.add((CoverUnit) cu);
				}
			}
		}
		return coverunits;
	}
	
	public Set<Parcel> location(){
		return location;
	}
	
	public Domain<D, E> domain(){
		return domain;
	}
	
	public abstract void post(CoverAllocationProblem cap);

	public boolean check(Instant start, Instant end, boolean verbose){ 
		return false;
	} 
	
	@Override
	public int compareTo(CoverAllocationConstraint<?, ?> o) {
		return code.compareTo(o.code);
	}
	
	public void setAllocator(CoverAllocator ca){
		this.ca = ca;
	}

	public CoverAllocator getAllocator(){
		return ca;
	}
	
}
