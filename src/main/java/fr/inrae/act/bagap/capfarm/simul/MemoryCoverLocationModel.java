package fr.inrae.act.bagap.capfarm.simul;

import java.util.Map;
import java.util.TreeMap;

import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.Farm;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.apiland.core.composition.DynamicAttribute;
import fr.inrae.act.bagap.apiland.core.composition.DynamicAttributeType;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class MemoryCoverLocationModel extends CoverLocationModel {

	private static final long serialVersionUID = 1L;
	
	private Map<String, DynamicAttribute<CoverUnit>> covers;
	
	private boolean quiet;

	public MemoryCoverLocationModel(CfmSimulator simulator, Farm farm) {
		this(simulator, farm, false);
	}
	
	public MemoryCoverLocationModel(CfmSimulator simulator, Farm farm, boolean quiet) {
		super(simulator, farm);
		covers = new TreeMap<String, DynamicAttribute<CoverUnit>>();
		this.quiet = quiet;
	}

	@Override
	public boolean make(Instant t) {
		if(!quiet){
			for(Parcel p : getCoverAllocator().parcels()){
				if(covers.get(p.getId()).getActive(t).getTime().start().equals(t)){
					p.getAttribute("cover").setValue(t, covers.get(p.getId()).getValue(t));
				}
			}
		}
		return true;
	}
	
	public void initParcel(String id, DynamicAttributeType type){
		covers.put(id, new DynamicAttribute<CoverUnit>(type));
	}

	public void setCover(String parcel, CoverUnit cover, Instant year) {
		covers.get(parcel).setValue(year, cover);
	}
	
	public CoverUnit getCover(String parcel, Instant t){
		return covers.get(parcel).getValue(t);
	}

}
