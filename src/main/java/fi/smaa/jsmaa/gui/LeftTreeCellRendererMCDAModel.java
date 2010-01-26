package fi.smaa.jsmaa.gui;

import java.awt.Component;

import javax.swing.JTree;

import fi.smaa.common.gui.ImageLoader;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModelMCDAModel;

@SuppressWarnings("serial")
public class LeftTreeCellRendererMCDAModel<M extends LeftTreeModelMCDAModel<?>> extends LeftTreeCellRenderer<M> {

	public LeftTreeCellRendererMCDAModel(M model) {
		super(model);
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value,	boolean sel,
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if (value == model.getPreferencesNode()) {
			setIcon(ImageLoader.getIcon(FileNames.ICON_PREFERENCES));
			setToolTipText("Preference information");
		}
		return this;
	}		

}
