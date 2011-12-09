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
package fi.smaa.common;

public class ValueIndexPair implements Comparable<ValueIndexPair> {
	public double value;
	public int altIndex;

	public ValueIndexPair(int altIndex, double value) {
		this.altIndex = altIndex;
		this.value = value;
	}

	public int compareTo(ValueIndexPair o) {
		if (this.value < o.value) {
			return 1;
		} else if (this.value > o.value) {
			return -1;
		}
		return 0;
	}	

	public String toString() {
		return altIndex + ": " + value;
	}
}
