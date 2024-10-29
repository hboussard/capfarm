package fr.inrae.act.bagap.capfarm.model;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class CoverManager {

	private static Map<String, CoverUnit> coverUnits = new TreeMap<String, CoverUnit>();
	
	private static Map<String, CoverGroup> coverGroups = new TreeMap<String, CoverGroup>();
	
	public static boolean hasCover(String code){
		if(coverUnits.containsKey(code) || coverGroups.containsKey(code)){
			return true;
		}
		return false;
	}
	
	public static Cover getCover(String code){
		if(coverUnits.containsKey(code)){
			return coverUnits.get(code);
		}
		return coverGroups.get(code);
	}
	
	public static CoverUnit getCoverUnit(String code){
		return coverUnits.get(code);
	}
	
	public static int getCoverIndex(String code){
		int i=1;
		for(String kc : coverUnits.keySet()){
			if(kc.equalsIgnoreCase(code)){
				return i;
			}
			i++;
		}
		throw new IllegalArgumentException();
	}
	
	public static CoverUnit getCoverUnit(String code, String name){
		if(!coverUnits.containsKey(code)){
			CoverUnit cu = new CoverUnit(code, name);
			coverUnits.put(code, cu);
		}
		return coverUnits.get(code);
	}
	
	public static Cover getCoverGroup(String code, String name, String covers) {
		//System.out.println(code+" "+name+" "+covers);
		if(!coverGroups.containsKey(code)){
			CoverGroup cg = new CoverGroup(code, name, initCovers(covers));
			coverGroups.put(code, cg);
		}
		
		for(String c : covers.replace("{", "").replace("}", "").split(",")){
			if(!coverGroups.get(code).contains(c)){
				coverGroups.get(code).add(getCoverUnit(c));
			}
		}
		return coverGroups.get(code);
	}
	
	private static Set<CoverUnit> initCovers(String covers) {
		Set<CoverUnit> group = new TreeSet<CoverUnit>();
		String[] cc = covers.replace("{", "").replace("}", "").split(",");
		for(String c : cc){
			if(!c.equalsIgnoreCase("")){
				group.add((CoverUnit) getCoverUnit(c)); 
			}
		}
		return group;
	}
	
	public static Set<CoverUnit> coverUnits(){
		Set<CoverUnit> cu = new TreeSet<CoverUnit>();
		for(Entry<String, CoverUnit> e : coverUnits.entrySet()){
			cu.add(e.getValue());
		}
		return cu;
	}
	
	public static Set<CoverGroup> coverGroups(){
		Set<CoverGroup> cg = new TreeSet<CoverGroup>();
		for(Entry<String, CoverGroup> e : coverGroups.entrySet()){
			cg.add(e.getValue());
		}
		return cg;
	}
	
	public static void display(){
		System.out.println();
		for(Entry<String, CoverUnit> e : coverUnits.entrySet()){
			System.out.println(e.getKey()+" "+e.getValue());
		}
		for(Entry<String, CoverGroup> e : coverGroups.entrySet()){
			System.out.println(e.getKey()+" "+e.getValue());
		}
	}
	
}
