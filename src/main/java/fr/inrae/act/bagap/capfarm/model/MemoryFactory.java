package fr.inrae.act.bagap.capfarm.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jumpmind.symmetric.csv.CsvReader;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocator;
import fr.inrae.act.bagap.capfarm.simul.CoverLocationModel;
import fr.inrae.act.bagap.capfarm.simul.MemoryCoverLocationModel;
import fr.inrae.act.bagap.apiland.core.composition.AttributeType;
import fr.inrae.act.bagap.apiland.core.composition.DynamicAttributeType;
import fr.inrae.act.bagap.apiland.core.element.type.DynamicElementTypeFactory;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.core.time.Interval;

public class MemoryFactory {

	public static void init(CoverLocationModel model, Instant t, String farmFolder, String method, int simulationNumber){
		
		CoverAllocator farm = model.getCoverAllocator();
		if(farm.hasMemory()){
			int memory = farm.getMemory();
			try {
				String memoryFile = farmFolder+"/"+farm.getCode()+"/"+farm.getConstraintSystem()+"/"+farm.getCode()+"_memory.csv";
				//System.out.println(memoryFile);
				if(memory == 0){
					CsvReader cr = new CsvReader(memoryFile);
					cr.setDelimiter(';');
					cr.readHeaders();
					
					if(method == null || method.equalsIgnoreCase("")){
						List<Integer> memories = new ArrayList<Integer>(); 
						while(cr.readRecord()){
							memories.add(Integer.parseInt(cr.get("memory")));
						}
						memory = memories.get(new Double(Math.random() * memories.size()).intValue());
					}else{
						if(method.startsWith("max")){
							method = method.replace("max(", "").replace(")", "");
							int max = -1;
							while(cr.readRecord()){
								//System.out.println(method);
								int value = Integer.parseInt(cr.get(method));
								if(value > max){
									memory = Integer.parseInt(cr.get("memory"));
									max = value;
								}
							}
							//System.out.println(farm.getCode()+" "+memory+" "+max);
						}else if(method.startsWith("selected")){
							method = method.replace("selected(", "").replace(")", "");
							//String[] farms = method.split(":");
							String[] farms = method.split("_");
							for(String f : farms){
								if(f.startsWith(farm.getCode()+"-")){
									//System.out.println(f+" "+farm.getCode()+"= "+f.replace(farm.getCode()+"=", ""));
									//memory = Integer.parseInt(f.replace(farm.getCode()+"=", ""));
									memory = Integer.parseInt(f.replace(farm.getCode()+"-", ""));
								}
							}
						}else if(method.startsWith("each")){
							int index = 0;
							while(cr.readRecord()){
								index++;
								memory = Integer.parseInt(cr.get("memory"));
								if(index == simulationNumber){
									 break;
								}
							}
						}else if(method.startsWith("base")){
							method = method.replace("base(", "").replace(")", "");
							//String[] farms = method.split(":");
							String[] farms = method.split("_");
							boolean ok = false;
							for(String f : farms){
								if(f.startsWith(farm.getCode()+"-")){
									//System.out.println(f+" "+farm.getCode()+"= "+f.replace(farm.getCode()+"=", ""));
									//memory = Integer.parseInt(f.replace(farm.getCode()+"=", ""));
									memory = Integer.parseInt(f.replace(farm.getCode()+"-", ""));
									ok = true;
								}
							}
							if(!ok){
								int index = 0;
								while(cr.readRecord()){
									index++;
									memory = Integer.parseInt(cr.get("memory"));
									if(index == simulationNumber){
										 break;
									}
								}
							}
						}else{
							throw new IllegalArgumentException("method "+method+" is taken into account yet");
						}
					}
					farm.setMemory(memory);
					//System.out.println("memory : "+farm.getCode()+" : "+memory);
					cr.close();
				}
				
				AttributeType type = DynamicElementTypeFactory.createAttributeType("memory", Interval.class, CoverUnit.class);
				
				CsvReader cr = new CsvReader(memoryFile);
				cr.setDelimiter(';');
				cr.readHeaders();
				while(cr.readRecord()){
					if(Integer.parseInt(cr.get("memory")) == memory){
						initMemoryFile((MemoryCoverLocationModel) model, t, (DynamicAttributeType) type,
								farmFolder+"/"+farm.getCode()+"/"+farm.getConstraintSystem()+"/"+cr.get("file"));
						break;
					}
				}
				cr.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void initMemoryFile(MemoryCoverLocationModel model, Instant t, DynamicAttributeType type, String memoryFile) {
		try {
			CsvReader cr = new CsvReader(memoryFile);
			cr.setDelimiter(';');
			cr.readHeaders();
			String seq;
			String[] sequence;
			Instant year;
			String[] infos;
			int nb;
			String parcel;
			while(cr.readRecord()){
				parcel = cr.get("parcel");
				model.initParcel(parcel, type);
				sequence = cr.get("seq_cover").replaceAll(" ", "").split("-");
				year = t;
				for(String cover : sequence){
					nb = 1;
					if(cover.contains("(")){
						infos = cover.replace(")", "").split("\\(");
						cover = infos[0];
						nb = Integer.parseInt(infos[1]);
					}
					model.setCover(parcel, (CoverUnit) Cover.get(cover), year);
					year = Instant.get(year.dayOfMonth(), year.month(), year.year()+nb);
				}
			}
			cr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
