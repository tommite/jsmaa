package fi.smaa.jsmaa.gui.presentation;

import fi.smaa.jsmaa.model.SMAACEAModel;

public class LeftTreeModelSMAACEA extends AbstractLeftTreeModel<SMAACEAModel>{
	
	public static final int DATA = 2;	
	
	public static final String dataNode = "Data"; 
	
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
	
	@Override
	public Object getChild(Object parent, int index) {
		if (parent == getRoot()) {
			if (index == DATA) {
				return dataNode;
			}
		}
		return super.getChild(parent, index);
	}	
	
	@Override
	public int getChildCount(Object parent) {
		if (parent == getRoot()) {
			return 4;
		}
		return super.getChildCount(parent);
	}	

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (parent == getRoot()) {
			if (child == dataNode) {
				return DATA;
			}
		}
		return super.getIndexOfChild(parent, child);
	}	
	
	@Override
	public boolean isLeaf(Object node) {
		if (node == dataNode) {
			return true;
		}
		return super.isLeaf(node);
	}	
	
}
