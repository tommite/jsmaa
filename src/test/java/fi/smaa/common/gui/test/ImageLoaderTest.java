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

package fi.smaa.common.gui.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.gui.ImageLoader;
import fi.smaa.jsmaa.gui.FileNames;

public class ImageLoaderTest {
	
	private ImageLoader loader;

	@Before
	public void setUp() {
		loader = new ImageLoader("/fi/smaa/jsmaa/gui");
	}
	
	@Test
	public void testGetIcon() throws Exception {
		assertNotNull(loader.getIcon(FileNames.ICON_ALTERNATIVE));
	}
	
	@Test(expected=FileNotFoundException.class)
	public void testGetIconNotExisting() throws Exception {
		loader.getIcon("gadgadghadhgadhgad.gif");
	}
}
