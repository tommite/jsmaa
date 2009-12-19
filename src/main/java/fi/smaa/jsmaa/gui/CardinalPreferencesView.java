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

package fi.smaa.jsmaa.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.common.gui.LayoutUtil;
import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.components.FocusTransferrer;
import fi.smaa.jsmaa.gui.components.MeasurementPanel;
import fi.smaa.jsmaa.gui.components.MeasurementPanel.MeasurementType;
import fi.smaa.jsmaa.model.CardinalMeasurement;
import fi.smaa.jsmaa.model.CardinalPreferenceInformation;
import fi.smaa.jsmaa.model.Criterion;

public class CardinalPreferencesView implements ViewBuilder {
	
	private CardinalPreferenceInformation pref;
	private Map<Criterion, ValueHolder> values;

	public CardinalPreferencesView(CardinalPreferenceInformation pref) {
		this.pref = pref;
		initValueHolders();
	}

	private void initValueHolders() {
		values = new HashMap<Criterion, ValueHolder>();
		for (Criterion c : pref.getCriteria()) {
			ValueHolder holder = new ValueHolder(pref.getMeasurement(c));
			values.put(c, holder);
			holder.addPropertyChangeListener(new HolderListener(c));
		}
	}

	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"right:pref, 3dlu, center:pref",
				"p, 3dlu, p" );
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addLabel("Criterion", cc.xy(1, 1));
		builder.addLabel("Weight constraint", cc.xy(3, 1));
		
		int row = 3;
		int i=0;
		JComponent firstComp = null;
		for (Criterion c : pref.getCriteria()) {
			row += 2;
			LayoutUtil.addRow(layout);
			JLabel label = new JLabel();
			Bindings.bind(label, "text",
					new PresentationModel<Criterion>(c).getModel(
							Criterion.PROPERTY_NAME)
			);
			builder.add(label, cc.xy(1, row));
			JComponent measurementComp = createMeasurementComponent(c);
			builder.add(measurementComp, cc.xy(3, row));
			if (firstComp == null) {
				firstComp = measurementComp;
			}
			i++;
		}		
		JPanel panel = builder.getPanel();
		if (firstComp != null) {
			panel.addFocusListener(new FocusTransferrer(firstComp));
		}
		return panel;
	}

	private JComponent createMeasurementComponent(Criterion c) {
		MeasurementType[] mvals = new MeasurementType[] { MeasurementType.EXACT, MeasurementType.INTERVAL };
		ValueHolder holder = values.get(c);
		MeasurementPanel vpanel = new MeasurementPanel(holder, mvals); 
		return vpanel;
	}
	
	private class HolderListener implements PropertyChangeListener {
		private Criterion c;
		public HolderListener(Criterion c) {
			this.c = c;
		}
		public void propertyChange(PropertyChangeEvent evt) {
			pref.setMeasurement(c, (CardinalMeasurement) evt.getNewValue());
		}
		
	}

}
