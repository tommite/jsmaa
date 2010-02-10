package fi.smaa.jsmaa.gui;

import java.awt.Component;

import javax.swing.JTree;

import fi.smaa.common.gui.ImageLoader;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModelSMAACEA;

@SuppressWarnings("serial")
public class LeftTreeCellRendererSMAACEA extends LeftTreeCellRenderer<LeftTreeModelSMAACEA> {

	public LeftTreeCellRendererSMAACEA(LeftTreeModelSMAACEA model) {
		super(model);
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value,	boolean sel,
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if (value == model.getDataNode()) {
			setIcon(ImageLoader.getIcon(FileNames.ICON_DATA));
			setToolTipText("Model data");
		} else if (value == model.getRankAcceptabilitiesNode()) {
			setIcon(ImageLoader.getIcon(FileNames.ICON_RANKACCEPTABILITIES));
			setToolTipText("Rank acceptability indices");									
		}
		return this;	
	}
}
