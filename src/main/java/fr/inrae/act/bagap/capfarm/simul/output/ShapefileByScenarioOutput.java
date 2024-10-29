package fr.inrae.act.bagap.capfarm.simul.output;

import java.util.HashMap;
import java.util.Map;

import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverManager;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.capfarm.simul.CoverLocationModel;
import fr.inrae.act.bagap.capfarm.simul.GlobalCoverLocationModel;
import fr.inrae.act.bagap.apiland.core.element.manager.DynamicElementFactory;
import fr.inrae.act.bagap.apiland.core.element.manager.DynamicLayerFactory;
import fr.inrae.act.bagap.apiland.core.element.type.DynamicElementTypeFactory;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.simul.OutputAnalysis;
import fr.inrae.act.bagap.apiland.simul.Scenario;
import fr.inrae.act.bagap.apiland.simul.Simulation;

public class ShapefileByScenarioOutput extends OutputAnalysis {
	
	private Map<Parcel, Map<CoverUnit, Integer>> count;
	
	@Override
	public void init(Scenario scenario){
		// mise en place de l'attribut "nb_'cover'" pour chaque couvert
		// dans les attributs de la couche
		count = new HashMap<Parcel , Map<CoverUnit, Integer>>();
		for(CoverLocationModel model : (GlobalCoverLocationModel) scenario.model().get("agriculture")){
			for(Parcel p : model.getCoverAllocator().parcels()){
							
				count.put(p, new HashMap<CoverUnit, Integer>());
							
				for(CoverUnit c : CoverManager.coverUnits()){
					p.getType().addAttributeType(DynamicElementTypeFactory.createAttributeType("nb_"+c.getCode(), null, Integer.class));
					p.getComposition().addAttribute(DynamicElementFactory.createAttribute(p.getType().getAttributeType("nb_"+c.getCode())));
					count.get(p).put(c, 0);
				}
			}
		}				
		//scenario.model().map().get("territory").getType().display();
	}
	
	@Override
	public void close(Simulation simulation){
		if(! simulation.isCancelled()){
			for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
				
				for(Parcel p : model.getCoverAllocator().parcels()){
					
					Instant t = simulation.manager().start();
					while(t.isBefore(simulation.manager().end()) || t.equals(simulation.manager().end())){
						CoverUnit cu = (CoverUnit) p.getAttribute("cover").getValue(t);
						count.get(p).put(cu, count.get(p).get(cu)+1);
						
						t = simulation.manager().delay().next(t);
					}
				}
			}
		}
	}
	
	@Override
	public void close(Scenario scenario){
		
		for(CoverLocationModel model : (GlobalCoverLocationModel) scenario.model().get("agriculture")){
			for(Parcel p : model.getCoverAllocator().parcels()){
				for(CoverUnit c : CoverManager.coverUnits()){
					p.getAttribute("nb_"+c.getCode()).setValue(scenario.manager().start(), count.get(p).get(c));
					
				}
			}
			
			DynamicLayerFactory.exportShape(
					model.getCoverAllocator().getTerritory(), 
					scenario.manager().start(),
					scenario.folder()+"count_cover_by_scenario");
		}
		
			
			
		// suppression de l'attribut "nb_'cover'" pour chaque couvert
		// dans les attributs de la couche
		for(CoverLocationModel model : (GlobalCoverLocationModel) scenario.model().get("agriculture")){
			for(Parcel p : model.getCoverAllocator().parcels()){
				for(Cover c : CoverManager.coverUnits()){
					p.getComposition().removeAttribute("nb_"+c.getCode());
				}
			}
		}
		for(CoverLocationModel model : (GlobalCoverLocationModel) scenario.model().get("agriculture")){
			for(Parcel p : model.getCoverAllocator().parcels()){
				for(Cover c : CoverManager.coverUnits()){
					p.getType().removeAttributeType("nb_"+c.getCode());
				}
			}
		}
		//scenario.model().map().get("territory").getType().display();
	}

	
}
