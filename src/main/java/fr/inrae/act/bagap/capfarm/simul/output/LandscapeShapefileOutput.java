package fr.inrae.act.bagap.capfarm.simul.output;

import fr.inrae.act.bagap.apiland.core.element.manager.DynamicLayerFactory;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.simul.OutputAnalysis;
import fr.inrae.act.bagap.apiland.simul.Simulation;

public class LandscapeShapefileOutput extends OutputAnalysis {
	
	@Override
	public void close(Simulation simulation){
		if(! simulation.isCancelled()){
			Instant t = simulation.manager().start();
			
			//new File(simulation.folder()+"vectorial/").mkdirs();
			
			while(t.isBefore(simulation.manager().end()) || t.equals(simulation.manager().end())){
				DynamicLayerFactory.exportShape(simulation.model().map().get("territory"), 
						t,
						//simulation.folder()+"vectorial/landscape_"+t.year());
						simulation.scenario().folder()+"landscape_"+t.year());
				
				t = simulation.manager().delay().next(t);
			}
		}
	}
	
}
