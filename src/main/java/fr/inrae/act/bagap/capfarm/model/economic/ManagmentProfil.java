package fr.inrae.act.bagap.capfarm.model.economic;

import java.util.Set;

import fr.inrae.act.bagap.capfarm.model.CoverUnit;

public abstract class ManagmentProfil {
	
	private CoverUnit[] covers;
	
	private int[] works; 
	// nb de jours travail / hectare / culture --> NON car pour la multiplication des scalaires a besoin d'entiers
	// nb d'heure / hectare / culture / historique de cultures --> � construire
	
	public ManagmentProfil(CoverUnit[] covers, int[] works){
		this.covers = covers;
		this.works = works;
	}

	public CoverUnit[] getCovers() {
		return covers;
	}

	public int[] getWorks() {
		return works;
	}
	
	public int work(CoverUnit cu, int area){
		int w = work(getCoverIndex(cu));
		//System.out.println(p +" "+ area+" "+(p*area));
		return w * new Double(area).intValue();
	}
		
	private int getCoverIndex(CoverUnit cu){
		for(int i=0; i<getCovers().length; i++){
			if(getCovers()[i].equals(cu)){
				return i;
			}
		}
		return -1;
	}
	
	public int[] works(){
		int[] works = new int[getCovers().length];
		for(int i=0; i<getCovers().length; i++){
			works[i] = (int) work(i);
		}
		return works;
	}
	
	public int[] works(Set<CoverUnit> cs){
		int[] works = new int[getCovers().length];
		for(int i=0; i<getCovers().length; i++){
			if(cs.contains(getCovers()[i])){
				works[i] = work(i);
			}else{
				works[i] = 0;
			}
		}
		return works;
	}
	
	protected abstract int work(int index);
	
	public void display(int[] areas){
		double work_total = 0;
		for(int i=0; i<getCovers().length; i++){
			work_total += areas[i]*work(i);
		}
		System.out.println("work total = "+work_total);
	}

}
