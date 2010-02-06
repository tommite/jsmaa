package fi.smaa.jsmaa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.SMAACEAModelImporter;
import fi.smaa.jsmaa.SMAACEAModelImporter.Type;
import fi.smaa.jsmaa.gui.presentation.InvalidInputException;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.SMAACEAModel;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class SMAACEAModelImporterTest {

	private ArrayList<String[]> list;
	private SMAACEAModelImporter data;

	@Before
	public void setUp() throws InvalidInputException {
		list = new ArrayList<String[]>();
		
		list.add(new String[]{"patientID", "treatmentID", "cost", "costCensor", "eff", "effCensor"});
		list.add(new String[]{"p1", "t1", "200", "0", "0.6", "0"});
		list.add(new String[]{"p2", "t2", "220", "1", "0.8", "0"});
		
		data = new SMAACEAModelImporter(list);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testConstructModelWithInvalidModel() throws InvalidInputException {
		list = new ArrayList<String[]>();
		list.add(new String[]{"patientID", "treatmentID", "cost", "costCensor"});
		list.add(new String[]{"p1", "1", "200", "0"});
		data = new SMAACEAModelImporter(list);
		
		data.setColumnType(0, Type.EFFICACY_CENCORING);
		data.constructModel();
	}
	
	@Test
	public void testConstructModel() {
		data.setColumnType(0, Type.PATIENT_ID);
		data.setColumnType(1, Type.TREATMENT_ID);
		data.setColumnType(2, Type.COST);
		data.setColumnType(3, Type.COST_CENSORING);		
		data.setColumnType(4, Type.EFFICACY);
		data.setColumnType(5, Type.EFFICACY_CENCORING);
		
		SMAACEAModel model = data.constructModel();
		// check alternatives
		List<Alternative> alts = model.getAlternatives();
		assertEquals(2, alts.size());
		assertEquals("t1", alts.get(0).getName());
		assertEquals("t2", alts.get(1).getName());
		// check criteria
		List<Criterion> crit = model.getCriteria();
		assertEquals(2, crit.size());
		ScaleCriterion c1 = (ScaleCriterion) crit.get(0);
		ScaleCriterion c2 = (ScaleCriterion) crit.get(1);
		assertEquals("Cost", c1.getName());
		assertFalse(c1.getAscending());		
		assertEquals("Effect", c2.getName());
		assertTrue(c2.getAscending());				
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
