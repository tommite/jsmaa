package fi.smaa.jsmaa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.SMAACEAModelImporter;
import fi.smaa.jsmaa.SMAACEAModelImporter.Type;
import fi.smaa.jsmaa.gui.presentation.InvalidInputException;

public class SMAACEAModelImporterTest {

	private ArrayList<String[]> list;
	private SMAACEAModelImporter data;

	@Before
	public void setUp() throws InvalidInputException {
		list = new ArrayList<String[]>();
		
		list.add(new String[]{"patientID", "treatmentID", "cost", "costCensor", "eff", "effCensor"});
		list.add(new String[]{"p1", "1", "200", "0", "0.6", "0"});
		list.add(new String[]{"p2", "2", "220", "1", "0.8", "0"});
		
		data = new SMAACEAModelImporter(list);
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
		data = new SMAACEAModelImporter(list);
	}
	
	@Test(expected=InvalidInputException.class)	
	public void testNoDataInConstructor() throws InvalidInputException {
		list = new ArrayList<String[]>();
		list.add(new String[]{"patientID", "treatmentID", "cost", "costCensor", "eff", "effCensor"});
		data = new SMAACEAModelImporter(list);
	}
		
	@Test(expected=InvalidInputException.class)	
	public void testTooManyColumnsInConstructor() throws InvalidInputException {
		list = new ArrayList<String[]>();
		list.add(new String[]{"patientID", "treatmentID", "cost", "costCensor", "eff", "effCensor", "extra column"});
		list.add(new String[]{"p1", "1", "200", "0", "0.6", "0", "9"});		
		data = new SMAACEAModelImporter(list);
	}
	
	@Test(expected=InvalidInputException.class)	
	public void testTooFewColumnsInConstructor() throws InvalidInputException {
		list = new ArrayList<String[]>();
		list.add(new String[]{"patientID", "treatmentID", "cost"});
		list.add(new String[]{"p1", "1", "200"});		
		data = new SMAACEAModelImporter(list);
	}	
	
	@Test
	public void testGetColumnType() {
		// check that initially they are in order
		for (int i=0;i<data.getColumnCount();i++) {
			assertEquals(SMAACEAModelImporter.Type.values()[i], data.getColumnType(i));
		}
	}
	
	@Test
	public void testSetColumnType() {
		SMAACEAModelImporter.Type oldType = SMAACEAModelImporter.Type.values()[0];		
		SMAACEAModelImporter.Type newType = SMAACEAModelImporter.Type.values()[1];
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
	
	@Test
	public void testIsComplete() throws InvalidInputException {
		list = new ArrayList<String[]>();
		
		list.add(new String[]{"patientID", "treatmentID", "cost", "eff"});
		list.add(new String[]{"p1", "1", "200", "0"});
		data = new SMAACEAModelImporter(list);
	
		Type oldType = data.getColumnType(0);
		data.setColumnType(0, Type.EFFICACY_CENCORING);
		assertFalse(data.isComplete());
		data.setColumnType(0, oldType);
		assertTrue(data.isComplete());
	}
}
