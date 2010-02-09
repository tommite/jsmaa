package fi.smaa.jsmaa.gui.components;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class Focuser {
	
	public static void focus(JTree tree, TreeModel model, Object o) {
		TreePath tp = null;
		if (o == model.getRoot()) { // ROOT
			tp = new TreePath(new Object[]{o});			
		} else if (model.getIndexOfChild(model.getRoot(), o) != -1) { // DIRECT CHILD OF ROOT
			tp = new TreePath(new Object[] {model.getRoot(), o });
		} else { // OTHER
			// go through all children of root
			for (int i=0;i<model.getChildCount(model.getRoot());i++) {
				Object parent = model.getChild(model.getRoot(), i);
				if (model.getIndexOfChild(parent, o) != -1) {
					tp = new TreePath(new Object[] {model.getRoot(), parent, o });
					break;
				}
			}
		}
		tree.setSelectionPath(tp);		
		expandTree(tree);
	}

	private static void expandTree(JTree tree) {
		for (int i=0;i<tree.getRowCount();i++) {
			tree.expandRow(i);
		}
	}
}
