package fi.smaa.jsmaa.gui.jfreechart;

import org.jfree.data.category.CategoryDataset;

public class PlotConverterFactory {
	
	public static PlotConverter getConverter(CategoryDataset s) {
		return new CategoryDatasetConverter((CategoryDataset) s);
	}
	
	private static class CategoryDatasetConverter implements PlotConverter {
		private CategoryDataset s;
		
		public CategoryDatasetConverter(CategoryDataset s) {
			this.s = s;
		}

		@Override
		public String getData() {
			String res = "Alt";
			for (int i=0;i<s.getRowCount();i++) {
				res += "\t\"" + s.getRowKey(i) + "\"";
			}			
			for (int i=0;i<s.getColumnCount();i++) {
				res += "\n";
				res += "\"" + s.getColumnKey(i) + "\"";
				for (int j=0;j<s.getRowCount();j++) {
					res += "\t" + s.getValue(j, i);
				}
			}
			return res;
		}

		@Override
		public String getScript() {
			String res = "set style data histograms\n"+
			"set style fill solid 1.00 border -1\n"+
			"set grid\n"+ 
			"plot 'data.dat'";
			for (int i=0;i<s.getRowCount();i++) {
				if (i != 0) {
					res += "''";
				}
				res += " u " + (i+2);
				if (i == s.getRowCount()-1) {
					res += ":xticlabels(1)";
				}
				res += " title columnhead";				
				if (i < s.getRowCount() - 1) {
					res += ", ";
				}
			}
			res += "\npause -1";
			return res;
		}	
	}

}
