package fr.inrae.act.bagap.capfarm.model.territory;

import java.util.HashSet;
import java.util.Set;

import fr.inrae.act.bagap.apiland.analysis.Stats;
import fr.inrae.act.bagap.apiland.core.element.DefaultDynamicLayer;
import fr.inrae.act.bagap.apiland.core.element.type.DynamicLayerType;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class FarmTerritory extends DefaultDynamicLayer<FarmUnit> {

	private static final long serialVersionUID = 1L;

	public FarmTerritory(DynamicLayerType type) {
		super(type);
	}
	
	public Set<Parcel> parcels(){
		return (Set<Parcel>) set(Parcel.class);
	}

	/*
	public <P extends Parcel> Set<P> parcels(){
		return (Set<P>) set();
	}
	*/
	
	public Set<Facility> facilities(){
		return (Set<Facility>) set(Facility.class);
	}
	
	public int getDistanceFromFacilitiesToParcel(String facilities, Parcel parcel){
		int d = Integer.MAX_VALUE;
		boolean findMoyen = false;
		for(Facility b : facilities()){
			if(b.containsFacility(facilities)){
				findMoyen = true;
				d = Math.min(d, (int) b.getShape().distance(parcel.getShape()));
			}
		}
		if(!findMoyen){
			System.out.println("the facilility '"+facilities+"' do not exists on the farm.");
			return -1;
		}
		return d;
	}

	public int totalParcelsArea() {
		int area = 0;
		for(Parcel p : parcels()){
			area += p.getArea();
		}
		return area;
	}

	public void displayParcelAllocation(Instant t) {
		for(Parcel p : parcels()){
			System.out.println("parcel "+p.getId()+" -> "+p.getAttribute("crop").getValue(t));
		}
	}

	public void clearParcels() {
		for(Parcel p : parcels()){
			p.clearAttributes();
		}
	}

	public int totalEdgesLength() {
		double length = 0;
		Set<Parcel> ever = new HashSet<Parcel>();
		for(Parcel p1 : parcels()){
			ever.add(p1);
			for(Parcel p2 : parcels()){
				if(!ever.contains(p2) && p1.getShape().intersects(p2.getShape())){	
					length += p1.getShape().intersection(p2.getShape()).getLength();
				}
			}
		}
		return (int) length;
	}

	public int[] edgesLength() {
		int size = (int) (Math.pow(parcels().size(), 2) - parcels().size()) / 2;
		int[] edgeLength = new int[size];
		Set<Parcel> ever = new HashSet<Parcel>();
		int i=0;
		for(Parcel p1 : parcels()){
			ever.add(p1);
			for(Parcel p2 : parcels()){
				if(!ever.contains(p2)){
					if(p1.getShape().intersects(p2.getShape())){
						edgeLength[i++] = (int) p1.getShape().intersection(p2.getShape()).getLength();	
					}else{
						edgeLength[i++] = 0;
					}	
				}
			}
		}
		return edgeLength;
	}
	
	@Override
	public void display(){
		System.out.println("ferme "+getId());
		Stats sp = new Stats();
		Stats sf = new Stats();
		for(FarmUnit tu : this){
			if(tu instanceof Parcel){
				//System.out.println(tu.getId());
				sp.add(((Parcel) tu).getArea());
			}else{
				sf.add(((Facility) tu).getArea());
			}
		}
		sp.calculate();
		sf.calculate();
		
		System.out.println("nombre de parcelles = "+sp.size());
		System.out.println("surface totale des parcelles = "+sp.getSum());
		System.out.println("surface moyenne des parcelles = "+sp.getAverage());
		
		
		System.out.println("nombre des moyens de production = "+sf.size());
		System.out.println("surface totale des moyens de production = "+sf.getSum());
		//System.out.println("taille moyenne des parcelless : "+sf.getAverage());
	}
	
	
}
