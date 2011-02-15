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
package fi.smaa.jsmaa.gui;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;

import fi.smaa.jsmaa.gui.presentation.LeftTreeModel;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModelSMAATRI;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.SMAATRIModel;

@SuppressWarnings("serial")
public class LeftTreeTransferHandler extends TransferHandler {
	
	private LeftTreeModel model;
	private SMAAModel smaaModel;

	public LeftTreeTransferHandler(LeftTreeModel model, SMAAModel smaaModel) {
		this.model = model;
		this.smaaModel = smaaModel;
	}
	
	@Override
	public boolean canImport(TransferSupport ts) {
		if (!isLeftTree(ts.getComponent())) {
			return false;
		}			
		if (!ts.isDrop()) {
			return false;
		}
		
	    DropLocation loc = ts.getDropLocation();
		
	    if (loc instanceof JTree.DropLocation) {
	    	if (isMovableNode((JTree.DropLocation)loc)) {
	    		try {
	    			Object o = ts.getTransferable().getTransferData(new DataFlavor(Object.class, ""));
	    			Object targetNode = ((JTree.DropLocation) loc).getPath().getLastPathComponent();
	    			if (o instanceof Criterion) {
	    				return targetNode == model.getCriteriaNode();
	    			} else if (o instanceof Alternative && smaaModel.getAlternatives().contains(o)) {
	    				return targetNode == model.getAlternativesNode();
	    			} else { //category
	    				return targetNode == ((LeftTreeModelSMAATRI)model).getCategoriesNode();
	    			}
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	    	}
	    }
		return false;
	}
	
	private boolean isMovableNode(JTree.DropLocation jdl) {
		Object lpc = jdl.getPath().getLastPathComponent();
		if (!lpc.equals(model.getCriteriaNode()) && !lpc.equals(model.getAlternativesNode())) {
			if (model instanceof LeftTreeModelSMAATRI) {
				if (!lpc.equals(((LeftTreeModelSMAATRI)model).getCategoriesNode())) {
					return false;
				}
			} else {
				return false;
			}
		}
		if (jdl.getChildIndex() < 0) {
			return false;
		}
		return true;
	}

	@Override
	public int getSourceActions(JComponent c) {
	    return MOVE;
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		if (!isLeftTree(c)) {
			return null;
		}
		Object lastPathComponent = ((JTree) c).getSelectionPath().getLastPathComponent();
		if (!(lastPathComponent instanceof Criterion) && !(lastPathComponent instanceof Alternative)) {
			return null;
		}
		return new TransferableObject(lastPathComponent);
	}
	
	@Override
	public boolean importData(TransferSupport t) {
		if (!t.isDrop()) {
			return false;
		}
		try {
			Object o = t.getTransferable().getTransferData(new DataFlavor(Object.class, ""));
			JTree.DropLocation jdl = (JTree.DropLocation) t.getDropLocation();			
			if (o instanceof Criterion) {
				Criterion c = (Criterion) o;
				moveCriterion(c, jdl.getChildIndex());
				return true;
			} else if (o instanceof Alternative && smaaModel.getAlternatives().contains(o)) {
				Alternative a = (Alternative) o;
				moveAlternative(a, jdl.getChildIndex());
				return true;
			} else if (o instanceof Alternative) { // has to be category
				Alternative a = (Alternative) o;
				moveCategory(a, jdl.getChildIndex());
				return true;				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;		
	}
		
	private boolean isLeftTree(Component component) {
		if (component instanceof JTree) {
			JTree jtree = (JTree) component;
			if (jtree.getModel() instanceof LeftTreeModel) {
				return true;
			}
		}
		return false;
	}
	
	public void moveCriterion(Criterion toMove, int newIndex) {
		int oldIndex = smaaModel.getCriteria().indexOf(toMove);
		if (oldIndex == newIndex) {
			return;
		}		
		List<Criterion> newCrit = new ArrayList<Criterion>(smaaModel.getCriteria());
		swap(newCrit, newIndex, oldIndex);
		smaaModel.reorderCriteria(newCrit);
	}
	
	public void moveAlternative(Alternative toMove, int newIndex) {
		int oldIndex = smaaModel.getAlternatives().indexOf(toMove);
		if (oldIndex == newIndex) {
			return;
		}		
		List<Alternative> newAlts = new ArrayList<Alternative>(smaaModel.getAlternatives());
		swap(newAlts, newIndex, oldIndex);
		smaaModel.reorderAlternatives(newAlts);
	}	

	
	public void moveCategory(Alternative toMove, int newIndex) {
		if (!(smaaModel instanceof SMAATRIModel)) {
			return;
		}
		int oldIndex = ((SMAATRIModel)smaaModel).getCategories().indexOf(toMove);
		if (oldIndex == newIndex) {
			return;
		}
		List<Alternative> newAlts = new ArrayList<Alternative>(((SMAATRIModel)smaaModel).getCategories());
		swap(newAlts, newIndex, oldIndex);
		((SMAATRIModel)smaaModel).reorderCategories(newAlts);
	}
	
	private <T> void swap(List<T> list, int index1, int index2) {
		T toMove1 = list.get(index1);
		T toMove2 = list.get(index2);
		list.set(index2, toMove1);
		list.set(index1, toMove2);		
	}
}
