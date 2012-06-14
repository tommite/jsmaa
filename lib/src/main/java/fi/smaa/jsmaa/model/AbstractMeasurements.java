/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper 2012.

    JSMAA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSMAA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSMAA.  If not, see <http://www.gnu.org/licenses/>.
*/
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