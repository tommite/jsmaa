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

package fi.smaa.jsmaa.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SMAATRIModel extends SMAAModel {
	
	private static final long serialVersionUID = -8040983176716927197L;
	private ImpactMatrix profileMatrix;
	private List<Alternative> categories = new ArrayList<Alternative>();

	public SMAATRIModel(String name) {
		super(name);
		profileMatrix = new ImpactMatrix();
		connectProfileListener();
	}
	
	public void setCategories(List<Alternative> categories) {
		this.categories = categories;
		profileMatrix.setAlternatives(this.categories);
		fireModelChange(ModelChangeEvent.PROFILES);
	}
	
	public void addCategory(Alternative cat) {
		List<Alternative> newCats = new ArrayList<Alternative>(categories);
		newCats.add(cat);
		setCategories(newCats);
	}
	
	public List<Alternative> getCategories() {
		return categories;
	}
		
	public void setCriteria(Collection<Criterion> crit) {
		super.setCriteria(crit);
		profileMatrix.setCriteria(getCriteria());
	}
		
	public void setCategoryUpperBound(CardinalCriterion crit, 
			Alternative category, CardinalMeasurement meas) {
		profileMatrix.setMeasurement(crit, category, meas);
	}
	
	public CardinalMeasurement getCategoryUpperBound(CardinalCriterion crit, Alternative category) {
		return profileMatrix.getMeasurement(crit, category);
	}
	
	private void connectProfileListener() {
		profileMatrix.addListener(impactListener);	
	}

	private void readObject(ObjectInputStream i) throws IOException, ClassNotFoundException {
		i.defaultReadObject();
		connectProfileListener();
	}
}
