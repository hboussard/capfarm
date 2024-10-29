package fr.inrae.act.bagap.capfarm.model.domain;

import fr.inrae.act.bagap.capfarm.model.territory.Parcel;

public class VariableValueDomain<D extends Number> extends SimpleDomain<D, Parcel> {

	private String variable;
	
	private String operator;
	
	public VariableValueDomain(String variable, String operator, D value) {
		super(value);
		this.variable = variable;
		this.operator = operator;
	}
	
	public VariableValueDomain(String variable, String operator, D value, Domain<D, Parcel> inverse) {
		super(value, inverse);
		this.variable = variable;
		this.operator = operator;
	}

	@Override
	public String toString(){
		return variable+" "+operator+" "+value();
	}
	
	@Override
	public boolean accept(Parcel p) {
		if(p.hasAttribute(variable)){
			double v = ((Number) p.getAttribute(variable).getValue(null)).doubleValue();
			switch(operator){
			case "=" : return v == value().doubleValue();
			case "!=" : return v != value().doubleValue();
			case "<" : return v < value().doubleValue();
			case ">" : return v > value().doubleValue();
			case "<=" : return v <= value().doubleValue();
			case ">=" : return v >= value().doubleValue();
			default : throw new IllegalArgumentException();
			}
		}
		throw new IllegalArgumentException(variable+" do not exists for parcel "+p);
	}

	@Override
	public Domain<D, Parcel> inverse() {
		if(inverse == null){
			String op = "";
			switch(operator){
			case "=" : op = "!="; break;
			case "!=" : op = "="; break;
			case "<" : op = ">="; break;
			case ">" : op = "<="; break;
			case "<=" : op = ">"; break;
			case ">=" : op = "<"; break;
			default : throw new IllegalArgumentException();
			}
			inverse = new VariableValueDomain<D>(variable, op, value(), this);
		}
		return inverse;
	}


}
