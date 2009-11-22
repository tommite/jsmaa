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
