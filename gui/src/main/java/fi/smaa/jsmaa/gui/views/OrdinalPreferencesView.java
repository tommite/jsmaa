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
package fi.smaa.jsmaa.gui.views;

import javax.swing.JComboBox;

import org.drugis.common.gui.ViewBuilder;

import fi.smaa.jsmaa.gui.RankSelectorGroup;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.OrdinalPreferenceInformation;

public class OrdinalPreferencesView extends AbstractPreferencesView<OrdinalPreferenceInformation> implements ViewBuilder {
	
	private RankSelectorGroup selectorGroup;

	public OrdinalPreferencesView(OrdinalPreferenceInformation prefs) {
		super(prefs);
		selectorGroup = new RankSelectorGroup(prefs.getRanks());	
	}

	@Override
	protected String getTypeName() {
		return "Rank";
	}

	@Override
	protected JComboBox getPreferenceComponent(Criterion c, int i) {
		return selectorGroup.getSelectors().get(i);
	}
}
