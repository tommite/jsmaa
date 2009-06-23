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

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import fi.smaa.jsmaa.model.AbstractCriterion;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.SMAAModelListener;

@SuppressWarnings("unchecked")
public class LeftTreeModel implements TreeModel{
	private ArrayList<TreeModelListener> treeModelListeners = new ArrayList<TreeModelListener>();
	private SMAAModel smaaModel;
	
	private static final int ALTERNATIVES = 0;
	private static final int CRITERIA = 1;
	private static final int PREFERENCES = 2;
	private static final int RESULTS = 3;
	
	private String alternativesNode = "Alternatives";
	private String criteriaNode = "Criteria";
	private String resultsNode = "Results";
	private String rankAccNode = "RankAcc";
	private String centralWeightsNode = "CW";
	private String preferencesNode = "Preferences";
	
	public LeftTreeModel(SMAAModel smaaModel) throws NullPointerException {
		if (smaaModel == null) {
			throw new NullPointerException();
		}
		this.smaaModel = smaaModel;
		smaaModel.addModelListener(new SMAAModelListener() {
			public void alternativesChanged() {
				fireTreeChange();
			}
			public void criteriaChanged() {
				fireTreeChange();
			}
			public void measurementsChanged() {				
			}
			public void preferencesChanged() {
			}
			public void alternativeOrCriteriaNameChanged() {
			}			
		});
	}
	
	public void setSMAAModel(SMAAModel model) {
		this.smaaModel = model;
		fireTreeChange();
	}
	
	public Object getModelNode() {
		return smaaModel;
	}
	
	public Object getAlternativesNode() {
		return alternativesNode;
	}
	
	public Object getCriteriaNode() {
		return criteriaNode;
	}
	
	public Object getResultsNode() {
		return resultsNode;
	}
	
	public Object getCentralWeightsNode() {
		return centralWeightsNode;
	}
	
	public Object getRankAcceptabilitiesNode() {
		return rankAccNode;
	}
	
	public Object getPreferencesNode() {
		return preferencesNode;
	}
	
	public TreePath getPathForAlternative(Alternative alt) {
		return new TreePath(new Object[]{smaaModel, alternativesNode, alt});
	}
	
	public TreePath getPathForCriterion(Criterion c) {
		return new TreePath(new Object[]{smaaModel, criteriaNode, c});
	}

	public Object getChild(Object parent, int index) {
		if (parent == getRoot()) {
			if (index == ALTERNATIVES) {
				return alternativesNode;
			} else if (index == CRITERIA) {
				return criteriaNode;
			} else if (index == RESULTS) {
				return resultsNode;
			} else if (index == PREFERENCES) {
				return preferencesNode;
			}
		} else if (parent == alternativesNode) {
			return new ArrayList(smaaModel.getAlternatives()).get(index);
		} else if (parent == criteriaNode) {
			return new ArrayList(smaaModel.getCriteria()).get(index);
		} else if (parent == resultsNode) {
			if (index == 0) {
				return rankAccNode;
			} else if (index == 1) {
				return centralWeightsNode;
			}
		}
		return null;
	}

	public int getChildCount(Object parent) {
		if (parent == getRoot()) {
			return 4;
		} else if (parent == alternativesNode) {
			return smaaModel.getAlternatives().size();
		} else if (parent == criteriaNode) {
			return smaaModel.getCriteria().size();
		} else if (parent == resultsNode) {
			return 2;
		}
		return 0;
	}

	public int getIndexOfChild(Object parent, Object child) {
		if (parent == getRoot()) {
			if (child == alternativesNode) {
				return ALTERNATIVES;
			} else if (child == criteriaNode) {
				return CRITERIA;
			} else if (child == resultsNode) {
				return RESULTS;
			} else if (child == preferencesNode) {
				return PREFERENCES;
			}
		} else if (parent == alternativesNode) {
			if (child instanceof Alternative) {
				int index = new ArrayList(smaaModel.getAlternatives()).indexOf(child);
				if (index != -1) {
					return index;
				}
			}
		} else if (parent == criteriaNode) {
			if (child instanceof AbstractCriterion) {
				int index = new ArrayList(smaaModel.getCriteria()).indexOf(child);
				if (index != -1) {
					return index;
				}
			}			
		} else if (parent == resultsNode) {
			if (child == rankAccNode) {
				return 0;
			} else if (child == centralWeightsNode) {
				return 1;
			}
		}
		return 0;
	}

	public Object getRoot() {
		return smaaModel;
	}

	public boolean isLeaf(Object node) {
		if (node instanceof Alternative) {
			if (smaaModel.getAlternatives().contains(node)) {
				return true;
			}
		} else if (node instanceof AbstractCriterion) {
			if (smaaModel.getCriteria().contains(node)) {
				return true;
			}
		} else if (node == alternativesNode) {
			return false;
		} else if (node == criteriaNode) {
			return false;
		} else if (node == resultsNode) {
			return false;
		} else if (node == rankAccNode) {
			return true;
		} else if (node == centralWeightsNode) {
			return true;
		} else if (node == preferencesNode) {
			return true;
		}
		return false;
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
		} else if (obj instanceof AbstractCriterion) {
			if (!smaaModel.getCriteria().contains(new CardinalCriterion((String)newValue))) {			
				((AbstractCriterion) obj).setName((String) newValue);
			}
		} else if (obj instanceof SMAAModel) {
			((SMAAModel) obj).setName((String) newValue);
		}
	}

	private void fireTreeChange() {
		for (TreeModelListener l : treeModelListeners) {
			l.treeStructureChanged( new TreeModelEvent(this, new Object[] { this.getRoot() }));
		}
	}
}
