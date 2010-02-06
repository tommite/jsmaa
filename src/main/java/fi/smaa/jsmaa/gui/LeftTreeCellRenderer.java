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

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import fi.smaa.common.gui.ImageLoader;
import fi.smaa.jsmaa.gui.presentation.AbstractLeftTreeModel;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.ScaleCriterion;

@SuppressWarnings("serial")
public class LeftTreeCellRenderer<M extends AbstractLeftTreeModel<?>> extends DefaultTreeCellRenderer {

	protected M model;
	
	public LeftTreeCellRenderer(M model) {
		this.model = model;
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value,	boolean sel,
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if (leaf && value instanceof Alternative) {
			setIcon(ImageLoader.getIcon(FileNames.ICON_ALTERNATIVE));
			setToolTipText("Alternative");
		} else if (leaf && value instanceof Criterion) {
			if (value instanceof ScaleCriterion) {
				setIcon(ImageLoader.getIcon(FileNames.ICON_CARDINALCRITERION));
				setToolTipText("Cardinal criterion");
			} else if (value instanceof OutrankingCriterion) {
				setIcon(ImageLoader.getIcon(FileNames.ICON_OUTRANKINGCRITERION));
				setToolTipText("Outranking criterion");					
			} else if (value instanceof OrdinalCriterion) {
				setIcon(ImageLoader.getIcon(FileNames.ICON_ORDINALCRITERION));
				setToolTipText("Ordinal criterion");						
			}
		} else {
			setToolTipText(null); //no tool tip
		}
		return this;
	}
}
