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

package fi.smaa.jsmaa.maut;

public class UtilIndexPair implements Comparable<UtilIndexPair> {
	public double util;
	public int altIndex;

	public UtilIndexPair(int altIndex, double util) {
		this.altIndex = altIndex;
		this.util = util;
	}

	public int compareTo(UtilIndexPair o) {
		if (this.util < o.util) {
			return 1;
		} else if (this.util > o.util) {
			return -1;
		}
		return 0;
	}	

	public String toString() {
		return altIndex + ": " + util;
	}
}
