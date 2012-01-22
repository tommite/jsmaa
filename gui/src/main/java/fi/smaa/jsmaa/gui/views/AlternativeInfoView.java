/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.

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

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import org.drugis.common.gui.LayoutUtil;
import org.drugis.common.gui.ViewBuilder;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.jsmaa.model.Alternative;

public class AlternativeInfoView implements ViewBuilder {
	private List<? extends Alternative> alts;
	
	public AlternativeInfoView(List<? extends Alternative> alts) {
		this.alts = alts;
	}

	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"fill:pref:grow",
				"p" );
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setBorder(BorderFactory.createEmptyBorder());
		CellConstraints cc = new CellConstraints();
		
		int row = 1;
		
		for (Alternative a : alts) {
			if (row > 1) {
				LayoutUtil.addRow(layout);	
			}
		
			builder.add(BasicComponentFactory.createLabel(
					new PresentationModel<Alternative>(a).getModel(Alternative.PROPERTY_NAME)),
					cc.xy(1, row));
			row += 2;
		}
		
		return builder.getPanel();
	}

}
