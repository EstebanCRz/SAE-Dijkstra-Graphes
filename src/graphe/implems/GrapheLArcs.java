package graphe.implems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
				if(s.getSource() != "")
					sommets.add(s.getSource());
				if(s.getDestination() != "")
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
				if (a.getSource().equals(sommet) && a.getDestination() != "") {
					succ.add(a.getDestination());
				}
			}
		}
		succ = succ.stream().distinct().collect(Collectors.toList());
		return succ;
	}

	@Override
	public int getValuation(String src, String dest) {
		int val = -1;
		for(Arc a: arcs) {
			if (a.getSource().equals(src) && a.getDestination().equals(dest)) {
				val = a.getValuation();
				break;
			}
		}
		return val;
	}

	@Override
	public boolean contientSommet(String sommet) {
	    List<String> sommets = getSommets();
	    return sommets.contains(sommet);
	}


	@Override
	public boolean contientArc(String src, String dest) {
	    for (Arc a : arcs) {
	        if (a.getSource().equals(src) && a.getDestination().equals(dest)) {
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
		if(contientSommet(source)) {
			if(this.contientArc(source, destination)) {
				throw new IllegalArgumentException("L'arc existe déjà dans le graphe.");
			}
			else if(valeur == -1){
				throw new IllegalArgumentException("L'arc ne peut pas avoir de valuation négative.");
			}
			else {
				arcs.removeIf(a -> a.getSource().equals(source) && a.getValuation() == -1);
	            arcs.add(new Arc(source, destination, valeur));
			}
		}
		else {
			if(valeur == -1)
				throw new IllegalArgumentException("L'arc ne peut pas avoir de valuation négative.");
			ajouterSommet(source);
			ajouterSommet(destination);
			arcs.removeIf(a -> a.getSource().equals(source) && a.getValuation() == -1);
            arcs.add(new Arc(source, destination, valeur));
		}
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
				throw new IllegalArgumentException("L'arc n'existe pas dans le graphe.");
			}
			else {
				for(Arc a: arcs) {
					if(a.getSource().equals(source) && a.getDestination().equals(destination)) {
						arcs.remove(a);
						break;
					}
				}
			}
		}
		else
			throw new IllegalArgumentException("Le sommet source ou destination n'existe pas dans le graphe.");
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    List<Arc> arcsTrie = new ArrayList<>(arcs);
	    arcsTrie.sort(Comparator.comparing(Arc::getSource).thenComparing(Arc::getDestination));

	    Set<String> arcsAjoutés = new HashSet<>();
	    Set<String> valuationPositive = new HashSet<>();

	    for (Arc a : arcsTrie) {
	        if (!a.getSource().isEmpty()) {
	            if (a.getValuation() == 0 && !hasArcWithPositiveValuation(a.getSource())) {
	                String sommetIsolé = a.getSource() + ":";
	                if (!arcsAjoutés.contains(sommetIsolé)) {
	                    sb.append(sommetIsolé).append(", ");
	                    arcsAjoutés.add(sommetIsolé);
	                }
	            } else {
	                if (!a.getDestination().isEmpty()) {
	                    String arcString = a.getSource() + "-" + a.getDestination() + "(" + a.getValuation() + ")";
	                    if (!arcsAjoutés.contains(arcString)) {
	                        sb.append(arcString).append(", ");
	                        arcsAjoutés.add(arcString);
	                    }
	                    if (a.getValuation() > 0) {
	                    	valuationPositive.add(a.getDestination());
	                    }
	                }
	            }
	        }
	    }

	    for (Arc a : arcsTrie) {
	        if (!a.getDestination().isEmpty()) {
	        	valuationPositive.remove(a.getDestination());
	        }
	    }

	    for (String nomSommet : valuationPositive) {
	        String sommetIsolé = nomSommet + ":";
	        if (!arcsAjoutés.contains(sommetIsolé)) {
	            sb.append(sommetIsolé).append(", ");
	            arcsAjoutés.add(sommetIsolé);
	        }
	    }

	    if (sb.length() > 0) {
	        sb.setLength(sb.length() - 2);
	    }
	    return sb.toString();
	}

	private boolean hasArcWithPositiveValuation(String source) {
	    for (Arc a : arcs) {
	        if (a.getSource().equals(source) && a.getValuation() > 0) {
	            return true;
	        }
	    }
	    return false;
	}
}