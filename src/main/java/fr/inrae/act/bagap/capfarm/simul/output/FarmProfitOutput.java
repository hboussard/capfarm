package fr.inrae.act.bagap.capfarm.simul.output;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jumpmind.symmetric.csv.CsvWriter;
import fr.inrae.act.bagap.capfarm.simul.CoverLocationModel;
import fr.inrae.act.bagap.capfarm.simul.GlobalCoverLocationModel;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.simul.OutputAnalysis;
import fr.inrae.act.bagap.apiland.simul.Scenario;
import fr.inrae.act.bagap.apiland.simul.Simulation;

public class FarmProfitOutput extends OutputAnalysis {
	
	private Map<String, CsvWriter> cws;
	
	@Override
	public void init(Scenario scenario){
		cws = new HashMap<String, CsvWriter>();
		try {
			for(CoverLocationModel model : (GlobalCoverLocationModel) scenario.model().get("agriculture")){
				CsvWriter cw = new CsvWriter(scenario.folder()+"profit"+model.getCoverAllocator().getCode()+".csv");
				cw.setDelimiter(';');
				cw.write("simulation");
				cw.write("farm");
				cw.write("year");
				cw.write("profit");
				cw.endRecord();
				cws.put(model.getCoverAllocator().getCode(), cw);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close(Scenario scenario){
		for(CsvWriter cw : cws.values()){
			cw.close();
		}
	}
	
	@Override
	public void close(Simulation simulation){
		if(!simulation.isCancelled()){
			try {
				for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
					CsvWriter cw = cws.get(model.getCoverAllocator().getCode());
					for(int y=simulation.manager().start().year(); y<=simulation.manager().end().year(); y++){
						cw.write(""+simulation.number());
						cw.write(""+model.getCoverAllocator().getCode());
						cw.write(y+"");
						cw.write(model.getCoverAllocator().getTerritory().getAttribute("profit").getValue(new Instant(simulation.manager().start().dayOfYear(), simulation.manager().start().month(), y))+"");
						cw.endRecord();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
}