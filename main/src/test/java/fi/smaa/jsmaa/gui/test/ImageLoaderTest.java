/*
	This file is part of JSMAA.
	(c) Tommi Tervonen, 2009	

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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.FileNames;
import fi.smaa.jsmaa.gui.ImageLoader;

public class ImageLoaderTest {
	
	@Before
	public void setUp() {
		ImageLoader.setImagePath("/fi/smaa/jsmaa/gui");
	}
	
	@Test
	public void testGetIcon() throws Exception {
		assertNotNull(ImageLoader.getIcon(FileNames.ICON_ALTERNATIVE));
	}
	
	@Test
	public void testGetIconNotExisting() throws Exception {
		assertNull(ImageLoader.getIcon("gadgadghadhgadhgad.gif"));
	}
}
