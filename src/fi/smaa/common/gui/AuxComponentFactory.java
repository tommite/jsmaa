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

import javax.swing.JComboBox;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.AbstractValueModel;

public class AuxComponentFactory {

	public static <T> JComboBox createBoundComboBox(T[] values, AbstractValueModel model) {
		SelectionInList<T> typeSelectionInList =
			new SelectionInList<T>(values, model);
		JComboBox type = BasicComponentFactory.createComboBox(typeSelectionInList);
		return type;
	}

}
