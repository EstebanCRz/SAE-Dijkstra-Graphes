package graphe;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IGrapheConst {
	List<String> getSommets(); // pas forcement triee
	List<String> getSucc(String sommet); // pas forcement triee
	int getValuation(String src, String dest); // les sommets doivent exister, -1 si pas d'arc
	boolean contientSommet(String sommet);
	boolean contientArc(String src, String dest);
	default String toAString() {
	    List<String> sommetsTries = new ArrayList<>(getSommets());
	    Collections.sort(sommetsTries);

	    List<String> descriptionsArcs = new ArrayList<>();

	    for (String sommet : sommetsTries) {
	        List<String> successeurs = getSucc(sommet);

	        if (successeurs.isEmpty()) {
	            descriptionsArcs.add(sommet + ":");
	        } else {
	            List<String> successeursTries = new ArrayList<>(successeurs);
	            Collections.sort(successeursTries);

	            for (String successeur : successeursTries) {
	                int poids = getValuation(sommet, successeur);
	                descriptionsArcs.add(sommet + "-" + successeur + "(" + poids + ")");
	            }
	        }
	    }

	    return String.join(", ", descriptionsArcs);
	}
	
	private static String distanceMin(int[] distances, Map<String, Boolean> visite) {
	    String distance = null;
	    for (String sommet : visite.keySet()) {
	    	if (!visite.get(sommet) && (distance == null || distances[Integer.parseInt(sommet)] < distances[Integer.parseInt(distance)])) {
	    	    distance = sommet;
	    	}
	    }
	    return distance;
	}

	default List<String> Djikstra(Object graphe, String debut, String arrive) {
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
}