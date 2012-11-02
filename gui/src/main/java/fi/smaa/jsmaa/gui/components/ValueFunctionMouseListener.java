package fi.smaa.jsmaa.gui.components;

import java.awt.geom.Rectangle2D;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PlotEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RectangleEdge;

import fi.smaa.jsmaa.model.ScaleCriterion;

public class ValueFunctionMouseListener implements ChartMouseListener {

	private ChartPanel chartPanel;
	private XYPlot plot;
	private ScaleCriterion crit;

	public ValueFunctionMouseListener(ChartPanel chartPanel, ScaleCriterion criterion) {
		JFreeChart chart = chartPanel.getChart();
		this.chartPanel = chartPanel;
		plot = chart.getXYPlot();
		this.crit = criterion;
	}

	@Override
	public void chartMouseMoved(ChartMouseEvent ev) {
		// TODO Auto-generated method stub
	}

	@Override
	public void chartMouseClicked(ChartMouseEvent ev) {
		ChartEntity ent = ev.getEntity();
		if (ent instanceof XYItemEntity) {
			System.out.println(ent);
		} else if (ent instanceof PlotEntity) { // add a new point
			int relativeX = ev.getTrigger().getX();
			int relativeY = ev.getTrigger().getY();
			Rectangle2D dataArea = chartPanel.getScreenDataArea();
			RectangleEdge edge = plot.getDomainAxisEdge();
			double realX = plot.getDomainAxis().java2DToValue(relativeX, dataArea, edge);
			double realY = plot.getRangeAxis().java2DToValue(relativeY, dataArea, edge);
		}
	}
}
