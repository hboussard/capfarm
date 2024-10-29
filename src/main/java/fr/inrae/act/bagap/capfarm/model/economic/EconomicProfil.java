package fr.inrae.act.bagap.capfarm.model.economic;

import java.util.Set;

import fr.inrae.act.bagap.capfarm.model.CoverUnit;

public abstract class EconomicProfil {
	
	public abstract CoverUnit[] getCovers();
	
	private int getCoverIndex(CoverUnit cu){
		for(int i=0; i<getCovers().length; i++){
			if(getCovers()[i].equals(cu)){
				return i;
			}
		}
		return -1;
	}
	
	public int[] profits(){
		int[] profits = new int[getCovers().length];
		for(int i=0; i<getCovers().length; i++){
			profits[i] = profit(i);
		}
		return profits;
	}
	
	protected abstract int profit(int index);
	
	public int profit(CoverUnit cu, int area){
		int p = profit(getCoverIndex(cu));
		return (int)(p * area / 10000.0);
	}
	
	public int[] profits(Set<CoverUnit> cs){
		int[] profits = new int[getCovers().length];
		for(int i=0; i<getCovers().length; i++){
			if(cs.contains(getCovers()[i])){
				profits[i] = profit(i);
			}else{
				profits[i] = 0;
			}
		}
		return profits;
	}
	
	public int[][] getHistoricalProfits(){
		int[][] historicalProfits = new int[getCovers().length][10];
		for(int i=0; i<getCovers().length; i++){
			historicalProfits[i] = getHistoricalProfit(i);
		}
		return historicalProfits;
	}

	protected int[] getHistoricalProfit(int coverIndex) {
		throw new UnsupportedOperationException();
	}
	
	/*
	public int[] getHistoricalProfit(CoverUnit cu, int area){
		int[] p = new int[getCovers().length];
		int[] var = new int[p.length];
		for(int i=0; i<=p.length; i++){
			p[i] = getHistoricalProfit(i);
			var[i] = (int)(p[i] * area / 10000.0);
		}
		return var;
	}
	*/
}
