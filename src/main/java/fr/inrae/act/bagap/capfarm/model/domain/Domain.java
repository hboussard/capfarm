package fr.inrae.act.bagap.capfarm.model.domain;

import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

public interface Domain<D, E> {

	int size();
	
	boolean accept(E e);
	
	Domain<D, E> inverse();
	
	D minimum();
	
	D maximum();

	Constraint postIntVar(IntVar intVar);
	
	Constraint postSpatialPattern(IntVar edgeLength);
}
