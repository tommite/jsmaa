package fi.smaa.jsmaa.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.models.StaticModel;

import au.com.bytecode.opencsv.CSVReader;

import com.jidesoft.swing.JideButton;

import fi.smaa.common.gui.ImageLoader;
import fi.smaa.jsmaa.ModelFileManager;
import fi.smaa.jsmaa.gui.presentation.InvalidInputException;
import fi.smaa.jsmaa.gui.presentation.SMAACEADataImportTM;
import fi.smaa.jsmaa.gui.presentation.SMAACEAImportDataType;

public class SMAACEAModelLoader {
	
	private ModelFileManager mgr;
	private JFrame parent;
	private File file;
	private StaticModel wizardModel;	

	public SMAACEAModelLoader(ModelFileManager mgr) {
		this.mgr = mgr;
	}

	public void start(JFrame parent) {
		this.parent = parent;
		wizardModel = new StaticModel();
		
		wizardModel.add(new ChooseFileStep());
		wizardModel.add(new SelectColumnsStep());
		
		Wizard wizard = new Wizard(wizardModel);
		wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);
		wizard.setPreferredSize(new Dimension(800, 600));
		wizard.showInDialog("Import SMAA-CEA model", parent, true);
	}

	@SuppressWarnings("serial")
	private class ChooseFileStep extends PanelWizardStep {
		public ChooseFileStep() {
			super("Choose file", "Choose the CSV file to import SMAA-CEA model from");
			setLayout(new FlowLayout());
			final JFileChooser fs = new JFileChooser(new File("."));
			JideButton openButton = new JideButton("Choose file", ImageLoader.getIcon(FileNames.ICON_OPENFILE));
			final JTextField field = new JTextField("no file selected");
			field.setColumns(20);
			field.setEnabled(false);			
			openButton.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int retval = fs.showOpenDialog(parent);
					if (retval == JFileChooser.APPROVE_OPTION) {
						field.setText(fs.getSelectedFile().getName());
						setComplete(fs.getSelectedFile() != null);
						file = fs.getSelectedFile();
						if (wizardModel.isNextAvailable()) {
							wizardModel.nextStep();
						}
					}
				}				
			});
			add(openButton);
			add(field);
		}
	}
	
	@SuppressWarnings("serial")
	private class SelectColumnsStep extends PanelWizardStep {
		private JScrollPane spane;

		public SelectColumnsStep() {
			super("Tag columns", "Select columns to use as cost, efficacy, and censoring inputs");
			spane = new JScrollPane();
			add(spane);
		}
		
		@Override
		public void prepare() {
			try {
				CSVReader reader = new CSVReader(new FileReader(file));
				List<String[]> data = reader.readAll();
				JTable table = new JTable(new SMAACEADataImportTM(data));

				table.setCellSelectionEnabled(false);
				
				for (int i=0;i<table.getColumnModel().getColumnCount();i++) {
					TableColumn col = table.getColumnModel().getColumn(i);
					MyCellRenderer renderer = new MyCellRenderer();
					col.setCellRenderer(renderer);
					col.setCellEditor(new MyCellEditor(renderer));
				}
				spane.setViewportView(table);
			} catch (InvalidInputException e) {
				JOptionPane.showMessageDialog(parent, e.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
				setComplete(false);
				wizardModel.previousStep();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}
	
	@SuppressWarnings("serial")
	private class MyCellRenderer extends JComboBox implements TableCellRenderer {
		
		private DefaultTableCellRenderer defRenderer = new DefaultTableCellRenderer();
		
		public MyCellRenderer() {
			super(SMAACEAImportDataType.values());
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (row != 0) {
				return defRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
			}
			setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
			setSelectedItem(value);
			return this; 			
		}
	}
	
	@SuppressWarnings("serial")
	private class MyCellEditor extends DefaultCellEditor {
		public MyCellEditor(JComboBox comboBox) {
			super(comboBox);
		}		
	}
}
