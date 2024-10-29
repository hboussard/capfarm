package fr.inrae.act.bagap.capfarm.model.constraint;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ErrorNode;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocator;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;

public class ConstraintLocationListener extends LocationBaseListener {
	
	private CoverAllocator allocator;
	
	private Deque<List<Set<Parcel>>> stack;
	
	private List<Set<Parcel>> level;
	
	private Set<Parcel> local;
	
	private Deque<Boolean> signs;
	
	private boolean add;
	
	private boolean first;
	
	private int nbErrors;
	
	private StringBuffer error;
	
	public ConstraintLocationListener(CoverAllocator allocator){
		this.allocator = allocator;
		stack = new ArrayDeque<List<Set<Parcel>>>();
		level = new ArrayList<Set<Parcel>>();
		signs = new ArrayDeque<Boolean>();
		error = new StringBuffer();
	}
	
	@Override 
	public void enterLocalisation(@NotNull LocationParser.LocalisationContext ctx) {
		//System.out.println("enterLocalisation '"+ctx.getText()+"'");
		
		local = new TreeSet<Parcel>(); // cr�ation d'une nouvelle localisation
		add = true;
		first = true;
	}
	
	@Override 
	public void exitLocalisation(@NotNull LocationParser.LocalisationContext ctx) {
		//System.out.println("exitLocalisation '"+ctx.getText()+"'");
		
		level.add(local);
	}
	
	@Override 
	public void enterAndterme(@NotNull LocationParser.AndtermeContext ctx) {
		//System.out.println("enterAndterme '"+ctx.getText()+"'");
		
		level.add(local); // stockage de la localisation en cours
		
		stack.push(level); // stockage du level en cours
		
		level = new ArrayList<Set<Parcel>>(); // mise en place d'un nouveau level
		
		signs.push(add); // stockage du signe
	}
	
	private Set<Parcel> and(Set<Parcel> s1, Set<Parcel> s2){
		Set<Parcel> l = new TreeSet<Parcel>();
		for(Parcel p1 : s1){
			if(s2.contains(p1)){
				l.add(p1);
			}
		}
		return l; 
	}
	
	@Override 
	public void exitAndterme(@NotNull LocationParser.AndtermeContext ctx) { 
		//System.out.println("exitAndterme '"+ctx.getText()+"'");
		
		Set<Parcel> land = level.get(0);
		for(int i=1; i<level.size(); i++){
			land = and(land, level.get(i));
		}
		
		level = stack.pop();
		local = level.get(level.size()-1);
		
		add = signs.pop();
		if(add){
			for(Parcel p : land){
				local.add(p);
			}
		}else{
			for(Parcel p : land){
				local.remove(p);
			}
		}
	}
	
	@Override 
	public void enterOrterme(@NotNull LocationParser.OrtermeContext ctx) { 
		//System.out.println("enterXorterme '"+ctx.getText()+"'");
		
		level.add(local); // stockage de la localisation en cours
		
		stack.push(level); // stockage du level en cours
		
		level = new ArrayList<Set<Parcel>>(); // mise en place d'un nouveau level
		
		signs.push(add); // stockage du signe
	}
	
	@Override 
	public void exitOrterme(@NotNull LocationParser.OrtermeContext ctx) { 
		//System.out.println("exitXorterme '"+ctx.getText()+"'");
		
		Set<Parcel> lor = new TreeSet<Parcel>();
		lor.addAll(level.get(0));
		for(int i=1; i<level.size(); i++){
			lor.addAll(level.get(i));
		}
		
		level = stack.pop();
		local = level.get(level.size()-1);
		
		add = signs.pop();
		if(add){
			for(Parcel p : lor){
				local.add(p);
			}
		}else{
			for(Parcel p : lor){
				local.remove(p);
			}
		}
	}
	
	@Override 
	public void enterXorterme(@NotNull LocationParser.XortermeContext ctx) { 
		//System.out.println("enterXorterme '"+ctx.getText()+"'");
		
		level.add(local); // stockage de la localisation en cours
		
		stack.push(level); // stockage du level en cours
		
		level = new ArrayList<Set<Parcel>>(); // mise en place d'un nouveau level
		
		signs.push(add); // stockage du signe
	}
	
