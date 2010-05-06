package fi.smaa.jsmaa.gui.jfreechart;

import org.jfree.data.category.CategoryDataset;

public abstract class AbstractPlotConverter<S extends CategoryDataset> implements PlotConverter {
	
	protected S dataset;

	protected AbstractPlotConverter(S s) {
		this.dataset = s;
	}

	public String getData() {
		String res = "Alt";
		for (int i=0;i<dataset.getRowCount();i++) {
			res += "\t\"" + dataset.getRowKey(i) + "\"";
		}			
		for (int i=0;i<dataset.getColumnCount();i++) {
			res += "\n";
			res += "\"" + dataset.getColumnKey(i) + "\"";
			for (int j=0;j<dataset.getRowCount();j++) {
				res += "\t" + dataset.getValue(j, i);
			}
		}
		return res;
	}
}
