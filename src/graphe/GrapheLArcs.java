package graphe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GrapheLArcs implements IGraphe {
	private List<Arc> arcs;
	private static final int ARC_NULL = 0;
	
	public GrapheLArcs (List<Arc> a) {
		this.arcs = a;
	}

	@Override
	public List<String> getSommets() {
		List<String> sommets = new ArrayList<String>();
		for(Arc s: arcs) {
			sommets.add(s.getSource());
			sommets.add(s.getDestination());
		}
		sommets = sommets.stream().distinct().collect(Collectors.toList()); // retire les doublons de la liste
		return sommets;
	}

	@Override
	public List<String> getSucc(String sommet) {
		List<String> succ = new ArrayList<>();
		for(Arc a: arcs) {
			if (a.getSource() == sommet) {
				succ.add(a.getDestination());
			}
		}
		return succ;
	}

	@Override
	public int getValuation(String src, String dest) {
		int val = -1;
		for(Arc a: arcs) {
			if (a.getSource() == src && a.getDestination() == dest) {
				val = a.getValuation();
				break;
			}
		}
		return val;
	}

	@Override
	public boolean contientSommet(String sommet) {
		for(Arc a: arcs) {
			if (a.getSource() == sommet || a.getDestination() == sommet) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean contientArc(String src, String dest) {
		for(Arc a: arcs) {
			if (a.getSource() == src && a.getDestination() == dest) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void ajouterSommet(String noeud) {
		if(!this.contientSommet(noeud)) {
			arcs.add(new Arc(noeud, "", ARC_NULL));
		}
	}

	@Override
	public void ajouterArc(String source, String destination, Integer valeur){
		if(this.contientArc(source, destination)) {
			throw new IllegalArgumentException("L'arc existe déjà");
		}
		else {
			Arc a = new Arc(source, destination, valeur);
			arcs.add(a);
		}
	}

	@Override
	public void oterSommet(String noeud) {
		assert(!arcs.isEmpty());
		int cpt = 0;
		for(Arc a: arcs) {
			if(a.getSource() == noeud || a.getDestination() == noeud) {
				arcs.remove(cpt);
			}
			++cpt;
		}
	}

	@Override
	public void oterArc(String source, String destination) {
		if(!this.contientArc(source, destination)) {
			throw new IllegalArgumentException("L'arc n'existe pas");
		}
		else {
			for(Arc a: arcs) {
				if(a.getSource() == source && a.getDestination() == destination) {
					arcs.remove(a);
					break;
				}
			}
		}
	}
	
}