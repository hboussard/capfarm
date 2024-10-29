package fr.inrae.act.bagap.capfarm.model;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import fr.inrae.act.bagap.capfarm.model.constraint.GenericCoverAllocationConstraint;

public class GenericConstraintSystem implements Covering {

	private String name;
	
	private Set<Cover> covers;
	
	private Map<String, GenericCoverAllocationConstraint> constraints;
	
	public GenericConstraintSystem(String name){
		this.name = name;
		this.covers = new TreeSet<Cover>();
		this.constraints = new TreeMap<String, GenericCoverAllocationConstraint>();
	}

	public String toString(){
		return getName();
	}
	
	public void display(){
		for(GenericCoverAllocationConstraint gc : constraints.values()){
			System.out.println(gc);
		}
	}
	
	public String getName(){
		return name;
	}
	
	public Set<Cover> getCovers() {
		return covers;
	}

	public Collection<GenericCoverAllocationConstraint> getConstraints() {
		return constraints.values();
	}
	
	public Collection<GenericCoverAllocationConstraint> getConstraintsMultipleCovers() {
		Collection<GenericCoverAllocationConstraint> cons = new TreeSet<GenericCoverAllocationConstraint>();
		for(GenericCoverAllocationConstraint gc : constraints.values()){
			if(gc.getCovers().length > 1 || gc.getCovers()[0].equalsIgnoreCase("ALL") || gc.getCovers()[0].equalsIgnoreCase("EACH")){
				cons.add(gc);
			}
		}
		return cons;
	}
	
	public Collection<GenericCoverAllocationConstraint> getConstraints(Cover c) {
		Collection<GenericCoverAllocationConstraint> cons = new TreeSet<GenericCoverAllocationConstraint>();
		for(GenericCoverAllocationConstraint gc : constraints.values()){
			if(gc.getCovers().length == 1 && gc.getCovers()[0].equals(c.getCode())){
				cons.add(gc);
			}
		}
		return cons;
	}

	@Override
	public void addCover(Cover c) {
		covers.add(c);
	}

	@Override
	public void addCovers(Collection<Cover> covers) {
		this.covers.addAll(covers);
	}
	
	public void addConstraint(GenericCoverAllocationConstraint constraint) {
		if(!constraints.containsKey(constraint.getCode())){
			constraints.put(constraint.getCode(), constraint);
			return; 
		}
		throw new IllegalArgumentException("constraint code '"+constraint.getCode()+"' already exists");
	}

}
