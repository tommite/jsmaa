package fi.smaa.jsmaa.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.SMAACEAImportData;
import fi.smaa.jsmaa.gui.presentation.InvalidInputException;

public class SMAACEAImportDataTest {

	private ArrayList<String[]> list;
	private SMAACEAImportData data;

	@Before
	public void setUp() throws InvalidInputException {
		list = new ArrayList<String[]>();
		
		list.add(new String[]{"patientID", "treatmentID", "cost", "costCensor", "eff", "effCensor"});
		list.add(new String[]{"p1", "1", "200", "0", "0.6", "0"});
		list.add(new String[]{"p2", "2", "220", "1", "0.8", "0"});
		
		data = new SMAACEAImportData(list);
	}
		
	@Test
	public void getDataAt() {
		for (int row=0;row<data.getRowCount();row++) {
			for (int col=0;col<data.getColumnCount();col++) {
				assertEquals(list.get(row+1)[col], data.getDataAt(row, col));
			}
		}
	}
	
	@Test(expected=InvalidInputException.class)
	public void testInvalidDataConstructor() throws InvalidInputException {
		list.add(new String[]{"p3"});
		data = new SMAACEAImportData(list);
	}
	
	@Test(expected=InvalidInputException.class)	
	public void testNoDataInConstructor() throws InvalidInputException {
		list = new ArrayList<String[]>();
		list.add(new String[]{"patientID", "treatmentID", "cost", "costCensor", "eff", "effCensor"});
		data = new SMAACEAImportData(list);
	}
		
	@Test(expected=InvalidInputException.class)	
	public void testTooManyColumnsInConstructor() throws InvalidInputException {
		list = new ArrayList<String[]>();
		list.add(new String[]{"patientID", "treatmentID", "cost", "costCensor", "eff", "effCensor", "extra column"});
		list.add(new String[]{"p1", "1", "200", "0", "0.6", "0", "9"});		
		data = new SMAACEAImportData(list);
	}
	
	@Test(expected=InvalidInputException.class)	
	public void testTooFewColumnsInConstructor() throws InvalidInputException {
		list = new ArrayList<String[]>();
		list.add(new String[]{"patientID", "treatmentID", "cost"});
		list.add(new String[]{"p1", "1", "200"});		
		data = new SMAACEAImportData(list);
	}	
	
	@Test
	public void testGetColumnType() {
		// check that initially they are in order
		for (int i=0;i<data.getColumnCount();i++) {
			assertEquals(SMAACEAImportData.Type.values()[i], data.getColumnType(i));
		}
	}
	
	@Test
	public void testSetColumnType() {
		SMAACEAImportData.Type oldType = SMAACEAImportData.Type.values()[0];		
		SMAACEAImportData.Type newType = SMAACEAImportData.Type.values()[1];
		data.setColumnType(0, newType);
		assertEquals(newType, data.getColumnType(0));
		// check that column previously holding the type swapped
		assertEquals(oldType, data.getColumnType(1));
	}
	
	@Test
	public void testGetColumnCount() {
		assertEquals(6, data.getColumnCount());
	}
	
	@Test
	public void testGetRowCount() {
		assertEquals(2, data.getRowCount());
	}
	
	@Test
	public void testGetColumnName() {
		for (int i=0;i<data.getColumnCount();i++) {
			assertEquals(list.get(0)[i], data.getColumnName(i));
		}
	}
}
