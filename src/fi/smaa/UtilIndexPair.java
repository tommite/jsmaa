package fi.smaa;

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
