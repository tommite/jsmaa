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

package fi.smaa.jsmaa;

import fi.smaa.jsmaa.model.SMAATRIModel;

public class SMAATRISimulationThread extends SimulationThread {
	
	private SMAATRIResults results;

	public SMAATRISimulationThread(SMAATRIModel model, int iterations) {
		super(model);
		results = new SMAATRIResults(model.getAlternatives(), model.getCategories(), 10);
		
		addPhase(new SimulationPhase() {
			public void iterate() {
				generateWeights();
				sampleCriteria();
				sortAlternatives();
			}
		}, iterations);
	}

	private void sortAlternatives() {
		
	}

	@Override
	public SMAATRIResults getResults() {
		return results;
	}

}
