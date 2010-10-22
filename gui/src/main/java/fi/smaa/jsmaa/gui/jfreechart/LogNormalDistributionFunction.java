package fi.smaa.jsmaa.gui.jfreechart;

import org.jfree.data.function.Function2D;
import org.jfree.data.function.NormalDistributionFunction2D;

import fi.smaa.jsmaa.model.LogNormalMeasurement;

public class LogNormalDistributionFunction implements Function2D {
	
	NormalDistributionFunction2D normal;
	
	public LogNormalDistributionFunction(LogNormalMeasurement meas) {
		normal = new NormalDistributionFunction2D(meas.getMean(), meas.getStDev());
	}
	
	public double getValue(double x) {
		return normal.getValue(Math.log(x));
	}

}
