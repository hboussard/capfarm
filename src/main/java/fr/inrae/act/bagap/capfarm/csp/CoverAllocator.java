package fr.inrae.act.bagap.capfarm.csp;

import java.util.Map;
import java.util.Set;
import fr.inrae.act.bagap.capfarm.model.CoverGroup;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.Covering;
import fr.inrae.act.bagap.capfarm.model.ConstraintSystem;
import fr.inrae.act.bagap.capfarm.model.constraint.CoverAllocationConstraint;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.element.DynamicElement;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public interface CoverAllocator extends Covering {

	String getCode();
	
	DynamicElement getTerritory();
	
	void setTerritory(DynamicElement element);
	
	int getArea();
	
	Set<CoverUnit> coverUnits();
	
	CoverUnit getCoverUnit(String code);
	
	Set<CoverGroup> coverGroups();
	
	Set<Parcel> parcels();
	
	Parcel parcel(String id);
	
	Set<CoverAllocationConstraint<?,?>> getConstraints();
	
	int totalParcelsArea();
	
	int totalEdgesLength();
	
	int[] edgesLength();
	
	void addConstraint(CoverAllocationConstraint<?, ?> constraint);
	
	int getDistanceFromFacilitiesToParcel(String facilities, Parcel parcel);
	
	void clearParcels();

	ConstraintSystem getConstraintSystem();
	
	void clearConstraintSystem();
	
	//void setGenericFarmingSystem(GenericFarmingSystem system);
	
	void checkConstraintSystem(Instant start, Instant end, boolean verbose);
	
	boolean hasMemory();
	
	int getMemory();
	
	void setMemory(int memory);
	
	void setMemory(boolean memory);
	
	// test de la procedure de FixedAllocationProblem
	Map<Parcel, CoverUnit> getSolution();
	
	void setSolution(Map<Parcel, CoverUnit> solution);

	Set<CoverUnit> historicalCovers();
	
}