	@Override 
	public void exitXorterme(@NotNull LocationParser.XortermeContext ctx) { 
		//System.out.println("exitXorterme '"+ctx.getText()+"'");
		
		Set<Parcel> lxor = new TreeSet<Parcel>();
		Set<Parcel> land = level.get(0);
		lxor.addAll(level.get(0));
		for(int i=1; i<level.size(); i++){
			land = and(land, level.get(i));
			lxor.addAll(level.get(i));
		}
		
		lxor.removeAll(land);
				
		level = stack.pop();
		local = level.get(level.size()-1);
		
		add = signs.pop();
		if(add){
			for(Parcel p : lxor){
				local.add(p);
			}
		}else{
			for(Parcel p : lxor){
				local.remove(p);
			}
		}
	}
	
	@Override 
	public void enterPartout(@NotNull LocationParser.PartoutContext ctx) { 
		//System.out.println("enterPartout '"+ctx.getText()+"'");
		
		local = allocator.parcels();
		first = false;
	}
	
	@Override 
	public void enterPlusminus(@NotNull LocationParser.PlusminusContext ctx) { 
		//System.out.println("enterPlusminus '"+ctx.getText()+"'");
		
		switch(ctx.getText()){
		case "+" : add = true; break;
		case "-" : 
			add = false; 
			if(first){
				local = allocator.parcels();
				first = false;
			}
			break;
		}
	}
	
	@Override 
	public void enterParcelles(@NotNull LocationParser.ParcellesContext ctx) { 
		//System.out.println("enterParcelles '"+ctx.getText()+"'");
		
		String[] pp = ctx.getText().replace("[", "").replace("]", "").split(",");
		
		if(add){
			for(String p : pp){
				local.add(allocator.parcel(p));
			}
		}else{
			for(String p : pp){
				Parcel parcel = allocator.parcel(p);
				if(local.contains(parcel)){
					local.remove(parcel);
				}
			}
		}
			
		first = false;
	}
	
	@Override 
	public void enterBoolatt(@NotNull LocationParser.BoolattContext ctx) { 
		//System.out.println("enterBoolatt '"+ctx.getText()+"'");
		
		String condition = ctx.getText().replace("[", "").replace("]", "");
		boolean trueValue = true;
		if(condition.contains("='F'") || condition.contains("=F")){
			trueValue = false;
		}
		condition = condition.replace("='F'", "").replace("=F", "").replace("='T'", "").replace("=T", "");
		
		if(add){
			if(trueValue){
				for(Parcel p : allocator.parcels()){
					if(((String) p.getAttribute(condition).getValue(null)).equals("T")){
						local.add(p);
					}
				}
			}else{
				for(Parcel p : allocator.parcels()){
					if(((String) p.getAttribute(condition).getValue(null)).equals("F")){
						local.add(p);
					}
				}
			}
		}else{
			if(trueValue){
				for(Parcel p : allocator.parcels()){
					if(((String) p.getAttribute(condition).getValue(null)).equals("T")){
						local.remove(p);
					}
				}
			}else{
				for(Parcel p : allocator.parcels()){
					if(((String) p.getAttribute(condition).getValue(null)).equals("F")){
						local.remove(p);
					}
				}
			}
			
		}
		
		first = false;
	}
	
