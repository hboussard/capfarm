package fr.inrae.act.bagap.capfarm.model.constraint;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import org.jumpmind.symmetric.csv.CsvReader;

import fr.inrae.act.bagap.capfarm.CAPFarm;
import fr.inrae.act.bagap.capfarm.csp.CoverAllocator;
import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverGroup;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.GenericConstraintSystem;
import fr.inrae.act.bagap.capfarm.model.domain.AllDomain;
import fr.inrae.act.bagap.capfarm.model.domain.AndDomain;
import fr.inrae.act.bagap.capfarm.model.domain.BoundedDomain;
import fr.inrae.act.bagap.capfarm.model.domain.Domain;
import fr.inrae.act.bagap.capfarm.model.domain.NumberDomain;
import fr.inrae.act.bagap.capfarm.model.domain.SetDomain;
import fr.inrae.act.bagap.capfarm.model.domain.VariableBooleanDomain;
import fr.inrae.act.bagap.capfarm.model.domain.VariableValueDomain;
import fr.inrae.act.bagap.capfarm.model.economic.constraint.ProfitConstraint;
import fr.inrae.act.bagap.capfarm.model.economic.constraint.ProfitVariabilityConstraint;
import fr.inrae.act.bagap.capfarm.model.economic.constraint.WorkConstraint;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;

public class ConstraintBuilder {

	private CoverAllocator allocator;
	
	private String code;
	
	private Set<Cover> covers;
	
	private ConstraintType type;
	
	private ConstraintMode mode;
	
	private Set<Parcel> location;
	
	private String domain;
	
	private String[] params;
	
	private GenericConstraintBuilder genericBuilder;
	
	private boolean hasLocation;
	
	private boolean checkOnly;

	public ConstraintBuilder(CoverAllocator allocator) {
		this.covers = new TreeSet<Cover>();
		this.location = new TreeSet<Parcel>();
		initCoverAllocator(allocator);
	}

	public void initCoverAllocator(CoverAllocator allocator){
		reset();
		this.allocator = allocator;
		genericBuilder = new GenericConstraintBuilder(allocator.getConstraintSystem().getGenericConstraintSystem());
	}
	
	private void reset(){
		code = null;
		covers.clear();
		type = null;
		mode = null;
		location.clear();
		domain = null;
		params = null;
		hasLocation = false;
		checkOnly = false;
	}
	
	public void setCode(String code) {
		this.code = code;
		genericBuilder.setCode(code);
	}
	
	public void setCover(String... covers) {
		if(covers[0].equalsIgnoreCase("EACH")){
			for(Cover c : allocator.coverUnits()){
				this.covers.add(c);
			}
		}else if(covers[0].equalsIgnoreCase("ALL")){
			for(Cover c : allocator.coverUnits()){
				this.covers.add(c);
			}
		}else if(covers[0].equalsIgnoreCase("EXCEPT")){
			for(Cover c : allocator.coverUnits()){
				boolean ok = true;
				for(int i=1; i<covers.length; i++){
					if(c.getCode().equalsIgnoreCase(covers[i])){		
						ok = false;
						break;
					}
				}
				if(ok){
					this.covers.add(c);
				}
			}
		}else{
			for(String code : covers){
				if(Cover.has(code)){
					this.covers.add(Cover.get(code));
				}else{
					throw new IllegalArgumentException("There is no cover : "+code);
				}
			}
		}
		genericBuilder.setCover(covers);
	}

	public void setType(ConstraintType type) {
		this.type = type;
		genericBuilder.setType(type);
	}
	
	public void setType(String type) {
		this.type = ConstraintType.valueOf(type);
		genericBuilder.setType(type);
	}

	public void setMode(ConstraintMode mode) {
		this.mode = mode;
		genericBuilder.setMode(mode);
	}
	
	public void setMode(String mode) {
		this.mode = ConstraintMode.valueOf(mode);
		genericBuilder.setMode(mode);
	}
	
