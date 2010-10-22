package fi.smaa.jsmaa.gui.jfreechart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.NormalDistributionFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.LogNormalMeasurement;
import fi.smaa.jsmaa.model.Measurement;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class PlotFactory {
	
	public static JFreeChart createUtilityFunctionChart(ScaleCriterion criterion) {
		XYDataset ufuncDS = new UtilityFunctionDataset(criterion);
		JFreeChart chart = ChartFactory.createXYLineChart("", "x", "v(x)",
				ufuncDS, PlotOrientation.VERTICAL,
				true, true, false);
 		return chart;
	}
	
	public static JFreeChart createProbabilityFunctionChart(SMAAModel model, ScaleCriterion criterion) {

		final JFreeChart chart = ChartFactory.createXYLineChart("", "x", "p(x)",
				null, PlotOrientation.VERTICAL,
				true, true, false);

		final XYPlot plot = (XYPlot) chart.getPlot();

		int dataSetIndex = 0;
		
		for (Alternative a : model.getAlternatives()) {
			Measurement m = model.getMeasurement(criterion, a);
			dataSetIndex++;
			Function2D func = null;
			if (m instanceof LogNormalMeasurement) {
				LogNormalMeasurement lm = (LogNormalMeasurement) m;
				func = new LogNormalDistributionFunction(lm);
			} else if (m instanceof GaussianMeasurement) {
				GaussianMeasurement gm = (GaussianMeasurement) m;
				func = new NormalDistributionFunction2D(gm.getMean(), gm.getStDev());
			} else if (m instanceof Interval) {
				Interval i = (Interval) m;
				func = new UniformDistributedFunction(i);
			}
			if (func != null) {
				plot.setDataset(dataSetIndex, 
						DatasetUtilities.sampleFunction2D(func, 
								criterion.getScale().getStart()-Double.MIN_VALUE,
								criterion.getScale().getEnd()+Double.MIN_VALUE, 
								100, a.getName()));
				plot.setRenderer(dataSetIndex, new XYLineAndShapeRenderer(true, false));
			}
		}
		return chart;
	}

}
