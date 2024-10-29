package fr.inrae.act.bagap.capfarm.simul;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblemFactory;
import fr.inrae.act.bagap.capfarm.csp.CoverAllocator;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class CSPCoverLocationModel extends CoverLocationModel {

	private static final long serialVersionUID = 1L;
	
	private CoverAllocationProblemFactory factory;
	
	private CSPCoverLocationStrategy strategy;

	public CSPCoverLocationModel(CfmSimulator simulator, CoverAllocator coverAllocator, CoverAllocationProblemFactory factory) {
		super(simulator, coverAllocator);
		this.factory = factory;
		strategy = new SimpleCSPCoverLocationStrategy();
	}
	
	@Override
	public boolean make(Instant t) {
		System.out.println("allocation at "+t.year());
		// delegation � une strategie d'allocation
		return strategy.make(factory, getCoverAllocator(), t);
	}
	
}
