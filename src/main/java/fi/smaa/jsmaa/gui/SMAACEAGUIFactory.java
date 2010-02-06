package fi.smaa.jsmaa.gui;

import java.awt.Window;

import javax.swing.JMenu;
import javax.swing.JTree;

import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModelSMAACEA;
import fi.smaa.jsmaa.model.SMAACEAModel;

public class SMAACEAGUIFactory extends AbstractGUIFactory<LeftTreeModelSMAACEA, SMAACEAModel> {
	
	public SMAACEAGUIFactory(Window parent, SMAACEAModel m, MenuDirector dir) {
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
	protected LeftTreeModelSMAACEA buildTreeModel() {
		return new LeftTreeModelSMAACEA(smaaModel);
	}

	@Override
	protected LeftTreeCellRenderer<?> buildCellRenderer() {
		return new LeftTreeCellRendererSMAACEA(getTreeModel());
	}
	
	@Override
	public ViewBuilder buildView(Object o) {
		return super.buildView(o);
	}
}
