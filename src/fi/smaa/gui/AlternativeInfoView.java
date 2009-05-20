package fi.smaa.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;

import nl.rug.escher.common.gui.LayoutUtil;
import nl.rug.escher.common.gui.ViewBuilder;


import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.Alternative;
import fi.smaa.SMAAModel;

public class AlternativeInfoView implements ViewBuilder {
	private SMAAModel model;
	
	public AlternativeInfoView(SMAAModel model) {
		this.model = model;
	}

	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"pref, 3dlu, pref, 3dlu, pref",
				"p" );
		
		int fullWidth = 5;

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator("Alternatives", cc.xyw(1,1, fullWidth));

		int row = 3;
		
		for (Alternative a : model.getAlternatives()) {
			LayoutUtil.addRow(layout);
		
			builder.add(BasicComponentFactory.createLabel(
					new PresentationModel<Alternative>(a).getModel(Alternative.PROPERTY_NAME)),
					cc.xy(1, row));
			JButton button = new JButton("Delete");
			button.addActionListener(new DeleteAction(a));

			builder.add(button, cc.xy(3, row));
			row += 2;
		}
		
		return builder.getPanel();
	}
	
	private class DeleteAction extends AbstractAction {
		private Alternative a;
		
		public DeleteAction(Alternative a) {
			this.a = a;
		}

		public void actionPerformed(ActionEvent e) {
			model.deleteAlternative(a);
		}
	}

}
