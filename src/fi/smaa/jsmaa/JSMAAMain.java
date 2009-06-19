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

package fi.smaa.jsmaa;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import nl.rug.escher.common.gui.GUIHelper;
import fi.smaa.jsmaa.gui.FileNames;
import fi.smaa.jsmaa.gui.JSMAAMainFrame;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.AlternativeExistsException;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.SMAAModel;

public class JSMAAMain {
	
	private JSMAAMainFrame app;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JSMAAMain main = new JSMAAMain();
		main.start();
	}
	
	private void start() {
		GUIHelper.initializeLookAndFeel();		
		app = new JSMAAMainFrame(buildDefaultModel());
		app.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				quitApplication();
			}
		});		
		app.setVisible(true);			
	}
	
	private static SMAAModel buildDefaultModel() {
		SMAAModel model = new SMAAModel("model");
		model.addCriterion(new CardinalCriterion("Criterion 1"));
		model.addCriterion(new CardinalCriterion("Criterion 2"));
		try {
			model.addAlternative(new Alternative("Alternative 1"));
			model.addAlternative(new Alternative("Alternative 2"));			
			model.addAlternative(new Alternative("Alternative 3"));
		} catch (AlternativeExistsException e) {
			e.printStackTrace();
		}
		return model;
	}
	
	private void quitApplication() {
		if (app.getModelUnsaved()) {
			int conf = JOptionPane.showConfirmDialog(app, 
					"Model not saved. Do you want do save changes before quitting JSMAA?",
					"Save changed",					
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
					app.getIcon(FileNames.ICON_STOP));
			if (conf == JOptionPane.CANCEL_OPTION) {
				return;
			} else if (conf == JOptionPane.YES_OPTION) {
				if (!app.save()) {
					return;
				}
			}
		}
		System.exit(0);
	}	

}
