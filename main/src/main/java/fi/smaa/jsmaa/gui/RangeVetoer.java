/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.

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

import fi.smaa.jsmaa.gui.presentation.Vetoer;
import fi.smaa.jsmaa.model.CardinalMeasurement;

public class RangeVetoer implements Vetoer {
	
	private CardinalMeasurement range;
	private String errorString;
	private boolean above;
	
	public RangeVetoer(CardinalMeasurement range, String errorString, boolean above) {
		this.range = range;
		this.errorString = errorString;
		this.above = above;
	}

	public boolean check(Object oldValue, Object newValue) {
		if (!above) {
			if ((Double) newValue > range.getRange().getStart()) {
				return false;
			}
		} else {
			if ((Double) newValue < range.getRange().getEnd()) {
				return false;
			}			
		}
		return true;
	}
	
	public String getErrorMessage(Object newVal) {
		return errorString;
	}
}
