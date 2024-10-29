package fr.inrae.act.bagap.capfarm.simul.output;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jumpmind.symmetric.csv.CsvWriter;

import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.capfarm.simul.CoverLocationModel;
import fr.inrae.act.bagap.capfarm.simul.GlobalCoverLocationModel;
import fr.inrae.act.bagap.apiland.simul.OutputAnalysis;
import fr.inrae.act.bagap.apiland.simul.Scenario;
import fr.inrae.act.bagap.apiland.simul.Simulation;

public class FarmMemoryOutput extends OutputAnalysis {
	
	private Map<String, CsvWriter> cws;
	
	private boolean isInit;
	
	private String path;
	
	public FarmMemoryOutput(){
		// do nothing
	}
	
	public FarmMemoryOutput(String path){
		if(path == null){
			// do nothing
		}else{
			this.path = path;
		}
	}
	
	@Override
	public void init(Scenario scenario){
		if(path == null){
			path = scenario.folder()+"memory/";
		}
		cws = new HashMap<String, CsvWriter>();
		new File(path).mkdir();
		isInit = false;
	}
	
	public void firstInit(Simulation simulation){
		if(!isInit){
			try {
				CsvWriter cw;
				for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
					new File(path+model.getCoverAllocator().getCode()+"/"+model.getCoverAllocator().getConstraintSystem()+"/memory/").mkdirs();
					cw = new CsvWriter(path+model.getCoverAllocator().getCode()+"/"+model.getCoverAllocator().getConstraintSystem()+"/"+model.getCoverAllocator().getCode()+"_memory.csv");
					cw.setDelimiter(';');
					cw.write("memory");
					cw.write("file");
					cw.endRecord();
					cws.put(model.getCoverAllocator().getCode(), cw);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			isInit = true;
		}
	}
	
	@Override
	public void close(Scenario scenario){
		for(CsvWriter cw : cws.values()){
			cw.close();
		}
		cws = null;
	}
	
	@Override
	public void close(Simulation simulation){
		if(!simulation.isCancelled()){
			firstInit(simulation);
			try {
				//new File(simulation.scenario().simulator().folder()+"memory/").mkdir();
				CsvWriter cw;
				for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
					
					cw = cws.get(model.getCoverAllocator().getCode());
					cw.write(simulation.number()+"");
					cw.write("memory/"+model.getCoverAllocator().getCode()+"_memory_"+simulation.number()+".csv");
					cw.endRecord();
					
					cw = new CsvWriter(path+model.getCoverAllocator().getCode()+"/"+model.getCoverAllocator().getConstraintSystem()+"/memory/"+model.getCoverAllocator().getCode()+"_memory_"+simulation.number()+".csv");
					cw.setDelimiter(';');
					cw.write("parcel");
					cw.write("seq_cover");
					//cw.write("historic");
					cw.endRecord();
					
					for(Parcel p : model.getCoverAllocator().parcels()){
						cw.write(p.getId());
						cw.write((String) p.getAttribute("strict_seq").getValue(simulation.manager().end()));
						//cw.write((String) p.getAttribute("seq_year").getValue(simulation.manager().end()));
						cw.endRecord();
					}
					
					cw.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
