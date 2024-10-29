package fr.inrae.act.bagap.capfarm.csp;

import fr.inrae.act.bagap.apiland.core.time.Instant;

public class CoverAllocationProblemFactory {

	public CoverAllocationProblem create(CoverAllocator coverAllocator, Instant t){
		return new CoverAllocationProblem(coverAllocator, t);
	}
	
}
