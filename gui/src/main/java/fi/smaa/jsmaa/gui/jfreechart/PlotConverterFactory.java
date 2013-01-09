/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013.

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
