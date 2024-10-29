package fr.inrae.act.bagap.capfarm.simul;

import fr.inrae.act.bagap.apiland.simul.Scenario;
import fr.inrae.act.bagap.apiland.simul.Simulator;

public class CfmScenario extends Scenario {

	private static final long serialVersionUID = 1L;

	public CfmScenario(Simulator simulator, int number) {
		super(simulator, number);
	}

	@Override
	public CfmManager manager(){
		return (CfmManager) super.manager();
	}
	
	
}
