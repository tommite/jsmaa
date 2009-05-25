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

public class LogNormalCriterion extends GaussianCriterion {

	public LogNormalCriterion(String name) {
		super(name);
	}
	
	public LogNormalCriterion(String name, Boolean ascending) {
		super(name, ascending);
	}

	@Override
	public String getTypeLabel() {
		return "LogNormal";
	}
	
	@Override
	protected Double deriveMin(Double mean, Double stDev) {
		return Math.exp(super.deriveMin(mean, stDev));
	}
	
	@Override
	protected Double deriveMax(Double mean, Double stDev) {
		return Math.exp(super.deriveMax(mean, stDev));		
	}
	
}
