package Djikstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graphe.core.IGrapheConst;

public class Dijkstra implements IGrapheConst{
	
	private static String distanceMin(int[] distances, Map<String, Boolean> visite) {
	    String distance = null;
	    for (String sommet : visite.keySet()) {
	    	if (!visite.get(sommet) && (distance == null || distances[Integer.parseInt(sommet)] < distances[Integer.parseInt(distance)])) {
	    	    distance = sommet;
	    	}
	    }
	    return distance;
	}

	public List<String> Dijkstra(Object graphe, String debut, String arrive) {
	    assert(contientSommet(debut) && contientSommet(arrive));
	    int nbSommets = getSommets().size();
	    int[] distances = new int[nbSommets];
	    HashMap<String, Boolean> visite = new HashMap<>();
	    String lettreDep = "A";
	    for (int i = 0; i < nbSommets; ++i) {
	        visite.put(lettreDep, false);
	        lettreDep += 1;
	    }
	    visite.replace(debut, true);
	    List<String> chemin = new ArrayList<>();
	    chemin.add(debut);

	    for (int i = 0; i < nbSommets; ++i) {
	        String distanceMinimum = distanceMin(distances, visite);
	        visite.replace(distanceMinimum, true);

	        for (String sommet : visite.keySet()) {
	            if (!visite.get(sommet) && contientArc(distanceMinimum, sommet)) {
	                int poidsArc = getValuation(distanceMinimum, sommet);
	                int distanceActuelle = distances[Integer.parseInt(sommet)];
	                int nouvelleDistance = distances[Integer.parseInt(distanceMinimum)] + poidsArc;
	                if (nouvelleDistance < distanceActuelle) {
	                    distances[Integer.parseInt(sommet)] = nouvelleDistance;
	                }
	            }
	        }

	        chemin.add(distanceMinimum);

	        if (distanceMinimum.equals(arrive)) {
	            break;
	        }
	    }

	    int distanceArrive = distances[Integer.parseInt(arrive)];
	    chemin.add(String.valueOf(distanceArrive));
	    
	    return chemin;
	}

	@Override
	public List<String> getSommets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSucc(String sommet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getValuation(String src, String dest) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean contientSommet(String sommet) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contientArc(String src, String dest) {
		// TODO Auto-generated method stub
		return false;
	}
}
