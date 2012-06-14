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
package fi.smaa.jsmaa.model;

import java.util.EventObject;

@SuppressWarnings("serial")
public class ModelChangeEvent extends EventObject {
	public static final int CRITERIA = 1;
	public static final int ALTERNATIVES = 2;
	public static final int MEASUREMENT_TYPE = 3;
	public static final int PREFERENCES = 4;
	public static final int PREFERENCES_TYPE = 5;
	public static final int MEASUREMENT = 6;
	public static final int CATEGORIES = 7;
	public static final int PARAMETER = 8;

	private int type;
	
	public ModelChangeEvent(Object source, int type) {
		super(source);
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
}
