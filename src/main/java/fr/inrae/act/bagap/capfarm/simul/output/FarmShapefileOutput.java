package fr.inrae.act.bagap.capfarm.simul.output;

import fr.inrae.act.bagap.capfarm.simul.CoverLocationModel;
import fr.inrae.act.bagap.capfarm.simul.GlobalCoverLocationModel;
import fr.inrae.act.bagap.apiland.core.element.manager.DynamicLayerFactory;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.simul.OutputAnalysis;
import fr.inrae.act.bagap.apiland.simul.Simulation;

public class FarmShapefileOutput extends OutputAnalysis {
	
	@Override
	public void close(Simulation simulation){
		if(! simulation.isCancelled()){
			Instant t = simulation.manager().start();
			
			while(t.isBefore(simulation.manager().end()) || t.equals(simulation.manager().end())){
				for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
					
					DynamicLayerFactory.exportShape(
							model.getCoverAllocator().getTerritory(), 
							t,
							simulation.scenario().folder()+model.getName()+/*"_"+simulation.number()+*/"_"+t.year());
				}
				
				t = simulation.manager().delay().next(t);
			}
		}
	}
	
}
