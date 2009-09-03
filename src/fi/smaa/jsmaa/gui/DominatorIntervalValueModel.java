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

import com.jgoodies.binding.value.ValueModel;

import fi.smaa.jsmaa.model.Interval;

@SuppressWarnings("serial")
public class DominatorIntervalValueModel extends IntervalValueModel {

	private Interval dominated;
	private String dominatorText;

	protected DominatorIntervalValueModel(JComponent parent, Interval interval,
			ValueModel subject, boolean start, Interval dominated, String dominatorText) {
		super(parent, interval, subject, start);
		this.dominated = dominated;
		this.dominatorText = dominatorText;
	}

	@Override
	public boolean proposedChange(Object oldVal, Object newVal) {
		if (!super.proposedChange(oldVal, newVal)) {
			return false;
		}
		if (start) {
			if ((Double) newVal < dominated.getEnd()) {
				errorMessage(dominatorText);
				return false;
			}
		}
		return true;
	}

}
