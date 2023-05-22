package graphe.implems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import graphe.core.Arc;
import graphe.core.IGraphe;

public class GrapheLAdj implements IGraphe {
	private Map<String, List<Arc>> lAdj;
	private static final String lettre = "A"; // donne le premier sommet lors de la création
	
	// permettra de construire un graphe sans arc de départ
	public GrapheLAdj() {
		lAdj = new HashMap<>();
	}
	
	// permettra de construire un graphe avec des arcs déjà renseignés
	public GrapheLAdj(List<Arc> arcs) {
		lAdj = new HashMap<>();
		
		// récupération du nombre de sommets
		List<String> sommets = new ArrayList<>();
		for( Map.Entry<String, Integer> map: indices.entrySet()) {
			String sommet = map.getKey();
			sommets.add(sommet);
		}
		
		int nbSommets = sommets.size();
		String lettreDep = lettre;
		for(int i = 0; i < nbSommets; ++i) {
			lAdj.put(lettreDep, new ArrayList<>());
			List<Arc> SArcs = new ArrayList<>(); // contiendra tous les arcs partant du sommet en cours d'étude
			for (Arc arc: arcs) {
				if (arc.getSource() == lettreDep)
					SArcs.add(arc);
			}
			if(!SArcs.isEmpty())
				lAdj.put(lettreDep, SArcs);
			lettreDep += 1;
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
		if (!contientSommet(noeud)) {
			lAdj.put(noeud, new ArrayList<>());
			ajouterArc(noeud, "", 0);
		}
		else {
			
		}
	}

	@Override
	public void ajouterArc(String source, String destination, Integer valeur) {
		if (contientSommet(source) && contientSommet(destination)) {
			if(contientArc(source, destination))
				throw new IllegalStateException("L'arc existe déjà dans le graphe."); 
			else {
				lAdj.get(source).add(new Arc(source, destination, valeur));
			}
		}
		else
			throw new IllegalArgumentException("Le sommet source ou destination n'existe pas dans le graphe.");
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
	    else {
	    	
	    }
	}

	@Override
	public void oterArc(String source, String destination) {
		if(contientSommet(source) && contientSommet(destination)) {
			if (!contientSommet(source))
		    	throw new IllegalStateException("L'arc n'existe pas dans le graphe.");
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
		else
			throw new IllegalArgumentException("Le sommet source ou destination n'existe pas dans le graphe.");
	}

}