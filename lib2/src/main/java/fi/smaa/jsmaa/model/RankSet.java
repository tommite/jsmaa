package fi.smaa.jsmaa.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RankSet<T extends DeepCopiable<T>> implements DeepCopiable<RankSet<T>>, Serializable {

	private static final long serialVersionUID = -2783389914146035900L;
	private transient RankListener rankListener = new RankListener();	
	private Map<T, Rank> map = new HashMap<T, Rank>();
	
	public RankSet() {
	}
	
	public RankSet(Map<T, Rank> map) {
		for (int i=1;i<=map.entrySet().size();i++) {
			if (!map.values().contains(new Rank(i))) {
				throw new IllegalArgumentException("map doesn't contain all ranks 1,...,n");
			}
		}
		this.map = map;
		connectRankListeners();
	}

	private void connectRankListeners() {
		for (Rank r : map.values()) {
			r.addPropertyChangeListener(rankListener);
		}
	}
	
	public void addObject(T o) {
		if (!map.containsKey(o)) {
			map.put(o, createNextAvailableRank());
		}
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		rankListener = new RankListener();		
		connectRankListeners();
	}
	
	private Rank createNextAvailableRank() {
		int maxRank = 0;
		for (Rank r : map.values()) {
			int rn = r.getRank();
			if (rn > maxRank) {
				maxRank = rn;
			}
		}
		Rank newRank = new Rank(maxRank + 1);
		newRank.addPropertyChangeListener(rankListener);
		return newRank;
	}
		
	public Set<T> getObjects() {
		return map.keySet();
	}

	public Rank getRank(T o) {
		return map.get(o);
	}
	
	public void deleteObject(T o) {
		if (map.containsKey(o)) {
			Rank r = map.get(o);
			map.remove(o);
			shiftDownOneFrom(r.getRank());
		}
	}
			
	private void shiftDownOneFrom(int rank) {
		for (Rank r : map.values()) {
			r.removePropertyChangeListener(rankListener);
		}
		for (Rank r : map.values()) {
			int oRank = r.getRank();
			if (oRank > rank) {
				r.setRank(oRank - 1);
			}
		}
		for (Rank r : map.values()) {
			r.addPropertyChangeListener(rankListener);
		}		
	}

	private class RankListener implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(Rank.PROPERTY_RANK)) {
				Rank r = (Rank) evt.getSource();
				int oldVal = (Integer) evt.getOldValue();
				int newVal = (Integer) evt.getNewValue();
				if (oldVal != newVal) {
					ensureRanks(r, oldVal, newVal);
				}
			}
		}		
	}

	private void ensureRanks(Rank r, int oldVal, int newVal) {
		for (Rank ra : map.values()) {
			if (ra.getRank() == newVal && ra != r) {
				ra.setRank(oldVal);
			}
		}
	}

	public RankSet<T> deepCopy() {
		RankSet<T> newSet = new RankSet<T>();
		Map<T, Rank> newMap = new HashMap<T, Rank>();
		for (T t : map.keySet()) {
			Rank r = map.get(t);
			newMap.put(t, r.deepCopy());
		}
		newSet.map = newMap;
		for (Rank r : newMap.values()) {
			r.addPropertyChangeListener(newSet.rankListener);
		}
		return newSet;
	}
}