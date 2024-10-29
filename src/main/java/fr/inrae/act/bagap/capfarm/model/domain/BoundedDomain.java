package fr.inrae.act.bagap.capfarm.model.domain;

public class BoundedDomain<D extends Number, E extends Number> extends AndDomain<D, E> {
	
	public BoundedDomain(String op1, D v1, String op2, D v2) {
		super(new NumberDomain<D, E>(op1, v1), new NumberDomain<D, E>(op2, v2));
	}

	@Override
	public D minimum(){
		return ((NumberDomain<D, E>) dA()).value();
	}
	
	@Override
	public D maximum(){
		return ((NumberDomain<D, E>) dB()).value();
	}
	
}
