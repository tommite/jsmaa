package fi.smaa.jsmaa.gui.presentation;

import fi.smaa.jsmaa.model.AbstractSMAAModel;
import fi.smaa.jsmaa.model.ModelChangeEvent;
import fi.smaa.jsmaa.model.SMAAModelListener;

public class LeftTreeModelMCDAModel<M extends AbstractSMAAModel> extends AbstractLeftTreeModel<M> {

	protected static final int PREFERENCES = 2;
	protected static final int RESULTS = 3;
	protected String resultsNode = "Results";
	protected String preferencesNode = "Preferences";

	public LeftTreeModelMCDAModel(M model) {
		super(model);
		smaaModel.addModelListener(new SMAAModelListener() {
			public void modelChanged(ModelChangeEvent ev) {
				if (ev.getType() == ModelChangeEvent.ALTERNATIVES ||
						ev.getType() == ModelChangeEvent.CRITERIA) {
					fireTreeChange();
				}
			}
		});		
	}

	public Object getResultsNode() {
		return resultsNode;
	}

	public Object getPreferencesNode() {
		return preferencesNode;
	}
	
	@Override
	public Object getChild(Object parent, int index) {
		if (parent == getRoot()) {
			if (index == RESULTS) {
				return resultsNode;
			} else if (index == PREFERENCES) {
				return preferencesNode;
			}
		}
		return super.getChild(parent, index);
	}	
	
	@Override
	public int getChildCount(Object parent) {
		if (parent == getRoot()) {
			return 4;
		} else if (parent == resultsNode) {
			return 2;
		}
		return super.getChildCount(parent);
	}
	
	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (parent == getRoot()) {
			if (child == resultsNode) {
				return RESULTS;
			} else if (child == preferencesNode) {
				return PREFERENCES;
			}
		}
		return super.getIndexOfChild(parent, child);
	}
	
	@Override
	public boolean isLeaf(Object node) {
		if (node == resultsNode) {
			return false;
		} else if (node == preferencesNode) {
			return true;
		}
		return super.isLeaf(node);
	}	
	
}