	@Override 
	public void enterStringatt(@NotNull LocationParser.StringattContext ctx) { 
		//System.out.println("enterStringatt '"+ctx.getText()+"'");
		
		String condition = ctx.getText().replace("[", "").replace("]", "");
		String[] infos = condition.split("=");
		String att = infos[0].replace(" ", "");
		String val = infos[1].replace(" ", "").replace("'", "");
		
		if(add){
			for(Parcel p : allocator.parcels()){
				if(((String) p.getAttribute(att).getValue(null)).equals(val)){
					local.add(p);
				}
			}
		}else{
			for(Parcel p : allocator.parcels()){
				if(((String) p.getAttribute(att).getValue(null)).equals(val)){
					local.remove(p);
				}
			}
		}
		
		first = false;
	}
	
	
	@Override 
	public void enterNumatt(@NotNull LocationParser.NumattContext ctx) { 
		//System.out.println("enterNumatt '"+ctx.getText()+"'");
		
		String condition = ctx.getText().replace("[", "").replace("]", "");
		
		// code = 1 --> <=
		// code = 2 --> >=
		// code = 3 --> =
		// code = 4 --> <
		// code = 5 --> >
		int code = -1; 
		String[] pp = null;
		if(condition.contains("<=")){
			pp = condition.split("<=");
			code = 1;
		}else if(condition.contains(">=")){
			pp = condition.split(">=");
			code = 2;
		}else if(condition.contains("=")){
			pp = condition.split("=");
			code = 3;
		}else if(condition.contains("<")){
			pp = condition.split("<");
			code = 4;
		}else if(condition.contains(">")){
			pp = condition.split(">");
			code = 5;
		}
		
		if(add){
			switch(code){
			case 1 : 
				for(Parcel p : allocator.parcels()){
					if(((Number) p.getAttribute(pp[0]).getValue(null)).doubleValue() <= Double.parseDouble(pp[1])){
						local.add(p);
					}
				}
				break;
			case 2 : 
				for(Parcel p : allocator.parcels()){
					if(((Number) p.getAttribute(pp[0]).getValue(null)).doubleValue() >= Double.parseDouble(pp[1])){
						local.add(p);
					}
				}
				break;
			case 3 : 
				for(Parcel p : allocator.parcels()){
					if(((Number) p.getAttribute(pp[0]).getValue(null)).doubleValue() == Double.parseDouble(pp[1])){
						local.add(p);
					}
				}
				break;
			case 4 : 
				for(Parcel p : allocator.parcels()){
					if(((Number) p.getAttribute(pp[0]).getValue(null)).doubleValue() < Double.parseDouble(pp[1])){
						local.add(p);
					}
				}
				break;
			case 5 : 
				for(Parcel p : allocator.parcels()){
					if(((Number) p.getAttribute(pp[0]).getValue(null)).doubleValue() > Double.parseDouble(pp[1])){
						local.add(p);
					}
				}
				break;
			}
		}else{
			switch(code){
			case 1 : 
				for(Parcel p : allocator.parcels()){
					if(((Number) p.getAttribute(pp[0]).getValue(null)).doubleValue() <= Double.parseDouble(pp[1])){
						local.remove(p);
					}
				}
				break;
			case 2 : 
				for(Parcel p : allocator.parcels()){
					if(((Number) p.getAttribute(pp[0]).getValue(null)).doubleValue() >= Double.parseDouble(pp[1])){
						local.remove(p);
					}
				}
				break;
			case 3 : 
				for(Parcel p : allocator.parcels()){
					if(((Number) p.getAttribute(pp[0]).getValue(null)).doubleValue() == Double.parseDouble(pp[1])){
						local.remove(p);
					}
				}
				break;
			case 4 : 
				for(Parcel p : allocator.parcels()){
					if(((Number) p.getAttribute(pp[0]).getValue(null)).doubleValue() < Double.parseDouble(pp[1])){
						local.remove(p);
					}
				}
				break;
			case 5 : 
				for(Parcel p : allocator.parcels()){
					if(((Number) p.getAttribute(pp[0]).getValue(null)).doubleValue() > Double.parseDouble(pp[1])){
						local.remove(p);
					}
				}
				break;
			}
		}
			
		first = false;
	}
	
