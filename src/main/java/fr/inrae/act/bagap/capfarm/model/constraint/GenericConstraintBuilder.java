package fr.inrae.act.bagap.capfarm.model.constraint;

import fr.inrae.act.bagap.capfarm.model.GenericConstraintSystem;

public class GenericConstraintBuilder {
	
	private GenericConstraintSystem system;
	
	private String code;
	
	private String[] covers;
	
	private ConstraintType type;
	
	private ConstraintMode mode;
	
	private String location;
	
	private String domain;
	
	private String[] params;
	
	public GenericConstraintBuilder(GenericConstraintSystem system) {
		this.system = system;
		reset();
	}
	
	private void reset(){
		code = null;
		covers = null;
		type = null;
		mode = null;
		location = null;
		domain = null;
		params = null;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setCover(String... covers) {
		this.covers = covers;
	}
	
	public String getCover(){
		return covers[0];
	}

	public void setType(ConstraintType type) {
		this.type = type;
	}
	
	public void setType(String type) {
		this.type = ConstraintType.valueOf(type);
	}

	public void setMode(ConstraintMode mode) {
		this.mode = mode;
	}
	
	public void setMode(String mode) {
		this.mode = ConstraintMode.valueOf(mode);
	}
	
	public void setLocation(String location){
		this.location = location;
	}
	
	public void setDomain(String domain){
		this.domain = domain;
	}
	
	public void setParams(String... params) {
		this.params = params;
	}
	
	private void initBuild() {
		if(location == null){
			setLocation("");
		}
		if(covers == null){
			setCover("ALL");
		}
		if(mode == null){
			setMode(ConstraintMode.ONLY);
		}
	}
	
	public GenericCoverAllocationConstraint build(){
		initBuild();
		GenericCoverAllocationConstraint constraint = new GenericCoverAllocationConstraint(code, type, mode, covers, location, domain, params);
		system.addConstraint(constraint);
		reset();
		return constraint;
	}
	
}
