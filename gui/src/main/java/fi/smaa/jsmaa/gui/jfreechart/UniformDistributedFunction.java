package fi.smaa.jsmaa.gui.jfreechart;

import org.jfree.data.function.Function2D;

import fi.smaa.jsmaa.model.Interval;

public class UniformDistributedFunction implements Function2D {
	
	private Interval ival;

	public UniformDistributedFunction(Interval ival) {
		this.ival = ival;
	}

	public double getValue(double x) {
		if (x < ival.getStart() || x > ival.getEnd()) {
			return 0.0;
		}
		return 1.0 / ival.getLength();
	}

}
