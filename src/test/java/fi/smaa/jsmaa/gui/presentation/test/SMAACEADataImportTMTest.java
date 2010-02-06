package fi.smaa.jsmaa.gui.presentation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.SMAACEAImportData;
import fi.smaa.jsmaa.gui.presentation.InvalidInputException;
import fi.smaa.jsmaa.gui.presentation.SMAACEADataImportTM;

public class SMAACEADataImportTMTest {

	private SMAACEAImportData data;
	private SMAACEADataImportTM model;

	@Before
	public void setUp() throws InvalidInputException {
		ArrayList<String[]> list = new ArrayList<String[]>();
		
		list.add(new String[]{"patientID", "treatmentID", "cost", "costCensor", "eff", "effCensor"});
		list.add(new String[]{"p1", "1", "200", "0", "0.6", "0"});
		list.add(new String[]{"p2", "2", "220", "1", "0.8", "0"});
		
		data = new SMAACEAImportData(list);
		model = new SMAACEADataImportTM(data);
	}
		
	@Test
	public void testGetColumnCount() {
		assertEquals(6, model.getColumnCount());
	}
	
	@Test
	public void testGetRowCount() {
		assertEquals(3, model.getRowCount());
	}
	
	@Test
	public void testGetColumnName() {
		assertEquals("patientID", model.getColumnName(0));
		assertEquals("treatmentID", model.getColumnName(1));
		assertEquals("cost", model.getColumnName(2));
		assertEquals("costCensor", model.getColumnName(3));
		assertEquals("eff", model.getColumnName(4));
		assertEquals("effCensor", model.getColumnName(5));		
	}
	
	@Test
	public void testGetValueAt() {

		for (int i=0;i<model.getColumnCount();i++) {
			assertTrue(model.getValueAt(0, i) instanceof SMAACEAImportData.Type);
		}

		for (int row=1;row<data.getRowCount();row++) {
			for (int col=0;col<data.getColumnCount();col++) {
				assertEquals(data.getDataAt(row-1, col), model.getValueAt(row, col));
			}
		}
	}
	
	@Test
	public void testSetValueAt() {
		model.setValueAt(SMAACEAImportData.Type.EFFICACY, 0, 0);
		assertEquals(SMAACEAImportData.Type.EFFICACY, model.getValueAt(0, 0));
	}	
}
