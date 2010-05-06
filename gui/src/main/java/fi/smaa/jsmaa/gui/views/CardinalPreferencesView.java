/*
	This file is part of JSMAA.
	(c) Tommi Tervonen, 2009	

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

package fi.smaa.jsmaa.gui.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import com.jgoodies.binding.value.ValueHolder;

import fi.smaa.jsmaa.gui.components.MeasurementPanel;
import fi.smaa.jsmaa.gui.components.MeasurementPanel.MeasurementType;
import fi.smaa.jsmaa.model.CardinalMeasurement;
import fi.smaa.jsmaa.model.CardinalPreferenceInformation;
import fi.smaa.jsmaa.model.Criterion;

public class CardinalPreferencesView extends AbstractPreferencesView<CardinalPreferenceInformation> {
	
	private Map<Criterion, ValueHolder> values;

	public CardinalPreferencesView(CardinalPreferenceInformation pref) {
		super(pref);
		initValueHolders();
	}

	private void initValueHolders() {
		values = new HashMap<Criterion, ValueHolder>();
		for (Criterion c : prefs.getCriteria()) {
			ValueHolder holder = new ValueHolder(prefs.getMeasurement(c));
			values.put(c, holder);
			holder.addPropertyChangeListener(new HolderListener(c));
		}
	}

	private class HolderListener implements PropertyChangeListener {
		private Criterion c;
		public HolderListener(Criterion c) {
			this.c = c;
		}
		public void propertyChange(PropertyChangeEvent evt) {
			prefs.setMeasurement(c, (CardinalMeasurement) evt.getNewValue());
		}
	}

	@Override
	protected JComponent getPreferenceComponent(Criterion c, int i) {
		MeasurementType[] mvals = new MeasurementType[] { MeasurementType.EXACT, MeasurementType.INTERVAL };
		ValueHolder holder = values.get(c);
		MeasurementPanel vpanel = new MeasurementPanel(holder, mvals); 
		return vpanel;
	}

	@Override
	protected String getTypeName() {
		return "Weight constraint";
	}
}
