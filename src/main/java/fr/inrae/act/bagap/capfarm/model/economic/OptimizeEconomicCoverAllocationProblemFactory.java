package fr.inrae.act.bagap.capfarm.model.economic;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblem;
import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblemFactory;
import fr.inrae.act.bagap.capfarm.csp.CoverAllocator;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.economic.csp.OptimizeEconomicCoverAllocationProblem;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class OptimizeEconomicCoverAllocationProblemFactory extends CoverAllocationProblemFactory {

	private String economicProfil, workProfil, distanceCoversProfil;
	
	public OptimizeEconomicCoverAllocationProblemFactory(String economicProfil, String workProfil, String distanceCoversProfil){
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
		//EcoomicProfil ep = EconomicProfilFactory.create(covers);
		
		//EconomicProfil ep = EconomicProfilFactory.create("C:/Hugues/modelisation/maelia/coupling/profil_economique.csv");
		EconomicProfil ep = EconomicProfilFactory.createMaelia(covers, economicProfil);
		
		MaeliaManagmentProfil mp = null;
		if(workProfil != null){
			if(distanceCoversProfil != null){
				mp = ManagmentProfilFactory.create(covers, workProfil, distanceCoversProfil);
			}else{
				mp = ManagmentProfilFactory.create(covers, workProfil);
			}
		}
		
		return new OptimizeEconomicCoverAllocationProblem(coverAllocator, t, ep, mp);
	}
	
}
