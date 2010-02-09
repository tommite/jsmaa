package fi.smaa.jsmaa.gui.presentation.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.presentation.SMAACEAModelImporterPM;
import fi.smaa.jsmaa.model.cea.InvalidInputException;
import fi.smaa.jsmaa.model.cea.SMAACEAModelImporter;
import fi.smaa.jsmaa.model.cea.SMAACEAModelImporter.Type;

public class SMAACEAModelImporterPMTest {

	private SMAACEAModelImporter importer;
	private SMAACEAModelImporterPM model;

	@Before
	public void setUp() throws InvalidInputException {
		List<String[]> data = new ArrayList<String[]>();
		data.add(new String[]{"pat. id", "treat. id", "censor", "cost", "effect"});
		data.add(new String[]{"p1", "t1", "0", "20.0", "30"});
		data.add(new String[]{"p1", "t1", "1", "0", "1"});
		importer = new SMAACEAModelImporter(data);
		model = new SMAACEAModelImporterPM(importer);
	}
	
	@Test
	public void testGetTypeSelectionModel() {
		List<Type> list1 = model.getTypeSelectionList(0);
		List<Type> list2 = model.getTypeSelectionList(1);
		List<Type> list3 = model.getTypeSelectionList(2);
		List<Type> list4 = model.getTypeSelectionList(3);
		List<Type> list5 = model.getTypeSelectionList(4);
		
		List<Type> charTypes = new ArrayList<Type>();
		charTypes.add(SMAACEAModelImporter.Type.PATIENT_ID);
		charTypes.add(SMAACEAModelImporter.Type.TREATMENT_ID);
		
		List<Type> doubleTypes = new ArrayList<Type>(charTypes);
		doubleTypes.add(SMAACEAModelImporter.Type.COST);
		doubleTypes.add(SMAACEAModelImporter.Type.EFFECT);
		
		List<Type> allTypes = new ArrayList<Type>(doubleTypes);
		allTypes.add(SMAACEAModelImporter.Type.COST_CENSORING);
				
		assertEquals(charTypes, list1);
		assertEquals(charTypes, list2);
		assertEquals(allTypes, list3);
		assertEquals(doubleTypes, list4);
		assertEquals(doubleTypes, list5);		
	}
}
