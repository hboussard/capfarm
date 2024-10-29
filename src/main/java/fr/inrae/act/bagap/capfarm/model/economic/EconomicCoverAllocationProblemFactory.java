package fr.inrae.act.bagap.capfarm.model.economic;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblemFactory;
import fr.inrae.act.bagap.capfarm.csp.CoverAllocator;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.economic.csp.EconomicCoverAllocationProblem;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class EconomicCoverAllocationProblemFactory extends CoverAllocationProblemFactory {

	private String economicProfil, workProfil, distanceCoversProfil;
	
	public EconomicCoverAllocationProblemFactory(String economicProfil, String workProfil, String distanceCoversProfil){
		this.economicProfil = economicProfil;
		this.workProfil = workProfil;
		this.distanceCoversProfil = distanceCoversProfil;
	}
	
	@Override
	public CoverAllocationProblem create(CoverAllocator coverAllocator, Instant t){
		
		CoverUnit[] covers = new CoverUnit[coverAllocator.coverUnits().size()];
		int index = 0;
		for(CoverUnit cu : coverAllocator.coverUnits()){
			covers[index++] = cu;
		}
		
		//EconomicProfil ep = EconomicProfilFactory.create(covers);
		EconomicProfil ep = null;
		if(economicProfil != null){
			ep = EconomicProfilFactory.create(covers, economicProfil);
		}
		ManagmentProfil mp = null;
		if(workProfil != null){
			if(distanceCoversProfil != null){
				mp = ManagmentProfilFactory.create(covers, workProfil, distanceCoversProfil);
			}else{
				mp = ManagmentProfilFactory.create(covers, workProfil);
			}
		}
		
		return new EconomicCoverAllocationProblem(coverAllocator, t, ep, mp);
	}
	
}
