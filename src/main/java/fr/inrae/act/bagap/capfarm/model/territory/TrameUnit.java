package fr.inrae.act.bagap.capfarm.model.territory;

import fr.inrae.act.bagap.capfarm.CAPFarm;
import fr.inrae.act.bagap.apiland.core.element.DefaultDynamicFeature;
import fr.inrae.act.bagap.apiland.core.element.type.DynamicFeatureType;

public class TrameUnit extends DefaultDynamicFeature {

	private static final long serialVersionUID = 1L;

	public TrameUnit(DynamicFeatureType type) {
		super(type);
	}

	public double getArea(){
		return getArea(CAPFarm.t);
	}
}
