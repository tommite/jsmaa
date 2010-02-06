package fi.smaa.jsmaa.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.models.StaticModel;

import au.com.bytecode.opencsv.CSVReader;

import com.jidesoft.swing.JideButton;

import fi.smaa.common.gui.ImageLoader;
import fi.smaa.jsmaa.ModelFileManager;
import fi.smaa.jsmaa.SMAACEAImportData;
import fi.smaa.jsmaa.gui.presentation.InvalidInputException;
import fi.smaa.jsmaa.gui.presentation.SMAACEADataImportTM;

public class SMAACEAModelLoader {

	private ModelFileManager mgr;
	private JFrame parent;
	private StaticModel wizardModel;
	private SMAACEAImportData importData;	

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
						File file = fs.getSelectedFile();
						loadFile(field, file);
						if (wizardModel.isNextAvailable()) {
							wizardModel.nextStep();
						}
					}
				}

				private void loadFile(final JTextField field, File file) {
					try {
						CSVReader reader = new CSVReader(new FileReader(file));
						importData = new SMAACEAImportData(reader.readAll());
						field.setText(file.getName());
						setComplete(file != null);
					}  catch (InvalidInputException e) {
						JOptionPane.showMessageDialog(parent, e.getMessage(), "Invalid input", JOptionPane.ERROR_MESSAGE);
						setComplete(false);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(parent, e.getMessage(), "Error loading file", JOptionPane.ERROR_MESSAGE);
						setComplete(false);
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
			super("Tag columns", "Select columns to use as patient ID, treatment ID, cost, efficacy, and possible censoring inputs");
			spane = new JScrollPane();
			add(spane);
		}

		@Override
		public void prepare() {
			SMAACEADataImportTM tableModel = new SMAACEADataImportTM(importData);
			tableModel.addTableModelListener(new SelectColumnsListener(tableModel));
			JTable table = new JTable(tableModel);

			table.setCellSelectionEnabled(false);

			for (int i=0;i<table.getColumnModel().getColumnCount();i++) {
				TableColumn col = table.getColumnModel().getColumn(i);
				SMAACEAImportDataCellRenderer renderer = new SMAACEAImportDataCellRenderer();
				col.setCellRenderer(renderer);
				col.setCellEditor(new DefaultCellEditor(renderer));
			}
			spane.setViewportView(table);
		}

		private class SelectColumnsListener implements TableModelListener {

			private SMAACEADataImportTM tableModel;

			public SelectColumnsListener(SMAACEADataImportTM tableModel) {
				this.tableModel = tableModel;
				checkTableChanged();
			}

			@Override
			public void tableChanged(TableModelEvent ev) {
				checkTableChanged();
			}

			private void checkTableChanged() {
				SMAACEAImportData.Type[] neededColumns = new SMAACEAImportData.Type[]{
						SMAACEAImportData.Type.COST,
						SMAACEAImportData.Type.EFFICACY,
						SMAACEAImportData.Type.PATIENT_ID,
						SMAACEAImportData.Type.TREATMENT_ID};

				List<SMAACEAImportData.Type> actualColumns = new ArrayList<SMAACEAImportData.Type>();
				for (int col=0;col<tableModel.getColumnCount();col++) {
					actualColumns.add((SMAACEAImportData.Type) tableModel.getValueAt(0, col));
				}
				setComplete(actualColumns.containsAll(Arrays.asList(neededColumns)));
			}	
		}		
	}
}
