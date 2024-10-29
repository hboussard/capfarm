package fr.inrae.act.bagap.capfarm.model.economic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jumpmind.symmetric.csv.CsvReader;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;

public class EconomicProfilFactory {

	public static EconomicProfil create(CoverUnit[] covers){
		int[] yields = {5000, 5500, 5200, 5100, 5200, 5200, 5100, 5000, 5500, 5200};		
		double[] prices = {155.5, 165.5, 145, 145.5, 140.5, 175.5, 145.5, 145, 145.5, 140.5};
		double[] charges = {150, 160, 240, 200, 350, 370, 240, 240, 200, 350};
		int[] bonus = {50, 60, 10, 20, 150, 100, 24, 60, 10, 20};
		//double[] works = {10, 5, 5, 20, 5, 15, 7, 5, 15, 7};
		
		return new BareilleEconomicProfil(covers, yields, prices, charges, bonus);
	}
	
	public static EconomicProfil create(CoverUnit[] covers, String file){
		try {
			CsvReader cr = new CsvReader(file);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			Map<String, Integer> myields = new HashMap<String, Integer>();
			Map<String, Double> mprices = new HashMap<String, Double>();
			Map<String, Double> mcharges = new HashMap<String, Double>();
			Map<String, Integer> mbonus = new HashMap<String, Integer>();
			//Map<String, Double> mworks = new HashMap<String, Double>();
			
			while(cr.readRecord()){
				String c = cr.get("cover");
				myields.put(c, Integer.parseInt(cr.get("yield")));
				mprices.put(c, Double.parseDouble(cr.get("price")));
				mcharges.put(c, Double.parseDouble(cr.get("charge")));
				mbonus.put(c, Integer.parseInt(cr.get("bonus")));
				//mworks.put(c, Double.parseDouble(cr.get("work")));
			}
			
			int[] yields = new int[covers.length];
			double[] prices = new double[covers.length];
			double[] charges = new double[covers.length];
			int[] bonus = new int[covers.length];
			//double[] works = new double[covers.length];
			
			for(int i=0; i<covers.length; i++){
				yields[i] = myields.get(covers[i].getCode());
				prices[i] = mprices.get(covers[i].getCode());
				charges[i] = mcharges.get(covers[i].getCode());
				bonus[i] = mbonus.get(covers[i].getCode());
				//works[i] = mworks.get(covers[i].getCode());
			}
			
			EconomicProfil ep = new BareilleEconomicProfil(covers, yields, prices, charges, bonus);
			//EconomicProfil ep = new MaeliaEconomicProfilOld(covers, yields, prices, charges, bonus);
			
			cr.close();
			
			return ep;
		} catch (IOException  e) {
			e.printStackTrace();
		}
		
		throw new IllegalArgumentException();
	}
	
	public static EconomicProfil createMaelia(CoverUnit[] covers, String file){
		try {
			CsvReader cr = new CsvReader(file);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			Map<String, Integer[]> myields = new HashMap<String, Integer[]>();
			Map<String, Integer[]> mprices = new HashMap<String, Integer[]>();
			Map<String, Integer[]> mcharges = new HashMap<String, Integer[]>();
			Map<String, Integer> mbonus = new HashMap<String, Integer>();
			
			while(cr.readRecord()){
				String c = cr.get("cover");
				myields.put(c, new Integer[5]);
				myields.get(c)[0] = Integer.parseInt(cr.get("yield_N-5"));
				myields.get(c)[1] = Integer.parseInt(cr.get("yield_N-4"));
				myields.get(c)[2] = Integer.parseInt(cr.get("yield_N-3"));
				myields.get(c)[3] = Integer.parseInt(cr.get("yield_N-2"));
				myields.get(c)[4] = Integer.parseInt(cr.get("yield_N-1"));
				mprices.put(c, new Integer[3]);
				mprices.get(c)[0] = Integer.parseInt(cr.get("price_N-3"));
				mprices.get(c)[1] = Integer.parseInt(cr.get("price_N-2"));
				mprices.get(c)[2] = Integer.parseInt(cr.get("price_N-1"));
				mcharges.put(c, new Integer[3]);
				mcharges.get(c)[0] = Integer.parseInt(cr.get("charge_N-3"));
				mcharges.get(c)[1] = Integer.parseInt(cr.get("charge_N-2"));
				mcharges.get(c)[2] = Integer.parseInt(cr.get("charge_N-1"));
				mbonus.put(c, Integer.parseInt(cr.get("bonus_N-1")));
			}
			
			int[] yield_N_5 = new int[covers.length];
			int[] yield_N_4 = new int[covers.length];
			int[] yield_N_3 = new int[covers.length];
			int[] yield_N_2 = new int[covers.length];
			int[] yield_N_1 = new int[covers.length];
			int[] price_N_3 = new int[covers.length];
			int[] price_N_2 = new int[covers.length];
			int[] price_N_1 = new int[covers.length];
			int[] charge_N_3 = new int[covers.length];
			int[] charge_N_2 = new int[covers.length];
			int[] charge_N_1 = new int[covers.length];
			int[] bonus_N_1 = new int[covers.length];
			
			for(int i=0; i<covers.length; i++){
				yield_N_5[i] = myields.get(covers[i].getCode())[0];
				yield_N_4[i] = myields.get(covers[i].getCode())[1];
				yield_N_3[i] = myields.get(covers[i].getCode())[2];
				yield_N_2[i] = myields.get(covers[i].getCode())[3];
				yield_N_1[i] = myields.get(covers[i].getCode())[4];
				price_N_3[i] = mprices.get(covers[i].getCode())[0];
				price_N_2[i] = mprices.get(covers[i].getCode())[1];
				price_N_1[i] = mprices.get(covers[i].getCode())[2];
				charge_N_3[i] = mcharges.get(covers[i].getCode())[0];
				charge_N_2[i] = mcharges.get(covers[i].getCode())[1];
				charge_N_1[i] = mcharges.get(covers[i].getCode())[2];
				bonus_N_1[i] = mbonus.get(covers[i].getCode());
			}
			
			EconomicProfil ep = new MaeliaEconomicProfil(covers, yield_N_5, yield_N_4, yield_N_3, yield_N_2, yield_N_1, price_N_3, price_N_2, price_N_1, charge_N_3, charge_N_2, charge_N_1, bonus_N_1);
			
			cr.close();
			
			return ep;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		throw new IllegalArgumentException();
	}
	
	
}
