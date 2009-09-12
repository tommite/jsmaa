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

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.common.gui.LayoutUtil;
import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.OrdinalPreferenceInformation;

public class OrdinalPreferencesView implements ViewBuilder {
	
	private OrdinalPreferenceInformation prefs;
	private List<Criterion> crit;

	public OrdinalPreferencesView(List<Criterion> crit, OrdinalPreferenceInformation prefs) {
		this.prefs = prefs;
		this.crit = crit;
	}

	public JComponent buildPanel() {		
		FormLayout layout = new FormLayout(
				"right:pref, 3dlu, center:pref",
				"p, 3dlu, p" );
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		RankSelectorGroup selectorGroup = new RankSelectorGroup(prefs.getRanks());
		
		builder.addLabel("Criterion", cc.xy(1, 1));
		builder.addLabel("Rank", cc.xy(3, 1));
		
		int row = 3;
		int i=0;
		for (Criterion c : crit) {
			row += 2;
			LayoutUtil.addRow(layout);
			JLabel label = new JLabel();
			Bindings.bind(label, "text",
					new PresentationModel<Criterion>(c).getModel(
							Criterion.PROPERTY_NAME)
			);
			builder.add(label, cc.xy(1, row));
			JComboBox selector = selectorGroup.getSelectors().get(i);
			builder.add(selector, cc.xy(3, row));
			i++;
		}
		
		return builder.getPanel();
	}
}
