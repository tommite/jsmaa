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

package nl.rug.escher.common.gui;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class LayoutUtil {

	public static void addColumn(FormLayout layout) {
		layout.appendColumn(ColumnSpec.decode("3dlu"));
		layout.appendColumn(ColumnSpec.decode("pref"));
	}

	public static void addRow(FormLayout layout) {
		layout.appendRow(RowSpec.decode("3dlu"));
		layout.appendRow(RowSpec.decode("p"));
	}

}
