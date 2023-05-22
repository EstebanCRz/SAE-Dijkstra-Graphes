package graphe.core;
public class Arc {
	private int valuation;
	private String source, destination;
	
	public Arc(String s, String d, int v) {
		assert v >= 0;
		this.source = s;
		this.destination = d;
		this.valuation = v;
	}
	
	public String getSource() {
		return this.source;
	}
	
	public String getDestination() {
		return this.destination;
	}
	
	public int getValuation() {
		return this.valuation;
	}
	
	public String toString() {
		return this.getSource() + "-" + this.getDestination() + "(" + this.getValuation() + ")";
	}
}