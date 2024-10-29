package fr.inrae.act.bagap.capfarm.simul;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblemFactory;
import fr.inrae.act.bagap.capfarm.csp.CoverAllocator;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class SimpleCSPCoverLocationStrategy extends CSPCoverLocationStrategy {

	@Override
	public boolean make(CoverAllocationProblemFactory factory, CoverAllocator allocator, Instant t) {
		return factory.create(allocator, t).execute();
	}

}
