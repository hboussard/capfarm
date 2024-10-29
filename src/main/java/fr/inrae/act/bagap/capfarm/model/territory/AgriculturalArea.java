package fr.inrae.act.bagap.capfarm.model.territory;

import java.util.HashSet;
import java.util.Set;

import fr.inrae.act.bagap.apiland.core.element.DefaultDynamicLayer;
import fr.inrae.act.bagap.apiland.core.element.type.DynamicLayerType;

public class AgriculturalArea extends DefaultDynamicLayer<FarmTerritory> implements Area {

	private static final long serialVersionUID = 1L;

	public AgriculturalArea(DynamicLayerType type) {
		super(type);
	}	

	@Override
	public void display(){
		System.out.println("trame agricole : nombre d'exploitations = "+size());
		for(FarmTerritory ft : this){
			ft.display();
		}
	}

	public int totalParcelsArea() {
		int area = 0;
		for(FarmTerritory ft : this){
			area += ft.totalParcelsArea();
		}
		return area;
	}
	
	public Set<Parcel> parcels(){
		Set<Parcel> parcels = new HashSet<Parcel>();
		for(FarmTerritory ft : this){
			parcels.addAll(ft.parcels());
		}
		return parcels;
	}
	
	public Parcel parcel(String id){
		for(FarmTerritory ft : this){
			if(ft.contains(id)){
				return (Parcel) ft.get(id);
			}
		}
		throw new IllegalArgumentException();
	}

	public int totalEdgesLength() {
		int length = 0;
		for(FarmTerritory ft : this){
			length += ft.totalEdgesLength();
		}
		return length;
	}

	public void clearParcels() {
		for(FarmTerritory ft : this){
			ft.clearParcels();
		}
	}

}

