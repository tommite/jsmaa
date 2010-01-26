package fi.smaa.jsmaa.gui;

import java.awt.Component;

import javax.swing.JTree;

import fi.smaa.common.gui.ImageLoader;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModelSMAA2;

@SuppressWarnings("serial")
public class LeftTreeCellRendererSMAA2 extends LeftTreeCellRendererMCDAModel<LeftTreeModelSMAA2> {

	public LeftTreeCellRendererSMAA2(LeftTreeModelSMAA2 model) {
		super(model);
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value,	boolean sel,
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if (value == model.getCentralWeightsNode()) {
			setIcon(ImageLoader.getIcon(FileNames.ICON_CENTRALWEIGHTS));
			setToolTipText("Central weights");									
		} else if (value == model.getRankAcceptabilitiesNode()) {
			setIcon(ImageLoader.getIcon(FileNames.ICON_RANKACCEPTABILITIES));
			setToolTipText("Rank acceptability indices");									
		}
		return this;
	}	

}
