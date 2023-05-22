package graphe.implems;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import graphe.core.Arc;
import graphe.core.IGraphe;

public class GrapheLArcs implements IGraphe {
	private List<Arc> arcs;
	private static final int ARC_NULL = 0;
	
	// permettra de construire un graphe sans arc de départ
	public GrapheLArcs () {
		arcs = new ArrayList<>();
	}
	
	// permettra de construire un graphe avec des arcs déjà renseignés
	public GrapheLArcs (List<Arc> arc) {
		arcs = new ArrayList<>();
		arcs = arc;
	}

	@Override
	public List<String> getSommets() {
		List<String> sommets = new ArrayList<>();
		if (!arcs.isEmpty()) {
			for(Arc s: arcs) {
				sommets.add(s.getSource());
				sommets.add(s.getDestination());
			}
			sommets = sommets.stream().distinct().collect(Collectors.toList()); // retire les doublons de la liste	
		}
		return sommets;
	}

	@Override
	public List<String> getSucc(String sommet) {
		List<String> succ = new ArrayList<>();
		if(!arcs.isEmpty()) {
			for(Arc a: arcs) {
				if (a.getSource() == sommet) {
					succ.add(a.getDestination());
				}
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
		else {
			
		}
	}

	@Override
	public void ajouterArc(String source, String destination, Integer valeur){
		if(contientSommet(source) && contientSommet(destination)) {
			if(this.contientArc(source, destination)) {
				throw new IllegalStateException("L'arc existe déjà dans le graphe.");
			}
			else {
				arcs.add(new Arc(source, destination, valeur));
			}
		}
		else
			throw new IllegalArgumentException("Le sommet source ou destination n'existe pas dans le graphe.");
	}

	@Override
	public void oterSommet(String noeud) {
	    if (contientSommet(noeud)) {
	        List<Arc> arcsASupprimer = new ArrayList<>();
	        for (Arc a : arcs) {
	            if (a.getSource().equals(noeud) || a.getDestination().equals(noeud)) {
	                arcsASupprimer.add(a);
	            }
	        }
	        arcs.removeAll(arcsASupprimer);
	    } 
	    else {
	        
	    }
	}


	@Override
	public void oterArc(String source, String destination) {
		if(contientSommet(source) && contientSommet(destination)){
			if(!this.contientArc(source, destination)) {
				throw new IllegalStateException("L'arc n'existe pas dans le graphe.");
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
		else
			throw new IllegalArgumentException("Le sommet source ou destination n'existe pas dans le graphe.");
	}
	
}