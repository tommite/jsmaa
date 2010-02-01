package fi.smaa.jsmaa.gui;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.models.StaticModel;

import fi.smaa.jsmaa.ModelFileManager;

public class SMAACEAModelLoader {
	
	private ModelFileManager mgr;

	public SMAACEAModelLoader(ModelFileManager mgr) {
		this.mgr = mgr;
	}

	public void start(JFrame parent) {
		StaticModel wizardModel = new StaticModel();
		
		wizardModel.add(new ChooseFileStep());
		wizardModel.add(new SelectColumnsStep());
		
		Wizard wizard = new Wizard(wizardModel);
		wizard.setDefaultExitMode(Wizard.EXIT_ON_FINISH);
		wizard.setPreferredSize(new Dimension(900, 600));
		wizard.showInDialog("Import SMAA-CEA model", parent, true);
	}

	@SuppressWarnings("serial")
	private class ChooseFileStep extends PanelWizardStep {
		public ChooseFileStep() {
			super("Choose file", "Choose the CSV file to import SMAA-CEA model from");
		}
	}
	
	@SuppressWarnings("serial")
	private class SelectColumnsStep extends PanelWizardStep {
		public SelectColumnsStep() {
			super("Tag columns", "Select columns to use as cost, efficacy, and censoring inputs");
		}
	}
}
