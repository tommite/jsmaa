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
package fi.smaa.jsmaa.gui.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.jgoodies.binding.value.AbstractValueModel;

import fi.smaa.jsmaa.gui.presentation.MultiVetoableValueModel;
import fi.smaa.jsmaa.gui.presentation.Vetoer;

public class MultiVetoableValueModelTest {
	
	private MultiVetoableValueModel model;

	@SuppressWarnings("serial")
	@Before
	public void setUp() {
		model = new MultiVetoableValueModel(null, new AbstractValueModel() {
			public Object getValue() {
				// TODO Auto-generated method stub
				return null;
			}
			public void setValue(Object newValue) {
				// TODO Auto-generated method stub	
			}
		}) {
		};
	}
	
	@Test
	public void testVetoers() {
		model.addVetoer(new Vetoer() {
			public boolean check(Object oldValue, Object newValue) {
				return true;
			}

			public String getErrorMessage(Object newValue) {
				return null;
			}			
		});
		assertTrue(model.proposedChange(null, null));
		model.addVetoer(new Vetoer() {
			public boolean check(Object oldValue, Object newValue) {
				return false;
			}

			public String getErrorMessage(Object newValue) {
				return null;
			}			
		});
		assertFalse(model.proposedChange(null, null));		
	}
	
	
}
