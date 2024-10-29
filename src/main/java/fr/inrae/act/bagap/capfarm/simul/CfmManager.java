package fr.inrae.act.bagap.capfarm.simul;

import java.net.URL;
import java.util.LinkedList;

import fr.inrae.act.bagap.capfarm.CAPFarm;
import fr.inrae.act.bagap.capfarm.model.territory.Territory;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.core.time.delay.YearDelay;
import fr.inrae.act.bagap.apiland.simul.OutputAnalysis;
import fr.inrae.act.bagap.apiland.simul.SimulationManager;

public class CfmManager extends SimulationManager {

	private static final long serialVersionUID = 1L;
	
	private Territory territory;
	
	private LinkedList<OutputAnalysis> outputs;
	
	private CfmMode mode = CfmMode.IDLE;
	
	private CfmProcessMode processMode = CfmProcessMode.ACTIVATE;
	
	private String paramProcessMode;
	
	private String methodProcessMode;
	
	private String probaTimeFolder;
	
	//private BufferedReader inputProbaTimeFolder;
	
	private String economicProfil, workProfil, distanceCoversProfil;
	
	private boolean check;

	public CfmManager(int s){
		super(s);
		setDelay(new YearDelay(1));
		outputs = new LinkedList<OutputAnalysis>();
		check = false;
		
		URL url = CfmManager.class.getResource("proba_times/");
		//System.out.println(url);
		if (url != null){
			probaTimeFolder = url.toString();
		}
		
	}
	
	@Override
	public void setStart(Instant t){
		CAPFarm.t = t;
		super.setStart(t);
	}

	public void setMode(CfmMode mode){
		this.mode = mode;
	}
	
	public void setProcessMode(CfmProcessMode mode, String paramProcessMode, String methodProcessMode){
		this.processMode = mode;
		this.paramProcessMode = paramProcessMode;
		this.methodProcessMode = methodProcessMode;
	}
	
	public void setProcessMode(CfmProcessMode mode, String paramProcessMode){
		this.processMode = mode;
		this.paramProcessMode = paramProcessMode;
	}
	
	public String methodProcessMode(){
		return methodProcessMode;
	}
	
	public String paramProcessMode(){
		return paramProcessMode;
	}

	public void setTerritory(Territory territory){
		this.territory = territory;
	}
	
	public void setEconomicProfil(String economicProfil){
		this.economicProfil = economicProfil;
	}
	
	public void setManagmentProfil(String workProfil, String distanceCoversProfil){
		this.workProfil = workProfil;
		this.distanceCoversProfil = distanceCoversProfil;
	}
	
	public void addOutput(OutputAnalysis output) {
		this.outputs.add(output);
	}

	public Territory territory(){
		return territory;
	}

	public LinkedList<OutputAnalysis> outputs(){
		return outputs;
	}
	
	public CfmMode mode(){
		return mode;
	}
	
	public CfmProcessMode processMode(){
		return processMode;
	}
	
	public String probaTimeFolder(){
		return probaTimeFolder;
	}
	
	/*
	public BufferedReader inputProbaTimeFolder(){
		return inputProbaTimeFolder;
	}
	*/
	
	public boolean checkConstraints(){
		return check;
	}
	
	public void checkConstraints(boolean check) {
		this.check = check;
	}
	
	public String economicProfil(){
		return economicProfil;
	}
	
	public String workProfil(){
		return workProfil;
	}
	
	public String distanceCoversProfil(){
		return distanceCoversProfil;
	}

	public void setProbaTimeFolder(String probaTimeFolder) {
		this.probaTimeFolder = probaTimeFolder;
	}
	
	
	
}
