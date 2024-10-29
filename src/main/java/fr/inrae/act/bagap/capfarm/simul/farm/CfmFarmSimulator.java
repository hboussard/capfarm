package fr.inrae.act.bagap.capfarm.simul.farm;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblemFactory;
import fr.inrae.act.bagap.capfarm.csp.FixedCoverAllocationProblemFactory;
import fr.inrae.act.bagap.capfarm.model.economic.EconomicCoverAllocationProblemFactory;
import fr.inrae.act.bagap.capfarm.model.economic.OptimizeEconomicCoverAllocationProblemFactory;
import fr.inrae.act.bagap.capfarm.simul.CSPCoverLocationModel;
import fr.inrae.act.bagap.capfarm.simul.CfmSimulator;
import fr.inrae.act.bagap.capfarm.simul.GlobalCoverLocationModel;
import fr.inrae.act.bagap.capfarm.simul.MemoryCoverLocationModel;

public class CfmFarmSimulator extends CfmSimulator {

	private static final long serialVersionUID = 1L;

	public CfmFarmSimulator(CfmFarmManager manager){
		super(manager);
	}

	@Override
	public CfmFarmManager manager(){
		return (CfmFarmManager) super.manager();
	}
	
	@Override
	protected void initFarms() {
		GlobalCoverLocationModel agriculture = new GlobalCoverLocationModel("agriculture", this);
		
		//affectation du territoire au model global
		//agriculture.setTerritory((AgriculturalArea) map().get("territory").get("AA"));
		
		CoverAllocationProblemFactory factory = null;
		switch(manager().mode()){
		case IDLE : factory = new CoverAllocationProblemFactory(); break;
		case ECONOMIC : factory = new EconomicCoverAllocationProblemFactory(manager().economicProfil(), manager().workProfil(), manager().distanceCoversProfil()); break;
		case OPTIMIZE : factory = new OptimizeEconomicCoverAllocationProblemFactory(manager().economicProfil(), manager().workProfil(), manager().distanceCoversProfil()); break;
		case SOLUTION : factory = new FixedCoverAllocationProblemFactory(); break;
		}
		
		switch(manager().processMode()){
		case ACTIVATE : agriculture.add(new CSPCoverLocationModel(this, manager().farm(), factory)); break;
		case MEMORY : agriculture.add(new MemoryCoverLocationModel(this, manager().farm())); break;
		default : throw new IllegalArgumentException(manager().processMode()+" not implemented yet");
		}
		
		model().add(agriculture);
	}

}
