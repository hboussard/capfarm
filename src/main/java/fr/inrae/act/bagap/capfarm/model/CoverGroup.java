package fr.inrae.act.bagap.capfarm.model;

import java.util.Iterator;
import java.util.Set;

public class CoverGroup extends Cover implements Iterable<CoverUnit> {
	
	private static final long serialVersionUID = 1L;

	private Set<CoverUnit> covers;
	
	public CoverGroup(String code, String name, Set<CoverUnit> covers) {
		super(code, name);
		this.covers = covers;
	}
	
	public void add(CoverUnit cover){
		covers.add(cover);
	}

	public Set<CoverUnit> getCovers() {
		return covers;
	}
	
	public boolean contains(Cover cover){
		return covers.contains(cover);
	}
	
	public boolean contains(String cover){
		for(Cover c : this){
			if(c.getCode().equalsIgnoreCase(cover)){
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterator<CoverUnit> iterator() {
		return covers.iterator();
	}
	
	public int size(){
		return covers.size();
	}
	
}
