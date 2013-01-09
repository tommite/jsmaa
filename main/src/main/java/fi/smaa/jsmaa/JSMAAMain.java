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
package fi.smaa.jsmaa;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.drugis.common.gui.GUIHelper;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.windows.WindowsLookAndFeel;

import fi.smaa.jsmaa.gui.FileNames;
import fi.smaa.jsmaa.gui.ImageFactory;
import fi.smaa.jsmaa.gui.JSMAAMainFrame;

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
		try {
			String osName = System.getProperty("os.name");

			if (osName.startsWith("Windows")) {
				UIManager.setLookAndFeel(new WindowsLookAndFeel());
			} else  if (osName.startsWith("Mac")) {
				// do nothing, use the Mac Aqua L&f
			} else {
				try {
					UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
				} catch (Exception e) {
					UIManager.setLookAndFeel(new PlasticLookAndFeel());
				}
			}
		} catch (Exception e) {
			// Likely the Looks library is not in the class path; ignore.
		}
		app = new JSMAAMainFrame(DefaultModels.getSMAA2Model());
		app.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				quitApplication();
			}
		});		
		app.setVisible(true);
		GUIHelper.centerWindow(app);
	}

	private void quitApplication() {
		if (!app.modelManager.getSaved()) {
			int conf = JOptionPane.showConfirmDialog(app, 
					"Model not saved. Do you want do save changes before quitting JSMAA?",
					"Save changed",					
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
					ImageFactory.IMAGELOADER.getIcon(FileNames.ICON_STOP));
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
