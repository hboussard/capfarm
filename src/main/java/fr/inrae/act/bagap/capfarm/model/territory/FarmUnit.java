package fr.inrae.act.bagap.capfarm.model.territory;

import fr.inrae.act.bagap.apiland.core.element.DefaultDynamicFeature;
import fr.inrae.act.bagap.apiland.core.element.type.DynamicFeatureType;

public abstract class FarmUnit extends DefaultDynamicFeature {

	private static final long serialVersionUID = 1L;

	public FarmUnit(DynamicFeatureType type) {
		super(type);
	}

}
