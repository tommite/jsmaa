package fi.smaa.jsmaa.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;

import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.components.ResultsCellRenderer;
import fi.smaa.jsmaa.gui.components.ResultsTable;
import fi.smaa.jsmaa.gui.jfreechart.CentralWeightsDataset;
import fi.smaa.jsmaa.gui.jfreechart.RankAcceptabilitiesDataset;
import fi.smaa.jsmaa.gui.presentation.CentralWeightTableModel;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModel;
import fi.smaa.jsmaa.gui.presentation.RankAcceptabilityTableModel;
import fi.smaa.jsmaa.gui.views.ResultsView;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.simulator.SMAA2Results;

public class SMAA2ViewFactory extends AbstractViewFactory<LeftTreeModel, SMAAModel> {
	
	private SMAA2Results results;

	protected SMAA2ViewFactory(LeftTreeModel treeModel, SMAAModel smaaModel, SMAA2Results smaaResults) {
		super(treeModel, smaaModel);
		this.results = smaaResults;
	}
	
	@Override
	public ViewBuilder getView(Object o) {
		if (o == treeModel.getCentralWeightsNode()) {
			CategoryDataset dataset = new CentralWeightsDataset(results);
			final JFreeChart chart = ChartFactory.createLineChart(
			        "", "Criterion", "Central Weight",
			        dataset, PlotOrientation.VERTICAL, true, true, false);
			LineAndShapeRenderer renderer = new LineAndShapeRenderer(true, true);
			chart.getCategoryPlot().setRenderer(renderer);
			
			ResultsTable table = new ResultsTable(new CentralWeightTableModel(results));
			table.setDefaultRenderer(Object.class, new ResultsCellRenderer(1.0));
			return new ResultsView("Central weight vectors", table, chart);
		} else if (o == treeModel.getRankAcceptabilitiesNode()) {
			CategoryDataset dataset = new RankAcceptabilitiesDataset(results);
			final JFreeChart chart = ChartFactory.createStackedBarChart(
			        "", "Alternative", "Rank Acceptability",
			        dataset, PlotOrientation.VERTICAL, true, true, false);
			chart.getCategoryPlot().getRangeAxis().setUpperBound(1.0);
			ResultsTable table = new ResultsTable(new RankAcceptabilityTableModel(results));
			table.setDefaultRenderer(Object.class, new ResultsCellRenderer(1.0));		
			return new ResultsView("Rank acceptability indices", table, chart);
		} else {
			return super.getView(o);
		}	
	}
}
