package fr.inrae.act.bagap.capfarm.model;

import java.io.Serializable;

public abstract class Cover implements Serializable, Comparable<Cover> {

	private static final long serialVersionUID = 1L;

	private String code;
	
	private String name;
	
	public Cover(String code, String name){
		this.code = code;
		this.name = name;
	}
	
	@Override
	public String toString(){
		return getCode();
	}
	
	@Override
	public int hashCode() {
		return code.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Cover){
			return code.equals(((Cover) other).code);
		}
		return super.equals(other);
	}
	
	@Override
	public int compareTo(Cover other) {
		return code.compareTo(other.code);
	}
	
	public String getCode(){
		return code;
	}
	
	public String getName(){
		return name;
	}
	
	public static Cover get(String code){
		return CoverManager.getCover(code);
	}
	
	public static boolean has(String code){
		return CoverManager.hasCover(code);
	}
	
}
