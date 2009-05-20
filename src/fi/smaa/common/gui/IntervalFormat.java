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

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

import fi.smaa.common.Interval;


public class IntervalFormat extends Format {

	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo,
			FieldPosition pos) {
		Interval interval = (Interval) obj;
		if (interval != null) {
			toAppendTo.append(interval.toString());
		}
		// TODO fix to be "correct"
		return toAppendTo;
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		// TODO implement
		return null;
	}

}
