package fr.inrae.act.bagap.capfarm;

import fr.inrae.act.bagap.capfarm.model.CoverFactory;
import fr.inrae.act.bagap.capfarm.model.Farm;
import fr.inrae.act.bagap.capfarm.model.ConstraintSystemFactory;
import fr.inrae.act.bagap.capfarm.model.GenericConstraintSystem;
import fr.inrae.act.bagap.capfarm.model.constraint.ConstraintBuilder;
import fr.inrae.act.bagap.capfarm.model.constraint.ConstraintMode;
import fr.inrae.act.bagap.capfarm.model.constraint.ConstraintType;
import fr.inrae.act.bagap.capfarm.model.constraint.GenericConstraintBuilder;
import fr.inrae.act.bagap.capfarm.model.territory.Territory;
import fr.inrae.act.bagap.capfarm.model.territory.TerritoryFactory;
import fr.inrae.act.bagap.capfarm.simul.farm.CfmFarmManager;
import fr.inrae.act.bagap.capfarm.simul.farm.CfmFarmSimulator;
import fr.inrae.act.bagap.capfarm.simul.output.ConsoleOutput;
import fr.inrae.act.bagap.capfarm.simul.output.FarmShapefileOutput;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class Papier {

	private static final String path = "C:/Hugues/modelisation/capfarm/article/data/";
	private static Instant start = Instant.get(1, 7, 2010);
	private static Instant end = Instant.get(1, 7, 2019);
	
	public static void main(String[] args) {
		CAPFarm.t = start;
		scriptOneFarm();
	}
	
	private static void scriptOneFarm(){
		// cr�ation d'une ferme
		Farm farm = new Farm("153723");
		
		// int�gration du territoire d'une ferme
		Territory territory = TerritoryFactory.init(path+"sig/site_indre2.shp", start);
		//TerritoryFactory.init(territory, farm);
			
		// cr�ation du type "laitier" ou "porcin"
		//GenericFarmingSystem system = buildTypeLaitier();
		GenericConstraintSystem system = buildTypePorcin();
		
		// affectation de ce syst�me de production � une ferme
		new ConstraintBuilder(farm).build(system);
		
		// param�trisation du simulateur
		CfmFarmManager sm = new CfmFarmManager(farm, 1);
		sm.setPath(path);
		sm.setStart(start);
		sm.setEnd(end);
		sm.addOutput(new FarmShapefileOutput());
		sm.addOutput(new ConsoleOutput());
		
		// cr�ation et lancement du simulateur
		CfmFarmSimulator s = new CfmFarmSimulator(sm);
		s.allRun();
	}
	
	private static GenericConstraintSystem buildTypePorcin(){
		String type = "porcin";
		
		// cr�ation d'un type de ferme
		GenericConstraintSystem system = new GenericConstraintSystem("type");
		
		// int�gration des couverts
		CoverFactory.init(system, path+"type/"+type+"/covers.txt", null);
		
		// mise en place des contraintes
		GenericConstraintBuilder cb = new GenericConstraintBuilder(system);
					
		cb.setCode("C01");
		cb.setType(ConstraintType.Duration);
		cb.setDomain("[1,1]");
		cb.build();
		
		// contraintes sur la prairie permanente
		cb.setCode("C02"); 
		cb.setCover("P"); 	
		cb.setType(ConstraintType.ParcelArea);
		cb.setMode(ConstraintMode.ONLY);
		cb.setDomain("[,1]");
		cb.build();
		
		cb.setCode("C03"); 
		cb.setCover("P"); 	
		cb.setType(ConstraintType.ParcelArea);
		cb.setMode(ConstraintMode.ALWAYS);
		cb.setDomain("[,1]");
		cb.build();
		
		cb.setCode("C04");
		cb.setCover("P");
		cb.setType(ConstraintType.NextCover);
		cb.setDomain("[P]");
		cb.build();
		
		// contraintes sur le ma�s
		cb.setCode("C05"); 
		cb.setCover("M"); 	
		cb.setType(ConstraintType.TotalArea);
		cb.setDomain("[5%,]");
		cb.build();
		
		cb.setCode("C06");
		cb.setCover("M");
		cb.setType(ConstraintType.NextCover);
		cb.setDomain("[B]");
		cb.build();
					
		// contraintes sur le bl�
		cb.setCode("C07"); 
		cb.setCover("B"); 	
		cb.setType(ConstraintType.TotalArea);
		cb.setDomain("[10%,]");
		cb.build();

		cb.setCode("C08");
		cb.setCover("B");
		cb.setType(ConstraintType.NextCover);
		cb.setDomain("[M,O,C]");
		cb.build();
					
		// contraintes sur l'orge
		cb.setCode("C09");
		cb.setCover("O"); 	
		cb.setType(ConstraintType.TotalArea);
		cb.setDomain("[5%,]");
		cb.build();
		
		cb.setCode("C10");
		cb.setCover("O"); 	
		cb.setType(ConstraintType.Delay);
		cb.setDomain("[3,]");
		cb.setParams("O");
		cb.build();
		
		cb.setCode("C11");
		cb.setCover("O");
		cb.setType(ConstraintType.NextCover);
		cb.setDomain("[M,C]");
		cb.build();
		
		// contraintes sur le colza
		cb.setCode("C12");
		cb.setCover("C"); 	
		cb.setType(ConstraintType.TotalArea);
		cb.setDomain("[10%,]");
		cb.build();
		
		cb.setCode("C13");
		cb.setCover("C"); 	
		cb.setType(ConstraintType.Delay);
		cb.setDomain("[3,]");
		cb.build();
		
		cb.setCode("C14"); 
		cb.setCover("C"); 	
		cb.setType(ConstraintType.OnBooleanCondition);
		cb.setMode(ConstraintMode.NEVER);
		cb.setDomain("[irrigate=TRUE]");
		cb.build();
		
		cb.setCode("C15");
		cb.setCover("C");
		cb.setType(ConstraintType.NextCover);
		cb.setDomain("[B]");
		cb.build();
		
		ConstraintSystemFactory.exportSystem(system, path+"type/"+type+"/system_"+type+".csv");
		
		//FarmingSystemFactory.importSystem(system, path+"type/"+type+"/system_"+type+".csv");
		
		system.display();
		
		return system;
	}
	
	private static GenericConstraintSystem buildTypeLaitier(){
		String type = "laitier";
		
		// cr�ation d'un type de ferme
		GenericConstraintSystem system = new GenericConstraintSystem(type);
				
		// int�gration des couverts
		CoverFactory.init(system, path+"type/"+type+"/covers.txt", null);
		
		// mise en place des contraintes
		GenericConstraintBuilder cb = new GenericConstraintBuilder(system);
		
		// contraintes sur la prairie permanente
		cb.setCode("L01"); 
		cb.setCover("P"); 	
		cb.setType(ConstraintType.ParcelArea);
		cb.setDomain("[,1]");
		cb.build();
		
		cb.setCode("L02");
		cb.setCover("P");
		cb.setType(ConstraintType.NextCover);
		cb.setDomain("[P]");
		cb.build();
		
		// contraintes sur le ray gras
		cb.setCode("L03"); 
		cb.setCover("R");
		cb.setType(ConstraintType.Duration);
		cb.setDomain("[5,7]");
		cb.build();
		
		cb.setCode("L04"); 
		cb.setCover("R");
		cb.setType(ConstraintType.DistanceFromFacilities);
		cb.setDomain("[,5]");
		cb.setParams("head");
		cb.build();
		
		cb.setCode("L05"); 
		cb.setCover("R"); 	
		cb.setType(ConstraintType.ParcelArea);
		cb.setDomain("[1,]");
		cb.build();
		
		cb.setCode("L06");
		cb.setCover("R");
		cb.setType(ConstraintType.NextCover);
		cb.setDomain("[M]");
		cb.build();
		
		// contraintes sur la luzerne
		cb.setCode("L07"); 
		cb.setCover("L");
		cb.setType(ConstraintType.Delay);
		cb.setDomain("[8,]");
		cb.build();
		
		cb.setCode("L08"); 
		cb.setCover("L");
		cb.setType(ConstraintType.Duration);
		cb.setDomain("[3,4]");
		cb.build();
		
		cb.setCode("L09"); 
		cb.setCover("L");
		cb.setType(ConstraintType.DistanceFromFacilities);
		cb.setDomain("[0.4,]");
		cb.setParams("head");
		cb.build();
		
		cb.setCode("L10");
		cb.setCover("L"); 	
		cb.setType(ConstraintType.ParcelArea);
		cb.setDomain("[1,]");
		cb.build();
		
		cb.setCode("L11");
		cb.setCover("L");
		cb.setType(ConstraintType.NextCover);
		cb.setDomain("[M,B]");
		cb.build();
		
		// contraintes sur le bl�
		cb.setCode("L12");
		cb.setCover("B"); 	
		cb.setType(ConstraintType.TotalArea);
		cb.setDomain("[2,]");
		cb.build();
		
		cb.setCode("L13"); 
		cb.setCover("B");
		cb.setType(ConstraintType.Duration);
		cb.setDomain("[1,1]");
		cb.build();
		
		cb.setCode("L14");
		cb.setCover("B"); 	
		cb.setType(ConstraintType.ParcelArea);
		cb.setDomain("[1,]");
		cb.build();
		
		cb.setCode("L15");
		cb.setCover("B");
		cb.setType(ConstraintType.NextCover);
		cb.setDomain("[M,L,R]");
		cb.build();
		
		// contraintes sur le ma�s
		cb.setCode("L16"); 
		cb.setCover("M");
		cb.setType(ConstraintType.Duration);
		cb.setDomain("[1,1]");
		cb.build();
		
		cb.setCode("L17"); 
		cb.setCover("M");
		cb.setType(ConstraintType.Repetition);
		cb.setDomain("[0,3]");
		cb.build();
		
		cb.setCode("L18");
		cb.setCover("M"); 	
		cb.setType(ConstraintType.ParcelArea);
		cb.setDomain("[1,]");
		cb.build();
		
		cb.setCode("L19");
		cb.setCover("M"); 	
		cb.setType(ConstraintType.TotalArea);
		cb.setDomain("[30%,]");
		cb.build();
		
		cb.setCode("L20");
		cb.setCover("M");
		cb.setType(ConstraintType.NextCover);
		cb.setDomain("[M,B,R]");
		cb.build();
		
		ConstraintSystemFactory.exportSystem(system, path+"type/"+type+"/system_"+type+".csv");
		
		//FarmingSystemFactory.importSystem(system, path+"type/"+type+"/system_"+type+".csv");
		
		system.display();
		
		return system;
	}
	
}
