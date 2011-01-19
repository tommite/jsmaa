package fi.smaa.jsmaa.model;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

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
		return new Interval(0.0, 1.0);
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
	
	protected static final XMLFormat<LogitNormalMeasurement> XML = new XMLFormat<LogitNormalMeasurement>(LogitNormalMeasurement.class) {
		@Override
		public LogitNormalMeasurement newInstance(Class<LogitNormalMeasurement> cls, InputElement ie) throws XMLStreamException {
			return new LogitNormalMeasurement(ie.getAttribute("mean").toDouble(),
					ie.getAttribute("stdev").toDouble());
		}				
		@Override
		public boolean isReferenceable() {
			return GaussianMeasurement.XML.isReferenceable();
		}
		@Override
		public void read(InputElement ie, LogitNormalMeasurement meas) throws XMLStreamException {
			GaussianMeasurement.XML.read(ie, meas);			
		}
		@Override
		public void write(LogitNormalMeasurement meas, OutputElement oe) throws XMLStreamException {
			GaussianMeasurement.XML.write(meas, oe);
		}
	};
}