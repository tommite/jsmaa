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
package fi.smaa.jsmaa.gui.presentation;

import java.util.List;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.AbstractValueModel;

import fi.smaa.jsmaa.model.CardinalPreferenceInformation;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.MissingPreferenceInformation;
import fi.smaa.jsmaa.model.OrdinalPreferenceInformation;
import fi.smaa.jsmaa.model.SMAAModel;

@SuppressWarnings("serial")
public class PreferencePresentationModel extends PresentationModel<SMAAModel> {
	
	public static final String PREFERENCE_TYPE = "preferenceType";
	private OrdinalPreferenceInformation ordinalPreferences;
	private CardinalPreferenceInformation cardinalPreferences;
	private boolean includeCardinal;
	
	public enum PreferenceType {
		MISSING("Missing"),
		ORDINAL("Ordinal"),
		CARDINAL("Cardinal");
		
		private String label;
		
		PreferenceType(String label) {
			this.label = label;
		}
		
		public String getLabel() {
			return label;
		}		
	}
	
	public PreferencePresentationModel(SMAAModel model, boolean includeCardinal) {
		super(model);
		this.includeCardinal = includeCardinal;
	}
	
	public boolean includesCardinalPreferences() {
		return includeCardinal;
	}
	
	@Override
	public AbstractValueModel getModel(String property) {
		if (property.equals(PREFERENCE_TYPE)) {
			return new PreferenceTypeValueModel();
		}
		return super.getModel(property);
	}
	
	public class PreferenceTypeValueModel extends AbstractValueModel {
		public Object getValue() {
			return getPreferenceType();			
		}
		public void setValue(Object val) {
			Object oldVal = getPreferenceType();
			setPreferenceType((PreferenceType) val);
			fireValueChange(oldVal, val);
		}
	}
	
	public void setPreferenceType(PreferenceType preferenceType) {
		Object oldVal = getPreferenceType();
		if (preferenceType == PreferenceType.CARDINAL) {
			getBean().setPreferenceInformation(generateCardinalPreferences());
		} else if (preferenceType == PreferenceType.ORDINAL) {
			getBean().setPreferenceInformation(generateOrdinalPreferences());
		} else if (preferenceType == PreferenceType.MISSING) {
			getBean().setPreferenceInformation(generateMissingPreferences());
		} else {
			throw new RuntimeException("unknown preference type");
		}
		firePropertyChange(PREFERENCE_TYPE, oldVal, preferenceType);
	}
	
	private CardinalPreferenceInformation generateCardinalPreferences() {
		if (cardinalPreferences == null) {
			cardinalPreferences = new CardinalPreferenceInformation(getBean().getCriteria());
		}
		return cardinalPreferences;
	}

	private OrdinalPreferenceInformation generateOrdinalPreferences() {
		if (ordinalPreferences == null) {
			List<Criterion> crit = getBean().getCriteria();
			ordinalPreferences = new OrdinalPreferenceInformation(crit);			
		}
		return ordinalPreferences;
	}

	private MissingPreferenceInformation generateMissingPreferences() {
		return new MissingPreferenceInformation(getBean().getCriteria().size());
	}

	public PreferenceType getPreferenceType() {
		if (getBean().getPreferenceInformation() instanceof MissingPreferenceInformation) {
			return PreferenceType.MISSING;
		} else if (getBean().getPreferenceInformation() instanceof CardinalPreferenceInformation) {
			return PreferenceType.CARDINAL;
		} else if (getBean().getPreferenceInformation() instanceof OrdinalPreferenceInformation) {
			return PreferenceType.ORDINAL;
		}
		throw new RuntimeException("unknown preference type");
	}
}
