/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper 2012.

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
package fi.smaa.jsmaa.gui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;

public class TwoComponentFocusTraversalPolicy extends FocusTraversalPolicy {

	private Component firstComp;
	private Component secondComp;

	public TwoComponentFocusTraversalPolicy(Component firstComp, Component secondComp) {
		this.firstComp = firstComp;
		this.secondComp = secondComp;
	}

	private Component getOtherComponent(Component component) {
		if (component == firstComp) {
			return secondComp;
		} else if (component == secondComp) {
			return firstComp;
		}
		return null;
	}
	
	@Override
	public Component getComponentAfter(Container container, Component component) {
		return getOtherComponent(component);
	}	

	@Override
	public Component getComponentBefore(Container container,
			Component component) {
		return getOtherComponent(component);
	}

	@Override
	public Component getDefaultComponent(Container container) {
		return firstComp;
	}

	@Override
	public Component getFirstComponent(Container container) {
		return firstComp;
	}

	@Override
	public Component getLastComponent(Container container) {
		return secondComp;
	}
}