	public void setLocation(String location){
		
		hasLocation = true;
		LocationParser parser = new LocationParser(new CommonTokenStream(new LocationLexer(new ANTLRInputStream(location)))); 
		ConstraintLocationListener listener = new ConstraintLocationListener(allocator);
		
		//ErrorConstraintLocationListener listener = new ErrorConstraintLocationListener();
		new ParseTreeWalker().walk(listener, parser.evaluate());
		
		if(!listener.hasError()){
			this.location.addAll(listener.buildLocation());	
		}else{
			System.err.println("error in location constraint : "+listener.toStringError());
		}
		
		genericBuilder.setLocation(location);
	}
	
	public void setDomain(String domain){
		this.domain = domain;
		genericBuilder.setDomain(domain);
	}
	
	public void setParams(String... params) {
		this.params = params;
		genericBuilder.setParams(params);
	}
	
	public void setCheckOnly(boolean check){
		checkOnly = check;
	}
	
	public void build(GenericConstraintSystem system){
		
		// affectation des couverts
		allocator.addCovers(system.getCovers());
		
		// affectation des contraintes
		for(GenericCoverAllocationConstraint gc : system.getConstraints()){
			
			setCode(gc.getCode());
			setType(gc.getType());
			setMode(gc.getMode());
			setCover(gc.getCovers());
			setLocation(gc.getLocation());
			setDomain(gc.getDomain());
			setParams(gc.getParams());
			
			build();
		}
		
		allocator.getConstraintSystem().setGenericConstraintSystem(system);
	}
	
	public void build() {
		initBuild();
		switch(type){
		case ParcelArea : 
			initParcelAreaConstraints();
			break;
		case ParcelCount : 
			initParcelCountConstraints();
			break;
		case TotalArea : 
			 initTotalAreaConstraints();
			break;
		case DistanceFromFacilities : 
			initDistanceFromFacilitiesConstraints();
			break;
		case DistanceBetweenCovers : 
			initDistanceBetweenCoversConstraints();
			break;
		case OnBooleanCondition : 
			initOnBooleanConditionConstraints();
			break;
		case OnNumericCondition : 
			initOnNumericConditionConstraints();
			break;
		case Delay : 
			initDelayConstraints();
			break;
		case Duration : 
			initDurationConstraints();
			break;
		case Repetition : 
			initRepetitionConstraints();
			break;
		case NextCover : 
			initNextCoverConstraints();
			break;
		case TemporalPattern : 
			initTemporalPatternConstraints();
			break;
		case SpatialPattern : 
			initSpatialPatternConstraints();
			break;
		case LinkedFields : 
			initLinkedFieldsConstraints();
			break;
		case OnLocation : 
			initOnLocationConstraints();
			break;
		case Profit : 
			initProfitConstraints();
			break;
		case Work : 
			initWorkConstraints();
			break;
		case ProfitVariability : 
			initProfitVariabilityConstraints();
			break;
		default : throw new IllegalArgumentException("constraint type '"+type+"' not implemented yet.");
		}
		reset();
		genericBuilder.build();
	}

	private void initBuild() {
		if(!hasLocation){
			setLocation("");
		}
		if(covers.size() == 0){
			setCover("ALL");
		}
		if(mode == null){
			setMode(ConstraintMode.ONLY);
		}
	}
	
	private void initParcelAreaConstraints() {
		Domain<Integer, Integer> domain = buildAreaDomain();
		CoverAllocationConstraint<?, ?> constraint = new ParcelAreaConstraint(code, checkOnly, mode, covers, location, domain);
		allocator.addConstraint(constraint);
	}
	
	private void initParcelCountConstraints() {
		Domain<Integer, Integer> domain = buildIntDomain(1);
		CoverAllocationConstraint<?, ?> constraint = new ParcelCountConstraint(code, checkOnly, mode, covers, location, domain);
		allocator.addConstraint(constraint);
	}
	
