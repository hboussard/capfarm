package fr.inrae.act.bagap.capfarm.model.economic;

import fr.inrae.act.bagap.capfarm.model.CoverUnit;

public class MaeliaManagmentProfil extends ManagmentProfil {
	
	private int[][] distanceCovers;
	
	public MaeliaManagmentProfil(CoverUnit[] covers, int[] works) {
		super(covers, works);
	}
	
	public MaeliaManagmentProfil(CoverUnit[] covers, int[] works, int[][] distanceCovers) {
		super(covers, works);
		this.distanceCovers = distanceCovers;
	}

	@Override
	protected int work(int index) {
		return getWorks()[index];
	}
	
	public int[][] getDistanceCovers(){
		return distanceCovers;
	}

}
