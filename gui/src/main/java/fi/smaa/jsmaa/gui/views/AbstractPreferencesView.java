/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.

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

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.drugis.common.gui.LayoutUtil;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.jsmaa.gui.components.FocusTransferrer;
import fi.smaa.jsmaa.model.AbstractPreferenceInformation;
import fi.smaa.jsmaa.model.Criterion;

public abstract class AbstractPreferencesView<T extends AbstractPreferenceInformation<?>> {

	protected T prefs;
	private ScaleRenderer scaleRenderer = new DefaultScaleRenderer();

	public AbstractPreferencesView(T prefs) {
		this.prefs = prefs;
	}
	
	public void setScaleRenderer(ScaleRenderer renderer) {
		this.scaleRenderer = renderer;
	}
	
	protected abstract JComponent getPreferenceComponent(Criterion c, int i);
	protected abstract String getTypeName();
	
	public JComponent buildPanel() {		
		FormLayout layout = new FormLayout(
				"right:pref, 7dlu, center:pref, 3dlu, center:pref",
				"p, 3dlu, p" );
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addLabel("Criterion", cc.xy(1, 1));
		builder.addLabel("Scale", cc.xy(3, 1));
		builder.addLabel(getTypeName(), cc.xy(5, 1));
		
		int row = 3;
		int i=0;
		JComponent firstComp = null;
		for (Criterion c : prefs.getCriteria()) {
			row += 2;
			LayoutUtil.addRow(layout);
			JLabel label = new JLabel();
			Bindings.bind(label, "text",
					new PresentationModel<Criterion>(c).getModel(
							Criterion.PROPERTY_NAME)
			);
			builder.add(label, cc.xy(1, row));
			builder.add(scaleRenderer.getScaleComponent(c), cc.xy(3, row));			
			JComponent measurementComp = getPreferenceComponent(c, i);
			builder.add(measurementComp, cc.xy(5, row));
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
}