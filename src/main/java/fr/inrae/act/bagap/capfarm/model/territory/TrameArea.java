package fr.inrae.act.bagap.capfarm.model.territory;

import fr.inrae.act.bagap.apiland.analysis.Stats;
import fr.inrae.act.bagap.apiland.core.element.DefaultDynamicLayer;
import fr.inrae.act.bagap.apiland.core.element.type.DynamicLayerType;

public class TrameArea extends DefaultDynamicLayer<TrameUnit> implements Area {

	private static final long serialVersionUID = 1L;

	public TrameArea(DynamicLayerType type) {
		super(type);
	}

	@Override
	public void display(){
		System.out.println("trame non agricole : "+size()+" elements ");
		Stats s = new Stats();
		for(TrameUnit tu : this){
			s.add(tu.getArea());
		}
		s.calculate();
		System.out.println("taille moyenne des �l�ments : "+s.getAverage());
	}
}
