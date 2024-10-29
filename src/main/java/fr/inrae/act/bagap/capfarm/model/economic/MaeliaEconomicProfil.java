package fr.inrae.act.bagap.capfarm.model.economic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.inrae.act.bagap.capfarm.model.CoverUnit;

/**
 * � am�liorer en ne calculant qu'une seule fois pour chaque culture les profits pontentiels
 * @author H.Boussard
 *
 */
public class MaeliaEconomicProfil extends EconomicProfil {
	
	private CoverUnit[] covers;
	
	// les rendements des 5 derni�res ann�es
	private int[] yield_N_5, yield_N_4, yield_N_3, yield_N_2, yield_N_1;
	
	// les prix des 3 derni�res ann�es
	private int[] price_N_3, price_N_2, price_N_1;
	
	// les charges des 3 derni�res ann�es
	private int[] charge_N_3, charge_N_2, charge_N_1;
	
	// les primes de la derni�re ann�e
	private int[] bonus_N_1;
	
	public MaeliaEconomicProfil(CoverUnit[] covers, int[] yield_N_5, int[] yield_N_4, int[] yield_N_3, int[] yield_N_2,
			int[] yield_N_1, int[] price_N_3, int[] price_N_2, int[] price_N_1, int[] charge_N_3, int[] charge_N_2,
			int[] charge_N_1, int[] bonus_N_1) {
		this.covers = covers;
		this.yield_N_5 = yield_N_5;
		this.yield_N_4 = yield_N_4;
		this.yield_N_3 = yield_N_3;
		this.yield_N_2 = yield_N_2;
		this.yield_N_1 = yield_N_1;
		this.price_N_3 = price_N_3;
		this.price_N_2 = price_N_2;
		this.price_N_1 = price_N_1;
		this.charge_N_3 = charge_N_3;
		this.charge_N_2 = charge_N_2;
		this.charge_N_1 = charge_N_1;
		this.bonus_N_1 = bonus_N_1;
	}
	
	@Override
	public CoverUnit[] getCovers() {
		return covers;
	}

	@Override
	protected int profit(int index){
		//System.out.println((getSecondBestYield(index)+" "+(getSecondBestPrice(index))+" "+getSecondBestCharge(index)+" "+bonus_N_1[index]));
		//System.out.println((int)((getSecondBestYield(index) * (getSecondBestPrice(index) / 10.0)) - getSecondBestCharge(index) + bonus_N_1[index]));
		return (int)((getSecondBestYield(index) * (getSecondBestPrice(index) / 10.0)) - getSecondBestCharge(index) + bonus_N_1[index]) ;
	}
	
	private int getSecondBestYield(int index){
		return getBestOf(index, 2, yield_N_5, yield_N_4, yield_N_3, yield_N_2, yield_N_1);
	}
	
	private int getSecondBestPrice(int index){
		return getBestOf(index, 2, price_N_3, price_N_2, price_N_1);
	}
	
	private int getSecondBestCharge(int index){
		return getBestOf(index, 2, charge_N_3, charge_N_2, charge_N_1);
	}
	
	private int getBestOf(int index, int best, int[]... tabs){
		List<Integer> list = new ArrayList<Integer>();
		for(int[] tab : tabs){
			list.add(tab[index]);
		}
		Collections.sort(list);
		
		return list.get(list.size()-best);
	}
	
	@Override
	protected int[] getHistoricalProfit(int index) {
		int[] var = new int[10];
		var[0] = (int)((yield_N_5[index] * (price_N_2[index] / 10.0)) - charge_N_2[index] + bonus_N_1[index]);
		var[1] = (int)((yield_N_5[index] * (price_N_1[index] / 10.0)) - charge_N_1[index] + bonus_N_1[index]);
		var[2] = (int)((yield_N_4[index] * (price_N_2[index] / 10.0)) - charge_N_2[index] + bonus_N_1[index]);
		var[3] = (int)((yield_N_4[index] * (price_N_1[index] / 10.0)) - charge_N_1[index] + bonus_N_1[index]);
		var[4] = (int)((yield_N_3[index] * (price_N_2[index] / 10.0)) - charge_N_2[index] + bonus_N_1[index]);
		var[5] = (int)((yield_N_3[index] * (price_N_1[index] / 10.0)) - charge_N_1[index] + bonus_N_1[index]);
		var[6] = (int)((yield_N_2[index] * (price_N_2[index] / 10.0)) - charge_N_2[index] + bonus_N_1[index]);
		var[7] = (int)((yield_N_2[index] * (price_N_1[index] / 10.0)) - charge_N_1[index] + bonus_N_1[index]);
		var[8] = (int)((yield_N_1[index] * (price_N_2[index] / 10.0)) - charge_N_2[index] + bonus_N_1[index]);
		var[9] = (int)((yield_N_1[index] * (price_N_1[index] / 10.0)) - charge_N_1[index] + bonus_N_1[index]);
		
		return var;
	}
	
	
}