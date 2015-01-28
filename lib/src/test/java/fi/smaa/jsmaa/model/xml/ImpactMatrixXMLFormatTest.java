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
package fi.smaa.jsmaa.model.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import javolution.xml.stream.XMLStreamException;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.ImpactMatrix;
import fi.smaa.jsmaa.model.BaselineGaussianMeasurement;
import fi.smaa.jsmaa.model.RelativeGaussianMeasurementBase;
import fi.smaa.jsmaa.model.RelativeLogitNormalMeasurement;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class ImpactMatrixXMLFormatTest {

	@Test
	public void testMarshalImpactMatrix() throws XMLStreamException {
		Alternative a = new Alternative("a");
		ScaleCriterion c = new ScaleCriterion("c");
		ExactMeasurement m = new ExactMeasurement(1.0);
		
		ImpactMatrix mat = new ImpactMatrix();
		mat.addAlternative(a);
		mat.addCriterion(c, true);
		mat.setMeasurement(c, a, m);
		
		ImpactMatrix nm = XMLHelper.fromXml(XMLHelper.toXml(mat, ImpactMatrix.class));
		
		assertEquals(1, nm.getCriteria().size());
		assertEquals(1, nm.getAlternatives().size());
		assertEquals(a.getName(), nm.getAlternatives().get(0).getName());
		assertEquals(c.getName(), nm.getCriteria().get(0).getName());
		assertEquals(m, nm.getMeasurement(nm.getCriteria().get(0), nm.getAlternatives().get(0)));
	}
	
	@Test
	public void testMarshalImpactMatrixWithRelativeMeasurements() throws XMLStreamException {
		Alternative a = new Alternative("a");
		Alternative b = new Alternative("b");
		ScaleCriterion c = new ScaleCriterion("c");
			
		ImpactMatrix mat = new ImpactMatrix();
		mat.addAlternative(a);
		mat.addAlternative(b);
		mat.addCriterion(c, true);
		BaselineGaussianMeasurement baseline = mat.getBaseline(c);
		baseline.setMean(1.0);
		baseline.setStDev(0.3);
		RelativeGaussianMeasurementBase ma = new RelativeLogitNormalMeasurement(baseline);
		ma.getRelative().setMean(0.5);
		ma.getRelative().setStDev(0.4);
		mat.setMeasurement(c, a, ma);
		RelativeGaussianMeasurementBase mb = new RelativeLogitNormalMeasurement(baseline);
		mb.getRelative().setMean(-0.1);
		mb.getRelative().setStDev(0.5);
		mat.setMeasurement(c, b, mb);
		
		ImpactMatrix nm = XMLHelper.fromXml(XMLHelper.toXml(mat, ImpactMatrix.class));
		
		assertEquals(1, nm.getCriteria().size());
		assertEquals(2, nm.getAlternatives().size());
		assertEquals(a.getName(), nm.getAlternatives().get(0).getName());
		assertEquals(b.getName(), nm.getAlternatives().get(1).getName());
		assertEquals(c.getName(), nm.getCriteria().get(0).getName());
		assertEquals(baseline, nm.getBaseline(nm.getCriteria().get(0)));
		assertEquals(ma, nm.getMeasurement(nm.getCriteria().get(0), nm.getAlternatives().get(0)));
		assertEquals(mb, nm.getMeasurement(nm.getCriteria().get(0), nm.getAlternatives().get(1)));
		assertSame(nm.getBaseline(nm.getCriteria().get(0)), 
				((RelativeGaussianMeasurementBase)nm.getMeasurement(nm.getCriteria().get(0), nm.getAlternatives().get(0))).getBaseline());
		assertSame(nm.getBaseline(nm.getCriteria().get(0)), 
				((RelativeGaussianMeasurementBase)nm.getMeasurement(nm.getCriteria().get(0), nm.getAlternatives().get(1))).getBaseline());
	}
}
