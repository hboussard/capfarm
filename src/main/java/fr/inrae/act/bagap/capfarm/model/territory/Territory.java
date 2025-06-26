package fr.inrae.act.bagap.capfarm.model.territory;


import fr.inrae.act.bagap.apiland.core.element.DefaultDynamicLayer;
import fr.inrae.act.bagap.apiland.core.element.type.DynamicLayerType;

public class Territory extends DefaultDynamicLayer<Area> {

	private static final long serialVersionUID = 1L;

	public Territory(DynamicLayerType type) {
		super(type);
	}	

	@Override
	public void display(){
		for(Area a : this){
			a.display();
		}
		System.out.println();
	}
	
	public int totalParcelsArea() {
		int area = 0;
		for(Area a : this){
			if(a instanceof AgriculturalArea){
				area += ((AgriculturalArea) a).totalParcelsArea();
			}
		}
		return area;
	}
	/*
	public int totalParcelsArea() {
		int area = 0;
		for(Area a : this){
			if(a instanceof AgriculturalArea){
				area += ((AgriculturalArea) a).totalParcelsArea();
			}
		}
		return area;
	}
	
	public Set<Parcel> parcels(){
		Set<Parcel> parcels = new HashSet<Parcel>();
		for(Area a : this){
			if(a instanceof AgriculturalArea){
				parcels.addAll(((AgriculturalArea) a).parcels());
			}
		}
		return parcels;
	}
	
	public Parcel parcel(String id){
		for(Area a : this){
			if(a instanceof AgriculturalArea){
				if(((AgriculturalArea) a).contains(id)){
					return ((AgriculturalArea) a).parcel(id);
				}
			}
		}
		throw new IllegalArgumentException();
	}
	*/
}


