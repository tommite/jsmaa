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


public class Rank extends Measurement {
	private Integer rank;
	
	public final static String PROPERTY_RANK = "rank";
	
	public Rank(Integer rank) {
		if (rank < 1) {
			throw new IllegalArgumentException();
		}
		this.rank = rank;
	}
	
	public Integer getRank() {
		return rank;
	}
	
	public void setRank(Integer rank) {
		if (rank < 1) {
			throw new IllegalArgumentException();
		}
		Object oldVal = this.rank;
		this.rank = rank;
		firePropertyChange(PROPERTY_RANK, oldVal, this.rank);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof Rank)) {
			return false;
		}
		Rank or = (Rank) other;
		return getRank() == or.getRank();
	}
	
	@Override
	public String toString() {
		return rank.toString();
	}

	public Object deepCopy() {
		return new Rank(rank);
	}
}
