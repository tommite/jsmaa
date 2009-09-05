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

import java.util.ArrayList;
import java.util.List;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.AbstractValueModel;

import fi.smaa.jsmaa.model.OrdinalPreferenceInformation;
import fi.smaa.jsmaa.model.Rank;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.ModelChangeEvent;
import fi.smaa.jsmaa.model.SMAAModelListener;

@SuppressWarnings("serial")
public class SMAAModelPreferencePresentationModel extends PresentationModel<SMAAModel> {
	public static final String ORDINAL_ENABLED = "ordinalEnabled";
	public static final String ORDINAL_RANKS = "ordinalRanks";
	
	private OrdinalPreferenceInformation ordinalPreferences;
	
	public SMAAModelPreferencePresentationModel(SMAAModel model) {
		super(model);
		initOrdinalRanks(model);
		model.addModelListener(new ModelChangeListener());
	}

	private void initOrdinalRanks(SMAAModel model) {
		if (model.getPreferenceInformation() instanceof OrdinalPreferenceInformation) {
			ordinalPreferences = (OrdinalPreferenceInformation) model.getPreferenceInformation();
		} else {
			ArrayList<Rank> ranks = new ArrayList<Rank>();
			for (int i=0;i<model.getCriteria().size();i++) {
				ranks.add(new Rank(i+1));
			}
			ordinalPreferences = new OrdinalPreferenceInformation(ranks);
		}
	}
	
	public List<Rank> getOrdinalRanks() {
		if (ordinalPreferences != getBean().getPreferenceInformation()) {
			initOrdinalRanks(getBean());
		}
		return ordinalPreferences.getRanks();
	}
	
	@Override
	public AbstractValueModel getModel(String property) {
		if (property.equals(ORDINAL_ENABLED)) {
			return new OrdinalEnabledValueModel();
		}
		return super.getModel(property);
	}
	
	private class ModelChangeListener implements SMAAModelListener {
		public void modelChanged(ModelChangeEvent type) {
			if (type == ModelChangeEvent.PREFERENCES) {
				firePropertyChange(ORDINAL_ENABLED, 
						null,
						getBean().getPreferenceInformation() instanceof OrdinalPreferenceInformation);										
			} else if (type == ModelChangeEvent.CRITERIA) {
				initOrdinalRanks(getBean());
			}
		}
	}
	
	public class OrdinalEnabledValueModel extends AbstractValueModel {
		public Object getValue() {
			return isOrdinalInBean();			
		}
		public void setValue(Object val) {
			boolean oldVal = isOrdinalInBean();
			boolean enable = (Boolean)val;
			if (enable) {
				getBean().setPreferenceInformation(ordinalPreferences);
			} else {
				getBean().setMissingPreferences();
			}
			fireValueChange(oldVal, enable);
		}
	}
	
	private boolean isOrdinalInBean() {
		return getBean().getPreferenceInformation() instanceof OrdinalPreferenceInformation;
	}
	
}
