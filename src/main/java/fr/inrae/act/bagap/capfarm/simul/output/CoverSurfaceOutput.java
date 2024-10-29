package fr.inrae.act.bagap.capfarm.simul.output;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import org.jumpmind.symmetric.csv.CsvWriter;

import fr.inrae.act.bagap.capfarm.model.Cover;
import fr.inrae.act.bagap.capfarm.model.CoverGroup;
import fr.inrae.act.bagap.capfarm.model.CoverUnit;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.capfarm.simul.CoverLocationModel;
import fr.inrae.act.bagap.capfarm.simul.GlobalCoverLocationModel;
import fr.inrae.act.bagap.apiland.core.time.Instant;
import fr.inrae.act.bagap.apiland.simul.OutputAnalysis;
import fr.inrae.act.bagap.apiland.simul.Scenario;
import fr.inrae.act.bagap.apiland.simul.Simulation;

public class CoverSurfaceOutput extends OutputAnalysis {
	
	private DecimalFormat format;
	
	private String csv;
	
	private CsvWriter cw;
	
	public CoverSurfaceOutput(String csv){
		this.csv = csv;
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format = new DecimalFormat("0.00", symbols);
	}
	
	@Override
	public void init(Scenario scenario){
		try {
			
			for(CoverLocationModel model : (GlobalCoverLocationModel) scenario.model().get("agriculture")){
				cw = new CsvWriter(csv);
				cw.setDelimiter(';');
				cw.write("simulation");
				cw.write("farm");
				cw.write("year");
				for(Cover c : model.getCoverAllocator().coverUnits()){
					cw.write(c+"");
				}
				cw.endRecord();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close(Scenario scenario){
		cw.close();
	}
	
	@Override
	public void close(Simulation simulation){
		if(!simulation.isCancelled()){
			try {
				for(CoverLocationModel model : (GlobalCoverLocationModel) simulation.model().get("agriculture")){
					for(int y=simulation.manager().start().year(); y<=simulation.manager().end().year(); y++){
						cw.write(""+simulation.number());
						cw.write(""+model.getCoverAllocator().getCode());
						cw.write(y+"");
						for(Cover c : model.getCoverAllocator().coverUnits()){
							double area = 0;
							for(Parcel p : model.getCoverAllocator().parcels()){
								if(c instanceof CoverUnit){
									if(p.getAttribute("cover").hasValue(c, new Instant(simulation.manager().start().dayOfYear(), simulation.manager().start().month(), y))){
										area += p.getArea()/10000.0;						
									}
								}else{
									for(CoverUnit cu : (CoverGroup) c){
										if(p.getAttribute("cover").hasValue(cu, new Instant(simulation.manager().start().dayOfYear(), simulation.manager().start().month(), y))){
											area += p.getArea()/10000.0;						
										}
									}
								}
								
							}
							cw.write(""+format.format(area));
						}
						cw.endRecord();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
}