	@Override 
	public void enterArea(@NotNull LocationParser.AreaContext ctx) {
		//System.out.println("enterArea '"+ctx.getText()+"'");
		
		String condition = ctx.getText().replace("AREA", "");
		
		int code = -1; 
		String[] pp = null;
		if(condition.contains("<=")){
			pp = condition.split("<=");
			code = 1;
		}else if(condition.contains(">=")){
			pp = condition.split(">=");
			code = 2;
		}else if(condition.contains("=")){
			pp = condition.split("=");
			code = 3;
		}else if(condition.contains("<")){
			pp = condition.split("<");
			code = 4;
		}else if(condition.contains(">")){
			pp = condition.split(">");
			code = 5;
		}
		
		if(add){
			for(Parcel p : allocator.parcels()){
				int area = p.getArea();
				switch(code){
				case 1 : 
					if(area <= Double.parseDouble(pp[1])*10000){
						local.add(p);
					}
					break;
				case 2 : 
					if(area >= Double.parseDouble(pp[1])*10000){
						local.add(p);
					}
					break;
				case 3 : 
					if(area == Double.parseDouble(pp[1])*10000){
						local.add(p);
					}
					break;
				case 4 : 
					if(area < Double.parseDouble(pp[1])*10000){
						local.add(p);
					}
					break;
				case 5 : 
					if(area > Double.parseDouble(pp[1])*10000){
						local.add(p);
					}
					break;
				}
			}
		}else{
			for(Parcel p : allocator.parcels()){
				int area = p.getArea();
				switch(code){
				case 1 : 
					if(area <= Double.parseDouble(pp[1])*10000){
						local.remove(p);
					}
					break;
				case 2 : 
					if(area >= Double.parseDouble(pp[1])*10000){
						local.remove(p);
					}
					break;
				case 3 : 
					if(area == Double.parseDouble(pp[1])*10000){
						local.remove(p);
					}
					break;
				case 4 : 
					if(area < Double.parseDouble(pp[1])*10000){
						local.remove(p);
					}
					break;
				case 5 : 
					if(area > Double.parseDouble(pp[1])*10000){
						local.remove(p);
					}
					break;
				}
			}
		}
		
		first = false;
	}
	
	@Override 
	public void enterDistance(@NotNull LocationParser.DistanceContext ctx) { 
		//System.out.println("enterDistance '"+ctx.getText()+"'");
		
		String condition = ctx.getText().replace("DISTANCE(", "").replace(")", "");
		
		// code = 1 --> <=
		// code = 2 --> >=
		// code = 3 --> =
		// code = 4 --> <
		// code = 5 --> >
		int code = -1; 
		String[] pp = null;
		if(condition.contains("<=")){
			pp = condition.split("<=");
			code = 1;
		}else if(condition.contains(">=")){
			pp = condition.split(">=");
			code = 2;
		}else if(condition.contains("=")){
			pp = condition.split("=");
			code = 3;
		}else if(condition.contains("<")){
			pp = condition.split("<");
			code = 4;
		}else if(condition.contains(">")){
			pp = condition.split(">");
			code = 5;
		}
		
		if(add){
			for(Parcel p : allocator.parcels()){
				int distance = allocator.getDistanceFromFacilitiesToParcel(pp[0], p);
				switch(code){
				case 1 : 
					if(distance <= Double.parseDouble(pp[1])*1000){
						local.add(p);
					}
					break;
				case 2 : 
					if(distance >= Double.parseDouble(pp[1])*1000){
						local.add(p);
					}
					break;
				case 3 : 
					if(distance == Double.parseDouble(pp[1])*1000){
						local.add(p);
					}
					break;
				case 4 : 
					if(distance < Double.parseDouble(pp[1])*1000){
						local.add(p);
					}
					break;
				case 5 : 
					if(distance > Double.parseDouble(pp[1])*1000){
						local.add(p);
					}
					break;
				}
			}
		}else{
			for(Parcel p : allocator.parcels()){
				int distance = allocator.getDistanceFromFacilitiesToParcel(pp[0], p);
				switch(code){
				case 1 : 
					if(distance <= Double.parseDouble(pp[1])*1000){
						local.remove(p);
					}
					break;
				case 2 : 
					if(distance >= Double.parseDouble(pp[1])*1000){
						local.remove(p);
					}
					break;
				case 3 : 
					if(distance == Double.parseDouble(pp[1])*1000){
						local.remove(p);
					}
					break;
				case 4 : 
					if(distance < Double.parseDouble(pp[1])*1000){
						local.remove(p);
					}
					break;
				case 5 : 
					if(distance > Double.parseDouble(pp[1])*1000){
						local.remove(p);
					}
					break;
				}
			}
		}
		
		first = false;
	}
	
	public Set<Parcel> buildLocation() {
		if(first){
			local = allocator.parcels();
		}
		return local;
	}
	
	public boolean hasError(){
		return nbErrors > 0;
	}
	
	public String toStringError(){
		error.append(" "+nbErrors+" errors.");
		return error.toString();
	}
	
	@Override public void visitErrorNode(@NotNull ErrorNode node) { 
		nbErrors++;
		error.append(node.getText()+" ");
	}
	
}
