package fr.inrae.act.bagap.capfarm.simul.output;

import fr.inrae.act.bagap.capfarm.model.Farm;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.capfarm.simul.CoverLocationModel;
import fr.inrae.act.bagap.capfarm.simul.GlobalCoverLocationModel;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.simul.OutputAnalysis;
import fr.inrae.act.bagap.apiland.simul.Simulation;

public class ConsoleOutput extends OutputAnalysis {
	
	@Override
	public void close(Simulation simulation){
		if(!simulation.isCancelled()){
			
			for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
				
				System.out.println(model.getCoverAllocator().getCode());
				for(Parcel p : model.getCoverAllocator().parcels()){
					System.out.print(p.getId()+" : ");
					Instant t = simulation.manager().end();
					System.out.println(p.getAttribute("strict_seq").getValue(t));
				}		
			}
			/*
			StringBuilder sb = new StringBuilder();
			for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
				sb.append(((Farm) model.getCoverAllocator()).getCode()+"-"+((Farm) model.getCoverAllocator()).getMemory()+"_");
			}
			sb.deleteCharAt(sb.length()-1);
			String memory = sb.toString();
			System.out.println(memory);
			*/
		}
	}

}
