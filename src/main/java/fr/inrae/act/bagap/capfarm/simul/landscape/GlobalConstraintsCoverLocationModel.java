package fr.inrae.act.bagap.capfarm.simul.landscape;

import java.util.HashMap;
import java.util.Map;
import fr.inrae.act.bagap.capfarm.csp.CoverAllocator;
import fr.inrae.act.bagap.capfarm.csp.FixedCoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.territory.FarmTerritory;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.capfarm.simul.CoverLocationModel;
import fr.inrae.act.bagap.capfarm.simul.GlobalCoverLocationModel;
import fr.inrae.act.bagap.capfarm.simul.MemoryCoverLocationModel;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.simul.Simulator;

public class GlobalConstraintsCoverLocationModel extends GlobalCoverLocationModel {

	private static final long serialVersionUID = 1L;
	
	private CoverAllocator coverAllocator;
	
	public GlobalConstraintsCoverLocationModel(String name, Simulator simulator, CoverAllocator coverAllocator) {
		super(name, simulator);
		this.coverAllocator = coverAllocator;
		
	}
	
	public CoverAllocator getCoverAllocator(){
		return coverAllocator;
	}
	
	@Override
	public boolean run(Instant t) {
		
		Map<Parcel, CoverUnit> fixed = new HashMap<Parcel, CoverUnit>();
		
		for(CoverLocationModel model : this){
			for(Parcel p : ((FarmTerritory) model.getElement()).parcels()){
				fixed.put(p, ((MemoryCoverLocationModel) model).getCover(p.getId(), t));
			}
		}
		
		if(new FixedCoverAllocationProblem(coverAllocator, t, fixed).execute()){
			
			for(CoverLocationModel model : this){
				model.run(t);
			}
			
			return true;
		}else{
			return false;
		}
	}
	
}
