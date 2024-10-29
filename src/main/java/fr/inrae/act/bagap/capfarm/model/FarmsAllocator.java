package fr.inrae.act.bagap.capfarm.model;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocator;
import fr.inrae.act.bagap.capfarm.model.constraint.CoverAllocationConstraint;
import fr.inrae.act.bagap.capfarm.model.territory.AgriculturalArea;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.element.DynamicElement;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class FarmsAllocator implements CoverAllocator {

	private String code;
	
	private ConstraintSystem system;
	
	private AgriculturalArea territory;
	
	public FarmsAllocator(String code){
		this.code = code;
		this.system = new ConstraintSystem(code);
	}
	
	@Override
	public void setTerritory(DynamicElement element) {
		this.territory = (AgriculturalArea) element;
	}
	
	@Override
	public void addCover(Cover c) {
		system.addCover(c);
	}

	@Override
	public void addCovers(Collection<Cover> covers) {
		system.addCovers(covers);
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public DynamicElement getTerritory() {
		return territory;
	}

	@Override
	public int getArea() {
		return territory.totalParcelsArea();
	}

	@Override
	public Set<CoverUnit> coverUnits() {
		return system.coverUnits();
	}

	@Override
	public CoverUnit getCoverUnit(String code) {
		return system.getCoverUnit(code);
	}

	@Override
	public Set<CoverGroup> coverGroups() {
		return system.coverGroups();
	}

	@Override
	public Set<Parcel> parcels() {
		return territory.parcels();
	}

	@Override
	public Parcel parcel(String id) {
		return territory.parcel(id);
	}

	@Override
	public Set<CoverAllocationConstraint<?, ?>> getConstraints() {
		return system.getConstraints();
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
		throw new UnsupportedOperationException();
	}

	@Override
	public void addConstraint(CoverAllocationConstraint<?, ?> constraint) {
		system.addConstraint(constraint);
		constraint.setAllocator(this);
	}

	@Override
	public int getDistanceFromFacilitiesToParcel(String facilities, Parcel parcel) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearParcels() {
		territory.clearParcels();
	}

	@Override
	public ConstraintSystem getConstraintSystem() {
		return system;
	}

	@Override
	public void clearConstraintSystem() {
		this.system = new ConstraintSystem(code);
	}

	@Override
	public void checkConstraintSystem(Instant start, Instant end, boolean verbose) {
		for(CoverAllocationConstraint<?, ?> ca : system.getConstraints()){
			ca.check(start, end, true);
		}
	}

	@Override
	public boolean hasMemory() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMemory() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMemory(int memory) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMemory(boolean memory) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<Parcel, CoverUnit> getSolution() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSolution(Map<Parcel, CoverUnit> solution) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<CoverUnit> historicalCovers() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
