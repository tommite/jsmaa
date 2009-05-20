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

package fi.smaa;

import java.util.ArrayList;
import java.util.List;

import com.jgoodies.binding.beans.Model;

public class SMAAModel extends Model {
	
	public final static String PROPERTY_ALTERNATIVES = "alternatives";
	public final static String PROPERTY_CRITERIA = "criteria";	
	public final static String PROPERTY_NAME = "name";
		
	private List<Alternative> alternatives;
	private List<Criterion> criteria;	
	private String name;

	public SMAAModel() {
		init();
	}
	
	public SMAAModel(String name) {
		this.name = name;
		init();
	}

	private void init() {
		alternatives = new ArrayList<Alternative>();
		criteria = new ArrayList<Criterion>();
	}
	
	public List<Alternative> getAlternatives() {
		return alternatives;
	}
	
	public void setAlternatives(List<Alternative> alts) {
		Object oldval = this.alternatives;
		this.alternatives = alts;
		updateCriteriaAlternatives();
		firePropertyChange(PROPERTY_ALTERNATIVES, oldval, this.alternatives);
	}

	public void setName(String name) {
		Object oldVal = this.name;
		this.name = name;
		firePropertyChange(PROPERTY_NAME, oldVal, name);
	}

	public String getName() {
		return name;
	}	
	
	public void addAlternative(Alternative alt) {
		List<Alternative> alts = new ArrayList<Alternative>(getAlternatives());
		alts.add(alt);
		setAlternatives(alts);
	}

	public List<Criterion> getCriteria() {
		return criteria;
	}
	
	/**
	 * Side-effect: sets the numAlts in each criterion.
	 * 
	 * @param criteria
	 */
	public void setCriteria(List<Criterion> criteria) {
		Object oldVal = this.criteria;
		this.criteria = criteria;
		updateCriteriaAlternatives();
		firePropertyChange(PROPERTY_CRITERIA, oldVal, criteria);
	}
	
	private void updateCriteriaAlternatives() {
		for (Criterion c : criteria) {
			c.setAlternatives(alternatives);
		}
	}

	public void addCriterion(Criterion cri) {
		List<Criterion> crit = new ArrayList<Criterion>();
		crit.addAll(getCriteria());
		crit.add(cri);
		setCriteria(crit);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String toStringDeep() {
		String ret = name + " : " + alternatives.size() + " alternatives - " + criteria.size() + " criteria\n";
		ret += "Alternatives: " + alternatives + "\n";
		ret += "Criteria: " + criteria + "\n";
		ret += "Measurements:\n";
		for (Criterion crit : criteria) {
			ret += crit.measurementsToString() + "\n";
		}
		return ret;
	}
	
	public void deleteAlternative(Alternative a) {
		List<Alternative> newAlts = new ArrayList<Alternative>();
		newAlts.addAll(alternatives);
		if (newAlts.remove(a)) {
			setAlternatives(newAlts);
		}
	}
	
}
