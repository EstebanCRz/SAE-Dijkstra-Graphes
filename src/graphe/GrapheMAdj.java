package graphe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GrapheMAdj implements IGraphe{
	private int[][] matrice;
	private Map<String, Integer> indices;
	private static final int NOT_ARC = -1; //indique qu'il n'y a pas d'arc entre les deux sommets
	private String lettre = "A";
	
	public GrapheMAdj(int nbSommets) {
		for(int i = 0; i < nbSommets; ++i) {
			for(int j = 0; i < nbSommets; ++j) {
				matrice[i][j] = -1;
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
		for(String key : indices.keySet()) {
			int i = indices.get(key);
			if(matrice[ind][i] != NOT_ARC) {
				succ.add(key);
			}
		}
		return succ;
	}
	
	@Override
	public int getValuation(String src, String dest) {
		int indS = indices.get(src);
		int indDest = indices.get(dest);
		return matrice[indS][indDest];
	}
	
	@Override
	public boolean contientSommet(String sommet) {
		return indices.containsKey(sommet);
	}
	
	@Override
	public boolean contientArc(String src, String dest) {
		int indS = indices.get(src);
		int indDest = indices.get(dest);
		if(matrice[indS][indDest] != NOT_ARC)
			return true;
		else
			return false;
	}
	
	@Override
	public void ajouterSommet(String noeud) {
		if(!this.contientSommet(noeud)) {
			for(int i = 0; i < indices.size(); ++i) {
				matrice[indices.size()][i] = NOT_ARC;
			}
			indices.put(noeud, indices.size());
		}
	}
	
	@Override
	public void ajouterArc(String source, String destination, Integer valeur) {
		int indS = indices.get(source);
		int indDest = indices.get(destination);
		if(matrice[indS][indDest] != NOT_ARC) {
			throw new IllegalArgumentException("L'arc existe déjà");
		}
		else {
			matrice[indS][indDest] = valeur;
		}
	}
	
	@Override
	public void oterSommet(String noeud) {
		if(this.contientSommet(noeud)) {
			int ind = indices.get(noeud);
			int taille = indices.size();
			for (int i = ind; i < taille - 1; i++) {
	            // Décale les indices de la map pour maintenir la cohérence avec la matrice
				indices.put(noeud + 1, i);
	        }
	        indices.remove(noeud);
			for (int i = ind; i < taille - 1; i++) { // supprime les lignes où le sommet apparaît
	            for (int j = 0; j < taille; j++) {
	                matrice[i][j] = matrice[i+1][j];
	            }
	        }
	        for (int j = ind; j < taille - 1; j++) { // supprime les colonnes où le sommet apparaît
	            for (int i = 0; i < taille - 1; i++) {
	                matrice[i][j] = matrice[i][j+1];
	            }
	        }
		}
	}
	
	@Override
	public void oterArc(String source, String destination) {
		int indS = indices.get(source);
		int indDest = indices.get(destination);
		if(matrice[indS][indDest] == NOT_ARC) {
			throw new IllegalArgumentException("L'arc n'existe pas");
		}
		else {
			matrice[indS][indDest] = NOT_ARC;
		}
	}
	
	
}