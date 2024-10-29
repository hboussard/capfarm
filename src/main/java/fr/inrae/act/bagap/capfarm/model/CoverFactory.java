package fr.inrae.act.bagap.capfarm.model;

import java.io.File;
import java.io.IOException;

import org.jumpmind.symmetric.csv.CsvReader;
import org.jumpmind.symmetric.csv.CsvWriter;

public class CoverFactory {

	public static void init(String types){
		File folder = new File(types);
		for(File f : folder.listFiles()){
			try {
				// initialisation des couverts
				//System.out.println(f.toString()+"/covers.txt");
				CsvReader cr = new CsvReader(f.toString()+"/covers.txt");
				cr.setDelimiter(';');
				cr.readHeaders();
				while(cr.readRecord()){
					CoverManager.getCoverUnit(cr.get("code"), cr.get("name"));
				}
				cr.close();
				
				if(new File(f.toString()+"/groups.txt").exists()){ // initialisation des groupes de couverts
					cr = new CsvReader(f+"/groups.txt");
					cr.setDelimiter(';');
					cr.readHeaders();
					while(cr.readRecord()){
						CoverManager.getCoverGroup(cr.get("code"), cr.get("name"), cr.get("covers"));
					}
					cr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void init(Covering system, String coverFile){
		init(system, coverFile, null);
	}
	
	public static void init(Covering system, String coverFile, String groupFile) {
		//System.out.println("initialisation des couverts");
		
		try {
			// initialisation des couverts
			CsvReader cr = new CsvReader(coverFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			while(cr.readRecord()){
				system.addCover(CoverManager.getCoverUnit(cr.get("code"), cr.get("name")));
			}
			cr.close();
			
			if(groupFile != null && !groupFile.equalsIgnoreCase("") && new File(groupFile).exists()){ // initialisation des groupes de couverts
				cr = new CsvReader(groupFile);
				cr.setDelimiter(';');
				cr.readHeaders();
				while(cr.readRecord()){
					system.addCover(CoverManager.getCoverGroup(cr.get("code"), cr.get("name"), cr.get("covers")));
				}
				cr.close();
			}else{
				//throw new IllegalArgumentException("group file :'"+groupFile+"' does not exists.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void export(String coverFile, String groupFile){
		exportCoverUnits(coverFile);
		exportCoverGroups(groupFile);
	}
	
	public static void exportCoverUnits(String coverFile){
		try {
			CsvWriter cw = new CsvWriter(coverFile);
			cw.setDelimiter(';');
	
			cw.write("code");
			cw.write("name");
			cw.endRecord();
			
			for(CoverUnit cu : CoverManager.coverUnits()){
				cw.write(cu.getCode());
				cw.write(cu.getName());
				cw.endRecord();
			}
			
			cw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void exportCoverGroups(String groupFile){
		try {
			CsvWriter cw = new CsvWriter(groupFile);
			cw.setDelimiter(';');
	
			cw.write("code");
			cw.write("name");
			cw.write("covers");
			cw.endRecord();
			
			for(CoverGroup cg : CoverManager.coverGroups()){
				cw.write(cg.getCode());
				cw.write(cg.getName());
				cw.write(cg.getCovers().toString().replaceAll("\\[", "{").replaceAll("\\]", "}").replaceAll(" ", ""));
				cw.endRecord();
			}
			
			cw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
