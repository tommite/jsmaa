package fi.smaa.jsmaa.gui.presentation;

import java.util.ArrayList;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.SMAA2Model;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class AbstractLeftTreeModel<M extends SMAAModel> implements TreeModel {

	private ArrayList<TreeModelListener> treeModelListeners = new ArrayList<TreeModelListener>();
	protected M smaaModel;
	protected static final int ALTERNATIVES = 0;
	protected static final int CRITERIA = 1;
	protected String alternativesNode = "Alternatives";
	protected String criteriaNode = "Criteria";

	public AbstractLeftTreeModel(M model) {
		if (model == null) {
			throw new NullPointerException();
		}		
		this.smaaModel = model;
	}

	public Object getAlternativesNode() {
		return alternativesNode;
	}

	public Object getCriteriaNode() {
		return criteriaNode;
	}

	public Object getRoot() {
		return smaaModel;
	}

	public void removeTreeModelListener(TreeModelListener l) {
		treeModelListeners.remove(l);
	}

	public void addTreeModelListener(TreeModelListener l) {
		if (!treeModelListeners.contains(l)) {
			treeModelListeners.add(l);
		}
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
		Object obj = path.getLastPathComponent();

		if (obj instanceof Alternative) {
			if (!smaaModel.getAlternatives().contains(new Alternative((String)newValue))) {
				((Alternative) obj).setName((String) newValue);
			}
		} else if (obj instanceof Criterion) {
			if (!smaaModel.getCriteria().contains(new ScaleCriterion((String)newValue))) {			
				((Criterion) obj).setName((String) newValue);
			}
		} else if (obj instanceof SMAA2Model) {
			((SMAA2Model) obj).setName((String) newValue);
		}
	}

	protected void fireTreeChange() {
		for (TreeModelListener l : treeModelListeners) {
			l.treeStructureChanged( new TreeModelEvent(this, new Object[] { this.getRoot() }));
		}
	}

	public TreePath getPathForAlternative(Alternative alt) {
		return new TreePath(new Object[]{smaaModel, alternativesNode, alt});
	}

	public TreePath getPathForCriterion(Criterion c) {
		return new TreePath(new Object[]{smaaModel, criteriaNode, c});
	}

	@SuppressWarnings("unchecked")
	public Object getChild(Object parent, int index) {
		if (parent == getRoot()) {
			if (index == ALTERNATIVES) {
				return alternativesNode;
			} else if (index == CRITERIA) {
				return criteriaNode;
			}
		} else if (parent == alternativesNode) {
			return new ArrayList(smaaModel.getAlternatives()).get(index);
		} else if (parent == criteriaNode) {
			return new ArrayList(smaaModel.getCriteria()).get(index);
		}
		return null;
	}
	
	public int getChildCount(Object parent) {
		if (parent == getRoot()) {
			return 2;
		} else if (parent == alternativesNode) {
			return smaaModel.getAlternatives().size();
		} else if (parent == criteriaNode) {
			return smaaModel.getCriteria().size();
		}
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	public int getIndexOfChild(Object parent, Object child) {
		if (parent == getRoot()) {
			if (child == alternativesNode) {
				return ALTERNATIVES;
			} else if (child == criteriaNode) {
				return CRITERIA;
			}
		} else if (parent == alternativesNode) {
			if (child instanceof Alternative) {
				int index = new ArrayList(smaaModel.getAlternatives()).indexOf(child);
				if (index != -1) {
					return index;
				}
			}
		} else if (parent == criteriaNode) {
			if (child instanceof Criterion) {
				int index = new ArrayList(smaaModel.getCriteria()).indexOf(child);
				if (index != -1) {
					return index;
				}
			}			
		}
		return -1;
	}
	
	public boolean isLeaf(Object node) {
		if (node instanceof Alternative) {
			if (smaaModel.getAlternatives().contains(node)) {
				return true;
			}
		} else if (node instanceof Criterion) {
			if (smaaModel.getCriteria().contains(node)) {
				return true;
			}
		} else if (node == alternativesNode) {
			return false;
		} else if (node == criteriaNode) {
			return false;
		}
		return false;
	}

	public Object getModelNode() {
		return smaaModel;
	}
	
}