	private void initTotalAreaConstraints() {
		int locationArea = 0;
		for(Parcel p : location){
			locationArea += p.getArea();
		}
		Domain<Integer, Integer> domain = buildAreaDomain(locationArea);
		//System.out.println(covers.iterator().next()+" "+domain);
		CoverAllocationConstraint<?, ?> constraint = new TotalAreaConstraint(code, checkOnly, mode, covers, location, domain);
		allocator.addConstraint(constraint);
	}

	private void initDistanceFromFacilitiesConstraints() {
		Domain<Integer, Integer> domain = buildDistanceDomain();
		CoverAllocationConstraint<?, ?> constraint = new DistanceFromFacilitiesConstraint(code, checkOnly, mode, covers, location, domain, params[0]);
		allocator.addConstraint(constraint);
	}

	private void initDistanceBetweenCoversConstraints() {
		Set<Cover> targets = new TreeSet<Cover>();
		for(String p : params){
			targets.add(Cover.get(p));
		}
		Domain<Integer, Integer> domain = buildDistanceDomain();
		CoverAllocationConstraint<?, ?> constraint = new DistanceBetweenCoversConstraint(code, checkOnly, mode, covers, location, domain, targets);
		allocator.addConstraint(constraint);
	}
	
	private void initOnBooleanConditionConstraints() {
		Domain<Boolean, Parcel> domain = buildBooleanDomain();
		CoverAllocationConstraint<?, ?> constraint = new OnBooleanConditionConstraint(code, checkOnly, mode, covers, location, domain);
		allocator.addConstraint(constraint);
	}

	private void initOnNumericConditionConstraints() {
		Domain<Double, Parcel> domain = buildDoubleDomain();
		CoverAllocationConstraint<?, ?> constraint = new OnNumericConditionConstraint(code, checkOnly, mode, covers, location, domain);
		allocator.addConstraint(constraint);
	}
	
	private void initDelayConstraints() {
		Set<Cover> targets = new TreeSet<Cover>();
		if(params[0] != null){
			//System.out.println(params[0]);
			targets.add(Cover.get(params[0]));	
		}else{
			targets.addAll(covers);
		}
		Domain<Integer, Integer> domain = buildIntDomain(1);
		CoverAllocationConstraint<?, ?> constraint = new DelayConstraint(code, checkOnly, mode, covers, location, domain, targets);
		allocator.addConstraint(constraint);
	}
	
	private void initDurationConstraints() {
		if(params == null){
			setParams("middle");
		}
		boolean hasMax = hasMax();
		Domain<Integer, Integer> domain = buildBoundedDomain(1, 1, 12);
		CoverAllocationConstraint<?, ?> constraint = new DurationConstraint(code, checkOnly, mode, covers, location, domain, params[0], hasMax);
		allocator.addConstraint(constraint);
	}

	private void initRepetitionConstraints() {
		if(params == null){
			setParams("middle");
		}
		boolean hasMax = hasMax();
		Domain<Integer, Integer> domain = buildBoundedDomain(1, 0, 12);
		CoverAllocationConstraint<?, ?> constraint = null;
		if(genericBuilder.getCover().equalsIgnoreCase("EACH")){
			Set<Cover> cov = new TreeSet<Cover>();
			for(Cover c : covers){
				cov.clear();
				cov.add(c);
				constraint = new RepetitionConstraint(code+"_"+c.getCode(), checkOnly, mode, cov, location, domain, params[0], hasMax);
				allocator.addConstraint(constraint);
			}
		}else{
			constraint = new RepetitionConstraint(code, checkOnly, mode, covers, location, domain, params[0], hasMax);
			allocator.addConstraint(constraint);
		}
		
	}
	
	private boolean hasMax() {
		if(domain.contains("[") || domain.contains("]")){
			String[] d = domain.replace("[", "").replace("]", "").replace(" ", "").split(",", 2);
			if (!d[1].equalsIgnoreCase("")) {
				return true;
			}else{
				return false;
			}
		}
		return true;
	}

