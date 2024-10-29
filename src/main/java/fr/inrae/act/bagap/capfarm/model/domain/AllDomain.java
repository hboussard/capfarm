package fr.inrae.act.bagap.capfarm.model.domain;

import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

public class AllDomain extends SimpleDomain {

	public AllDomain() {
		super(null);
	}

	@Override
	public boolean accept(Object e) {
		return true;
	}

	@Override
	public Domain inverse() {
		return new NullDomain();
	}

	@Override
	public Constraint postIntVar(IntVar intVar) {
		// TODO
		return null;
	}

}
