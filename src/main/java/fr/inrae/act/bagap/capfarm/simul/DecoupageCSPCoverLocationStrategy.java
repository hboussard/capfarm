package fr.inrae.act.bagap.capfarm.simul;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.search.strategy.ISF;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.util.tools.ArrayUtils;

import fr.inrae.act.bagap.capfarm.csp.CoverAllocationProblemFactory;
import fr.inrae.act.bagap.capfarm.csp.CoverAllocator;
import fr.inrae.act.bagap.capfarm.model.constraint.ConstraintBuilder;
import fr.inrae.act.bagap.capfarm.model.territory.Parcel;
import fr.inrae.act.bagap.capfarm.simul.graph.Arc;
import fr.inrae.act.bagap.capfarm.simul.graph.Graph;
import fr.inrae.act.bagap.apiland.core.time.Instant;

public class DecoupageCSPCoverLocationStrategy extends CSPCoverLocationStrategy{

	private ConstraintBuilder cb;
	
	private static final int max = 5;
	
	private Graph<Parcel> graph;
	
	public DecoupageCSPCoverLocationStrategy(CoverAllocator allocator){
		init(allocator);
	}
	
	private void init(CoverAllocator allocator){
		
		cb = new ConstraintBuilder(allocator);
		
		Map<Integer, Set<Parcel>> pgps = new HashMap<Integer, Set<Parcel>>();
		for (Parcel p : allocator.parcels()) {
			int pgp = p.getPGP();
			if(pgp != -1){
				if(!pgps.containsKey(pgp)){
					pgps.put(pgp, new HashSet<Parcel>());
				}
				pgps.get(pgp).add(p);
			}
		}
		
		graph = new Graph<Parcel>();
		Set<Parcel> ever = new HashSet<Parcel>();
		for(Set<Parcel> set : pgps.values()){
			ever.clear();
			for(Parcel p1 : set){
				ever.add(p1);
				for(Parcel p2 : set){
					if(!ever.contains(p2)){
						if(p1.getShape().intersects(p2.getShape())){
							//if(p1.getShape().intersection(p2.getShape()).getLength() > 0){
							graph.addArc(p1, p2);						
						}
					}
				}	
			}
		}
		
		graph.detectCycles();
	}
	
	private ArrayList<Arc<Parcel>> getDecoupage(int nb){
		Solver solver = new Solver("decoupage");
		BoolVar[] boolArcs = new BoolVar[graph.countArcs()];
		for(int i=0; i<graph.countArcs(); i++){
			boolArcs[i] = VF.bool("ba_"+i, solver);
		}
		IntVar sum = VF.bounded("sum", 0, graph.countArcs(), solver);
		solver.post(ICF.sum(boolArcs, sum));
		solver.post(ICF.arithm(sum, "=", nb));
		long r = new Double(Math.random() * 1000000000.0).longValue();
		solver.set(ISF.random(ArrayUtils.append(boolArcs), r));
		ArrayList<Arc<Parcel>> liaisons = new ArrayList<Arc<Parcel>>();
		if(solver.findSolution()) {
			for(int i=0; i<graph.countArcs(); i++){
				if(((IntVar) boolArcs[i]).getValue() == 0){
					liaisons.add(graph.getArc(i));
				}
			}
		}
		return liaisons;
	}

	@Override
	public boolean make(CoverAllocationProblemFactory factory, CoverAllocator allocator, Instant t) {
		
		for(int a=0; a<graph.countArcs(); a++){
			for(int i=0; i<max; i++){
				ArrayList<Arc<Parcel>> liaisons = getDecoupage(a);
				for(Arc<Parcel> arc : liaisons){
					//allocator.addConstraint(cb.initLinkedFieldsConstraint(arc));
				}
				if(factory.create(allocator, t).execute()){
					return true;
				}
			}
		}
		return false;
	}
	
}
