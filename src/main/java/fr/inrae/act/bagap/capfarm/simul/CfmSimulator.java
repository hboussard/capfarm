package fr.inrae.act.bagap.capfarm.simul;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.jumpmind.symmetric.csv.CsvReader;

import fr.inrae.act.bagap.capfarm.csp.ProbaTimeManager;
import fr.inrae.act.bagap.apiland.simul.OutputAnalysis;
import fr.inrae.act.bagap.apiland.simul.Simulator;

public abstract class CfmSimulator extends Simulator {

	private static final long serialVersionUID = 1L;

	public CfmSimulator(CfmManager manager){
		super(manager, new CfmFactory());
	}

	@Override
	public CfmManager manager(){
		return (CfmManager) super.manager();
	}
	
	public boolean allRun() {
		boolean simulationOk = true;
		init(1);
		simulationOk = run();
		close();
		return simulationOk;
	}
	
	@Override
	protected void initModel(){
		initProbaTimes();
		initTerritory();
		initFarms();
		initOutput();
	}
	
	private List<InputStream> readJar(String filename, String folder) {
		File file = new File(filename);
		if(!filename.endsWith(".jar")||!file.exists()){
			System.out.println("Fichier jar introuvable: "+filename);
			return null;
		}
		try  {
			List<InputStream> files = new ArrayList<InputStream>();
			JarFile jarfile = new JarFile(filename);
			for (Enumeration<JarEntry> elem = jarfile.entries(); elem.hasMoreElements();){
				ZipEntry ze = (ZipEntry) elem.nextElement();
				//System.out.println(o);
				if(ze.getName().contains(folder) && ze.getName().length() > folder.length()){
					//System.out.println(ze);
					files.add(jarfile.getInputStream(ze));
				}
			}
			//jarfile.close();
			return files;
		}catch(Exception err){
			err.printStackTrace();
			return null;
        }
    }

	private void initProbaTimes() {
		try {
			
			//String probaTimeFolder = "jar:file:/C:/Hugues/workspace/apiland/target/apiland-1.0.jar!/fr/inra/sad/bagap/apiland/capfarm/simul/proba_times/";
			String probaTimeFolder = manager().probaTimeFolder();
			//System.out.println(probaTimeFolder);
			
			ProbaTimeManager.initProbaTimes();
			String name;
			int time;
			CsvReader cr;
			String[] probas;
			
			if(probaTimeFolder.startsWith("jar:")){
				String[] s = probaTimeFolder.split("!");
				String jar = s[0].replace("file:/", "").replace("jar:", "");
				String ptf = s[1].replaceFirst("/", "");
				
				JarFile jarfile = new JarFile(jar);
				for (Enumeration<JarEntry> elem = jarfile.entries(); elem.hasMoreElements();){
					ZipEntry ze = (ZipEntry) elem.nextElement();
					if(ze.getName().contains(ptf) && ze.getName().length() > ptf.length()){
						InputStream in = jarfile.getInputStream(ze);
						BufferedReader buf = new BufferedReader(new InputStreamReader(in));
						name = new File(ze.getName()).getName().replace(".txt", "");
						//System.out.println(name);
						ProbaTimeManager.addProbaType(name);
						cr = new CsvReader(buf);
						cr.setDelimiter(';');
						cr.readHeaders();
						while(cr.readRecord()){
							time = Integer.parseInt(cr.get("time"));
							ProbaTimeManager.addProbaTime(name, time);
							probas = cr.get("probas").split("\\|");
							ProbaTimeManager.addProbaTimes(name, time, probas);
						}
						buf.close();
						cr.close();
						in.close();
					}
				}
				jarfile.close();
				
			}else{
				File folder = new File(manager().probaTimeFolder().replace("file:/", ""));
				for(File duration : folder.listFiles()){
					name = duration.getName().replace(".txt", "");
					//System.out.println(name);
					ProbaTimeManager.addProbaType(name);
					cr = new CsvReader(duration.toString());
					cr.setDelimiter(';');
					cr.readHeaders();
					while(cr.readRecord()){
						time = Integer.parseInt(cr.get("time"));
						ProbaTimeManager.addProbaTime(name, time);
						probas = cr.get("probas").split("\\|");
						ProbaTimeManager.addProbaTimes(name, time, probas);
					}
					cr.close();
				}
			}
		} catch (IOException  e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void initFarms();

	private void initTerritory(){
		map().put("territory", manager().territory());
	}
	
	private void initOutput() {
		for(OutputAnalysis oa : manager().outputs()){
			addOutput(oa);
		}
	}
	
	/*
	private void initRasterization() {

		String cercle2km = "C:/Hugues/projets/agriconnect/restitution/data/sig/cercle_ouvert_2km.shp";
		DynamicLayer<?> layer = DynamicLayerFactory.initWithShape(cercle2km, manager().start());
	
		// rasterisation de la couche "territoire"
		OperationBuilder builder = new OperationBuilder(new OpRasterizationType());
		builder.setParameter("representation", "the_geom");
		builder.setParameter("cellsize", 10);
		int delta = 0;
		builder.setParameter("minX", layer.minX()-delta);
		builder.setParameter("maxX", layer.maxX()+delta);
		builder.setParameter("minY", layer.minY()-delta);
		builder.setParameter("maxY", layer.maxY()+delta);
		OpRasterization operation = builder.build();
		
		// rasterisation effectuee 1 seule fois
		operation.make(manager().start(), map().get("territory"));
	}
	*/
}

