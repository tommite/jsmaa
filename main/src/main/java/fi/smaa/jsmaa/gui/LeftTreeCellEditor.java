/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013-2015.

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

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Category;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class LeftTreeCellEditor extends DefaultTreeCellEditor {

	private ArrayList<String> oldNames = new ArrayList<String>();
	private String oldName;
	private SMAAModel model;
	JTree tree;
	private Object editObject;

	public LeftTreeCellEditor(SMAAModel model, JTree tree, DefaultTreeCellRenderer renderer) {
		super(tree, renderer);
		this.model = model;
		this.tree = tree;
		
		addCellEditorListener(new MyCellEditorListener());
	}
		
	private class MyCellEditorListener implements CellEditorListener {
		public void editingCanceled(ChangeEvent e) {
			validateEditing();					
		}

		public void editingStopped(ChangeEvent e) {
			validateEditing();
		}

		private void validateEditing() {
			String newName = (String) getCellEditorValue();
			//Object editObject = lastPath.getLastPathComponent();

			if (!isValidName(newName)) {
				String objName = "object";
				if (editObject instanceof Category) {
					objName = "a category";
				} else if (editObject instanceof Alternative) {
					objName = "an alternative";
				} else if (editObject instanceof Criterion) {
					objName = "a criterion";
				}
				showErrorObjectExists(newName, objName);
				tree.startEditingAtPath(lastPath);							
			}
		}
	}
		
	private void showErrorObjectExists(String name, String objName) {
		JOptionPane.showMessageDialog(tree, "There exists " + objName + " with name: " + name 
				+ ", input another one.", "Input error", JOptionPane.ERROR_MESSAGE);
	}	

	private boolean isValidName(String name) {
		return !oldNames.contains(name) || name.equals(oldName);
	}

	@Override
	public void prepareForEditing() {
		oldNames.clear();
		editObject = lastPath.getLastPathComponent();
		if (editObject instanceof Category) {
			Category cat = (Category) editObject;
			oldName = cat.getName();
			for (Category a : ((SMAATRIModel) model).getCategories()) {
				oldNames.add(a.getName());
			}
		} else if (editObject instanceof Alternative) {
			Alternative alt = (Alternative) editObject;
			oldName = alt.getName();
			for (Alternative a : model.getAlternatives()) {
				oldNames.add(a.getName());
			}
		} else if (editObject instanceof Criterion) {
			oldName = ((Criterion) editObject).getName();				
			for (Criterion c : model.getCriteria()) {
				oldNames.add(c.getName());
			}
		}
		super.prepareForEditing();
	}
}
