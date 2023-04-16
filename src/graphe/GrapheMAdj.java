package graphe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrapheMAdj implements IGraphe{
	private int[][] matrice;
	private Map<String, Integer> indices;
	private static final int NOT_ARC = -1; //indique qu'il n'y a pas d'arc entre les deux sommets
	private String lettre = "A";
	private int nbSommets;
	
	// permet de construire un graphe sans arc
	public GrapheMAdj(int nbSommets) {
		indices = new HashMap<>();
		this.matrice = new int[nbSommets][nbSommets];
		for(int i = 0; i < nbSommets; ++i) {
			for(int j = 0; i < nbSommets; ++j) {
				matrice[i][j] = -1;
			}
		}
		for(int i = 0; i < nbSommets; ++i) {
			indices.put(lettre, i);
			lettre += 1;
		}
		this.nbSommets = nbSommets;
	}
	
	//permet de construire un graphe avec des arcs déjà renseignés
	public GrapheMAdj(int nbSommets, List<Arc> arcs) {
		indices = new HashMap<>();
		this.matrice = new int[nbSommets][nbSommets];
		this.nbSommets = nbSommets;
		for(int i = 0; i < nbSommets; ++i) {
			for(int j = 0; i < nbSommets; ++j) {
				for (Arc a: arcs) {
					if(a.getSource() == getKeyFromValue(indices, i) && a.getDestination() == getKeyFromValue(indices, j))
						matrice[i][j] = a.getValuation();
					else
						matrice[i][j] = -1;
				}
			}
		}
		for(int i = 0; i < nbSommets; ++i) {
			indices.put(lettre, i);
			lettre += 1;
		}
	}
	
	@Override
	public List<String> getSommets() {
		List<String> sommets = new ArrayList<>();
		for( Map.Entry<String, Integer> map: indices.entrySet()) {
			String sommet = map.getKey();
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
	            nouvelleMatrice[i][nbSommets] = 0;
	            nouvelleMatrice[nbSommets][i] = 0;
	        }
	        matrice = nouvelleMatrice;
	        indices.put(noeud, nbSommets);
	        nbSommets++;
	    }
	}

	
	@Override
	public void ajouterArc(String source, String destination, Integer valeur) {
		int indS = indices.get(source);
		int indDest = indices.get(destination);
		if(matrice[indS][indDest] != NOT_ARC) {
			throw new IllegalStateException("L'arc existe déjà dans le graphe.");
			//l'arc existe déjà donc c'est une IllegalStateException et non pas une IllegalArgumentException
		}
		else {
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
	}

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
		int indS = indices.get(source);
		int indDest = indices.get(destination);
		if(matrice[indS][indDest] == NOT_ARC) {
			throw new IllegalStateException("L'arc n'existe pas dans le graphe.");
			//l'arc est déjà absent du graphe donc c'est une IllegalStateException et non pas une IllegalArgumentException
		}
		else {
			matrice[indS][indDest] = NOT_ARC;
		}
	}
	
	
}