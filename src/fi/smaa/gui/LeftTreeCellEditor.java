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

package fi.smaa.gui;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;

import fi.smaa.Alternative;
import fi.smaa.Criterion;
import fi.smaa.SMAAModel;

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

			if (editObject instanceof Alternative) {
				if (!isValidName(newName)) {
					showErrorAlternativeExists(newName);
					tree.startEditingAtPath(lastPath);							
				}
			} else if (editObject instanceof Criterion) {
				if (!isValidName(newName)) {
					showErrorCriterionExists(newName);
					tree.startEditingAtPath(lastPath);
				}
			}
		}
	}
	
	private void showErrorCriterionExists(String name) {
		JOptionPane.showMessageDialog(tree, "There exists a criterion with name: " + name 
				+ ", input another one.", "Input error", JOptionPane.ERROR_MESSAGE);		
	}					
	
	private void showErrorAlternativeExists(String name) {
		JOptionPane.showMessageDialog(tree, "There exists an alternative with name: " + name 
				+ ", input another one.", "Input error", JOptionPane.ERROR_MESSAGE);
	}	

	private boolean isValidName(String name) {
		return !oldNames.contains(name) || name.equals(oldName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void prepareForEditing() {
		oldNames.clear();
		editObject = lastPath.getLastPathComponent();
		if (editObject instanceof Alternative) {
			oldName = ((Alternative) editObject).getName();
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
