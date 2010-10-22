package fi.smaa.jsmaa.gui.jfreechart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.xy.AbstractXYDataset;

import fi.smaa.jsmaa.model.ScaleCriterion;

@SuppressWarnings("serial")
public class UtilityFunctionDataset extends AbstractXYDataset implements PropertyChangeListener {
	
	private ScaleCriterion crit;

	public UtilityFunctionDataset(ScaleCriterion crit) {
		this.crit = crit;
		crit.addPropertyChangeListener(this);
	}
	
	public ScaleCriterion getCriterion() {
		return crit;
	}

	@Override
	public int getSeriesCount() {
		return 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Comparable getSeriesKey(int index) {
		if (index == 0) {
			return "value";
		}
		throw new IndexOutOfBoundsException("index " + index);
	}

	public int getItemCount(int index) {
		if (index == 0) {
			return 2;
		}
		throw new IndexOutOfBoundsException("index " + index);
	}

	public Number getX(int series, int item) {
		if (series == 0) {
			if (item == 0) {
				return crit.getScale().getStart();
			} else if (item == 1) {
				return crit.getScale().getEnd();
			}
		}
		throw new IndexOutOfBoundsException("series " + series + " item " + item);
	}

	public Number getY(int series, int item) {
		if (series == 0) {
			if ((item == 0 && crit.getAscending()) ||
					(item == 1 && !crit.getAscending())) {
				return new Double(0.0);
			} else 	if ((item == 1 && crit.getAscending()) ||
					(item == 0 && !crit.getAscending())) {
				return new Double(1.0);
			}
		}
		throw new IndexOutOfBoundsException("series " + series + " item " + item);
	}

	public void propertyChange(PropertyChangeEvent ev) {
		notifyListeners(new DatasetChangeEvent(this, this));
	}
}
