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

package fi.smaa;

import com.jgoodies.binding.beans.Model;

public class Alternative extends Model {
	private String name;
	
	public final static String PROPERTY_NAME = "name";
	
	public Alternative(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String _name) {
		String oldVal = this.name;
		this.name = _name;
		firePropertyChange(PROPERTY_NAME, oldVal, _name);
	}	
			
	@Override
	public String toString() {
		return name;
	}
	
	public boolean deepEquals(Alternative other) {
		return name.equals(other.name);
	}
	
	public Alternative deepCopy() {
		return new Alternative(name);
	}

}