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

package fi.smaa.jsmaa.gui;

import java.awt.Component;
import java.io.FileNotFoundException;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import fi.smaa.common.gui.ImageLoader;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModel;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModelSMAATRI;
import fi.smaa.jsmaa.model.AbstractCriterion;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class LeftTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -6982388941837275497L;
	private ImageLoader loader;
	private LeftTreeModel model;
	
	public LeftTreeCellRenderer(LeftTreeModel model, ImageLoader loader) {
		this.loader = loader;
		this.model = model;
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value,	boolean sel,
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		try {
			if (leaf && value instanceof Alternative) {
				setIcon(loader.getIcon(FileNames.ICON_ALTERNATIVE));
				setToolTipText("Alternative");
			} else if (leaf && value instanceof AbstractCriterion) {
				if (value instanceof ScaleCriterion) {
					setIcon(loader.getIcon(FileNames.ICON_CARDINALCRITERION));
					setToolTipText("Cardinal criterion");
				} else if (value instanceof OutrankingCriterion) {
					setIcon(loader.getIcon(FileNames.ICON_OUTRANKINGCRITERION));
					setToolTipText("Outranking criterion");					
				} else if (value instanceof OrdinalCriterion) {
					setIcon(loader.getIcon(FileNames.ICON_ORDINALCRITERION));
					setToolTipText("Ordinal criterion");						
				}
			} else if (value == model.getCentralWeightsNode()) {
				setIcon(loader.getIcon(FileNames.ICON_CENTRALWEIGHTS));
				setToolTipText("Central weights");									
			} else if (value == model.getRankAcceptabilitiesNode()) {
				setIcon(loader.getIcon(FileNames.ICON_RANKACCEPTABILITIES));
				setToolTipText("Rank acceptability indices");									
			} else if (value == model.getPreferencesNode()) {
				setIcon(loader.getIcon(FileNames.ICON_PREFERENCES));
				setToolTipText("Preference information");
			} else if (model instanceof LeftTreeModelSMAATRI &&
					((LeftTreeModelSMAATRI) model).getCatAccNode() == value) {
				setIcon(loader.getIcon(FileNames.ICON_RANKACCEPTABILITIES));
				setToolTipText("Category acceptability indices");													
			} else {
				setToolTipText(null); //no tool tip
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return this;
	}
}
