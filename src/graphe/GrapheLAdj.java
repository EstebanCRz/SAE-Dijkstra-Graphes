package graphe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GrapheLAdj implements IGraphe {
	private Map<String, List<Arc>> lAdj;
	private String lettre = "A"; // permet d'initialiser la map
	
	// permet de construire un graphe sans arc
	public GrapheLAdj(int nbSommets) {
		lAdj = new HashMap<>();
		for(int i = 0; i < nbSommets; ++i) {
			lAdj.put(lettre, new ArrayList<>());
			lettre += 1;
		}
	}
	
	// permet de construire un graphe avec des arcs déjà renseignés
	public GrapheLAdj(int nbSommets, List<Arc> arcs) {
		lAdj = new HashMap<>();
		for(int i = 0; i < nbSommets; ++i) {
			lAdj.put(lettre, new ArrayList<>());
			List<Arc> SArcs = new ArrayList<>(); // contiendra tous les arcs partant du sommet en cours d'étude
			for (Arc arc: arcs) {
				if (arc.getSource() == lettre)
					SArcs.add(arc);
			}
			if(!SArcs.isEmpty())
				lAdj.put(lettre, SArcs);
			lettre += 1;
		}
	}
	
	@Override
	public List<String> getSommets() {
		List<String> sommets = new ArrayList<>();
		for( Entry<String, List<Arc>> map: lAdj.entrySet()) {
			String sommet = map.getKey();
			sommets.add(sommet);
		}
		return sommets;
	}

	@Override
	public List<String> getSucc(String sommet) {
	    List<String> succ = new ArrayList<>();
	    if (contientSommet(sommet)) {
	        List<Arc> arcs = lAdj.get(sommet);
	        for (Arc arc : arcs) {
	            succ.add(arc.getDestination());
	        }
	    }
	    return succ;
	}


	@Override
	public int getValuation(String src, String dest) {
		List<Arc> arcs = lAdj.get(src);
        for (Arc arc : arcs) {
            if (arc.getDestination().equals(dest)) {
                return arc.getValuation();
            }
        }
		return -1;
	}

	@Override
	public boolean contientSommet(String sommet) {
		return lAdj.containsKey(sommet);
	}

	@Override
	public boolean contientArc(String src, String dest) {
		List<Arc> arcs = lAdj.get(src);
        for (Arc arc : arcs) {
        	if (arc.getSource() == src && arc.getDestination() == dest)
        		return true;
        }
		return false;
	}

	@Override
	public void ajouterSommet(String noeud) {
		lAdj.put(noeud, new ArrayList<Arc>());
		ajouterArc(noeud, "", 0);
	}

	@Override
	public void ajouterArc(String source, String destination, Integer valeur) {
		if(contientArc(source, destination))
			throw new IllegalStateException("L'arc existe déjà dans le graphe."); 
			//l'arc existe déjà donc c'est une IllegalStateException et non pas une IllegalArgumentException
		else {
			lAdj.get(source).add(new Arc(source, destination, valeur));
		}
	}

	@Override
	public void oterSommet(String noeud) {
	    if (contientSommet(noeud)) {
	        List<Arc> arcs = lAdj.get(noeud);
	        for (Arc arc : arcs) {
	            String dest = arc.getDestination();
	            List<Arc> arcsSortants = lAdj.get(dest);
	            if (dest.equals(noeud)) {
	                arcsSortants.remove(arc);
	            }
	        }
	        lAdj.remove(noeud);
	    }
	}

	@Override
	public void oterArc(String source, String destination) {
	    if (!contientSommet(source))
	    	throw new IllegalStateException("L'arc n'existe pas dans le graphe.");
	    	//l'arc est déjà absent du graphe donc c'est une IllegalStateException et non pas une IllegalArgumentException
	    else {
		    List<Arc> arcs = lAdj.get(source);
	        for (Arc arc : arcs) {
	            if (arc.getDestination().equals(destination)) {
	                arcs.remove(arc);
	                return;
	            }
	        }
	    }
	}

}