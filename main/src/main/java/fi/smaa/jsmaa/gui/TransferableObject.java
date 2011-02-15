/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.

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