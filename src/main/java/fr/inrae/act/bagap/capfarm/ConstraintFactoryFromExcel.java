package fr.inrae.act.bagap.capfarm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jumpmind.symmetric.csv.CsvWriter;

import fr.inrae.act.bagap.capfarm.model.CoverFactory;
import fr.inrae.act.bagap.capfarm.model.CoverManager;
import fr.inrae.act.bagap.capfarm.model.ConstraintSystemFactory;
import fr.inrae.act.bagap.capfarm.model.GenericConstraintSystem;
import fr.inrae.act.bagap.capfarm.model.constraint.ConstraintType;
import fr.inrae.act.bagap.capfarm.model.constraint.GenericConstraintBuilder;

public class ConstraintFactoryFromExcel {

	private GenericConstraintSystem system;
	
	private GenericConstraintBuilder builder;
	
	private String conscode;
	
	private int iconscode;
	
	private String date, codeFarm, farmer, farmType, mail, address, tel;
	
	private int essai, nbYearHistoric, nbYearSimulation, nbSimulation;
	
	private String output;
	
	private Map<String, String> genericCovers;
	
	private List<String> covers;
	
	private Map<String, Set<String>> groups;
	
	private List<String> parcelles;
	
	//private boolean[][] next;
	
	private Map<String, Set<String>> zones;
	
	public ConstraintFactoryFromExcel(String path, String input){
		this.output = path;
		new File(output).mkdirs();
		integrate(path+input);
	}
	
	private void integrate(String input){
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new File(input));
			
			// 1. construire un genericfarmingsystem
			system = new GenericConstraintSystem("contraintes");
			builder = new GenericConstraintBuilder(system);
			conscode = "CO";
			iconscode = 1;
			
			integrateInfos(workbook);
			integrateGenericsCovers(workbook);
			integrateParcellaire(workbook);
			integrateCovers(workbook);
			integrateNextCovers(workbook);
			integrateGroups(workbook);
			integrateConstraints(workbook);
			
