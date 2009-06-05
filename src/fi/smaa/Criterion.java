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

import java.util.List;
import java.util.Map;

import com.jgoodies.binding.beans.Observable;

public interface Criterion extends Observable, DeepCopiable {

	public final static String PROPERTY_NAME = "name";
	public final static String PROPERTY_TYPELABEL = "typeLabel";
	public final static String PROPERTY_ALTERNATIVES = "alternatives";
	public static final String PROPERTY_MEASUREMENTS = "measurements";

	public String getName();
	public List<Alternative> getAlternatives();
	public String getTypeLabel();
	public Map<Alternative, ? extends Measurement> getMeasurements();
	public void setAlternatives(List<Alternative> alternatives);
	public Object deepCopy();
}