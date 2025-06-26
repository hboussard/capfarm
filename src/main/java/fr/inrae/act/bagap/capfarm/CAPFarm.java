package fr.inrae.act.bagap.capfarm;

import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.capfarm.model.ConstraintSystemFactory;
import fr.inrae.act.bagap.capfarm.model.CoverFactory;
import fr.inrae.act.bagap.capfarm.model.GenericConstraintSystem;

public class CAPFarm {

	public static Instant t = Instant.get(1, 1, 1);
	
	public static boolean parseBoolean(String b){
		switch(b){
		case "T" : return true;
		case "t" : return true;
		case "TRUE" : return true;
		case "true" : return true;
		case "True" : return true;
		case "1" : return true;
		case "'T'" : return true;
		case "'t'" : return true;
		case "'TRUE'" : return true;
		case "'true'" : return true;
		case "'True'" : return true;
		case "'1'" : return true;
		}
		return Boolean.parseBoolean(b);
	}
	
	public static GenericConstraintSystem importSystem(String name, String systemFile, String cover) {

		return importSystem(name, systemFile, cover, null);
	}
	
	public static GenericConstraintSystem importSystem(String name, String systemFile, String cover, String group) {
		
		// creation d'un type de systeme
		GenericConstraintSystem system = new GenericConstraintSystem(name);

		// integration des couverts
		CoverFactory.init(system, cover, group);

		ConstraintSystemFactory.importSystem(system, systemFile);

		return system;
	}
	
}
