package fr.inrae.act.bagap.capfarm.simul.output;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jumpmind.symmetric.csv.CsvWriter;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.capfarm.simul.CoverLocationModel;
import fr.inrae.act.bagap.capfarm.simul.GlobalCoverLocationModel;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.simul.OutputAnalysis;
import fr.inrae.act.bagap.apiland.simul.Scenario;
import fr.inrae.act.bagap.apiland.simul.Simulation;

public class FarmMemoryOutputFromProfit extends OutputAnalysis {
	
	private Map<String, CsvWriter> cws;
	
	//private Map<String, Integer> numbers;
	
	private String path;
	
	private String system;
	
	public FarmMemoryOutputFromProfit(){
		// do nothing
	}
	
	public FarmMemoryOutputFromProfit(String path, String system){
		if(path == null){
			// do nothing
		}else{
			this.path = path;
		}
		this.system = system;
	}
	
	@Override
	public void init(Scenario scenario){
		if(path == null){
			path = scenario.folder()+"memory/";
		}
		new File(path).mkdir();
		cws = new HashMap<String, CsvWriter>();
		try {
			CsvWriter cw;
			for(CoverLocationModel model : (GlobalCoverLocationModel) scenario.model().get("agriculture")){
				
				new File(path+model.getCoverAllocator().getCode()+"/"+system+"/memory/").mkdirs();
				cw = new CsvWriter(path+model.getCoverAllocator().getCode()+"/"+system+"/"+model.getCoverAllocator().getCode()+"_memory.csv");
				cw.setDelimiter(';');
				cw.write("memory");
				cw.write("file");
				cw.write("profit");
				cw.write("zoneprofit");
				cw.endRecord();
				cws.put(model.getCoverAllocator().getCode(), cw);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//numbers = new HashMap<String, Integer>();
		//for(CoverLocationModel model : (GlobalCoverLocationModel) scenario.model().get("agriculture")){
		//	numbers.put(model.getCoverAllocator().getCode(), 1);
		//}
	}
	
	@Override
	public void close(Scenario scenario){
		for(CsvWriter cw : cws.values()){
			cw.close();
		}
		cws = null;
		//numbers = null;
	}
	
	@Override
	public void close(Simulation simulation){
		if(!simulation.isCancelled()){
			try {
				CsvWriter cw;
				for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
					cw = cws.get(model.getCoverAllocator().getCode());
					int profit = 0;
					for(int y=simulation.manager().start().year(); y<=simulation.manager().end().year(); y++){
						profit += (Integer) model.getCoverAllocator().getTerritory().getAttribute("profit").getValue(new Instant(simulation.manager().start().dayOfYear(), simulation.manager().start().month(), y));
					}
					
					int zoneprofit = 0;
					for(int y=simulation.manager().start().year(); y<=simulation.manager().end().year(); y++){
						zoneprofit += (Integer) model.getCoverAllocator().getTerritory().getAttribute("zoneprofit").getValue(new Instant(simulation.manager().start().dayOfYear(), simulation.manager().start().month(), y));
					}
					
					cw.write(simulation.number()+"");
					//cw.write(numbers.get(model.getCoverAllocator().getCode())+"");
					//numbers.put(model.getCoverAllocator().getCode(), numbers.get(model.getCoverAllocator().getCode())+1);
					cw.write("memory/"+model.getCoverAllocator().getCode()+"_memory_"+simulation.number()+".csv");
					cw.write(profit+"");
					cw.write(zoneprofit+"");
					cw.endRecord();
					
					cw = new CsvWriter(path+model.getCoverAllocator().getCode()+"/"+system+"/memory/"+model.getCoverAllocator().getCode()+"_memory_"+simulation.number()+".csv");
					cw.setDelimiter(';');
					cw.write("parcel");
					cw.write("seq_cover");
					//cw.write("historic");
					cw.endRecord();
						
					for(Parcel p : model.getCoverAllocator().parcels()){
						cw.write(p.getId());
						cw.write((String) p.getAttribute("strict_seq").getValue(simulation.manager().end()));
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
