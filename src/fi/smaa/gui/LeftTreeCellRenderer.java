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

package fi.smaa.gui;

import java.awt.Component;
import java.io.FileNotFoundException;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import fi.smaa.Alternative;
import fi.smaa.Criterion;
import fi.smaa.GaussianCriterion;
import fi.smaa.OrdinalCriterion;
import fi.smaa.UniformCriterion;

public class LeftTreeCellRenderer extends DefaultTreeCellRenderer {

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
				setIcon(loader.getIcon(ImageLoader.ICON_ALTERNATIVE));
				setToolTipText("Alternative");
			} else if (leaf && value instanceof Criterion) {
				if (value instanceof UniformCriterion) {
					setIcon(loader.getIcon(ImageLoader.ICON_UNIFORMCRITERION));
					setToolTipText("Uniform distributed criterion");
				} else if (value instanceof GaussianCriterion) {
					setIcon(loader.getIcon(ImageLoader.ICON_GAUSSIANCRITERION));
					setToolTipText("Gaussian distributed criterion");					
				} else if (value instanceof OrdinalCriterion) {
					setIcon(loader.getIcon(ImageLoader.ICON_ORDINALCRITERION));
					setToolTipText("Ordinal criterion");					
				}
			} else if (value == model.getCentralWeightsNode()) {
				setIcon(loader.getIcon(ImageLoader.ICON_CENTRALWEIGHTS));
				setToolTipText("Central weights");									
			} else if (value == model.getRankAcceptabilitiesNode()) {
				setIcon(loader.getIcon(ImageLoader.ICON_RANKACCEPTABILITIES));
				setToolTipText("Rank acceptability indices");									
			} else if (value == model.getPreferencesNode()) {
				setIcon(loader.getIcon(ImageLoader.ICON_PREFERENCES));
				setToolTipText("Preference information");
			} else {
				setToolTipText(null); //no tool tip
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return this;
	}
}
