package fr.inrae.act.bagap.capfarm.model.economic;

import fr.inrae.act.bagap.capfarm.model.CoverUnit;

public class BareilleEconomicProfil extends EconomicProfil {
	
	private CoverUnit[] covers;
	
	private int[] yields; // tonnes par hectare
	
	private double[] prices; // euros par tonne
	
	private double[] charges; // euros par hectare
	
	private int[] bonus; // euro par hectare
	
	public BareilleEconomicProfil(CoverUnit[] covers, int[] yields, double[] prices, double[] charges, int[] bonus){
		this.covers = covers;
		this.yields = yields;
		this.prices = prices;
		this.charges = charges;
		this.bonus = bonus;
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

	// en millieme d'euros par mcarre
	@Override
	protected int profit(int index){
		//System.out.println(yields[index]+" "+prices[index]+" "+new Double(((prices[index] / 1000.0) * (yields[index] / 10000.0) * 1000.0)).intValue());
		//return new Double(((prices[index] / 1000.0) * (yields[index] / 10000.0) + (bonus[index] / 10000.0) - (charges[index] / 10000.0)) * 1000.0).intValue();
		//return new Double(((prices[index] / 1000.0) * (yields[index] / 10000.0 + (bonus[index] / 10000.0) - (charges[index] / 10000.0)) * 1000.0)).intValue();
		//return new Double(((prices[index] / 1000.0) * (yields[index] / 10000.0) * 1000.0)).intValue();
		//return 10;
		//return yields[index] / 10;
		return getYields()[index];
	}

	@Override
	public CoverUnit[] getCovers() {
		return covers;
	}
	
	
}
