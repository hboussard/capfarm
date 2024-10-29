package fr.inrae.act.bagap.capfarm.csp;

import java.util.HashMap;
import java.util.Map;

public class ProbaTimeManager {

	private static Map<String, Map<Integer, Map<Integer, Double>>> probas;
	
	private static Map<String, Map<Integer, Map<Integer, Double>>> cumuls;
	
	public static void initProbaTimes(){
		probas = new HashMap<String, Map<Integer, Map<Integer, Double>>>();
		cumuls = new HashMap<String, Map<Integer, Map<Integer, Double>>>();
	}
	
	public static void addProbaType(String probaType){
		probas.put(probaType, new HashMap<Integer, Map<Integer, Double>>());
		cumuls.put(probaType, new HashMap<Integer, Map<Integer, Double>>());
	}
	
	public static void addProbaTime(String probaType, int time){
		probas.get(probaType).put(time, new HashMap<Integer, Double>());
		cumuls.get(probaType).put(time, new HashMap<Integer, Double>());
	}
	
	public static void addProbaTimes(String probaType, int time, String[] pb){
		int index = 0;
		double cumul = 0.0;
		double proba;
		for(String p : pb){
			proba = Double.parseDouble(p);
			probas.get(probaType).get(time).put(index, proba);
			cumuls.get(probaType).get(time).put(index, cumul);
			cumul += proba;
			index++;
		}
	}
	
	public static double getProba(String probaType, int time, int nbYear){
		//System.out.println(probaType+" "+time+" "+nbYear);
		return probas.get(probaType).get(time).get(nbYear);
		/*
		try{
			return probas.get(probaType).get(time).get(nbYear);
		}catch(Exception ex){
			return 10.0;
		}
		*/
	}
	
	public static double getCumul(String probaType, int time, int nbYear){
		return cumuls.get(probaType).get(time).get(nbYear);
		/*
		try{
			return cumuls.get(probaType).get(time).get(nbYear);
		}catch(Exception ex){
			return 10.0;
		}
		*/
	}
	
}
