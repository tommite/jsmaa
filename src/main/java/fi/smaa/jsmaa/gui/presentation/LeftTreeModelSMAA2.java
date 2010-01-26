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

package fi.smaa.jsmaa.gui.presentation;

import fi.smaa.jsmaa.model.SMAA2Model;

public class LeftTreeModelSMAA2 extends LeftTreeModelMCDAModel<SMAA2Model> {
	protected String rankAccNode = "RankAcc";
	protected String centralWeightsNode = "CW";
	
	public LeftTreeModelSMAA2(SMAA2Model smaaModel) throws NullPointerException {
		super(smaaModel);
	}

	public Object getCentralWeightsNode() {
		return centralWeightsNode;
	}
	
	public Object getRankAcceptabilitiesNode() {
		return rankAccNode;
	}
	
	@Override
	public Object getChild(Object parent, int index) {
		if (parent == resultsNode) {
			if (index == 0) {
				return rankAccNode;
			} else if (index == 1) {
				return centralWeightsNode;
			}
		}
		return super.getChild(parent, index);
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (parent == resultsNode) {
			if (child == rankAccNode) {
				return 0;
			} else if (child == centralWeightsNode) {
				return 1;
			}
		}
		return super.getIndexOfChild(parent, child);
	}

	@Override
	public boolean isLeaf(Object node) {
		if (node == rankAccNode) {
			return true;
		} else if (node == centralWeightsNode) {
			return true;
		}
		return super.isLeaf(node);
	}
}
