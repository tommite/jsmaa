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

package fi.smaa.common.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.UIManager;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.windows.WindowsLookAndFeel;

public class GUIHelper {

	public static void initializeLookAndFeel() {
		try {
			String osName = System.getProperty("os.name");
			
			if (osName.startsWith("Windows")) {
				UIManager.setLookAndFeel(new WindowsLookAndFeel());
			} else  if (osName.startsWith("Mac")) {
				// do nothing, use the Mac Aqua L&f
			} else {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
				} catch (Exception e) {
					UIManager.setLookAndFeel(new PlasticLookAndFeel());
				}
			}
		} catch (Exception e) {
			// Likely the Looks library is not in the class path; ignore.
		}
	}

	/**
	 * Center window on screen.
	 * @param window to center
	 */
	public static void centerWindow(Window window) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension fsize = window.getSize();
		int xLoc = (int) ((screenSize.getWidth() / 2) - (fsize.getWidth() / 2));
		int yLoc = (int) ((screenSize.getHeight() / 2) - (fsize.getHeight() / 2));
		window.setLocation(new Point(xLoc, yLoc));
	}

	/**
	 * Center window on another window.
	 * @param window to center
	 */
	public static void centerWindow(Window window, Window parent) {
		Point parentLocation = parent.getLocation();
		Dimension parentDim = parent.getSize();
		Dimension fsize = window.getSize();
		int xLoc = (int) parentLocation.getX() + (int) ((parentDim.getWidth() / 2) - (fsize.getWidth() / 2));
		int yLoc = (int) parentLocation.getY() + (int) ((parentDim.getHeight() / 2) - (fsize.getHeight() / 2));
		window.setLocation(new Point(xLoc, yLoc));
	}
	
}
