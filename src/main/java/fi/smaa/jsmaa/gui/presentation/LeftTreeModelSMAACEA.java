package fi.smaa.jsmaa.gui.presentation;

import fi.smaa.jsmaa.model.SMAACEAModel;

public class LeftTreeModelSMAACEA extends AbstractLeftTreeModel<SMAACEAModel>{
	
	public static final int DATA = 2;	
	
	public static final String dataNode = "Data";
	
	protected String rankAccNode = "RankAcc";	
	
	public LeftTreeModelSMAACEA(SMAACEAModel model) {
		super(model);
	}
	
	public Object getDataNode() {
		return dataNode;
	}
	
	@Override
	protected int getResultsIndex() {
		return 3;
	}
	
	public Object getRankAcceptabilitiesNode() {
		return rankAccNode;
	}	
	
	@Override
	public Object getChild(Object parent, int index) {
		if (parent == getRoot()) {
			if (index == DATA) {
				return dataNode;
			}
		} else if (parent == resultsNode) {
			if (index == 0) {
				return rankAccNode;
			}
		}
		return super.getChild(parent, index);
	}	
	
	@Override
	public int getChildCount(Object parent) {
		if (parent == getRoot()) {
			return 4;
		} else if (parent == resultsNode) {
			return 1;
		}
		return super.getChildCount(parent);
	}	

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (parent == getRoot()) {
			if (child == dataNode) {
				return DATA;
			}
		} else if (parent == resultsNode) {
			if (child == rankAccNode) {
				return 0;
			}
		}
		return super.getIndexOfChild(parent, child);
	}	
	
	@Override
	public boolean isLeaf(Object node) {
		if (node == dataNode) {
			return true;
		} else if (node == rankAccNode) {
			return true;
		}
		return super.isLeaf(node);
	}	
	
}
