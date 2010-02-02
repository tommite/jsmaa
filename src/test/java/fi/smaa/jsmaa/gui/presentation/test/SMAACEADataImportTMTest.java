package fi.smaa.jsmaa.gui.presentation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.presentation.SMAACEAImportDataType;
import fi.smaa.jsmaa.gui.presentation.InvalidInputException;
import fi.smaa.jsmaa.gui.presentation.SMAACEADataImportTM;

public class SMAACEADataImportTMTest {

	private List<String[]> data;
	private SMAACEADataImportTM model;

	@Before
	public void setUp() throws InvalidInputException {
		data = new ArrayList<String[]>();
		
		data.add(new String[]{"patientID", "treatmentID", "cost", "costCensor", "eff", "effCensor"});
		data.add(new String[]{"p1", "1", "200", "0", "0.6", "0"});
		data.add(new String[]{"p2", "2", "220", "1", "0.8", "0"});
		
		model = new SMAACEADataImportTM(data);
	}
	
	@Test(expected=InvalidInputException.class)
	public void testTooFewColumns() throws InvalidInputException {
		data = new ArrayList<String[]>();
		data.add(new String[]{"3", "4", "xx"});
		model = new SMAACEADataImportTM(data);
	}
	
	@Test(expected=InvalidInputException.class)
	public void testTooManyColumns() throws InvalidInputException {
		data = new ArrayList<String[]>();
		data.add(new String[]{"3", "4", "xx", "yy", "zz", "4r", "tr"});
		model = new SMAACEADataImportTM(data);
	}	
	
	@Test
	public void testGetColumnCount() {
		assertEquals(6, model.getColumnCount());
	}
	
	@Test
	public void testGetRowCount() {
		assertEquals(data.size(), model.getRowCount());
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
			assertTrue(model.getValueAt(0, i) instanceof SMAACEAImportDataType);
		}

		for (int i=1;i<data.size();i++) {
			String[] drow = data.get(i);
			for (int j=0;j<drow.length;j++) {
				assertEquals(drow[j], model.getValueAt(i, j));
			}
		}
	}
	
	@Test
	public void testSetValueAt() {
		model.setValueAt(SMAACEAImportDataType.EFFICACY, 0, 0);
		assertEquals(SMAACEAImportDataType.EFFICACY, model.getValueAt(0, 0));
	}
	
	@Test
	public void testComboboxesChange() {
		fail();
	}
}
