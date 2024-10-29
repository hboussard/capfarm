package fr.inrae.act.bagap.capfarm.simul.graph;

public class Arc<E> implements Comparable<Arc<E>> {
	
	private String name;
	
	private Node<E> node1, node2;
	
	public Arc(Node<E> node1, Node<E> node2){
		this.node1 = node1;
		this.node2 = node2;
		name = node1.toString()+"_"+node2.toString();
		node1.addArc(this);
		node2.addArc(this);
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public boolean hasNode(Node<E> node){
		return node1.equals(node) || node2.equals(node);
	}
	
	public Node<E> getNode(Node<E> node){
		if(node1.equals(node)){
			return node2;
		}
		if(node2.equals(node)){
			return node1;
		}
		throw new IllegalArgumentException(node+" not in arc "+this);
	}

	@Override
	public int compareTo(Arc<E> o) {
		if((node1.equals(o.node1) && node2.equals(o.node2)) || (node1.equals(o.node2) && node2.equals(o.node1))){
			return 0;
		}
		return -1;
	}
	
}
