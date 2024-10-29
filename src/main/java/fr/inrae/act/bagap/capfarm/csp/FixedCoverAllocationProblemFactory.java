package fr.inrae.act.bagap.capfarm.csp;

import fr.inrae.act.bagap.apiland.core.time.Instant;

public class FixedCoverAllocationProblemFactory extends CoverAllocationProblemFactory {

	public FixedCoverAllocationProblem create(CoverAllocator coverAllocator, Instant t){
		return new FixedCoverAllocationProblem(coverAllocator, t, coverAllocator.getSolution());
	}
	
}
