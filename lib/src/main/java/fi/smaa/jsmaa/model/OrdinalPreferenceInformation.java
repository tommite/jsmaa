/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013-2015.

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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import fi.smaa.common.RandomUtil;
import fi.smaa.jsmaa.model.xml.CriterionMeasurementPair;

public class OrdinalPreferenceInformation extends AbstractPreferenceInformation<Rank> {
	
	private static final long serialVersionUID = -8011596971699184854L;
	transient private double[] tmparr;
	transient private double[] samplearr;
	
	private RankSet<Criterion> ranks;
		
	public OrdinalPreferenceInformation(List<Criterion> criteria) {
		super(criteria);
		ranks = new RankSet<Criterion>();
		for (Criterion c : criteria) {
			ranks.addObject(c);
		}
		connectRankListener();
	}
	
	public Rank getMeasurement(Criterion c) {
		return ranks.getRank(c);
	}

	public List<Rank> getRanks() {
		List<Rank> rankList = new ArrayList<Rank>();
		for (Criterion c : criteria) {
			rankList.add(ranks.getRank(c));
		}
		return rankList;
	}
	
	private void initArrays() {
		if (tmparr == null) {
			tmparr = new double[criteria.size()];
		}
		if (samplearr == null) {
			samplearr = new double[criteria.size()];			
		}
	}
	
	public double[] sampleWeights(RandomUtil random) {
		initArrays();
		List<Integer> rankList = new ArrayList<Integer>();
		for (Criterion c : criteria) {
			rankList.add(ranks.getRank(c).getRank());
		}
		RankSampler r = new RankSampler(rankList);
		return r.sampleWeights(random);
	}
	
	@Override
	public String toString() {
		return getRanks().toString();
	}

	public OrdinalPreferenceInformation deepCopy() {
		OrdinalPreferenceInformation inf = new OrdinalPreferenceInformation(new ArrayList<Criterion>(criteria));
		inf.ranks = ranks.deepCopy();
		inf.connectRankListener();
		return inf;
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		connectRankListener();	
	}

	private void connectRankListener() {
		for (Criterion c : criteria) {
			ranks.getRank(c).addPropertyChangeListener(measListener);
		}
	}
	
	@SuppressWarnings("unused")
	private static final XMLFormat<OrdinalPreferenceInformation> XML 
		= new XMLFormat<OrdinalPreferenceInformation>(OrdinalPreferenceInformation.class) {
		
		@Override
		public OrdinalPreferenceInformation newInstance(Class<OrdinalPreferenceInformation> cls, InputElement ie) throws XMLStreamException {
			return new OrdinalPreferenceInformation(new ArrayList<Criterion>());
		}				
		@Override
		public boolean isReferenceable() {
			return true;
		}
		@Override
		public void read(InputElement ie, OrdinalPreferenceInformation pref) throws XMLStreamException {
			while (ie.hasNext()) {
				CriterionMeasurementPair p = ie.get("preference", CriterionMeasurementPair.class);
				pref.criteria.add(p.getCriterion());
				pref.ranks.addObject(p.getCriterion());
				pref.ranks.getRank(p.getCriterion()).setRank(((Rank)p.getMeasurement()).getRank());
			}
			pref.connectRankListener();
		}
		@Override
		public void write(OrdinalPreferenceInformation pref, OutputElement oe) throws XMLStreamException {
			for (Criterion c : pref.criteria) {
				CriterionMeasurementPair p = new CriterionMeasurementPair(c, pref.getMeasurement(c));
				oe.add(p, "preference", CriterionMeasurementPair.class);
			}
		}
	};		
}
