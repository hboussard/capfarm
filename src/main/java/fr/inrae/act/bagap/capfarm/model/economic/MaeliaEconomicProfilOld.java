package fr.inrae.act.bagap.capfarm.model.economic;

import fr.inrae.act.bagap.capfarm.model.CoverUnit;

public class MaeliaEconomicProfilOld extends EconomicProfil {
	
    private CoverUnit[] covers;
	
	private int[] yields; // tonnes par hectare
	
	private double[] prices; // euros par tonne
	
	private double[] charges; // euros par hectare
	
	private int[] bonus; // euro par hectare
	
	public MaeliaEconomicProfilOld(CoverUnit[] covers, int[] yields, double[] prices, double[] charges, int[] bonus/*, double[] works*/){
		this.covers = covers;
		this.yields = yields;
		this.prices = prices;
		this.charges = charges;
		this.bonus = bonus;
		//this.works = works;
	}

	@Override
	public CoverUnit[] getCovers() {
		return covers;
	}

	public int[] getYields() {
		return yields;
	}

	public double[] getPrices() {
		return prices;
	}

	public double[] getCharges() {
		return charges;
	}

	public int[] getBonus() {
		return bonus;
	}
	
	public void display(int[] areas){
		int profit_total = 0;
		for(int i=0; i<getCovers().length; i++){
			//System.out.println(covers[i].getName()+", rendement = "+yields[i]+"kg/ha et prix = "+prices[i]+"�/t ==> profit = "+profit(i)+" milliemes d'euro/mcarre");
			//System.out.println("surface = "+areas[i]);
			profit_total += areas[i]*profit(i);
		}
		System.out.println("profit total = "+profit_total);
	}
	
	protected int profit(int index){
		return (int)((getYields()[index] * (getPrices()[index] / 10.0)) - getCharges()[index] + getBonus()[index]) ;
	}
	
	
}