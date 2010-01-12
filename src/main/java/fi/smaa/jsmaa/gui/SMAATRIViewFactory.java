package fi.smaa.jsmaa.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;

import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.components.ResultsCellRenderer;
import fi.smaa.jsmaa.gui.components.ResultsTable;
import fi.smaa.jsmaa.gui.jfreechart.CategoryAcceptabilitiesDataset;
import fi.smaa.jsmaa.gui.presentation.CategoryAcceptabilityTableModel;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModelSMAATRI;
import fi.smaa.jsmaa.gui.views.AlternativeInfoView;
import fi.smaa.jsmaa.gui.views.ResultsView;
import fi.smaa.jsmaa.gui.views.TechnicalParameterView;
import fi.smaa.jsmaa.model.SMAATRIModel;
import fi.smaa.jsmaa.simulator.SMAATRIResults;

public class SMAATRIViewFactory extends AbstractViewFactory<LeftTreeModelSMAATRI, SMAATRIModel> {

	private SMAATRIResults results;

	public SMAATRIViewFactory(LeftTreeModelSMAATRI treeModel, SMAATRIModel smaaModel, SMAATRIResults results) {
		super(treeModel, smaaModel);
		this.results = results;
	}
	
	@Override
	public ViewBuilder getView(Object o) {
		if (o == treeModel.getModelNode()) {
			return new TechnicalParameterView(smaaModel);	
		} else if (o == treeModel.getCatAccNode()) {
			CategoryDataset dataset = new CategoryAcceptabilitiesDataset(results);
			final JFreeChart chart = ChartFactory.createStackedBarChart(
			        "", "Alternative", "Category Acceptability",
			        dataset, PlotOrientation.VERTICAL, true, true, false);
			chart.getCategoryPlot().getRangeAxis().setUpperBound(1.0);
			ResultsTable table = new ResultsTable(new CategoryAcceptabilityTableModel(results));		
			table.setDefaultRenderer(Object.class, new ResultsCellRenderer(1.0));		
			return new ResultsView("Category acceptability indices", table, chart);
		} else if (o == treeModel.getCategoriesNode()) {
			return new AlternativeInfoView(smaaModel.getCategories(), "Categories (in ascending order, top = worst)");
		} else {
			return super.getView(o);
		}		
	}

}
