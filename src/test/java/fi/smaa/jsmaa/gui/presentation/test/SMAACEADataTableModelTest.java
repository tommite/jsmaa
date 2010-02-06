package fi.smaa.jsmaa.gui.presentation.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.presentation.SMAACEADataTableModel;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.SMAACEAModel;
import fi.smaa.jsmaa.model.cea.DataPoint;

public class SMAACEADataTableModelTest {

	private SMAACEADataTableModel model;
	private SMAACEAModel ceaModel;

	@Before
	public void setUp() {
		ceaModel = new SMAACEAModel("model");
		Alternative a = new Alternative("t1");
		ceaModel.addAlternative(a);
		DataPoint p = new DataPoint(a, 1.0, 2.0, false, true);
		ceaModel.addDataPoint(p);
		model = new SMAACEADataTableModel(ceaModel);
	}
	
	@Test
	public void testGetColumnCount() {
		assertEquals(3, model.getColumnCount());
	}
	
	@Test
	public void testGetRowCount() {
		assertEquals(ceaModel.getDataPoints().size(), model.getRowCount());
	}
	
	@Test
	public void testGetValueAt() {
		List<DataPoint> pts = ceaModel.getDataPoints();
		for (int row=0;row<pts.size();row++) {
			DataPoint p = pts.get(row);
			assertEquals(p.getAlternative(), model.getValueAt(row, 0));
			assertEquals(p.getCost(), model.getValueAt(row, 1));
			assertEquals(p.getEffect(), model.getValueAt(row, 2));
		}
	}
	
	@Test
	public void testGetColumnName() {
		assertEquals("Treatment", model.getColumnName(0));
		assertEquals("Cost", model.getColumnName(1));
		assertEquals("Effect", model.getColumnName(2));
	}
}
