package fr.inrae.act.bagap.capfarm.csp;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.Variable;

public abstract class AbstractProblem {

	protected Solver solver;
	
	protected abstract void createSolver();

	protected abstract void buildModel();

	protected abstract void configureSearch();

	protected abstract boolean solve();

	protected void prettyOut(){}
    
    public boolean execute(){
		createSolver();
		buildModel();
		configureSearch();
		boolean solve = solve();
		prettyOut();
		return solve;
	}
    
    protected Variable getVariable(String name){
		for(int v=0; v<solver.getVars().length; v++) {
			if(solver.getVar(v).getName().equals(name)){
				return solver.getVar(v);
			}
		}
		return null;
	}
}
