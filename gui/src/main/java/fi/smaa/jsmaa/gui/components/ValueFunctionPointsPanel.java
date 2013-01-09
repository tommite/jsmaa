package fi.smaa.jsmaa.gui.components;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.drugis.common.gui.LayoutUtil;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.jsmaa.model.Point2D;
import fi.smaa.jsmaa.model.ScaleCriterion;

@SuppressWarnings("serial")
public class ValueFunctionPointsPanel extends JPanel {

	private ScaleCriterion crit;

	public ValueFunctionPointsPanel(ScaleCriterion crit) {
		setLayout(new FlowLayout());
		this.crit = crit;
		rebuild();
		crit.addPropertyChangeListener(new CritListener());
	}
	
	private void rebuild() {
		removeAll();
		FormLayout layout = new FormLayout(
				"left:pref:grow",
				"p, 3dlu, p, 3dlu, p, 3dlu, p" );
		
		PanelBuilder builder = new PanelBuilder(layout, this);
		CellConstraints cc = new CellConstraints();
		
		int yIndex = 1;
		DecimalFormat df = new DecimalFormat("0.00");
		DecimalFormat df2 = new DecimalFormat("0.##");
		int ptIndex = 1;
		for (final Point2D pt : crit.getValuePoints()) {
			LayoutUtil.addColumn(layout, "center:pref");
			yIndex += 2;
			builder.addLabel(df2.format(pt.getX()), cc.xy(yIndex, 3));			
			builder.addLabel(df.format(pt.getY()), cc.xy(yIndex, 5));
			if (ptIndex > 1 && ptIndex < crit.getValuePoints().size()) {
				JButton delButton = new JButton("x");
				builder.add(delButton, cc.xy(yIndex, 7));
				delButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ev) {
						crit.deleteValuePoint(pt);
					}
				});
			}
			ptIndex += 1;
		}
		
		builder.addLabel("Function defined as", cc.xyw(1, 1, crit.getValuePoints().size()*2 + 1));
		builder.addLabel("x", cc.xy(1, 3));
		builder.addLabel("v(x)", cc.xy(1, 5));
		builder.addLabel("Del", cc.xy(1, 7));
		
		revalidate();
		repaint();
	}
	
	private class CritListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent ev) {
			if (ev.getPropertyName().equals(ScaleCriterion.PROPERTY_VALUEPOINTS)) {
				rebuild();
			}
		}
	}
}
