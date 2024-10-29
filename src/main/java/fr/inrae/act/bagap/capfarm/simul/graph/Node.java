package fr.inrae.act.bagap.capfarm.simul.graph;

import java.util.HashSet;
import java.util.Set;

public class Node<E> {

	private E element;
	
	private Set<Arc<E>> arcs;
	
	public Node(E element){
		this.element = element;
		arcs = new HashSet<Arc<E>>();
	}
	
	@Override
	public String toString(){
		return element.toString();
	}
	
	public void addArc(Arc<E> arc){
		arcs.add(arc);
	}
	
	public E getElement(){
		return element;
	}
	
	public Set<Arc<E>> getArcs(){
		return arcs;
	}
}
