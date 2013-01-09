/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013.

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
