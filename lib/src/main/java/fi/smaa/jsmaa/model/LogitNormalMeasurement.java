package fi.smaa.jsmaa.model;

import org.drugis.common.stat.Statistics;

public class LogitNormalMeasurement extends GaussianMeasurement {
	private static final long serialVersionUID = -3227427739303388222L;
	
	public LogitNormalMeasurement(double mean, double stdDev) {
		super(mean, stdDev);
	}

	@Override
	public double sample() {
		return Statistics.ilogit(super.sample());
	}
	
	@Override
	public Interval getRange() {
		Interval r = super.getRange();
		return new Interval(Statistics.ilogit(r.getStart()), Statistics.ilogit(r.getEnd()));
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof LogitNormalMeasurement) {
			LogitNormalMeasurement other = (LogitNormalMeasurement)o;
			return other.getMean().equals(getMean()) && other.getStDev().equals(getStDev());
		}
		return false;
	}
	
	@Override
	public LogitNormalMeasurement deepCopy() {
		return new LogitNormalMeasurement(getMean(), getStDev());
	}
}