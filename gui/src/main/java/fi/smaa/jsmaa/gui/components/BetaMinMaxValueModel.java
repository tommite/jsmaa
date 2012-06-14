/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper 2012.

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
package fi.smaa.jsmaa.gui.components;

import javax.swing.JComponent;

import com.jgoodies.binding.value.ValueModel;

import fi.smaa.jsmaa.gui.presentation.MultiVetoableValueModel;
import fi.smaa.jsmaa.model.BetaMeasurement;

@SuppressWarnings("serial")
public class BetaMinMaxValueModel extends MultiVetoableValueModel{
	protected boolean start;
	protected BetaMeasurement meas;
	
	public BetaMinMaxValueModel(JComponent parent, BetaMeasurement meas, ValueModel subject, boolean start) {
		super(parent, subject);
		this.start = start;
		this.meas = meas;
	}

	@Override
	public boolean proposedChange(Object oldVal, Object newVal) {
		if (start) {
			if ((Double) newVal > meas.getMax()) {
				errorMessage("Beta min start has to be smaller than max");
				return false;
			}
		} else { // end
			if ((Double) newVal < meas.getMin()) {
				errorMessage("Beta max has to be larger than min");
				return false;
			}
		}
		return super.proposedChange(oldVal, newVal);
	}
}
