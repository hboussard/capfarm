package fr.inrae.act.bagap.capfarm.simul.farm;

import fr.inrae.act.bagap.capfarm.model.Farm;
import fr.inrae.act.bagap.capfarm.simul.CfmManager;

public class CfmFarmManager extends CfmManager {

	private static final long serialVersionUID = 1L;

	private Farm farm;
	
	public CfmFarmManager(Farm farm){
		this(farm, 1);
	}
	
	public CfmFarmManager(Farm farm, int s){
		super(s);
		setFarm(farm);
	}
	
	private void setFarm(Farm farm) {
		this.farm = farm;
	}
	
	public Farm farm(){
		return farm;
	}

}