			writeInfos();
			writeCovers();
			writeCoverGroups();
			writeConstraints();
			
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} 
	}
	
	private void integrateInfos(XSSFWorkbook workbook){
		XSSFSheet sheet = workbook.getSheet("infos");
		
		codeFarm = sheet.getRow(0).getCell(1).getStringCellValue();
		farmer = sheet.getRow(1).getCell(1).getStringCellValue();
		farmType = sheet.getRow(2).getCell(1).getStringCellValue();
		mail = sheet.getRow(3).getCell(1).getStringCellValue();
		address = sheet.getRow(4).getCell(1).getStringCellValue();
		tel = sheet.getRow(5).getCell(1).getStringCellValue();
		essai = (int) sheet.getRow(6).getCell(1).getNumericCellValue();
		date = sheet.getRow(7).getCell(1).getStringCellValue();
		nbYearHistoric = (int) sheet.getRow(8).getCell(1).getNumericCellValue();
		nbYearSimulation = (int) sheet.getRow(9).getCell(1).getNumericCellValue();
		nbSimulation = (int) sheet.getRow(10).getCell(1).getNumericCellValue();
	}
	
	private void integrateGenericsCovers(XSSFWorkbook workbook){
		XSSFSheet sheet = workbook.getSheet("cultures");
		genericCovers = new TreeMap<String, String>();
		groups = new HashMap<String, Set<String>>();
		for(Row r : sheet){
			if(r.getRowNum() > 1){
				genericCovers.put(r.getCell(1).getStringCellValue(), r.getCell(0).getStringCellValue());
				if(r.getCell(3) != null){
					String group = r.getCell(3).getStringCellValue();
					if(!group.equalsIgnoreCase("")){
						if(!groups.containsKey(group)){
							groups.put(group, new HashSet<String>());
							
						}
						groups.get(group).add(r.getCell(0).getStringCellValue());
					}
				}
			}
		}
		
		/*
		for(Entry<String, Set<String>> g : groups.entrySet()){
			System.out.println(g.getKey()+" : "+g.getValue());
		}
		*/
	}
	
	private void integrateCovers(XSSFWorkbook workbook){
		XSSFSheet sheet = workbook.getSheet("cultures-txt");
			
		// r�cup�ration des cultures d'interet
		covers = new ArrayList<String>();
		for(Row r : sheet){
			if(r.getRowNum() > 1){
				String cover = r.getCell(2).getStringCellValue();
				if(genericCovers.containsKey(cover)){
					covers.add(cover);
					system.addCover(CoverManager.getCoverUnit(genericCovers.get(cover), cover));
				}
			}
			
		}
		/*
		for(Cell c : sheet.getRow(0)){
			String cover = c.getStringCellValue();
			if(genericCovers.keySet().contains(cover)){
				covers.add(cover);
				system.addCover(CoverManager.getCoverUnit(genericCovers.get(cover), cover));
			}
		}
		*/
	}
	
	private void integrateNextCovers(XSSFWorkbook workbook){
		
		XSSFSheet sheet = workbook.getSheet("suivants-txt");
		integrateSingleNextCovers(sheet, 1);
		
		sheet = workbook.getSheet("suivants-2-txt");
		integrateSingleNextCovers(sheet, 2);
		
		sheet = workbook.getSheet("suivants-3-txt");
		integrateSingleNextCovers(sheet, 3);
		
		sheet = workbook.getSheet("suivants-4-txt");
		integrateSingleNextCovers(sheet, 4);
		
		sheet = workbook.getSheet("suivants-5-txt");
		integrateSingleNextCovers(sheet, 5);
	}
	
	private void integrateSingleNextCovers(XSSFSheet sheet, int nb){
		
		// r�cup�ration de la localisation
		String location = sheet.getRow(0).getCell(0).getStringCellValue();
		
		// r�cup�ration des cultures d'interet
		List<String> mycovers = new ArrayList<String>();
		for(Cell c : sheet.getRow(0)){
			String cover = c.getStringCellValue();
			if(genericCovers.keySet().contains(cover)){
				mycovers.add(genericCovers.get(cover));
			}
		}
		
		if(mycovers.size() > 0){
			// r�cup�ration des pr�c�dents-suivants
			boolean[][] next = new boolean[mycovers.size()][mycovers.size()];
			for(int j=0; j<mycovers.size(); j++){
				Row r = sheet.getRow(j+1);
				for(int i=0; i<mycovers.size(); i++){
					next[j][i] = new Boolean(r.getCell(i+1).toString());
				}
			}
			
			writeNextCovers(mycovers, next, location, nb);
			
			builder.setCode(conscode+(iconscode++));
			builder.setLocation(location);
			builder.setType(ConstraintType.NextCover);
			//builder.setParams(output+"next-"+nb+"-"+location+".txt");
			builder.setParams(output+"next-"+nb+".txt");
			builder.build();
		}
	}
	
	private void writeNextCovers(List<String> mycovers, boolean[][] next, String location, int nb){
		// �criture du fichier pr�c�dents-suivants
		try {
			//CsvWriter cw2 = new CsvWriter(output+"next-"+nb+"-"+location+".txt");
			CsvWriter cw2 = new CsvWriter(output+"next-"+nb+".txt");
			cw2.setDelimiter(';');
			cw2.write("previous");
			for(String cover : mycovers){
				cw2.write(cover);
			}
			cw2.endRecord();
			for(int j=0; j<mycovers.size(); j++){
				cw2.write(mycovers.get(j));
				for(int i=0; i<mycovers.size(); i++){
					if(next[j][i]){
						cw2.write("1");
					}else{
						cw2.write("0");
					}
				}
				cw2.endRecord();
			}
			cw2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void integrateGroups(XSSFWorkbook workbook){
		// r�cup�ration des groupes
		for(Entry<String, Set<String>> e : groups.entrySet()){
			String mycovers = e.getValue().toString().replaceAll("\\[", "{").replaceAll("\\]", "}").replaceAll(" ", "");
			system.addCover(CoverManager.getCoverGroup(e.getKey(), e.getKey(), mycovers));
		}		
	}
	
	private void integrateParcellaire(XSSFWorkbook workbook){
		
		XSSFSheet sheet = workbook.getSheet("parcellaire");
		
		// r�cup�ration des entetes
		Map<String, Integer> entetes = new HashMap<String, Integer>();
		int i = 0;
		for(Cell c : sheet.getRow(0)){
			String entete = c.getStringCellValue();
			if(!entete.equalsIgnoreCase("")){
				entetes.put(entete, i);
				//System.out.println(entete);
			}
			i++;
		}
		
		// r�cup�ration des zonages
		zones = new HashMap<String, Set<String>>();
		boolean zonage;
		for(Entry<String, Integer> e : entetes.entrySet()){
			zonage = true;
			for(Row r : sheet){
				if(r.getRowNum() > 0){
					String v = r.getCell(e.getValue()).toString();
					if(!v.equalsIgnoreCase("0.0") && !v.equalsIgnoreCase("1.0")){
						zonage = false;
						break;
					}
				}
			}
			if(zonage){
				zones.put(e.getKey(), new HashSet<String>());
			}
		}
		
		// r�cup�ration des parcelles
		if(entetes.containsKey("id")){
			parcelles = new ArrayList<String>();
			Set<Set<String>> links = new HashSet<Set<String>>();
			for(Row r : sheet){
				if(r.getRowNum() > 0){
					String parcelle = r.getCell(entetes.get("id")).getStringCellValue();
					parcelles.add(parcelle);
					for(String z : zones.keySet()){
						if(r.getCell(entetes.get(z)).toString().equalsIgnoreCase("1.0")){
							zones.get(z).add(parcelle);
						}
					}
					if(entetes.containsKey("parcellink")){
						if(!r.getCell(entetes.get("parcellink")).toString().equalsIgnoreCase("")){
							boolean ever = false;
							for(Set<String> l : links){
								if(l.contains(parcelle)){
									ever = true;
									break;
								}
							}
							if(!ever){
								String[] linked = r.getCell(entetes.get("parcellink")).toString().split(",");
								Set<String> link = new HashSet<String>();
								link.add(parcelle);
								for(String s : linked){
									link.add(s.replace(".0", ""));
								}
								links.add(link);	
							}
						}
					}
				}
			}
			
			for(Set<String> l : links){
				builder.setCode(conscode+(iconscode++));
				builder.setLocation(l.toString().replace(" ", ""));
				builder.setType(ConstraintType.LinkedFields);
				builder.build();
			}
		}
		
		
		/*
		for(Entry<String, Set<String>> z : zones.entrySet()){
			System.out.println(z.getKey()+" : "+z.getValue());
		}
		*/
		
	}
	
	private void integrateConstraints(XSSFWorkbook workbook){
		
		XSSFSheet sheet = workbook.getSheet("contraintes");
		
		// r�cup�ration des entetes
		Map<String, Integer> entetes = new HashMap<String, Integer>();
		int i = 0;
		for(Cell c : sheet.getRow(0)){
			String entete = c.getStringCellValue();
			if(!entete.equalsIgnoreCase("")){
				entetes.put(entete, i);
				//System.out.println(entete);
			}
			i++;
		}
		
		// 2. utiliser un genericconstraintbuilder pour construire les contraintes
		for(Row r : sheet){
			if(r.getRowNum() > 0){
				String culture = r.getCell(0).getStringCellValue();
				if(culture.equalsIgnoreCase("")){
					break;
				}
				String parcelles = r.getCell(entetes.get("parcelles")).getStringCellValue();
				boolean locationConstraint = true;
				
				// gestion des delay
				String delai_min = r.getCell(entetes.get("delai_min")).toString();
				String delai_max = r.getCell(entetes.get("delai_max")).toString();
				//String delai_repart = r.getCell(4).toString();
				if(!delai_min.equalsIgnoreCase("") || !delai_max.equalsIgnoreCase("")){
					builder.setCode(conscode+(iconscode++));
					builder.setLocation(parcelles);
					if(genericCovers.get(culture) == null){
						builder.setCover(culture);
						builder.setParams(culture);
					}else{
						builder.setCover(genericCovers.get(culture));
						builder.setParams(genericCovers.get(culture));
					}
					builder.setType(ConstraintType.Delay);
					builder.setDomain("["+delai_min+","+delai_max+"]");
					builder.build();
					locationConstraint = false;
				}
	
				// gestion des repetitions
				String repetition_min = r.getCell(entetes.get("repetition_min")).toString();
				String repetition_max = r.getCell(entetes.get("repetition_max")).toString();
				String repetition_repart = r.getCell(entetes.get("repetition_repart")).toString();
				if(repetition_repart.equalsIgnoreCase("")){
					repetition_repart = "middle";
				}
				if(!repetition_min.equalsIgnoreCase("") || !repetition_max.equalsIgnoreCase("")){
					builder.setCode(conscode+(iconscode++));
					builder.setLocation(parcelles);
					if(genericCovers.get(culture) == null){
						builder.setCover(culture);
					}else{
						builder.setCover(genericCovers.get(culture));
					}
					builder.setType(ConstraintType.Repetition);
					builder.setDomain("["+repetition_min+","+repetition_max+"]");
					builder.setParams(repetition_repart);
					builder.build();
					locationConstraint = false;
				}
				
				// gestion des durees
				String duree_min = r.getCell(entetes.get("duree_min")).toString();
				String duree_max = r.getCell(entetes.get("duree_max")).toString();
				String duree_repart = r.getCell(entetes.get("duree_repart")).toString();
				if(duree_repart.equalsIgnoreCase("")){
					duree_repart = "middle";
				}
				if(!duree_min.equalsIgnoreCase("") || !duree_max.equalsIgnoreCase("")){
					builder.setCode(conscode+(iconscode++));
					builder.setLocation(parcelles);
					if(genericCovers.get(culture) == null){
						builder.setCover(culture);
					}else{
						builder.setCover(genericCovers.get(culture));
					}
					builder.setType(ConstraintType.Duration);
					builder.setDomain("["+duree_min+","+duree_max+"]");
					builder.setParams(duree_repart);
					builder.build();
					locationConstraint = false;
				}
				
				// gestion des surfaces totales
				String aire_min = r.getCell(entetes.get("aire_min")).toString();
				String aire_max = r.getCell(entetes.get("aire_max")).toString();
				if(!aire_min.equalsIgnoreCase("") || !aire_max.equalsIgnoreCase("")){
					builder.setCode(conscode+(iconscode++));
					builder.setLocation(parcelles);
					if(genericCovers.get(culture) == null){
						builder.setCover(culture);
					}else{
						builder.setCover(genericCovers.get(culture));
					}
					builder.setType(ConstraintType.TotalArea);
					builder.setDomain("["+aire_min+","+aire_max+"]");
					builder.build();
					locationConstraint = false;
				}
				
				// gestion des distances au si�ge
				String distance_siege_min = r.getCell(entetes.get("distance_siege_min")).toString();
				String distance_siege_max = r.getCell(entetes.get("distance_siege_max")).toString();
				if(!distance_siege_min.equalsIgnoreCase("") || !distance_siege_max.equalsIgnoreCase("")){
					builder.setCode(conscode+(iconscode++));
					builder.setLocation(parcelles);
					if(genericCovers.get(culture) == null){
						builder.setCover(culture);
					}else{
						builder.setCover(genericCovers.get(culture));
					}
					builder.setType(ConstraintType.DistanceFromFacilities);
					builder.setDomain("["+distance_siege_min+","+distance_siege_max+"]");
					builder.setParams("siege");
					builder.build();
					locationConstraint = false;
				}
				
				// gestion des surfaces de parcelles
				String surface_parcelle_min = r.getCell(entetes.get("surface_parcelle_min")).toString();
				String surface_parcelle_max = r.getCell(entetes.get("surface_parcelle_max")).toString();
				if(!surface_parcelle_min.equalsIgnoreCase("") || !surface_parcelle_max.equalsIgnoreCase("")){
					builder.setCode(conscode+(iconscode++));
					builder.setLocation(parcelles);
					if(genericCovers.get(culture) == null){
						builder.setCover(culture);
					}else{
						builder.setCover(genericCovers.get(culture));
					}
					builder.setType(ConstraintType.ParcelArea);
					builder.setDomain("["+surface_parcelle_min+","+surface_parcelle_max+"]");
					builder.build();
					locationConstraint = false;
				}
				
				// gestion des distances entre couverts
				String distance_entre_couverts_min = r.getCell(entetes.get("distance_entre_couverts_min")).toString();
				String distance_entre_couverts_max = r.getCell(entetes.get("distance_entre_couverts_max")).toString();
				if(!distance_entre_couverts_min.equalsIgnoreCase("") || !distance_entre_couverts_max.equalsIgnoreCase("")){
					builder.setCode(conscode+(iconscode++));
					builder.setLocation(parcelles);
					if(genericCovers.get(culture) == null){
						builder.setCover(culture);
						builder.setParams(culture);
					}else{
						builder.setCover(genericCovers.get(culture));
						builder.setParams(genericCovers.get(culture));
					}
					builder.setType(ConstraintType.DistanceBetweenCovers);
					builder.setDomain("["+distance_entre_couverts_min+","+distance_entre_couverts_max+"]");
					builder.build();
					locationConstraint = false;
				}
				
				// gestion des localisation de couvert
				if(locationConstraint){
					builder.setCode(conscode+(iconscode++));
					builder.setLocation(parcelles);
					if(genericCovers.get(culture) == null){
						builder.setCover(culture);
					}else{
						builder.setCover(genericCovers.get(culture));
					}
					builder.setType(ConstraintType.OnLocation);
					builder.build();
				}
			}
		}
	}
	
	private void writeInfos() {
		try{
			Properties properties = new Properties();
			
			properties.setProperty("date", date);
			properties.setProperty("code", codeFarm);
			properties.setProperty("exploitant", farmer);
			properties.setProperty("type_exploitation", farmType);
			properties.setProperty("mail", mail);
			properties.setProperty("adresse", address);
			properties.setProperty("tel", tel);
			properties.setProperty("version", essai+"");
			properties.setProperty("nb_annees_historique", nbYearHistoric+"");
			properties.setProperty("nb_annees_simulations", nbYearSimulation+"");
			properties.setProperty("nb_simulations", nbSimulation+"");
			
			
			FileOutputStream out = new FileOutputStream(output+"infos.txt");
			properties.store(out,"info file generated with CAPFarm");
			out.close();
		}catch(FileNotFoundException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		} 
	}
	
	private void writeCovers(){
		CoverFactory.exportCoverUnits(output+"covers.txt");
	}
	
	private void writeCoverGroups(){
		CoverFactory.exportCoverGroups(output+"groups.txt");
	}
	
	private void writeConstraints(){
		// 3. exporter le fichier via le farmingsystemfactory
		ConstraintSystemFactory.exportSystem(system, output+"contraintes_"+codeFarm+"_"+essai+".csv");
	}
	
}
