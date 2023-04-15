package graphe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrapheLAdj implements IGraphe {
	private Map<String, List<Arc>> lAdj;
	
	public GrapheLAdj(List<Arc> arcs) {
		lAdj = new HashMap<>();
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

	@Override
	public void ajouterSommet(String noeud) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ajouterArc(String source, String destination, Integer valeur) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void oterSommet(String noeud) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void oterArc(String source, String destination) {
		// TODO Auto-generated method stub
		
	}
}