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

import javax.swing.JComboBox;
import javax.swing.JComponent;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.presentation.PreferencePresentationModel;
import fi.smaa.jsmaa.gui.presentation.PreferencePresentationModel.PreferenceType;
import fi.smaa.jsmaa.model.OrdinalPreferenceInformation;
import fi.smaa.jsmaa.model.SMAAModel;

public class PreferenceInformationView implements ViewBuilder {
	private PreferencePresentationModel model;
	
	public PreferenceInformationView(PreferencePresentationModel model) {
		this.model = model;
	}

	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"right:pref, 3dlu, pref",
				"p, 3dlu, p, 3dlu, p");
		
		int fullWidth = 3;
		
		ValueModel preferenceTypeModel = model.getModel(PreferencePresentationModel.PREFERENCE_TYPE);

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator("Preferences", cc.xyw(1, 1, fullWidth));
		
		PreferenceType[] typeList = new PreferenceType[] {
				PreferenceType.MISSING,
				PreferenceType.CARDINAL,
				PreferenceType.ORDINAL
		};
		
		SelectionInList<PreferenceType> typeSelInList 
			= new SelectionInList<PreferenceType>(typeList, preferenceTypeModel);
		

		JComboBox preferenceTypeBox = BasicComponentFactory.createComboBox(typeSelInList);
		builder.add(preferenceTypeBox, cc.xy(1, 3));

		builder.addLabel("Preference information", cc.xyw(3, 3, fullWidth-2));

		if (model.getPreferenceType() == PreferenceType.ORDINAL) {
			SMAAModel smodel = model.getBean();
			OrdinalPreferencesView oview = new OrdinalPreferencesView(smodel.getCriteria(),
					(OrdinalPreferenceInformation) smodel.getPreferenceInformation());
			builder.add(oview.buildPanel(), cc.xyw(1, 5, fullWidth));
		}
		return builder.getPanel();
	}
}
