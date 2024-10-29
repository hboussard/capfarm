package fr.inrae.act.bagap.capfarm;

import fr.inrae.act.bagap.apiland.core.time.Instant;

public class CAPFarm {

	public static Instant t = Instant.get(1,  1,  1);
	
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
	
}
