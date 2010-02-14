package fi.smaa.jsmaa.gui.views;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYSplineRenderer;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.common.gui.LayoutUtil;
import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.jfreechart.LambdaRankAcceptabilityDataset;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.simulator.SMAACEAResults;

public class SMAACEALambdaRankAccsResultsView implements ViewBuilder {

	private SMAACEAResults res;
	
	private JPanel figuresPanel = new JPanel();

	public SMAACEALambdaRankAccsResultsView(SMAACEAResults res) {
		this.res = res;
		updateFiguresPanel();
	}
	
	public void setResults(SMAACEAResults res) {
		this.res = res;
		updateFiguresPanel();		
	}
	
	public JComponent buildPanel() {
		return figuresPanel;
	}
	
	public void updateFiguresPanel() {
		FormLayout layout = new FormLayout(
				"pref",
				"p");		
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();	
		
		builder.addSeparator("Rank acceptabilities plotted against lambda", cc.xy(1, 1));		
		int index = 1;
		for (Alternative a: res.getAlternatives()) {
			LayoutUtil.addRow(layout);
			builder.add(buildFigurePart(a), cc.xy(1, index + 2));
			index += 2;
		}
		
		figuresPanel.removeAll();
		figuresPanel.add(builder.getPanel());
	}

	private Component buildFigurePart(Alternative a) {
		LambdaRankAcceptabilityDataset dataset = new LambdaRankAcceptabilityDataset(res, a);
		JFreeChart chart = ChartFactory.createXYLineChart(
				"", "Lambda", a.getName(),
				dataset, PlotOrientation.VERTICAL, true, true, false);
		XYSplineRenderer renderer = new XYSplineRenderer();
		renderer.setBaseShapesVisible(true);
		renderer.setPrecision(5);
		chart.getXYPlot().setRenderer(renderer);
		return new ChartPanel(chart);
	}
}