package fi.smaa.common;

import java.text.DecimalFormat;
import java.util.Collection;

import com.jgoodies.binding.beans.Model;

public class Interval extends Model {
	
	public static final String PROPERTY_START = "start";
	public static final String PROPERTY_END = "end";
	
	private Double start;
	private Double end;
	
	public Interval() {
		start = 0.0;
		end = 0.0;
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @throws InvalidIntervalException if end < start
	 */
	public Interval(Double start, Double end) throws InvalidIntervalException {
		if (end < start) {
			throw new InvalidIntervalException();
		}
		this.start = start;
		this.end = end;
	}
	
	public Double getStart() {
		return start;
	}
	
	public Double getEnd() {
		return end;
	}
	
	public void setStart(Double start) {
		Object oldVal = this.start;
		this.start = start;
		firePropertyChange(PROPERTY_START, oldVal, this.start);
	}
	
	public void setEnd(Double end) {
		Object oldVal = this.end;
		this.end = end;
		firePropertyChange(PROPERTY_END, oldVal, this.end);		
	}

	@Override
	public String toString() {
		DecimalFormat fmt = new DecimalFormat("#0.00");
		return "[" + fmt.format(start) + " - " + fmt.format(end) + "]";
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof Interval)) {
			return false;
		}
		Interval io = (Interval) other;
		return getStart().equals(io.getStart()) && getEnd().equals(io.getEnd());
	}
	
	public static Interval enclosingInterval(Collection<Interval> intervals) {
		if (intervals.size() == 0) {
			return null;
		}
		Double min = Double.MAX_VALUE;
		Double max = Double.MIN_VALUE;
		for (Interval i : intervals) {
			if (i.getStart() < min) {
				min = i.getStart();
			}
			if (i.getEnd() > max) {
				max = i.getEnd();
			}
		}
		return new Interval(min, max);
	}
	
	public Double getLength() {
		return end - start;
	}
	
	public boolean includes(Interval other) {
		return other.getStart() >= getStart() && other.getEnd() <= getEnd(); 
	}

}
