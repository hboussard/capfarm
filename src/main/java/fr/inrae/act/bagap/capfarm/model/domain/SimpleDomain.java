package fr.inrae.act.bagap.capfarm.model.domain;

import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

public abstract class SimpleDomain<D, E> implements Domain<D, E>{
	
	private D value;
	
	protected Domain<D, E> inverse;
	
	public SimpleDomain(D value){
		this.value = value;
	}
	
	public SimpleDomain(D value, Domain<D, E> inverse){
		this.value = value;
		this.inverse = inverse;
	}
	
	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}
	
	public D value(){
		return value;
	}

	@Override
	public D minimum(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public D maximum(){
		throw new UnsupportedOperationException();
	}

	@Override
	public Constraint postIntVar(IntVar intVar) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Constraint postSpatialPattern(IntVar edgeLength) {
		throw new UnsupportedOperationException();
	}

}
