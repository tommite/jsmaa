/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.

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
package fi.smaa.jsmaa;

import java.io.InputStream;
import java.util.Properties;

public class AppInfo {
	private static final String APPNAMEFALLBACK = "JSMAA";
	private static final String APPVERSIONFALLBACK = "UNKNOWN";

	public static String getAppVersion() {
		return getProperty("version", APPVERSIONFALLBACK);
	}

	public static String getAppName() {
		return getProperty("name", APPNAMEFALLBACK);
	}
	
	private static String getProperty(String property, String fallback) {
		try {
			InputStream is = AppInfo.class.getResourceAsStream("/META-INF/maven/fi.smaa.jsmaa/main/pom.properties");
			Properties props = new Properties();
			props.load(is);
			return props.getProperty(property, fallback);
		} catch (Exception e) {
			
		}
		return fallback;
	}
}
