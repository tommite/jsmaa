/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.

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

import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.JTree;

import org.drugis.common.gui.ViewBuilder;
import org.drugis.common.gui.task.TaskProgressBar;
import org.drugis.common.gui.task.TaskProgressModel;

import fi.smaa.jsmaa.gui.presentation.LeftTreeModel;

/**
 * Abstract factory for getting (right pane) views for objects, and other GUI components.
 * 
 * @author Tommi Tervonen
 */
public interface GUIFactory {
	public ViewBuilder buildView(Object o);
	public JMenuBar getMenuBar();
	public JTree getTree();
	public JToolBar getTopToolBar();
	public LeftTreeModel getTreeModel();
	public JToolBar getBottomToolBar();
	public TaskProgressBar getProgressBar();
	public TaskProgressModel getProgressModel();
}
