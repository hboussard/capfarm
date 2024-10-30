package fr.inrae.act.bagap.capfarm.csp;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverGroup;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.ConstraintSystem;
import fr.inrae.act.bagap.capfarm.model.constraint.CoverAllocationConstraint;
import fr.inrae.act.bagap.capfarm.model.territory.FarmTerritory;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.element.DynamicElement;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class CoverAllocatorComposite implements CoverAllocator {
	
	private ConstraintSystem system;
	
	private FarmTerritory territory;
	
	public CoverAllocatorComposite(){}
	
	@Override
	public Set<CoverUnit> coverUnits() {
		return system.coverUnits();
	}

	@Override
	public Set<CoverGroup> coverGroups() {
		return system.coverGroups();
	}
	
	@Override
	public Set<CoverAllocationConstraint<?, ?>> getConstraints() {
		return system.getConstraints();
	}
	
	@Override
	public void addConstraint(CoverAllocationConstraint<?, ?> constraint) {
		system.addConstraint(constraint);
	}
	
	@Override
	public Set<Parcel> parcels() {
		return territory.parcels();
	}

	@Override
	public int totalParcelsArea() {
		return territory.totalParcelsArea();
	}

	@Override
	public int totalEdgesLength() {
		return territory.totalEdgesLength();
	}

	@Override
	public int[] edgesLength() {
		return territory.edgesLength();
	}

	@Override
	public Parcel parcel(String id) {
		return (Parcel) territory.get(id);
	}

	public int getDistanceFromFacilitiesToParcel(String facilities, Parcel parcel){
		return territory.getDistanceFromFacilitiesToParcel(facilities, parcel);
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DynamicElement getTerritory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearParcels() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCover(Cover c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCovers(Collection<Cover> covers) {
		system.addCovers(covers);
	}

	@Override
	public int getArea() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ConstraintSystem getConstraintSystem() {
		return system;
	}

	@Override
	public CoverUnit getCoverUnit(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearConstraintSystem() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkConstraintSystem(Instant start, Instant end, boolean verbose) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasMemory() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getMemory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<Parcel, CoverUnit> getSolution() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSolution(Map<Parcel, CoverUnit> fixed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMemory(int memory) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void isMemory(boolean memory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTerritory(DynamicElement element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<CoverUnit> historicalCovers() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
