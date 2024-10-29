package fr.inrae.act.bagap.capfarm.model.domain;

import java.util.Set;

import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

public class SetDomain<D> implements Domain<D, D> {

	private Set<D> set;
	
	public SetDomain(Set<D> set) {
		this.set = set;
	}

	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(D d : set){
			sb.append(d.toString()+" ");
		}
		return sb.toString();
	}
	
	@Override
	public int size() {
		return set.size();
	}

	@Override
	public boolean accept(D e) {
		return set.contains(e);
	}
	
	public D getSingle(){
		return set.iterator().next();
	}
	
	public Set<D> set(){
		return set;
	}

	@Override
	public Domain<D, D> inverse() {
		throw new UnsupportedOperationException();
	}

	@Override
	public D minimum() {
		throw new UnsupportedOperationException();
	}

	@Override
	public D maximum() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Constraint postIntVar(IntVar intVar) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Constraint postSpatialPattern(IntVar coverArea) {
		throw new UnsupportedOperationException();
	}
}
