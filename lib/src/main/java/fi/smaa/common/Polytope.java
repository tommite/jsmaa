package fi.smaa.common;

import java.util.Collection;
import java.util.List;

import org.apache.commons.math.optimization.linear.LinearConstraint;

public class Polytope {
	
	private List<LinearConstraint> edges;

	// Edge-presentation
	public Polytope(List<LinearConstraint> edges) {
		this.edges = edges;
	}
	
	public Collection<LinearConstraint> getEdges() {
		return edges;
	}
	
	public int getDimension() {
		return edges.get(0).getCoefficients().getDimension();
	}
}
