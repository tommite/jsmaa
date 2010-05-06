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

package fi.smaa.jsmaa.gui.presentation;

import javax.swing.JComponent;

import com.jgoodies.binding.value.ValueModel;

import fi.smaa.jsmaa.gui.components.IntervalValueModel;
import fi.smaa.jsmaa.model.Interval;

public class ConstrainedIntervalValueModel extends IntervalValueModel {

	private static final long serialVersionUID = 9056905598002559084L;
	private Interval range;
	private String message;
	
	public ConstrainedIntervalValueModel(JComponent parent,
			Interval interval, ValueModel subject, boolean start, Interval range, String message) {
		super(parent, interval, subject, start);
		this.range = range;
		this.message = message;
	}
	
	@Override
	public boolean proposedChange(Object oldVal, Object newVal) {
		if (!range.includes((Double)newVal)) {
			errorMessage(message + range);
			return false;
		}
		return super.proposedChange(oldVal, newVal);
	}

}
