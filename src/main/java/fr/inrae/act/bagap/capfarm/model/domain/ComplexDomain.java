package fr.inrae.act.bagap.capfarm.model.domain;

public abstract class ComplexDomain<D, E> implements Domain<D, E> {

	private Domain<D, E> dA;
	
	private Domain<D, E> dB;

	protected Domain<D, E> inverse;
	
	public ComplexDomain(Domain<D, E> dA, Domain<D, E> dB){
		this.dA = dA;
		this.dB = dB;
	}
	
	public ComplexDomain(Domain<D, E> dA, Domain<D, E> dB, Domain<D, E> inverse){
		this(dA, dB);
		this.inverse = inverse;
	}
	
	protected Domain<D, E> dA(){
		return dA;
	}
	
	protected Domain<D, E> dB(){
		return dB;
	}
	
	@Override
 	public int size() {
		throw new UnsupportedOperationException();
	}

	@Override
	public D minimum(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public D maximum(){
		throw new UnsupportedOperationException();
	}

}
