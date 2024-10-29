package fr.inrae.act.bagap.capfarm.model.domain;

import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.LCF;
import org.chocosolver.solver.variables.IntVar;

/**
 * AND = Intersection de 2 sous-ensembles
 * @author sad48
 *
 * @param <D>
 * @param <E>
 */
public class AndDomain<D, E> extends ComplexDomain<D, E> {
	
	public AndDomain(Domain<D, E> dA, Domain<D, E> dB) {
		super(dA, dB);
	}
	
	public AndDomain(Domain<D, E> dA, Domain<D, E> dB, Domain<D,E> inverse) {
		super(dA, dB, inverse);
	}

	@Override
	public String toString(){
		return dA()+" AND "+dB();
	}
	
	@Override
	public boolean accept(E e) {
		return dA().accept(e) && dB().accept(e);
	}
	
	@Override
	public Domain<D, E> inverse() {
		if(inverse == null){
			inverse = new OrDomain<D, E>(dA().inverse(), dB().inverse(), this);
		}
		return inverse;
	}
	
	@Override
	public Constraint postIntVar(IntVar intVar){
		Constraint cdA = dA().postIntVar(intVar);
		Constraint cdB = dB().postIntVar(intVar);
		return LCF.and(cdA, cdB);
	}
	
	@Override
	public Constraint postSpatialPattern(IntVar coverArea){
		Constraint cdA = dA().postSpatialPattern(coverArea);
		Constraint cdB = dB().postSpatialPattern(coverArea);
		return LCF.and(cdA, cdB);
	}
	
}
