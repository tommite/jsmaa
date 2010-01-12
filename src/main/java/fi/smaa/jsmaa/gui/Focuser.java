package fi.smaa.jsmaa.gui;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import fi.smaa.jsmaa.gui.presentation.LeftTreeModel;

public class Focuser {
	
	public static void focus(JTree tree, LeftTreeModel model, Object o) {
		if (model.getIndexOfChild(model.getRoot(), o) != -1) {
			tree.setSelectionPath(new TreePath(new Object[] {model.getRoot(), o }));
		} else if (model.getIndexOfChild(model.getResultsNode(), o) != -1){
			tree.setSelectionPath(new TreePath(new Object[] {model.getRoot(), 
				model.getResultsNode(), o}));	
		} else {
			throw new IllegalArgumentException("cannot focus " + o);
		}
	}

}
