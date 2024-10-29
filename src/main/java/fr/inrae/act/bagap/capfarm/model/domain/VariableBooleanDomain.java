package fr.inrae.act.bagap.capfarm.model.domain;

import fr.inrae.act.bagap.capfarm.CAPFarm;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;

public class VariableBooleanDomain extends SimpleDomain<Boolean, Parcel>{
	
	private String variable;
	
	public VariableBooleanDomain(String variable, boolean bool) {
		super(bool);
		this.variable = variable;
	}
	
	@Override
	public String toString(){
		return variable+" = "+value();
	}
	
	@Override
	public boolean accept(Parcel p) {
		if(p.hasAttribute(variable)){
			return value() == CAPFarm.parseBoolean((String) p.getAttribute(variable).getValue(null));
		}
		return false;
	}

	@Override
	public Domain<Boolean, Parcel> inverse() {
		return new VariableBooleanDomain(variable, !value());
	}

}
