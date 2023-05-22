package graphe.implems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import graphe.core.Arc;
import graphe.core.IGraphe;

public class GrapheMAdj implements IGraphe{
	private int[][] matrice;
	private Map<String, Integer> indices;
	private static final int NOT_ARC = -1; //indique qu'il n'y a pas d'arc entre les deux sommets
	private static final String lettre = "A"; // donne le premier sommet lors de la création
	private int nbSommets;
	
	// permettra de construire un graphe sans arc de départ
	public GrapheMAdj() {
		indices = new HashMap<>();
		this.matrice = new int[0][0];
		this.nbSommets = 0;
	}
	
	//permettra de construire un graphe avec des arcs déjà renseignés
	public GrapheMAdj(List<Arc> arcs) {
		indices = new HashMap<>();
		String lettreDep = lettre;
		this.matrice = new int[nbSommets][nbSommets];
		
		// récupération du nombre de sommets
		Set<String> sommets = new HashSet<>();
	    for (Arc arc : arcs) {
	        sommets.add(arc.getSource());
	        sommets.add(arc.getDestination());
	    }
		this.nbSommets = sommets.size();
		
		for(int i = 0; i < nbSommets; ++i) {
			for(int j = 0; j < nbSommets; ++j) {
				for (Arc a: arcs) {
					if(a.getSource() == getKeyFromValue(indices, i) && a.getDestination() == getKeyFromValue(indices, j))
						matrice[i][j] = a.getValuation();
					else
						matrice[i][j] = -1;
				}
			}
		}
		for(int i = 0; i < nbSommets; ++i) {
			indices.put(lettreDep, i);
			lettreDep += 1;
		}
	}
	
	@Override
	public List<String> getSommets() {
		List<String> sommets = new ArrayList<>();
		for( Map.Entry<String, Integer> map: indices.entrySet()) {
			String sommet = map.getKey();
			if (sommet != "")
				sommets.add(sommet);
		}
		return sommets;
	}
	
	@Override
	public List<String> getSucc(String sommet) {
		int ind = indices.get(sommet);
		List<String> succ = new ArrayList<>();
		for(String key : indices.keySet()) { // pour chaque key, vérifie si dans la matrice il y a un arc entre sommet et la key
			int i = indices.get(key);
			if(matrice[ind][i] != NOT_ARC) {
				succ.add(key);
			}
		}
		succ = succ.stream().distinct().collect(Collectors.toList());
		return succ;
	}
	
	@Override
	public int getValuation(String src, String dest) {
		return matrice[indices.get(src)][indices.get(dest)];
	}
	
	@Override
	public boolean contientSommet(String sommet) {
		return indices.containsKey(sommet);
	}
	
	@Override
	public boolean contientArc(String src, String dest) {
		if(matrice[indices.get(src)][indices.get(dest)] != NOT_ARC)
			return true;
		else
			return false;
	}
	
	@Override
	public void ajouterSommet(String noeud) {
	    if (!this.contientSommet(noeud)) {
	        int[][] nouvelleMatrice = new int[nbSommets + 1][nbSommets + 1];
	        // Copie des anciennes valeurs dans la nouvelle matrice
	        for (int i = 0; i < nbSommets; i++) {
	            System.arraycopy(matrice[i], 0, nouvelleMatrice[i], 0, nbSommets);
	        }
	        // Initialisation des nouvelles valeurs de la matrice à 0
	        for (int i = 0; i <= nbSommets; i++) {
	            nouvelleMatrice[i][nbSommets] = -1;
	            nouvelleMatrice[nbSommets][i] = -1;
	        }
	        matrice = nouvelleMatrice;
	        indices.put(noeud, nbSommets);
	        nbSommets++;
	    }
	    else {

	    }
	}

	
	@Override
	public void ajouterArc(String source, String destination, Integer valeur) {
		if(contientSommet(source) && contientSommet(destination)) {
			int indS = indices.get(source);
			int indDest = indices.get(destination);
			if(matrice[indS][indDest] != NOT_ARC) {
				throw new IllegalArgumentException("L'arc existe déjà dans le graphe.");
			}
			else if (valeur == -1)
				throw new IllegalArgumentException("L'arc ne peut pas avoir de valuation négative.");
			else {
				matrice[indS][indDest] = valeur;
			}
		}
		else {
			if (valeur == -1) {
				throw new IllegalArgumentException("L'arc ne peut pas avoir de valuation négative.");
			}
			ajouterSommet(source);
			ajouterSommet(destination);
			int indS = indices.get(source);
			int indDest = indices.get(destination);
			matrice[indS][indDest] = valeur;
		}			
	}
	
