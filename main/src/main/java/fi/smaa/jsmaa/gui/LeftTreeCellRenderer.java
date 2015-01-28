/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013-2015.

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

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import fi.smaa.jsmaa.gui.presentation.LeftTreeModel;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModelSMAATRI;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class LeftTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -6982388941837275497L;
	private LeftTreeModel model;
	
	public LeftTreeCellRenderer(LeftTreeModel model) {
		this.model = model;
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value,	boolean sel,
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if (leaf && value instanceof Alternative) {
			setIcon(ImageFactory.IMAGELOADER.getIcon(FileNames.ICON_ALTERNATIVE));
			setToolTipText("Alternative");
		} else if (leaf && value instanceof Criterion) {
			if (value instanceof ScaleCriterion) {
				setIcon(ImageFactory.IMAGELOADER.getIcon(FileNames.ICON_CARDINALCRITERION));
				setToolTipText("Cardinal criterion");
			} else if (value instanceof OutrankingCriterion) {
				setIcon(ImageFactory.IMAGELOADER.getIcon(FileNames.ICON_OUTRANKINGCRITERION));
				setToolTipText("Outranking criterion");					
			} else if (value instanceof OrdinalCriterion) {
				setIcon(ImageFactory.IMAGELOADER.getIcon(FileNames.ICON_ORDINALCRITERION));
				setToolTipText("Ordinal criterion");						
			}
		} else if (value == model.getCentralWeightsNode()) {
			setIcon(ImageFactory.IMAGELOADER.getIcon(FileNames.ICON_CENTRALWEIGHTS));
			setToolTipText("Central weights");									
		} else if (value == model.getRankAcceptabilitiesNode()) {
			setIcon(ImageFactory.IMAGELOADER.getIcon(FileNames.ICON_RANKACCEPTABILITIES));
			setToolTipText("Rank acceptability indices");									
		} else if (value == model.getPreferencesNode()) {
			setIcon(ImageFactory.IMAGELOADER.getIcon(FileNames.ICON_PREFERENCES));
			setToolTipText("Preference information");
		} else if (model instanceof LeftTreeModelSMAATRI &&
				((LeftTreeModelSMAATRI) model).getCatAccNode() == value) {
			setIcon(ImageFactory.IMAGELOADER.getIcon(FileNames.ICON_RANKACCEPTABILITIES));
			setToolTipText("Category acceptability indices");													
		} else {
			setToolTipText(null); //no tool tip
		}
		return this;
	}
}
