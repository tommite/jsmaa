package fi.smaa.jsmaa.gui;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;

import fi.smaa.jsmaa.model.Criterion;

public class LeftTreeTransferHandler extends TransferHandler {
	
	private LeftTreeModel model;

	public LeftTreeTransferHandler(LeftTreeModel model) {
		this.model = model;
	}
	
	@Override
	public boolean canImport(TransferSupport ts) {
		if (!isLeftTree(ts.getComponent())) {
			return false;
		}			
		if (!ts.isDrop()) {
			return false;
		}
		
	    DropLocation loc = ts.getDropLocation();
		
	    if (loc instanceof JTree.DropLocation) {
	    	JTree.DropLocation tdl = (JTree.DropLocation)loc;
	    	return isCriteriaNode(tdl);
	    }
		return false;
	}
	
	private boolean isCriteriaNode(JTree.DropLocation jdl) {
		if (!jdl.getPath().getLastPathComponent().equals(model.getCriteriaNode())) {
			return false;
		}
		if (jdl.getChildIndex() < 0) {
			return false;
		}
		return true;
	}

	@Override
	public int getSourceActions(JComponent c) {
	    return MOVE;
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		if (!isLeftTree(c)) {
			return null;
		}
		Object lastPathComponent = ((JTree) c).getSelectionPath().getLastPathComponent();
		if (!(lastPathComponent instanceof Criterion)) {
			return null;
		}
		Criterion crit = (Criterion) lastPathComponent;
		return new TransferableCriterion(crit);
	}
	
	private class TransferableCriterion implements Transferable {
		
		private Criterion crit;

		public TransferableCriterion(Criterion crit) {
			this.crit = crit;
		}
		
		@Override
		public Object getTransferData(DataFlavor df)
				throws UnsupportedFlavorException, IOException {
			return crit;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[]{new DataFlavor(Criterion.class, null)};
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor df) {
			return df.equals(new DataFlavor(Criterion.class, null));
		}
	}
	
	@Override
	public boolean importData(TransferSupport t) {
		if (!t.isDrop()) {
			return false;
		}
		try {
			Object o = t.getTransferable().getTransferData(new DataFlavor(Criterion.class, ""));
			if (!(o instanceof Criterion)) {
				return false;
			}
			Criterion c = (Criterion) o;
			
			JTree.DropLocation jdl = (JTree.DropLocation) t.getDropLocation();
			
			model.moveCriterion(c, jdl.getChildIndex());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean isLeftTree(Component component) {
		if (component instanceof JTree) {
			JTree jtree = (JTree) component;
			if (jtree.getModel() instanceof LeftTreeModel) {
				return true;
			}
		}
		return false;
	}	
}
