package fi.smaa.jsmaa.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class TransferableObject implements Transferable {	
	private Object obj;
	public TransferableObject(Object obj) {
		this.obj = obj;
	}
	public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
		return obj;
	}
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[]{new DataFlavor(obj.getClass(), null)};
	}
	public boolean isDataFlavorSupported(DataFlavor df) {
		return df.equals(new DataFlavor(obj.getClass(), null));
	}
}