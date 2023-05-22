package graphe.implems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
		Set<String> sommets = new HashSet<>();
		for (Arc arc : arcs) {
			sommets.add(arc.getSource());
		    sommets.add(arc.getDestination());
		}
		
		int nbSommets = sommets.size();
		String lettreDep = lettre;
		for(int i = 0; i < nbSommets; ++i) {
			lAdj.put(lettreDep, new ArrayList<>());
			List<Arc> SArcs = new ArrayList<>(); // contiendra tous les arcs partant du sommet en cours d'étude
			for (Arc arc: arcs) {
				if (arc.getSource().equals(lettreDep))
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
			if (sommet != "")
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
	        	if(arc.getDestination() != "")
	        		succ.add(arc.getDestination());
	        }
	    }
	    succ = succ.stream().distinct().collect(Collectors.toList());
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
        	if (arc.getSource().equals(src) && arc.getDestination().equals(dest))
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
		if (contientSommet(source)) {
			if(contientArc(source, destination))
				throw new IllegalArgumentException("L'arc existe déjà dans le graphe."); 
			else if (valeur == -1)
				throw new IllegalArgumentException("L'arc ne peut pas avoir de valuation négative.");
			else {
				lAdj.get(source).add(new Arc(source, destination, valeur));
			}
		}
		else
			if(valeur == -1)
				throw new IllegalArgumentException("L'arc ne peut pas avoir de valuation négative.");
			ajouterSommet(source);
			ajouterSommet(destination);
			lAdj.get(source).add(new Arc(source, destination, valeur));
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
		    	throw new IllegalArgumentException("L'arc n'existe pas dans le graphe.");
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
	
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    List<String> cléTriées = new ArrayList<>(lAdj.keySet());
	    Collections.sort(cléTriées);

	    Set<String> listeArcs = new HashSet<>();
	    Set<String> positiveVertices = new HashSet<>();

	    for (String key : cléTriées) {
	        List<Arc> arcs = lAdj.get(key);
	        if (arcs != null && !arcs.isEmpty()) {
	            for (Arc a : arcs) {
	                if (a.getValuation() > 0) {
	                    positiveVertices.add(a.getDestination());
	                }
	            }
	        }
	    }

	    for (String key : cléTriées) {
	        List<Arc> arcs = lAdj.get(key);
	        if (arcs != null && !arcs.isEmpty()) {
	            Collections.sort(arcs, Comparator.comparing(Arc::getDestination));
	            for (Arc a : arcs) {
	                if (!a.getSource().isEmpty() && a.getValuation() != 0) {
	                    String arcString = a.getSource() + "-" + a.getDestination() + "(" + a.getValuation() + ")";
	                    if (!listeArcs.contains(arcString)) {
	                        sb.append(arcString).append(", ");
	                        listeArcs.add(arcString);
	                    }
	                }
	            }
	        }
	    }

	    for (String vertex : cléTriées) {
	    	if (vertex != "") {
		        if (!positiveVertices.contains(vertex) && !listeArcs.contains(vertex + ":")) {
		            sb.append(vertex).append(":, ");
		            listeArcs.add(vertex + ":");
		        }
	    	}
	    }

	    if (sb.length() > 0) {
	        sb.setLength(sb.length() - 2);
	    }

	    return sb.toString();
	}

}