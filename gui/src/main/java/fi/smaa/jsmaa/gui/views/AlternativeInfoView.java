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

import java.util.List;

import javax.swing.JComponent;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.jsmaa.gui.LayoutUtil;
import fi.smaa.jsmaa.gui.ViewBuilder;
import fi.smaa.jsmaa.model.Alternative;

public class AlternativeInfoView implements ViewBuilder {
	private List<Alternative> alts;
	private String header;
	
	public AlternativeInfoView(List<Alternative> alts, String header) {
		this.alts = alts;
		this.header = header;
	}

	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"left:pref:grow",
				"p" );
		
		int fullWidth = 1;

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator(header, cc.xyw(1,1, fullWidth));

		int row = 3;
		
		for (Alternative a : alts) {
			LayoutUtil.addRow(layout);
		
			builder.add(BasicComponentFactory.createLabel(
					new PresentationModel<Alternative>(a).getModel(Alternative.PROPERTY_NAME)),
					cc.xy(1, row));
			row += 2;
		}
		
		return builder.getPanel();
	}

}
