package fi.smaa.jsmaa.gui;

import java.awt.Window;

import javax.swing.JMenu;
import javax.swing.JTree;

import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModelSMAA2;
import fi.smaa.jsmaa.model.SMAA2Model;

public class SMAACEAGUIFactory extends AbstractGUIFactory<LeftTreeModelSMAA2, SMAA2Model> {
	
	public SMAACEAGUIFactory(Window parent, SMAA2Model m, MenuDirector dir) {
		super(parent, m, dir);
	}

	@Override
	protected JMenu buildResultsMenu() {
		JMenu men = new JMenu("Results");
		return men;
	}

	@Override
	protected JTree buildTree() {
		return super.buildTree();
	}

	@Override
	protected LeftTreeModelSMAA2 buildTreeModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewBuilder buildView(Object o) {
		throw new IllegalArgumentException("no view known for object "+ o);		
	}

	@Override
	protected LeftTreeCellRenderer<?> buildCellRenderer() {
		// TODO Auto-generated method stub
		return null;
	}
}
