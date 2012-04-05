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
package fi.smaa.jsmaa.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import fi.smaa.jsmaa.model.xml.CategoryList;

public class SMAATRIModel extends SMAAModel {
	
	private static final long serialVersionUID = -739020656344899318L;
	private ImpactMatrix profileMatrix;
	private List<Category> categories = new ArrayList<Category>();
	private boolean optimistic;
	private Interval lambda;
	
	public static final String PROPERTY_RULE = "rule";
	public static final String PROPERTY_LAMBDA = "lambda";

	public SMAATRIModel(String name) {
		super(name);
		profileMatrix = new ImpactMatrix();
		optimistic = true;
		lambda = new Interval(0.65, 0.85);
		profileMatrix.addListener(impactListener);
		lambda.addPropertyChangeListener(new LambdaListener());
	}
	
	public void setRule(boolean optimistic) {
		this.optimistic = optimistic;
		fireModelChange(ModelChangeEvent.PARAMETER);
	}
		
	public Interval getLambda() {
		return lambda;
	}
		
	public boolean getRule() {
		return optimistic;
	}
	
	synchronized public void addCategory(Category cat) {
		categories.add(cat);
		if (categories.size() > 1) {
			profileMatrix.addAlternative(categories.get(categories.size()-2));
		}
		fireModelChange(ModelChangeEvent.CATEGORIES);
	}
	
	synchronized public void deleteCategory(Alternative cat) {
		if (categories.remove(cat)) {
			profileMatrix.deleteAlternative(cat);
			fireModelChange(ModelChangeEvent.CATEGORIES);
		}
	}
	
	public List<Category> getCategories() {
		return categories;
	}
	
	/**
	 * All criteria should be outranking-criteria.
	 */
	@Override
	synchronized public void addCriterion(Criterion c) {
		if (!(c instanceof OutrankingCriterion)) {
			throw new IllegalArgumentException("All criteria should be outranking-criteria");
		}
		profileMatrix.addCriterion(c);		
		super.addCriterion(c);
	}
	
	@Override
	synchronized public void deleteCriterion(Criterion c) {
		profileMatrix.deleteCriterion(c);		
		super.deleteCriterion(c);
	}
	
	public void setCategoryUpperBound(OutrankingCriterion crit, 
			Alternative category, CardinalMeasurement meas) {
		profileMatrix.setMeasurement(crit, category, meas);
	}
	
	public CardinalMeasurement getCategoryUpperBound(OutrankingCriterion crit, Alternative category) {
		return (CardinalMeasurement) profileMatrix.getMeasurement(crit, category);
	}
	
	private void readObject(ObjectInputStream i) throws IOException, ClassNotFoundException {
		i.defaultReadObject();
		profileMatrix.addListener(impactListener);
		lambda.addPropertyChangeListener(new LambdaListener());
	}
	
	private class LambdaListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			fireModelChange(ModelChangeEvent.PARAMETER);
		}
	}
	
	@Override
	synchronized public SMAATRIModel deepCopy() {
		SMAATRIModel model = new SMAATRIModel(getName());
		super.deepCopyContents(model);
		List<Category> cats = new ArrayList<Category>();
		for (Category cat : categories) {
			cats.add(cat.deepCopy());
		}
		model.categories = cats;
		
		// set the profile matrix with categories minus the last one
		List<Alternative> catsMinusLastOne = new ArrayList<Alternative>();
		for (int i=0;i<model.categories.size()-1;i++) {
			catsMinusLastOne.add(model.categories.get(i));
		}
		model.profileMatrix = profileMatrix.deepCopy(catsMinusLastOne, model.getCriteria());
		model.setRule(optimistic);		
		model.getLambda().setStart(getLambda().getStart());
		model.getLambda().setEnd(getLambda().getEnd());
		return model;
	}

	public void reorderCategories(List<Category> newCats) {
		assert(newCats.size() == categories.size());
		if (categories.size() == 0) {
			return;
		}
		Category oldLastCat = this.categories.get(this.categories.size()-1);
		Category newLastCat = newCats.get(newCats.size()-1);
		
		this.categories = newCats;
		
		// if the old & new last cats differ, remove the new last category, because it doesn't need an upper bound
		// and add the old one, as it does
		if (newLastCat != oldLastCat) {
			profileMatrix.deleteAlternative(newLastCat);
			profileMatrix.addAlternative(oldLastCat);
		}
		// reorder
		List<Alternative> catList = new ArrayList<Alternative>(newCats);
		catList.remove(newCats.get(newCats.size()-1));		
		
		profileMatrix.reorderAlternatives(catList);
		
		fireModelChange(ModelChangeEvent.CATEGORIES);
	}
	
	@Override
	public void reorderCriteria(List<Criterion> newCrit) {
		profileMatrix.reorderCriteria(newCrit);
		super.reorderCriteria(newCrit);
	}

	public FullJointMeasurements getProfileImpactMatrix() {
		return profileMatrix;
	}
	
	@SuppressWarnings("unused")
	private static final XMLFormat<SMAATRIModel> XML = new XMLFormat<SMAATRIModel>(SMAATRIModel.class) {		
		@Override
		public boolean isReferenceable() {
			return false;
		}
		@Override
		public SMAATRIModel newInstance(Class<SMAATRIModel> cls, InputElement ie) throws XMLStreamException {
			return new SMAATRIModel(ie.getAttribute("name").toString());
		}
		@Override
		public void read(InputElement ie, SMAATRIModel model) throws XMLStreamException {
			model.optimistic = ie.getAttribute("optimisticRule").toBoolean();			
			SMAAModel.XML.read(ie, model);
			Interval nival = ie.get("lambda", Interval.class);
			model.lambda.setEnd(1.0);
			model.lambda.setStart(nival.getStart());
			model.lambda.setEnd(nival.getEnd());
			
			CategoryList categories = ie.get("categories", CategoryList.class);
			for (Alternative a : categories.getList()) {
				model.addCategory((Category) a);
			}
			ImpactMatrix im = ie.get("upperBounds", ImpactMatrix.class);
			model.profileMatrix = im;
			model.profileMatrix.addListener(model.impactListener);			
		}
		@Override
		public void write(SMAATRIModel model, OutputElement oe) throws XMLStreamException {
			oe.setAttribute("optimisticRule", model.getRule());			
			SMAAModel.XML.write(model, oe);
			oe.add(model.lambda, "lambda", Interval.class);
			oe.add(new CategoryList(model.categories), "categories", CategoryList.class);			
			oe.add(model.profileMatrix, "upperBounds", ImpactMatrix.class);
		}		
	};	
}
