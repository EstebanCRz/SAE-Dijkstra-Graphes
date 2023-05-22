package graphe.implems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import graphe.core.Arc;
import graphe.core.IGraphe;

public class GrapheHHAdj implements IGraphe {
	private Map<String, Map<String, Integer>> hhadj;
	private static final String lettre = "A"; // donne le premier sommet du graphe lors de la création
	
	// permettra de construire un graphe sans arc de départ
	public GrapheHHAdj() {
		hhadj = new HashMap<>();
	}
	
	// permettra de construire un graphe avec des arcs déjà renseignés
	public GrapheHHAdj(List<Arc> arcs) {
		hhadj = new HashMap<>();
		String lettreDep = lettre;
		
		// récupération du nombre de sommets
		Set<String> sommets = new HashSet<>();
		for (Arc arc : arcs) {
			sommets.add(arc.getSource());
			sommets.add(arc.getDestination());
		}
		int nbSommets = sommets.size();
		
		for (int i = 0; i < nbSommets; i++) {
			hhadj.put(lettreDep, new HashMap<>());
			for (Arc arc: arcs) {
				if (arc.getSource().equals(lettreDep)) {
					hhadj.get(lettreDep).put(arc.getDestination(), arc.getValuation());
				}
			}
			lettreDep += 1;
		}
	}
	
	@Override
	public List<String> getSommets() {
		List<String> sommets = new ArrayList<>();
		for( Entry<String, Map<String, Integer>> map: hhadj.entrySet()) {
			String sommet = map.getKey();
			sommets.add(sommet);
		}
		return sommets;
	}

	@Override
	public List<String> getSucc(String sommet) {
	    List<String> successeurs = new ArrayList<>();
	    if (contientSommet(sommet)) {
	        Map<String, Integer> arcsSortants = hhadj.get(sommet);
	        for (String successeur : arcsSortants.keySet()) {
	            successeurs.add(successeur);
	        }
	    }
	    return successeurs;
	}


	@Override
	public int getValuation(String src, String dest) {
	    if (!contientSommet(src) || !hhadj.get(src).containsKey(dest)) {
	        // Si le sommet source ou le sommet destination n'existent pas dans le graphe, on renvoie -1
	        return -1;
	    }
	    return hhadj.get(src).get(dest);
	}


	@Override
	public boolean contientSommet(String sommet) {
		return hhadj.containsKey(sommet);
	}

	@Override
	public boolean contientArc(String src, String dest) {
		return hhadj.get(src).containsKey(dest);
	}

	@Override
	public void ajouterSommet(String noeud) {
		if(!contientSommet(noeud)) {
			hhadj.put(noeud, new HashMap<>());
		}
		else {
			
		}
	}

	@Override
	public void ajouterArc(String source, String destination, Integer valeur) {
		if(contientSommet(source) && contientSommet(destination)) {
			if(hhadj.get(source).containsKey(destination))
				throw new IllegalStateException("L'arc existe déjà dans le graphe.");
			else
				hhadj.get(source).put(destination, valeur);
		}
		else
			throw new IllegalArgumentException("Le sommet source ou destination n'existe pas dans le graphe.");
	}

	@Override
	public void oterSommet(String noeud) {
		if(contientSommet(noeud)) {
			for (Map.Entry<String, Map<String, Integer>> entry : hhadj.entrySet()) {
		        Map<String, Integer> destinations = entry.getValue();
		        if (destinations.containsKey(noeud)) {
		            destinations.remove(noeud);
		        }
		    }
		    hhadj.remove(noeud);
		}
		else {
			
		}
	}

	@Override
	public void oterArc(String source, String destination) {
	    if (contientSommet(source) && contientSommet(destination)) {
	    	if(hhadj.get(source).containsKey(destination))
	    		hhadj.get(source).remove(destination);
	    	else
	    		throw new IllegalStateException("L'arc n'existe pas dans le graphe.");
	    } 
	    else
	        throw new IllegalArgumentException("Le sommet source ou destination n'existe pas dans le graphe.");
	}

}