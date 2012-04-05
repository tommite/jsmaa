package fi.smaa.jsmaa.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractMeasurements extends AbstractEntity implements FullJointMeasurements {
	private static final long serialVersionUID = -3700988320972977116L;

	protected final List<Criterion> criteria;
	protected final List<Alternative> alternatives;
	private transient List<ImpactMatrixListener> thisListeners = new ArrayList<ImpactMatrixListener>();

	public AbstractMeasurements() {
		this(Collections.<Criterion>emptyList(), Collections.<Alternative>emptyList());
	}
	
	public AbstractMeasurements(List<Criterion> criteria, List<Alternative> alternatives) {
		super();
		this.criteria = new ArrayList<Criterion>(criteria);
		this.alternatives = new ArrayList<Alternative>(alternatives);
	}

	@Override
	public void addListener(ImpactMatrixListener l) {
		if (thisListeners.contains(l)) {
			return;
		}
		thisListeners.add(l);
	}

	@Override
	public void removeListener(ImpactMatrixListener l) {
		thisListeners.remove(l);
	}

	@Override
	public List<Alternative> getAlternatives() {
		return alternatives;
	}

	@Override
	public List<Criterion> getCriteria() {
		return criteria;
	}

	protected void assertExistAlternativeAndCriterion(Criterion crit, Alternative alt) {
		assert(criteria.contains(crit));
		assert(alternatives.contains(alt));
	}

	protected void fireMeasurementChanged() {
		for (ImpactMatrixListener l : thisListeners) {
			l.measurementChanged();
		}
	}

	protected void fireMeasurementTypeChanged() {
		for (ImpactMatrixListener l : thisListeners) {
			l.measurementTypeChanged();
		}
	}
	
	private void readObject(ObjectInputStream i) throws IOException, ClassNotFoundException {
		i.defaultReadObject();
		thisListeners = new ArrayList<ImpactMatrixListener>();
	}
}