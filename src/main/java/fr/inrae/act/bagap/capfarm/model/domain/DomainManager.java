package fr.inrae.act.bagap.capfarm.model.domain;

public class DomainManager {

	public static Domain<Double, Double> getDoubleDoubleDomain(String value) {
		value = value.replace(" ", "");
		if(value.startsWith("[")){
			if(value.endsWith("]")){
				String[] d = value.replace("[", "").replace("]", "").replace(" ", "").split(",", 2);
				if(!d[0].equalsIgnoreCase("")){
					double d0 = Double.parseDouble(d[0]);
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new BoundedDomain<Double, Double>(">=", d0, "<=", d1);
					}else{
						return new NumberDomain<Double, Double>(">=", d0);
					}
				}else{
					if(!d[1].equalsIgnoreCase("")){
						double d1 =Double.parseDouble(d[1]);
						return new NumberDomain<Double, Double>("<=", d1);
					}else{
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}else{ // ends with "["
				String[] d = value.replace("[", "").split(",", 2);
				if(!d[0].equalsIgnoreCase("")){
					double d0 = Double.parseDouble(d[0]);
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new BoundedDomain<Double, Double>(">=", d0, "<", d1);
					}else{
						return new NumberDomain<Double, Double>(">=", d0);
					}
				}else{
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new NumberDomain<Double, Double>("<", d1);
					}else{
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}
		}else{ // begins with "]"
			if(value.endsWith("]")){
				String[] d = value.replace("]", "").split(",", 2);
				if(!d[0].equalsIgnoreCase("")){
					double d0 = Double.parseDouble(d[0]);
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new BoundedDomain<Double, Double>(">", d0, "<=", d1);
					}else{
						return new NumberDomain<Double, Double>(">", d0);
					}
				}else{
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new NumberDomain<Double, Double>("<=", d1);
					}else{
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}else{ // ends with "["
				String[] d = value.replace("[", "").replace("]", "").split(",", 2);
				if(!d[0].equalsIgnoreCase("")){
					double d0 = Double.parseDouble(d[0]);
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new BoundedDomain<Double, Double>(">", d0, "<", d1);
					}else{
						return new NumberDomain<Double, Double>(">", d0);
					}
				}else{
					if(!d[1].equalsIgnoreCase("")){
						double d1 = Double.parseDouble(d[1]);
						return new NumberDomain<Double, Double>("<", d1);
					}else{
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}
		}
	}
	
}