	private void initNextCoverConstraints() {
		if(domain != null && !domain.equalsIgnoreCase("")){
			Domain<CoverUnit, CoverUnit> domain = buildCoverDomain();
			CoverAllocationConstraint<?, ?> constraint = new NextCoverConstraint(code, checkOnly, mode, covers, location, domain);
			allocator.addConstraint(constraint);
		}else if(params != null || !new File(params[0]).exists()){
			/*if(!new File(params[0]).exists()){
				throw new FileNotFoundException("le fichier pr�c�dent-suivant n'existe pas : "+params[0]);
			}*/
			try {	
				//System.out.println("ici");
				CsvReader cr = new CsvReader(params[0]);
				cr.setDelimiter(';');
				cr.readHeaders();
				CoverUnit prec;
				Set<CoverUnit> next;
				CoverAllocationConstraint<?, ?> constraint;
				Set<Cover> cov = new TreeSet<Cover>();
				Set<Cover> total = new TreeSet<Cover>();
				while(cr.readRecord()){
					cov.clear();
					prec = allocator.getCoverUnit(cr.get("previous"));
					cov.add(prec);
					total.add(prec);
					next = new TreeSet<CoverUnit>();
					for(int c=1; c<cr.getHeaderCount(); c++){
						if(cr.get(c).equals("1")){
							next.add(allocator.getCoverUnit(cr.getHeaders()[c]));
						}
					}
					if(next.size() != allocator.coverUnits().size()){
						//System.out.println(cov+" / "+next);
						constraint = new NextCoverConstraint(code+"_"+prec.getCode(), checkOnly, mode, cov, location, new SetDomain<CoverUnit>(next));
						allocator.addConstraint(constraint);
					}
				}
				
				
				constraint = new OnLocationConstraint(new File(params[0]).getName(), false, ConstraintMode.ALWAYS, total, location);
				allocator.addConstraint(constraint);
				
				cr.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}else{
			throw new IllegalArgumentException("initialization problem for NextCover constraint");
		}
	}
	
	private void initTemporalPatternConstraints() {
		String[] p = params[0].split("-");
		Map<Integer, Cover> pc = new TreeMap<Integer, Cover>();
		for(int i=0; i<p.length; i++){
			if(!p[i].equalsIgnoreCase("X")){
				if(Cover.has(p[i])){
					if(p[i].startsWith("~")){
						pc.put(-1*(p.length-1-i), Cover.get(p[i].replace("~", "")));
					}else{
						pc.put(p.length-1-i, Cover.get(p[i]));
					}
				}else{
					throw new IllegalArgumentException("Cover "+p[i]+" does not exists");
				}
			}
		}
		CoverAllocationConstraint<?, ?> constraint = new TemporalPatternConstraint(code, checkOnly, mode, covers, location, pc);
		allocator.addConstraint(constraint);
	}

	private void initSpatialPatternConstraints() {
		Domain<Integer, Integer> domain = buildIntDomain(1);
		CoverAllocationConstraint<?, ?> constraint = new SpatialPatternConstraint(code, checkOnly, mode, covers, location, domain);
		allocator.addConstraint(constraint);
	}
	
	private void initLinkedFieldsConstraints() {
		if(mode == ConstraintMode.ONLY){
			setMode(ConstraintMode.ALWAYS);
		}
		CoverAllocationConstraint<?, ?> constraint = new LinkedFieldsConstraint(code, checkOnly, mode, covers, location);
		allocator.addConstraint(constraint);
	}
	
	private void initOnLocationConstraints() {
		CoverAllocationConstraint<?, ?> constraint = new OnLocationConstraint(code, checkOnly, mode, covers, location);
		allocator.addConstraint(constraint);
	}
	
	private void initProfitConstraints(){
		Domain<Integer, Integer> domain = buildIntDomain(100);
		
		//CoverUnit[] cc = allocator.coverUnits().toArray(new CoverUnit[allocator.coverUnits().size()]);
		//EconomicProfil ep = EconomicProfilFactory.create(cc);
		
		CoverAllocationConstraint<?, ?> constraint = new ProfitConstraint(code, checkOnly, mode, covers, location, domain);
		allocator.addConstraint(constraint);
	}
	
	private void initProfitVariabilityConstraints(){
		Domain<Integer, Integer> domain = buildIntDomain(100);
		
		CoverAllocationConstraint<?, ?> constraint = new ProfitVariabilityConstraint(code, checkOnly, mode, covers, location, domain);
		allocator.addConstraint(constraint);
	}
	
	private void initWorkConstraints(){
		Domain<Integer, Integer> domain = buildIntDomain(1);
		
		CoverAllocationConstraint<?, ?> constraint = new WorkConstraint(code, checkOnly, mode, covers, location, domain);
		allocator.addConstraint(constraint);
	}
	
	
	/*public CoverAllocationConstraint<Integer, Integer> initLinkedFieldsConstraint(Arc arc){
		setCover("ALL");
		CoverAllocationConstraint<Integer, Integer> cac = new LinkedFieldsConstraint(generic, "lf_"+arc.toString(), ConstraintMode.ONLY, covers, arc.getParcels(), null);
		reset();
		return cac;
		allocator.addConstraint(constraint);
	}*/

	private Set<CoverUnit> getCoverUnits(Set<Cover> covers){
		Set<CoverUnit> coverunits = new HashSet<CoverUnit>();
		for(Cover c : covers){
			if(c instanceof CoverUnit){
				coverunits.add((CoverUnit) c);
			}else{
				for(CoverUnit cu : (CoverGroup) c){
					coverunits.add((CoverUnit) cu);
				}
			}
		}
		return coverunits;
	}
	
	private Domain<CoverUnit, CoverUnit> buildCoverDomain() {
		if(domain.startsWith("[") && domain.endsWith("]")){
			domain = domain.replace("[", "").replace("]", "").replace(" ", "");
			String[] d = domain.split(",");
			Set<CoverUnit> targets = new TreeSet<CoverUnit>();
			if(d[0].equalsIgnoreCase("ALL")){
				for(CoverUnit c : allocator.coverUnits()){
					targets.add(c);
				}
			}else{
				for(String code : d){
					targets.add((CoverUnit) Cover.get(code));
				}
			}
			return new SetDomain<CoverUnit>(targets);
		}
		return null;
	}

	private Domain<Double, Parcel> buildDoubleDomain() {
		
		if(domain.startsWith("[") && domain.endsWith("]")){
			domain = domain.replace("[", "").replace("]", "").replace(" ", "");
			String op = "";
			if(domain.contains("<=")){
				op = "<=";
			}else if(domain.contains(">=")){
				op = ">=";
			}else if(domain.contains("<")){
				op = "<";
			}else if(domain.contains(">")){
				op = ">";
			}else if(domain.contains("!=")){
				op = "!=";
			}else if(domain.contains("=")){
				op = "=";
			}else{
				throw new IllegalArgumentException(domain);
			}
			String[] d = domain.split(op, 2);
			
			return new VariableValueDomain<Double>(d[0], op, Double.parseDouble(d[1]));
		}
		return null;
	}

	private Domain<Integer, Integer> buildAreaDomain(){
		return buildAreaDomain(allocator.getArea());
	}
	
	private Domain<Integer, Integer> buildAreaDomain(int locationArea){
		if(domain.contains("min") || domain.contains("max")){
			return buildMinMaxAreaDomain();
		}
		if(domain.contains("%")){
			return buildPercentDomain(locationArea);
		}
		return buildIntDomain(10000.0);
	}
	
	private Domain<Integer, Integer> buildMinMaxAreaDomain() {
		String[] d = domain.replace("[", "").replace("]", "").replace(" ", "").split(",", 2);
		
		int area = allocator.getArea();
		double v1 = 0;
		if(!d[0].equalsIgnoreCase("")){
			if(d[0].contains("min")){
				String[] dd = d[0].replace("min(", "").replace(")", "").split("\\|", 2);
				if(dd[0].contains("%") && !dd[1].contains("%")){
					double a = new Double(dd[0].replace("%", ""));
					a = new Double(a * area / 100.0).intValue();
					double b = new Double(dd[1]);
					b = b * 10000.0;
					v1 = Math.min(a, b);
				}else if(dd[1].contains("%") && !dd[0].contains("%")){
					double a = new Double(dd[1].replace("%", ""));
					a = new Double(a * area / 100.0).intValue();
					double b = new Double(dd[0]);
					b = b * 10000.0;
					v1 = Math.min(a, b);
				}else{
					throw new IllegalArgumentException(domain);
				}
			}else if(d[0].contains("max")){
				String[] dd = d[0].replace("max(", "").replace(")", "").split("\\|", 2);
				if(dd[0].contains("%") && !dd[1].contains("%")){
					double a = new Double(dd[0].replace("%", ""));
					a = new Double(a * area / 100.0).intValue();
					double b = new Double(dd[1]);
					b = b * 10000.0;
					v1 = Math.max(a, b);
				}else if(dd[1].contains("%") && !dd[0].contains("%")){
					double a = new Double(dd[1].replace("%", ""));
					a = new Double(a * area / 100.0).intValue();
					double b = new Double(dd[0]);
					b = b * 10000.0;
					v1 = Math.max(a, b);
				}else{
					throw new IllegalArgumentException(domain);
				}
			}
		}
		double v2 = area;
		if(!d[1].equalsIgnoreCase("")){
			if(d[1].contains("min")){
				String[] dd = d[1].replace("min(", "").replace(")", "").split("\\|", 2);
				if(dd[0].contains("%") && !dd[1].contains("%")){
					double a = new Double(dd[0].replace("%", ""));
					a = new Double(a * area / 100.0).intValue();
					double b = new Double(dd[1]);
					b = b * 10000.0;
					v2 = Math.min(a, b);
				}else if(dd[1].contains("%") && !dd[0].contains("%")){
					double a = new Double(dd[1].replace("%", ""));
					a = new Double(a * area / 100.0).intValue();
					double b = new Double(dd[0]);
					b = b * 10000.0;
					v2 = Math.min(a, b);
				}else{
					throw new IllegalArgumentException(domain);
				}
			}else if(d[1].contains("max")){
				String[] dd = d[1].replace("max(", "").replace(")", "").split("\\|", 2);
				if(dd[0].contains("%") && !dd[1].contains("%")){
					double a = new Double(dd[0].replace("%", ""));
					a = new Double(a * area / 100.0).intValue();
					double b = new Double(dd[1]);
					b = b * 10000.0;
					v2 = Math.max(a, b);
				}else if(dd[1].contains("%") && !dd[0].contains("%")){
					double a = new Double(dd[1].replace("%", ""));
					a = new Double(a * area / 100.0).intValue();
					double b = new Double(dd[0]);
					b = b * 10000.0;
					v2 = Math.max(a, b);
				}else{
					throw new IllegalArgumentException(domain);
				}
			}
		}
		
		StringBuilder domain2 = new StringBuilder();
		domain2.append(domain.charAt(0));
		domain2.append(v1);
		domain2.append(',');
		domain2.append(v2);
		domain2.append(domain.charAt(domain.length()-1));
		domain = domain2.toString();
		
		return buildIntDomain(1.0);
	}

	private Domain<Integer, Integer> buildPercentDomain(int max) {
		String[] d = domain.replace("[", "").replace("]", "").replace(" ", "").replace("%", "").split(",", 2);
		
		//int area = allocator.getArea();
		int area = max;
		double v1 = 0;
		if(!d[0].equalsIgnoreCase("")){
			v1 = new Double(d[0]);
			v1 = new Double(v1 * area / 100.0).intValue();
		}
		double v2 = area;
		if(!d[1].equalsIgnoreCase("")){
			v2 = new Double(d[1]);
			v2 = new Double(v2 * area / 100.0).intValue();
		}
		
		StringBuilder domain2 = new StringBuilder();
		domain2.append(domain.charAt(0));
		domain2.append(v1);
		domain2.append(',');
		domain2.append(v2);
		domain2.append(domain.charAt(domain.length()-1));
		domain = domain2.toString();
		
		return buildIntDomain(1.0);
	}

	private Domain<Integer, Integer> buildDistanceDomain(){
		return buildIntDomain(1000.0);
	}
	
	private Domain<Integer, Integer> buildIntDomain(double coeff){
		if (domain.startsWith("[")) {
			if (domain.endsWith("]")) {
				String[] d = domain.replace("[", "").replace("]", "").replace(" ", "").split(",", 2);
				if (!d[0].equalsIgnoreCase("")) {
					int d0 = new Double(Double.parseDouble(d[0]) * coeff).intValue();
					if (!d[1].equalsIgnoreCase("")) {
						int d1 = new Double(Double.parseDouble(d[1]) * coeff).intValue();
						return new AndDomain<Integer, Integer>(new NumberDomain<Integer, Integer>(">=", d0),
								new NumberDomain<Integer, Integer>("<=", d1));
					} else {
						return new NumberDomain<Integer, Integer>(">=", d0);
					}
				} else {
					if (!d[1].equalsIgnoreCase("")) {
						int d1 = new Double(Double.parseDouble(d[1]) * coeff).intValue();
						return new NumberDomain<Integer, Integer>("<=", d1);
					} else {
						// domain ALL !!!
						return new AllDomain();
					}
				}
			} else { // ends with "["
				String[] d = domain.replace("[", "").replace(" ", "").split(",", 2);
				if (!d[0].equalsIgnoreCase("")) {
					int d0 = new Double(Double.parseDouble(d[0]) * coeff).intValue();
					if (!d[1].equalsIgnoreCase("")) {
						int d1 = new Double(Double.parseDouble(d[1]) * coeff).intValue();
						return new AndDomain<Integer, Integer>(new NumberDomain<Integer, Integer>(">=", d0),
								new NumberDomain<Integer, Integer>("<", d1));
					} else {
						return new NumberDomain<Integer, Integer>(">=", d0);
					}
				} else {
					if (!d[1].equalsIgnoreCase("")) {
						int d1 = new Double(Double.parseDouble(d[1]) * coeff).intValue();
						return new NumberDomain<Integer, Integer>("<", d1);
					} else {
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}
		} else { // begins with "]"
			if (domain.endsWith("]")) {
				String[] d = domain.replace("]", "").replace(" ", "").split(",", 2);
				if (!d[0].equalsIgnoreCase("")) {
					int d0 = new Double(Double.parseDouble(d[0]) * coeff).intValue();
					if (!d[1].equalsIgnoreCase("")) {
						int d1 = new Double(Double.parseDouble(d[1]) * coeff).intValue();
						return new AndDomain<Integer, Integer>(new NumberDomain<Integer, Integer>(">", d0),
								new NumberDomain<Integer, Integer>("<=", d1));
					} else {
						return new NumberDomain<Integer, Integer>(">", d0);
					}
				} else {
					if (!d[1].equalsIgnoreCase("")) {
						int d1 = new Double(Double.parseDouble(d[1]) * coeff).intValue();
						return new NumberDomain<Integer, Integer>("<=", d1);
					} else {
						// domain ALL !!!
						return new AllDomain();
					}
				}
			} else { // ends with "["
				String[] d = domain.replace("[", "").replace("]", "").replace(" ", "").split(",", 2);
				if (!d[0].equalsIgnoreCase("")) {
					int d0 = new Double(Double.parseDouble(d[0]) * coeff).intValue();
					if (!d[1].equalsIgnoreCase("")) {
						int d1 = new Double(Double.parseDouble(d[1]) * coeff).intValue();
						return new AndDomain<Integer, Integer>(new NumberDomain<Integer, Integer>(">", d0),
								new NumberDomain<Integer, Integer>("<", d1));
					} else {
						return new NumberDomain<Integer, Integer>(">", d0);
					}
				} else {
					if (!d[1].equalsIgnoreCase("")) {
						int d1 = new Double(Double.parseDouble(d[1]) * coeff).intValue();
						return new NumberDomain<Integer, Integer>("<", d1);
					} else {
						// domain ALL !!!
						return new AllDomain();
					}
				}
			}
		}
	}
	
	private Domain<Integer, Integer> buildBoundedDomain(double coeff, int declaredMin, int declaredMax){
		if(domain.contains("[") || domain.contains("]")){
			int d0, d1;
			if(domain.startsWith("[")){
				if(domain.endsWith("]")){
					String[] d = domain.replace("[", "").replace("]", "").replace(" ", "").split(",", 2);
					if (!d[0].equalsIgnoreCase("")) {
						d0 = new Double(Double.parseDouble(d[0])*coeff).intValue();
					}else{
						d0 = declaredMin;
					}
					if (!d[1].equalsIgnoreCase("")) {
						d1 = new Double(Double.parseDouble(d[1])*coeff).intValue();
					}else{
						d1 = declaredMax;
					}
					return new BoundedDomain<Integer, Integer>(">=", d0, "<=", d1);
				}else{ // ends with "["
					String[] d = domain.replace("[", "").split(",", 2);
					if (!d[0].equalsIgnoreCase("")) {
						d0 = new Double(Double.parseDouble(d[0])*coeff).intValue();
					}else{
						d0 = declaredMin;
					}
					if (!d[1].equalsIgnoreCase("")) {
						d1 = new Double(Double.parseDouble(d[1])*coeff).intValue();
					}else{
						d1 = declaredMax;
					}
					return new BoundedDomain<Integer, Integer>(">=", d0, "<", d1);
				}
			}else{ // begins with "]"
				if(domain.endsWith("]")){
					String[] d = domain.replace("]", "").split(",", 2);
					if (!d[0].equalsIgnoreCase("")) {
						d0 = new Double(Double.parseDouble(d[0])*coeff).intValue();
					}else{
						d0 = declaredMin;
					}
					if (!d[1].equalsIgnoreCase("")) {
						d1 = new Double(Double.parseDouble(d[1])*coeff).intValue();
					}else{
						d1 = declaredMax;
					}
					return new BoundedDomain<Integer, Integer>(">", d0, "<=", d1);
				}else{ // ends with "["
					String[] d = domain.replace("[", "").replace("]", "").split(",", 2);
					if (!d[0].equalsIgnoreCase("")) {
						d0 = new Double(Double.parseDouble(d[0])*coeff).intValue();
					}else{
						d0 = declaredMin;
					}
					if (!d[1].equalsIgnoreCase("")) {
						d1 = new Double(Double.parseDouble(d[1])*coeff).intValue();
					}else{
						d1 = declaredMax;
					}
					return new BoundedDomain<Integer, Integer>(">", d0, "<", d1);
				}
			}
		}else{
			domain = "["+domain+","+domain+"]";
			return buildBoundedDomain(coeff, declaredMin, declaredMax);
		}
	}
	
	private Domain<Boolean, Parcel> buildBooleanDomain(){
		if(domain.startsWith("[") && domain.endsWith("]")){
			String[] d = domain.replace("[", "").replace("]", "").replace(" ", "").split("=", 2);
			boolean bool = CAPFarm.parseBoolean(d[1]);
			return new VariableBooleanDomain(d[0], bool);
		}
		return null;
	}
	
}
