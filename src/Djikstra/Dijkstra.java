package Djikstra;

import java.util.Map;
import java.util.PriorityQueue;

import graphe.core.IGrapheConst;

public class Dijkstra {
	
	public static void dijkstra(IGrapheConst g, String source, Map<String, Integer> dist, Map<String, String> prev) {
		for (String sommet : g.getSommets()) {
	        dist.put(sommet, Integer.MAX_VALUE);
	    }
	    dist.put(source, 0);

	    // File de priorité pour obtenir le sommet avec la plus petite distance
	    PriorityQueue<String> queue = new PriorityQueue<>((s1, s2) -> dist.get(s1) - dist.get(s2)); 
	    queue.offer(source);

	    while (!queue.isEmpty()) {
	        String sommetCourant = queue.poll();

	        // Parcours des voisins du sommet courant
	        for (String voisin : g.getSucc(sommetCourant)) {
	            int distance = dist.get(sommetCourant) + g.getValuation(sommetCourant, voisin);

	            // Mise à jour de la distance si une distance plus courte est trouvée
	            if (distance < dist.get(voisin)) {
	                dist.put(voisin, distance);
	                prev.put(voisin, sommetCourant);
	                queue.offer(voisin);
	            }
	        }
	    }
	}
	
}