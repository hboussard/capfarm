package fr.inrae.act.bagap.capfarm.model.economic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jumpmind.symmetric.csv.CsvReader;

import fr.inrae.act.bagap.capfarm.model.CoverUnit;

public class ManagmentProfilFactory {

	public static ManagmentProfil create(CoverUnit[] covers){
		int[] works = {10, 5, 5, 20, 5, 15, 7, 5, 15, 7};
		
		return new MaeliaManagmentProfil(covers, works);
	}
	
	public static MaeliaManagmentProfil create(CoverUnit[] covers, String workProfil){
		return new MaeliaManagmentProfil(covers, readWorks(covers, workProfil));
	}
	
	public static MaeliaManagmentProfil create(CoverUnit[] covers, String workProfil, String distanceCoversProfil){
		return new MaeliaManagmentProfil(covers, readWorks(covers, workProfil), readDistanceCovers(covers, distanceCoversProfil));
	}
	
	private static int[] readWorks(CoverUnit[] covers, String workProfil){
		try {
			CsvReader cr = new CsvReader(workProfil);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			Map<String, Integer> mworks = new HashMap<String, Integer>();
			
			while(cr.readRecord()){
				String c = cr.get("cover");
				mworks.put(c, Integer.parseInt(cr.get("work")));
			}
			
			int[] works = new int[covers.length];
			
			for(int i=0; i<covers.length; i++){
				works[i] = mworks.get(covers[i].getCode());
			}
			
			cr.close();
			
			return works;
		} catch (IOException  e) {
			e.printStackTrace();
		}
		
		throw new IllegalArgumentException();
	}
	
	private static int[][] readDistanceCovers(CoverUnit[] covers, String distanceCoversProfil){
		try {
			CsvReader cr = new CsvReader(distanceCoversProfil);
			cr.setDelimiter(';');
			cr.readHeaders();
			
			Map<String, Map<String, Integer>> mdistances = new HashMap<String, Map<String, Integer>>();
			
			while(cr.readRecord()){
				String c = cr.get("distance");
				mdistances.put(c, new HashMap<String, Integer>());
				for(CoverUnit cu : covers){
					mdistances.get(c).put(cu.getCode(), Integer.parseInt(cr.get(cu.getCode())));
				}
			}
			
			int[][] distanceCovers = new int[covers.length][covers.length];
			
			for(int i1=0; i1<covers.length; i1++){
				for(int i2=0; i2<covers.length; i2++){
					distanceCovers[i1][i2] = mdistances.get(covers[i1].getCode()).get(covers[i2].getCode());
				}
			}
			
			cr.close();
			
			return distanceCovers;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		throw new IllegalArgumentException();
	}
	
}
