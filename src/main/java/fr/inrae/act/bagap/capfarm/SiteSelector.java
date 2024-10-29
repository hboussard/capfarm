package fr.inrae.act.bagap.capfarm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileWriter;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.data.shapefile.shp.ShapefileException;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.shapefile.shp.ShapefileWriter;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class SiteSelector {

	private static String path = "C:/Hugues/modelisation/capfarm/methodo/fond_de_carte/RPG/2014/";
	private static String input = path+"RPG_Robillard";
	private static String attributEA = "ILOTS-AN_6"; // nom de l'attribut "exploitation"
	private static String name = "robillard"; // nom du fichier de sortie
	private static String attributCommune = "COMMUNE_IL"; // nom de l'attribut "commune"
	private static double codeCommune = 35104; // code commune
	
	private static double x = 479800.0;
	private static double y = 6880480.0;
	
	private static double rayon = 3000; // en metres
		
	private static double minFarmArea = 5.0; // en hectare 
	
	private static double minX, maxX, minY, maxY;
	
	private static DbaseFileHeader header;
	
	public static void main(String[] args){
		getEAFromBuffer(); // � lancer apr�s avoir modifier tes param�tres
	}
	
	private static void getEAFromBuffer(){
		Set<String> eas = getExploitationsFromBuffer();
		Set<Polygon> ilots = getIlots(eas);
		writeIlots(ilots);
		exportBuffer();
	}
	
	private static Set<String> getExploitationsFromBuffer(){
		Set<String> eas = new TreeSet<String>();
		Map<String, Set<Polygon>> areas = new TreeMap<String, Set<Polygon>>();
		try {
			ShpFiles sf = new ShpFiles(input+".shp");
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new org.locationtech.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			header = dfr.getHeader();
			
			int indexEA = -1;
			for(int i=0; i<header.getNumFields(); i++){
				if(header.getFieldName(i).equalsIgnoreCase(attributEA)){
					indexEA = i;
				}
			}
			
			WKTReader wkt = new WKTReader();
			Polygon zone = (Polygon) ((Point) wkt.read("POINT ("+x+" "+y+")")).buffer(rayon);
			/*
			minX = zone.getEnvelopeInternal().getMinX();
			maxX = zone.getEnvelopeInternal().getMaxX();
			minY = zone.getEnvelopeInternal().getMinY();
			maxY = zone.getEnvelopeInternal().getMaxY();
			*/
			Object[] entry;
			while(sfr.hasNext()){
				Polygon p = (Polygon) ((MultiPolygon) sfr.nextRecord().shape()).getGeometryN(0);
				entry = dfr.readEntry();
				p.setUserData(entry);
				if(p.intersects(zone)){
					//eas.add(entry[indexEA]+"");
					if(!areas.containsKey(entry[indexEA])){
						areas.put((String) entry[indexEA], new HashSet<Polygon>());
					}
					areas.get((String) entry[indexEA]).add(p);
				}
			}
			
			sfr.close();
			dfr.close();
			int nbTooSmallEA = 0;
			double totalArea = 0.0;
			double rejectArea = 0.0;
			for(Entry<String, Set<Polygon>> e : areas.entrySet()){
				double area = 0.0;
				String code = "";
				for(Polygon p : e.getValue()){
					area += p.intersection(zone).getArea();
					code = (String) ((Object[]) p.getUserData())[indexEA];
				}
				if(area >= minFarmArea*10000.0){
					eas.add(code+"");
					totalArea += area;
					System.out.println("EA : "+code+", surface cumul�e = "+area);
				}else{
					nbTooSmallEA++;
					rejectArea += area;
					System.out.println("rejet�e : "+code+", surface cumul�e = "+area);
				}
			}
			
			//System.out.println(entry[indexEA]+" : "+p.intersection(zone).getArea());
			System.out.println("nombre d'exploitations concern�es : "+eas.size()+", nombre d'exploitations rejet�es : "+nbTooSmallEA);
			System.out.println("taux de couverture agricole trait�e : "+(totalArea*100.0/zone.getArea())+"%");
			System.out.println("taux de couverture agricole rejet�e : "+(rejectArea*100.0/zone.getArea())+"%");
			System.out.println("taux de couverture agricole : "+((totalArea+rejectArea)*100.0/zone.getArea())+"%");
			System.out.println("taux de couverture non agricole : "+((100-(totalArea+rejectArea)*100.0/zone.getArea()))+"%");
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return eas;
	}
	
	private static Set<Polygon> getIlots(Set<String> eas){
		Set<Polygon> ilots = new TreeSet<Polygon>();
	
		try {
			ShpFiles sf = new ShpFiles(input+".shp");
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new org.locationtech.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			
			minX = Double.MAX_VALUE;
			maxX = Double.MIN_VALUE;
			minY = Double.MAX_VALUE;
			maxY = Double.MIN_VALUE;
			
			int indexEA = -1;
			for(int i=0; i<header.getNumFields(); i++){
				if(header.getFieldName(i).equalsIgnoreCase(attributEA)){
					indexEA = i;
					break;
				}
			}
			
			Object[] entry;
			while(sfr.hasNext()){
				Polygon p = (Polygon) ((MultiPolygon) sfr.nextRecord().shape()).getGeometryN(0);
				entry = dfr.readEntry();
				//if(eas.contains(((String) entry[indexEA]))){
				if(eas.contains(entry[indexEA]+"")){
					p.setUserData(entry);
					ilots.add(p);
					minX = Math.min(minX, p.getEnvelopeInternal().getMinX());
					maxX = Math.max(maxX, p.getEnvelopeInternal().getMaxX());
					minY = Math.min(minY, p.getEnvelopeInternal().getMinY());
					maxY = Math.max(maxY, p.getEnvelopeInternal().getMaxY());
				}
			}
			
			sfr.close();
			dfr.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ilots;
	}
	
	private static void writeIlots(Set<Polygon> ilots){
		String output = path+name+"_"+rayon+"_"+minFarmArea;
		try(FileOutputStream fos = new FileOutputStream(output+".dbf");
				FileOutputStream shp = new FileOutputStream(output + ".shp");
				FileOutputStream shx = new FileOutputStream(output + ".shx");){
			
			header.setNumRecords(ilots.size());
			DbaseFileWriter dfw = new DbaseFileWriter(header, fos.getChannel());
			ShapefileWriter sfw = new ShapefileWriter(shp.getChannel(), shx.getChannel());
			sfw.writeHeaders(new Envelope(minX, maxX, minY, maxY), ShapeType.POLYGON, ilots.size(), 1000000);
			
			for(Polygon p : ilots){
				sfw.writeGeometry(p);
				dfw.write((Object[]) p.getUserData());
			}
			
			dfw.close();
			sfw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		copyFile(input+".prj", output+".prj");
	}
	
	private static void exportBuffer() {
		String output = path+"buffer_"+rayon+"_"+x+"-"+y;
		try(FileOutputStream fos = new FileOutputStream(output+".dbf");
				FileOutputStream shp = new FileOutputStream(output + ".shp");
				FileOutputStream shx = new FileOutputStream(output + ".shx");){
			
			WKTReader wkt = new WKTReader();
			Polygon zone = (Polygon) ((Point) wkt.read("POINT ("+x+" "+y+")")).buffer(rayon);
			
			DbaseFileHeader h = new DbaseFileHeader();
			h.setNumRecords(1);
			DbaseFileWriter dfw = new DbaseFileWriter(h, fos.getChannel());
			ShapefileWriter sfw = new ShapefileWriter(shp.getChannel(), shx.getChannel());
			sfw.writeHeaders(new Envelope(zone.getEnvelopeInternal().getMinX(), zone.getEnvelopeInternal().getMaxX(), 
					zone.getEnvelopeInternal().getMinY(), zone.getEnvelopeInternal().getMaxY()), ShapeType.POLYGON, 1, 1000000);
			
			sfw.writeGeometry(zone);
			dfw.write(new Object[0]);
			
			dfw.close();
			sfw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		copyFile(input+".prj", output+".prj");
	}
	
	private static void getEAFromCommune(){
		Set<String> eas = getExploitationsFromCode();
		Set<Polygon> ilots = getIlots(eas);
		writeIlots(ilots);
	}
	
	private static Set<String> getExploitationsFromCode(){
		Set<String> eas = new TreeSet<String>();
		
		try {
			ShpFiles sf = new ShpFiles(input+".shp");
			ShapefileReader sfr = new ShapefileReader(sf, true, false, new org.locationtech.jts.geom.GeometryFactory());
			DbaseFileReader dfr = new DbaseFileReader(sf, true, Charset.defaultCharset());
			header = dfr.getHeader();
			
			minX = sfr.getHeader().minX();
			maxX = sfr.getHeader().maxX();
			minY = sfr.getHeader().minY();
			maxY = sfr.getHeader().maxY();
			
			int indexEA = -1, indexCommune = -1;
			for(int i=0; i<header.getNumFields(); i++){
				if(header.getFieldName(i).equalsIgnoreCase(attributEA)){
					indexEA = i;
				}
				if(header.getFieldName(i).equalsIgnoreCase(attributCommune)){
					indexCommune = i;
				}
			}
			
			Object[] entry;
			while(sfr.hasNext()){
				Polygon p = (Polygon) ((MultiPolygon) sfr.nextRecord().shape()).getGeometryN(0);
				entry = dfr.readEntry();
				if(((double) entry[indexCommune]) == codeCommune){
					eas.add((String) entry[indexEA]);
				}
			}
			
			System.out.println("nombre d'exploitation concern�es : "+eas.size());
			
			sfr.close();
			dfr.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ShapefileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return eas;
	}
	
	public static String getPath() {
		return path;
	}

	public static void setPath(String path) {
		SiteSelector.path = path;
	}

	public static String getInput() {
		return input;
	}

	public static void setInput(String input) {
		SiteSelector.input = input;
	}

	public static String getAttributEA() {
		return attributEA;
	}

	public static void setAttributEA(String attributEA) {
		SiteSelector.attributEA = attributEA;
	}

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		SiteSelector.name = name;
	}

	public static String getAttributCommune() {
		return attributCommune;
	}

	public static void setAttributCommune(String attributCommune) {
		SiteSelector.attributCommune = attributCommune;
	}

	public static double getCodeCommune() {
		return codeCommune;
	}

	public static void setCodeCommune(double codeCommune) {
		SiteSelector.codeCommune = codeCommune;
	}

	public static double getRayon() {
		return rayon;
	}

	public static void setRayon(double rayon) {
		SiteSelector.rayon = rayon;
	}

	
	private static boolean copyFile(String source, String dest){
		try{
			// Declaration et ouverture des flux
			java.io.FileInputStream sourceFile = new java.io.FileInputStream(new File(source));
	 
			try{
				java.io.FileOutputStream destinationFile = null;
	 
				try{
					destinationFile = new FileOutputStream(new File(dest));
	 
					// Lecture par segment de 0.5Mo 
					byte buffer[] = new byte[512 * 1024];
					int nbLecture;
					
					while ((nbLecture = sourceFile.read(buffer)) != -1){
						destinationFile.write(buffer, 0, nbLecture);
					}
				} finally {
					destinationFile.close();
				}
			} finally {
				sourceFile.close();
			}
		} catch (IOException e){
			e.printStackTrace();
			return false; // Erreur
		}
		return true; // R�sultat OK  
	}
	

	// Agriconnect site ouvert
	//private static double x = 367883.4587;
	//private static double y = 6783447.113;
		
	// Agriconnect site ferm�
	//private static double x = 357770.2965;
	//private static double y = 6830159.9776;
		
	// gester 31 (Haute Garonne)
	//private static double x = 578354;
	//private static double y = 6256011;
		
	// gester 36 (Indre)
	//private static double x = 594794 
	//private static double y = 6652998
	
}
