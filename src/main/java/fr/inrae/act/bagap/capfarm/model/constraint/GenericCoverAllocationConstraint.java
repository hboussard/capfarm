package fr.inrae.act.bagap.capfarm.model.constraint;

public class GenericCoverAllocationConstraint implements Comparable<GenericCoverAllocationConstraint> {

	private String code;
	
	private String[] covers;
	
	private String location;
	
	private ConstraintType type;
	
	private ConstraintMode mode;
	
	private String domain;
	
	private String[] params;
	
	public GenericCoverAllocationConstraint(String code, ConstraintType type, ConstraintMode mode, String[] covers, String location, String domain, String... params){
		this.code = code;
		this.covers = covers;
		this.location = location;
		this.type = type;
		this.mode = mode;
		this.domain = domain;
		this.params = params;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(String c : covers){
			sb.append(c+", ");
		}
		String covs = sb.substring(0, sb.length()-2);
		return "constraint '"+code+"' for "+covs+" : "+type+" "+mode+" in domain "+domain;
	}

	public String getCode() {
		return code;
	}

	public String[] getCovers() {
		return covers;
	}

	public String getLocation() {
		return location;
	}

	public ConstraintType getType() {
		return type;
	}
	
	public ConstraintMode getMode() {
		return mode;
	}
	
	public String getDomain() {
		return domain;
	}

	public String[] getParams() {
		return params;
	}

	@Override
	public int compareTo(GenericCoverAllocationConstraint o) {
		return code.compareTo(o.code);
	}
	
	
}