	@Override
	public void oterSommet(String noeud) {
	    if (contientSommet(noeud)) {
	        int ind = indices.get(noeud);
	        int taille = indices.size();
	        indices.remove(noeud);
	        for (int i = ind; i < taille - 1; i++) {
	            String s = getKeyFromValue(indices, i+1);
	            indices.put(s, i);
	        }
	        for (int i = ind; i < taille - 1; i++) {
	            System.arraycopy(matrice, i+1, matrice, i, taille-i-1);
	        }
	        for (int i = 0; i < taille-1; i++) {
	            System.arraycopy(matrice[i], ind+1, matrice[i], ind, taille-ind-1);
	        }
	        --nbSommets;
	        GrapheMAdj.redimensionnerMatrice(matrice, nbSommets);
	    }
	    else {
	    	
	    }
	}
	
	// permet de récupérer la clé associée à une valeur dans la map
	private static String getKeyFromValue(Map<String, Integer> map, int value) {
	    for (Map.Entry<String, Integer> entry : map.entrySet()) {
	        if (entry.getValue().equals(value)) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
	
	private static void redimensionnerMatrice(int[][] matrice, int nouvelleTaille) {
	    int[][] newMatrice = new int[nouvelleTaille][nouvelleTaille];
	    int limite = Math.min(nouvelleTaille, matrice.length);
	    for (int i = 0; i < limite; i++) {
	        System.arraycopy(matrice[i], 0, newMatrice[i], 0, limite);
	    }
	    matrice = newMatrice;
	}
	
	@Override
	public void oterArc(String source, String destination) {
		if(contientSommet(source) && contientSommet(destination)) {
			int indS = indices.get(source);
			int indDest = indices.get(destination);
			if(matrice[indS][indDest] == NOT_ARC) {
				throw new IllegalArgumentException("L'arc n'existe pas dans le graphe.");
			}
			else {
				matrice[indS][indDest] = NOT_ARC;
			}
		}
		else
			throw new IllegalArgumentException("Le sommet source ou destination n'existe pas dans le graphe.");
	}	
	
	public String toString() {
	    StringBuilder sb = new StringBuilder();

	    List<String> sommetSortant = getSommets();
	    Collections.sort(sommetSortant);

	    Set<String> arcsAjoutés = new HashSet<>();
	    Set<String> valuationPositives = new HashSet<>();

	    for (String src : sommetSortant) {
	        boolean estIsole = true; // Variable pour vérifier si le sommet est isolé
	        boolean aUneSource = false; // Variable pour vérifier si le sommet a une source
	        for (String dest : sommetSortant) {
	            int val = getValuation(src, dest);
	            if (val != -1) {
	                String arcString = src + "-" + dest + "(" + val + ")";
	                sb.append(arcString).append(", ");
	                arcsAjoutés.add(arcString);
	                estIsole = false;
	                if (val > 0) {
	                	valuationPositives.add(dest);
	                }
	                aUneSource = true;
	            }
	        }
	        if (estIsole && aUneSource) {
	            sb.append(src).append(":, ");
	        }
	    }

	    for (String sommet : sommetSortant) {
	        if (!valuationPositives.contains(sommet) && !arcsAjoutés.contains(sommet + ":")) {
	            sb.append(sommet).append(":, ");
	        }
	    }

	    if (sb.length() > 0) {
	        sb.setLength(sb.length() - 2);
	    }
	    return sb.toString();
	}

}