package fi.smaa.jsmaa.gui.jfreechart;


public class PlotConverterFactory {
	
	public static PlotConverter getConverter(SMAADataSet<?> s) {
		if (s instanceof CentralWeightsDataset) {
			return new CentralWeightsDatasetConverter((CentralWeightsDataset) s);
		} else if (s instanceof AlternativeColumnCategoryDataset<?>) {
			return new CategoryDatasetConverter((AlternativeColumnCategoryDataset<?>) s);
		}
		throw new IllegalArgumentException("no plot converters available for " + s);
	}
	
	private static class CentralWeightsDatasetConverter extends AbstractPlotConverter<CentralWeightsDataset> {
		
		public CentralWeightsDatasetConverter(CentralWeightsDataset s) {
			super(s);
		}

		@Override
		public String getScript() {
			String res = "set grid\n"+
			"set xrange[0.9:" + dataset.getColumnCount() +".1]\n"+
			"plot 'data.dat'";
			for (int i=0;i<dataset.getRowCount();i++) {
				if (i != 0) {
					res += "''";
				}
				res += " u " + (i+2);
				if (i == dataset.getRowCount()-1) {
					res += ":xticlabels(1)";
				}
				res += " with linespoints title \"" + dataset.getRowKey(i)+"\"";				
				if (i < dataset.getRowCount() - 1) {
					res += ", ";
				}
			}
			res += "\npause -1";
			return res;
		}
	}

	private static class CategoryDatasetConverter extends AbstractPlotConverter<AlternativeColumnCategoryDataset<?>> {
		
		public CategoryDatasetConverter(AlternativeColumnCategoryDataset<?> dataset) {
			super(dataset);
		}

		@Override
		public String getScript() {
			String res = "set style data histograms\n"+
			"set style fill solid 1.00 border -1\n"+
			"set grid\n"+ 
			"plot 'data.dat'";
			for (int i=0;i<dataset.getRowCount();i++) {
				if (i != 0) {
					res += "''";
				}
				res += " u " + (i+2);
				if (i == dataset.getRowCount()-1) {
					res += ":xticlabels(1)";
				}
				res += " title columnhead";				
				if (i < dataset.getRowCount() - 1) {
					res += ", ";
				}
			}
			res += "\npause -1";
			return res;
		}	
	}

}
