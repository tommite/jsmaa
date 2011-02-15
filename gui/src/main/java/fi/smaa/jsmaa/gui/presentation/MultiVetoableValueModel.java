/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.

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
package fi.smaa.jsmaa.gui.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.jgoodies.binding.value.AbstractVetoableValueModel;
import com.jgoodies.binding.value.ValueModel;

@SuppressWarnings("serial")
public abstract class MultiVetoableValueModel extends AbstractVetoableValueModel {
	
	private List<Vetoer> vetoers;
	private JComponent parent;
	protected static final String INPUT_ERROR = "Input error";		

	public MultiVetoableValueModel(JComponent parent, ValueModel subject) {
		super(subject);
		this.parent = parent;
		vetoers = new ArrayList<Vetoer>();
	}
	
	public void addVetoer(Vetoer vetoer) {
		vetoers.add(vetoer);
	}
	
	@Override
	public boolean proposedChange(Object oldVal, Object newVal) {
		for (Vetoer v : vetoers) {
			if (!v.check(oldVal, newVal)) {
				if (v.getErrorMessage(newVal) != null) {
					errorMessage(v.getErrorMessage(newVal));
				}
				return false;
			}
		}
		return true;
	}

	protected void errorMessage(String msg) {
		JOptionPane.showMessageDialog(parent, msg, INPUT_ERROR, JOptionPane.ERROR_MESSAGE);		
	}
}