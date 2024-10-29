package fr.inrae.act.bagap.capfarm.model.domain;

import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.variables.IntVar;

public class NumberDomain<D extends Number, E extends Number> extends SimpleDomain<D, E> {
	
	private String operator;
	
	public NumberDomain(String operator, D value){
		super(value);
		this.operator = operator;
	}
	
	public NumberDomain(String operator, D value, Domain<D, E> inverse){
		super(value, inverse);
		this.operator = operator;
	}

	@Override
	public String toString(){
		return operator+" "+value();
	}
	
	@Override
	public boolean accept(Number v) {
		double vv = v.doubleValue();
		switch(operator){
		case "=" : return vv == value().doubleValue();
		case "!=" : return vv != value().doubleValue();
		case "<" : return vv < value().doubleValue();
		case ">" : return vv > value().doubleValue();
		case "<=" : return vv <= value().doubleValue();
		case ">=" : return vv >= value().doubleValue();
		default : throw new IllegalArgumentException();
		}
	}
	
	@Override
	public Domain<D, E> inverse() {
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
			inverse = new NumberDomain<D, E>(op, value(), this);
		}
		return inverse;
	}

	public String operator(){
		return operator;
	}
	
	@Override
	public Constraint postIntVar(IntVar intVar){
		return ICF.arithm(intVar, operator(), value().intValue());
	}
	
	@Override
	public Constraint postSpatialPattern(IntVar edgeLength){
		return ICF.arithm(edgeLength, operator(), value().intValue());
	}
	
}
