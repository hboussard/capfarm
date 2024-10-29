package fr.inrae.act.bagap.capfarm.simul;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblemFactory;
import fr.inrae.act.bagap.capfarm.csp.CoverAllocator;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public abstract class CSPCoverLocationStrategy {

	public abstract boolean make(CoverAllocationProblemFactory factory, CoverAllocator allocator, Instant t);
	
}
