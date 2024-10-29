package fr.inrae.act.bagap.capfarm.simul.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Graph<E> {

	private Set<Node<E>> nodes;
	
	private List<Arc<E>> arcs;
	
	private Set<Cycle<E>> cycles;
	
	public Graph(){
		nodes = new HashSet<Node<E>>();
		arcs = new ArrayList<Arc<E>>();
		cycles = new HashSet<Cycle<E>>();
	}
	
	public Node<E> addNode(E e){
		if(!hasNode(e)){
			nodes.add(new Node<E>(e));
		}
		return getNode(e);
	}
	
	private Node<E> getNode(E e) {
		// sure it exists
		for(Node<E> n : nodes){
			if(n.getElement().equals(e)){
				return n;
			}
		}
		throw new IllegalArgumentException(e+"");
	}

	private boolean hasNode(E e) {
		for(Node<E> n : nodes){
			if(n.getElement().equals(e)){
				return true;
			}
		}
		return false;
	}

	public Arc<E> addArc(E e1, E e2){
		Node<E> n1 = addNode(e1);
		Node<E> n2 = addNode(e2);
		if(!hasArc(n1, n2)){
			arcs.add(new Arc<E>(n1, n2));
		}
		return getArc(n1, n2);
	}
	
	private Arc<E> getArc(Node<E> n1, Node<E> n2) {
		/// sure it exists
		for(Arc<E> a : arcs){
			if(a.hasNode(n1) && a.hasNode(n2)){
				return a;
			}
		}
		throw new IllegalArgumentException(n1+" "+n2+"");
	}

	private boolean hasArc(Node<E> n1, Node<E> n2) {
		for(Arc<E> a : arcs){
			if(a.hasNode(n1) && a.hasNode(n2)){
				return true;
			}
		}
		return false;
	}

	public int countNodes(){
		return nodes.size();
	}
	
	public int countArcs(){
		return arcs.size();
	}
	
	public Arc<E> getArc(int index){
		return arcs.get(index);
	}
	
	public void detectCycles(){
		cycles = new HashSet<Cycle<E>>();
		
		Node<E> n = nodes.iterator().next();
		System.out.println(n);
		Set<Set<Arc<E>>> parcours = treatParcours(new TreeSet<Arc<E>>(), n, null);
		
		System.out.println(parcours.size());
		for(Set<Arc<E>> parcour : parcours){
			for(Arc<E> p : parcour){
				System.out.print(p+" ");
			}
			System.out.println();
		}
	}
	
	private Set<Set<Arc<E>>> treatParcours(Set<Arc<E>> parcour, Node<E> n, Arc<E> olda) {
		System.out.println("rentre avec "+n);
		Set<Set<Arc<E>>> parcours = new HashSet<Set<Arc<E>>>();
		
		boolean ok = false;
		for(Arc<E> a : n.getArcs()){
			System.out.println("test de "+a);
			if(!parcour.contains(a) && !a.equals(olda)){
				ok = true;
				Set<Arc<E>> pbis = new TreeSet<Arc<E>>();
				pbis.addAll(parcour);
				pbis.add(a);	
				System.out.println(a.getNode(n));
				parcours.addAll(treatParcours(pbis, a.getNode(n), a));
			}
		}
		
		if(!ok){
			parcours.add(parcour);
		}
		
		return parcours;
	}

	public int countCycles(){
		return cycles.size();
	}
	
	public boolean hasCycle(){
		return cycles.size() > 0;
	}
}
