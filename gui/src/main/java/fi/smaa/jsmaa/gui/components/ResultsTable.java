package fi.smaa.jsmaa.gui.components;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class ResultsTable extends JTable{

	public ResultsTable(TableModel model) {
		super(model);
		setTableHeader(new EnhancedTableHeader(getColumnModel(), this));
		setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);

		setDefaultRenderer(getColumnClass(0), renderer);
		DefaultTableCellRenderer headerRend = (DefaultTableCellRenderer) getTableHeader().getDefaultRenderer();
		headerRend.setHorizontalAlignment(SwingConstants.CENTER);
	}
}
