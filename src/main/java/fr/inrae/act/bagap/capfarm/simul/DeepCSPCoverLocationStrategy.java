package fr.inrae.act.bagap.capfarm.simul;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblemFactory;
import fr.inrae.act.bagap.capfarm.csp.CoverAllocator;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class DeepCSPCoverLocationStrategy extends CSPCoverLocationStrategy {

	private static final int max = 500;
	
	@Override
	public boolean make(CoverAllocationProblemFactory factory, CoverAllocator allocator, Instant t) {
		for(int i=0; i<max; i++){
			System.out.println("cover location at "+t);
			if(factory.create(allocator, t).execute()){
				return true;
			}
		}
		return false;
	}

}
