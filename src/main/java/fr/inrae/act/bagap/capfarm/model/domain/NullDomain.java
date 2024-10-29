package fr.inrae.act.bagap.capfarm.model.domain;

import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

public class NullDomain extends SimpleDomain {

	public NullDomain() {
		super(null);
	}

	@Override
	public boolean accept(Object e) {
		return false;
	}

	@Override
	public Domain inverse() {
		return new AllDomain();
	}

	@Override
	public Constraint postIntVar(IntVar intVar) {
		// TODO
		return null;
	}

}
