package fi.smaa.jsmaa.gui;

import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModel;
import fi.smaa.jsmaa.gui.presentation.PreferencePresentationModel;
import fi.smaa.jsmaa.gui.views.AlternativeInfoView;
import fi.smaa.jsmaa.gui.views.AlternativeView;
import fi.smaa.jsmaa.gui.views.CriteriaListView;
import fi.smaa.jsmaa.gui.views.CriterionView;
import fi.smaa.jsmaa.gui.views.PreferenceInformationView;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.SMAAModel;

public class AbstractViewFactory<T extends LeftTreeModel, M extends SMAAModel> implements ViewFactory{

	protected T treeModel;
	protected M smaaModel;
	
	protected AbstractViewFactory(T treeModel, M smaaModel) {
		this.treeModel = treeModel;
		this.smaaModel = smaaModel;
	}
	
	public ViewBuilder getView(Object o) {
		if (o == treeModel.getAlternativesNode()) {
			return new AlternativeInfoView(smaaModel.getAlternatives(), "Alternatives");			
		} else if (o == treeModel.getCriteriaNode()){
			return new CriteriaListView(smaaModel);
		} else if (o instanceof Criterion) {
			return new CriterionView(((Criterion)o), smaaModel);
		} else if (o instanceof Alternative) {
			return new AlternativeView((Alternative) o);
		} else if (o == treeModel.getPreferencesNode()) {
			return new PreferenceInformationView(new PreferencePresentationModel(smaaModel));
		} else {
			throw new IllegalArgumentException("no view known for object "+ o);
		}	
	}
}
