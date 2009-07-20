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

package fi.smaa.jsmaa.gui;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.jgoodies.binding.value.AbstractVetoableValueModel;
import com.jgoodies.binding.value.ValueModel;

import fi.smaa.jsmaa.model.Interval;

public class IntervalValueModel extends AbstractVetoableValueModel {
	
	private static final long serialVersionUID = -5651105205219639989L;
	protected boolean start;
	private Interval interval;
	private JComponent parent;
	private static final String INPUT_ERROR = "Input error";	
	
	protected IntervalValueModel(JComponent parent, Interval interval, ValueModel subject, boolean start) {
		super(subject);
		this.start = start;
		this.interval = interval;
		this.parent = parent;
	}

	@Override
	public boolean proposedChange(Object oldVal, Object newVal) {
		if (start) {
			if ((Double) newVal > interval.getEnd()) {
				errorMessage("Interval [start, end]: start has to be smaller than end");
				return false;
			}
		} else { // end
			if ((Double) newVal < interval.getStart()) {
				errorMessage("Interval [start, end]: end has to be larger than start");
				return false;
			}
		}
		return true;
	}
	
	protected void errorMessage(String msg) {
		JOptionPane.showMessageDialog(parent, msg, INPUT_ERROR, JOptionPane.ERROR_MESSAGE);		
	}
}
