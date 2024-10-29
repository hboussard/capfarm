package fr.inrae.act.bagap.capfarm.simul.output;

import java.util.HashMap;
import java.util.Map;

import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverManager;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.capfarm.simul.CoverLocationModel;
import fr.inrae.act.bagap.capfarm.simul.GlobalCoverLocationModel;
import fr.inrae.act.bagap.apiland.core.composition.StaticAttribute;
import fr.inrae.act.bagap.apiland.core.element.manager.DynamicLayerFactory;
import fr.inrae.act.bagap.apiland.core.element.type.DynamicElementTypeFactory;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.simul.OutputAnalysis;
import fr.inrae.act.bagap.apiland.simul.Simulation;

public class ShapefileBySimulationOutput extends OutputAnalysis {
	
	@Override
	public void close(Simulation simulation){
		if(! simulation.isCancelled()){
			
			
			// mise en place de l'attribut "nb_'cover'" pour chaque couvert
			// dans les attributs de la couche
			Map<Parcel, Map<CoverUnit, Integer>> count = new HashMap<Parcel , Map<CoverUnit, Integer>>();
			for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
				for(Parcel p : model.getCoverAllocator().parcels()){
					
					count.put(p, new HashMap<CoverUnit, Integer>());
					
					for(CoverUnit c : CoverManager.coverUnits()){
						//System.out.println("cr�ation de l'attribut type : "+"nb_"+c.getCode());
						if(!p.getType().hasAttributeType("nb_"+c.getCode())){
							p.getType().addAttributeType(DynamicElementTypeFactory.createAttributeType("nb_"+c.getCode(), null, Integer.class));
						}
						p.getComposition().addAttribute(new StaticAttribute(DynamicElementTypeFactory.createAttributeType("nb_"+c.getCode(), null, Integer.class)));
						count.get(p).put(c, 0);
					}
				}
			}
			
			//simulation.model().map().get("territory").getType().display();
			
			for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
				for(Parcel p : model.getCoverAllocator().parcels()){
					Instant t = simulation.manager().start();
					while(t.isBefore(simulation.manager().end()) || t.equals(simulation.manager().end())){
						CoverUnit cu = (CoverUnit) p.getAttribute("cover").getValue(t);
						count.get(p).put(cu, count.get(p).get(cu)+1);
						
						
						t = simulation.manager().delay().next(t);
					}
					
					for(CoverUnit c : CoverManager.coverUnits()){
						p.getAttribute("nb_"+c.getCode()).setValue(simulation.manager().start(), count.get(p).get(c));
					}
					
				}
			}
			
			for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
				DynamicLayerFactory.exportShape(
						model.getCoverAllocator().getTerritory(), 
						simulation.manager().start(),
						simulation.scenario().folder()+"count_cover_by_simulation");
			}
			
			// suppression de l'attribut "nb_'cover'" pour chaque couvert
			// dans les attributs de la couche
			for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
				for(Parcel p : model.getCoverAllocator().parcels()){
					for(Cover c : CoverManager.coverUnits()){
						model.getCoverAllocator().getTerritory().getType().removeAttributeType("nb_"+c.getCode());
					}
					break;
				}
			}
			
		}
		
		
	}
	
}
