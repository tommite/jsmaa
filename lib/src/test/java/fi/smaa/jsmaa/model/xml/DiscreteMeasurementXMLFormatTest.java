package fi.smaa.jsmaa.model.xml;

import java.util.ArrayList;

import javolution.xml.stream.XMLStreamException;
import junit.framework.Assert;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.DiscreteMeasurement;
import fi.smaa.jsmaa.model.Point2D;
import fi.smaa.jsmaa.model.PointOutsideIntervalException;

public class DiscreteMeasurementXMLFormatTest {

	@Test
	public void testDiscreteMeasurement() {
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(4, 0.5));
		points.add(new Point2D(5, 0.5));
		DiscreteMeasurement dm = null;
		try {
			dm = new DiscreteMeasurement(points);
		} catch (PointOutsideIntervalException e) {
			Assert.fail(e.getMessage());
		}

		DiscreteMeasurement nm = null;
		try {
			nm = (DiscreteMeasurement) XMLHelper.fromXml(XMLHelper.toXml(dm,
					DiscreteMeasurement.class));
		} catch (XMLStreamException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(dm, nm);
	}

}
