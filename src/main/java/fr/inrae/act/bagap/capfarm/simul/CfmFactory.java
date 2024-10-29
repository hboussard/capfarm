package fr.inrae.act.bagap.capfarm.simul;

import fr.inrae.act.bagap.apiland.simul.Scenario;
import fr.inrae.act.bagap.apiland.simul.SimulationFactory;
import fr.inrae.act.bagap.apiland.simul.Simulator;

public class CfmFactory extends SimulationFactory {

	private static final long serialVersionUID = 1L;

	@Override
	public CfmScenario createScenario(Simulator simulator, int number) {
		return new CfmScenario(simulator, number);
	}

	@Override
	public CfmSimulation createSimulation(Scenario scenario, int number) {
		return new CfmSimulation(scenario, number);
	}

	
}
