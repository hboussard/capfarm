package fr.inrae.act.bagap.capfarm.simul;

import java.util.Set;
import java.util.TreeSet;
import fr.inrae.act.bagap.capfarm.model.Farm;
import fr.inrae.act.bagap.capfarm.model.territory.AgriculturalArea;
import fr.inrae.act.bagap.apiland.simul.Simulator;
import fr.inrae.act.bagap.apiland.simul.model.CompositeModel;

public class GlobalCoverLocationModel extends CompositeModel<CoverLocationModel> {

	private static final long serialVersionUID = 1L;

	private AgriculturalArea territory;
	
	private String memory;
	
	public GlobalCoverLocationModel(String name, Simulator simulator) {
		super(name, simulator);
	}
	
	public void setTerritory(AgriculturalArea territory) {
		this.territory = territory;	
	}
	
	public AgriculturalArea getTerritory(){
		return territory;
	}
	
	public Set<Farm> farms(){
		Set<Farm> farms = new TreeSet<Farm>();
		for(CoverLocationModel m : this){
			farms.add((Farm) m.getCoverAllocator());
		}
		return farms;
	}
	
	public void setMemory(String memory) {
		this.memory = memory;
	}
	
	public String getMemory() {
		return memory;
	}

}
