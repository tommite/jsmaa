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

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ImageLoader {
	
	public static final String IMAGE_PATH = "/gfx/";
	private Map<String, Icon> icons;
	public static final String ICON_ORDINALCRITERION = "criterion_ordinal.gif";
	public static final String ICON_GAUSSIANCRITERION = "criterion_gaussian.gif";
	public static final String ICON_UNIFORMCRITERION = "criterion_uniform.gif";
	public static final String ICON_ALTERNATIVE = "alternative.gif";
	public static final String ICON_RANKACCEPTABILITIES = "rankacceptabilities.gif";
	public static final String ICON_CENTRALWEIGHTS = "centralweights.gif";
	public static final String ICON_DELETE = "delete.gif";
	public static final String ICON_RENAME = "rename.gif";
	public static final String ICON_ADD = "add.gif";
	public static final String ICON_CRITERIALIST = "criterialist.gif";
	public static final String ICON_OPENFILE = "openfile.gif";
	public static final String ICON_SAVEFILE = "savefile.gif";
	public static final String ICON_STOP = "stop.gif";
	
	public ImageLoader() {
		icons = new HashMap<String, Icon>();
	}
	
	public Icon getIcon(String name) throws FileNotFoundException {
		if (icons.containsKey(name)) {
			return icons.get(name);
		} else {
		    java.net.URL imgURL = getClass().getResource(deriveGfxPath(name));
		    if (imgURL == null) {
		    	throw new FileNotFoundException("File not found for icon " + deriveGfxPath(name));
		    }
		    ImageIcon icon = new ImageIcon(imgURL);
	        icons.put(name, icon);
	        return icon;
		}
	}

	private String deriveGfxPath(String name) {
		return IMAGE_PATH + name;
	}
}
