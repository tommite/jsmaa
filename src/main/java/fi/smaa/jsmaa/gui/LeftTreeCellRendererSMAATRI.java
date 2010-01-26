package fi.smaa.jsmaa.gui;

import java.awt.Component;

import javax.swing.JTree;

import fi.smaa.common.gui.ImageLoader;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModelSMAATRI;

@SuppressWarnings("serial")
public class LeftTreeCellRendererSMAATRI extends LeftTreeCellRendererMCDAModel<LeftTreeModelSMAATRI> {

	public LeftTreeCellRendererSMAATRI(LeftTreeModelSMAATRI model) {
		super(model);
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,	boolean sel,
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if (model.getCatAccNode() == value) {
			setIcon(ImageLoader.getIcon(FileNames.ICON_RANKACCEPTABILITIES));
			setToolTipText("Category acceptability indices");													
		}
		return this;
	}
}
