package fi.smaa;

import com.jgoodies.binding.beans.Model;

public class Rank extends Model {
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
}
