package fi.smaa.jsmaa.gui;

import java.awt.Window;
import java.util.Collections;

import javax.swing.JMenu;
import javax.swing.JToolBar;
import javax.swing.JTree;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.components.LambdaPanel;
import fi.smaa.jsmaa.gui.components.ResultsCellRenderer;
import fi.smaa.jsmaa.gui.components.ResultsTable;
import fi.smaa.jsmaa.gui.jfreechart.RankAcceptabilitiesDataset;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModelSMAACEA;
import fi.smaa.jsmaa.gui.presentation.RankAcceptabilityTableModel;
import fi.smaa.jsmaa.gui.views.ResultsView;
import fi.smaa.jsmaa.gui.views.SMAACEADataView;
import fi.smaa.jsmaa.gui.views.SMAACEALambdaRankAccsResultsView;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.SMAACEAModel;
import fi.smaa.jsmaa.simulator.SMAACEAResults;

public class SMAACEAGUIFactory extends AbstractGUIFactory<LeftTreeModelSMAACEA, SMAACEAModel, SMAACEAResults> {
	
	private RankAcceptabilitiesDataset rankAcceptabilitiesDataset;
	private RankAcceptabilityTableModel rankAcceptabilitiesTM;
	private SMAACEALambdaRankAccsResultsView lambdaResView;

	@SuppressWarnings("unchecked")
	public SMAACEAGUIFactory(Window parent, SMAACEAModel m, MenuDirector dir) {
		super(parent, m, dir);
		SMAACEAResults emptyResults = new SMAACEAResults(Collections.EMPTY_LIST, 1);

		rankAcceptabilitiesDataset = new RankAcceptabilitiesDataset(emptyResults);
		rankAcceptabilitiesTM = new RankAcceptabilityTableModel(emptyResults);
		lambdaResView = new SMAACEALambdaRankAccsResultsView(emptyResults);		
	}

	@Override
	protected JMenu buildResultsMenu() {
		JMenu men = new JMenu("Results");
		return men;
	}

	@Override
	protected JTree buildTree() {
		return super.buildTree();
	}

	@Override
	protected LeftTreeModelSMAACEA buildTreeModel() {
		return new LeftTreeModelSMAACEA(smaaModel);
	}

	@Override
	protected LeftTreeCellRenderer<?> buildCellRenderer() {
		return new LeftTreeCellRendererSMAACEA(getTreeModel());
	}
	
	@Override
	protected JToolBar buildBottomToolBar() {
		JToolBar tb = super.buildBottomToolBar();
		tb.add(new LambdaPanel(smaaModel.getLambda(), new Interval(0.0, 100000.0), 1.0));
		return tb;
	}
	
	@Override
	public ViewBuilder buildView(Object o) {
		if (o == treeModel.getDataNode()) {
			return new SMAACEADataView(smaaModel);
		} else if (o == treeModel.getRankAcceptabilitiesNode()) {
			final JFreeChart chart = ChartFactory.createStackedBarChart(
					"", "Alternative", "Rank Acceptability",
					rankAcceptabilitiesDataset, PlotOrientation.VERTICAL, true, true, false);
			chart.getCategoryPlot().getRangeAxis().setUpperBound(1.0);
			ResultsTable table = new ResultsTable(rankAcceptabilitiesTM);
			table.setDefaultRenderer(Object.class, new ResultsCellRenderer(1.0));		
			return new ResultsView(parent, "Rank acceptability indices", table, chart);
		} else if (o == treeModel.getRankAcceptabilitiesLambdaNode()) {		
			return lambdaResView;
		}
		return super.buildView(o);
	}

	public void setResults(SMAACEAResults results) {
		rankAcceptabilitiesDataset.setResults(results);
		rankAcceptabilitiesTM.setResults(results);
		lambdaResView.setResults(results);
	}
}
