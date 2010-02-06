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

package fi.smaa.jsmaa.gui.presentation;

import java.util.ArrayList;

import javax.swing.tree.TreePath;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.ModelChangeEvent;
import fi.smaa.jsmaa.model.SMAAModelListener;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class LeftTreeModelSMAATRI extends LeftTreeModelMCDAModel<SMAATRIModel> {
	
	private static int CATEGORIES = 2;
	protected static final int PREFERENCES = 3;
	private String categoriesNode = "Categories";
	private String categoryAcceptabilitiesNode = "CatAcc";

	public LeftTreeModelSMAATRI(SMAATRIModel smaaModel) throws NullPointerException {
		super(smaaModel);
		
		smaaModel.addModelListener(new SMAAModelListener() {
			public void modelChanged(ModelChangeEvent ev) {
				if (ev.getType() == ModelChangeEvent.CATEGORIES) {
					fireTreeChange();
				}
			}
		});		
	}
	
	@Override
	protected int getResultsIndex() {
		return 4;
	}
	
	public Object getCategoriesNode() {
		return categoriesNode;
	}
	
	public Object getCatAccNode() {
		return categoryAcceptabilitiesNode;
	}
	
	public TreePath getPathForCategory(Alternative cat) {
		return new TreePath(new Object[]{smaaModel, categoriesNode, cat});
	}	
	
	@Override
	public Object getChild(Object parent, int index) {
		if (parent == getRoot()) {
			if (index == CATEGORIES) {
				return categoriesNode;
			} else if (index == PREFERENCES) {
				return preferencesNode;
			}
		} else if (parent == categoriesNode) {
			return smaaModel.getCategories().get(index);
		} else if (parent == resultsNode) {
			return categoryAcceptabilitiesNode;
		}
		return super.getChild(parent, index);
	}
	
	@Override
	public int getChildCount(Object parent) {
		if (parent == getRoot()) {
			return 5;
		} else if (parent == categoriesNode) {			
			return ((SMAATRIModel) smaaModel).getCategories().size();
		} else if (parent == resultsNode) {
			return 1;
		} 
		return super.getChildCount(parent);
	}
	
	@Override
	public boolean isLeaf(Object node) {
		if (node instanceof Alternative && smaaModel.getCategories().contains(node)) {
			return true;
		} else if (node == categoryAcceptabilitiesNode) {
			return true;
		}
		return super.isLeaf(node);
	}
	
	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		Object obj = path.getLastPathComponent();
		
		if (obj instanceof Alternative) {
			if (smaaModel.getCategories().contains(obj)) {
				((Alternative) obj).setName((String) newValue);				
				return;
			}
		}
		super.valueForPathChanged(path, newValue);
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (parent == categoriesNode) {
			if (child instanceof Alternative) {
				int index = new ArrayList<Alternative>(smaaModel.getCategories()).indexOf(child);
				if (index != -1) {
					return index;
				}
			}
		} else if (parent == getRoot()) {
			if (child == categoriesNode) {
				return CATEGORIES;
			} else if (child == preferencesNode) {
				return PREFERENCES;
			}
		} else if (parent == getResultsNode()) {
			if (child == categoryAcceptabilitiesNode) {
				return 0;
			}
		}
		return super.getIndexOfChild(parent, child);
	}
}
