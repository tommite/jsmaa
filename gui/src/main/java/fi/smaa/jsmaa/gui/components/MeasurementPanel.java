/*
	This file is part of JSMAA.
	(c) Tommi Tervonen, 2009	

    JSMAA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSMAA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSMAA.  If not, see <http://www.gnu.org/licenses/>.
*/

package fi.smaa.jsmaa.gui.components;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatter;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

import fi.smaa.jsmaa.model.BetaMeasurement;
import fi.smaa.jsmaa.model.CardinalMeasurement;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.LogNormalMeasurement;

@SuppressWarnings("serial")
public class MeasurementPanel extends JPanel {
	
	private ValueModel holder;
	
	private MeasurementType[] allowedValues;

	private JComponent valuePanel;

	private JComboBox chooserPanel;

	private JComboBox chooserComboBox;
	
	public enum MeasurementType {
		EXACT("Exact"),
		INTERVAL("Interval"),
		GAUSSIAN("Gaussian"),
		LOGNORMAL("LogNormal"),
		BETA("Beta");
		
		private String label;
		
		MeasurementType(String label) {
			this.label = label; 
		}
		
		public String getLabel() {
			return label;
		}
		
		@Override
		public String toString() {
			return label;
		}
	}
	
	public MeasurementPanel(ValueModel measurementHolder) {
		allowedValues = MeasurementType.values();
		this.holder = measurementHolder;
				
		init();
		rebuildPanel();
	}
	
	public MeasurementPanel(ValueHolder measurementHolder, MeasurementType[] allowedValues) {
		this.allowedValues = allowedValues;
		this.holder = measurementHolder;
		
		init();
		rebuildPanel();
	}	
	
	public void init() {
		holder.addValueChangeListener(new HolderListener());		
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));		
		chooserPanel = buildChooserPanel();		
		addFocusListener(new FocusTransferrer(chooserPanel));
	}

	private class HolderListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			rebuildPanel();
		}		
	}

	public void rebuildPanel() {
		removeAll();
		valuePanel = buildValuePanel();
		add(chooserPanel);
		add(valuePanel);		
		revalidate();		
	}

	private JComboBox buildChooserPanel() {
		ValueModel valueModel = new ChooserValueModel();
		SelectionInList<MeasurementType> selInList = new SelectionInList<MeasurementType>(allowedValues, valueModel);
		chooserComboBox = new JComboBox();
		chooserComboBox.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				valuePanel.requestFocusInWindow();
				updateChooserTooltipText();
			}
		});
		updateChooserTooltipText();
		Bindings.bind(chooserComboBox, selInList);
		return chooserComboBox;
	}

	protected void updateChooserTooltipText() {
		String text = null;
		if (holder.getValue() instanceof Interval) {
			text = "Interval is input as [min, max]";
		} else if (holder.getValue() instanceof LogNormalMeasurement) {
			text = "Log-normal distributed measurement is input as ln(mean) \u00B1 ln(stdev)";
		} else if (holder.getValue() instanceof GaussianMeasurement) {
			text = "Gaussian distributed measurement is input as mean \u00B1 stdev";
		} else if (holder.getValue() instanceof BetaMeasurement) {
			text = "Beta distributed measurement is input as alpha, beta, min, max";
		}
		chooserComboBox.setToolTipText(text);
	}

	private JComponent buildValuePanel() {
		CardinalMeasurement m = (CardinalMeasurement) holder.getValue();
		JComponent measComp = null;
		if (m instanceof ExactMeasurement) {
			ExactMeasurement em = (ExactMeasurement) m;
			JFormattedTextField tf = BasicComponentFactory.createFormattedTextField(
					new PresentationModel<ExactMeasurement>(em).getModel(ExactMeasurement.PROPERTY_VALUE),
					new DefaultFormatter());
			tf.setHorizontalAlignment(JTextField.CENTER);
			tf.setColumns(10);
			measComp = tf;
		} else if (m instanceof Interval) {
			Interval ival = (Interval) m;
			measComp = new IntervalPanel(this, new PresentationModel<Interval>(ival));
		} else if (m instanceof GaussianMeasurement) {
			GaussianMeasurement gm = (GaussianMeasurement) m;
		    measComp = new GaussianMeasurementPanel(this, new PresentationModel<GaussianMeasurement>(gm));
		} else if (m instanceof BetaMeasurement) {
			BetaMeasurement gm = (BetaMeasurement) m;
		    measComp = new BetaMeasurementPanel(this, new PresentationModel<BetaMeasurement>(gm));
		} else {
			throw new RuntimeException("unknown measurement type");
		}
		return measComp;
	}
	
	private class ChooserValueModel extends AbstractValueModel {
		public Object getValue() {
			CardinalMeasurement m = (CardinalMeasurement) holder.getValue();
			if (m instanceof ExactMeasurement) {
				return MeasurementType.EXACT;
			} else if (m instanceof Interval) {
				return MeasurementType.INTERVAL;
			} else if (m instanceof LogNormalMeasurement) {
				return MeasurementType.LOGNORMAL;
			} else if (m instanceof GaussianMeasurement) {
				return MeasurementType.GAUSSIAN;
			} else if (m instanceof BetaMeasurement) {
				return MeasurementType.BETA;
			} else {
				throw new RuntimeException("unknown measurement type");
			}
		}

		public void setValue(Object newValue) {
			MeasurementType type = (MeasurementType) newValue;

			Interval oldBounds = null;
			if (holder.getValue() instanceof GaussianMeasurement) {
				Double mean = ((GaussianMeasurement) holder.getValue()).getMean();
				oldBounds = new Interval(mean, mean);
			} else if (holder.getValue() instanceof CardinalMeasurement) {
				oldBounds = ((CardinalMeasurement) holder.getValue()).getRange();
			}
			if (type == MeasurementType.EXACT) {
				if (oldBounds != null) {
					holder.setValue(new ExactMeasurement(oldBounds.getMiddle()));					
				} else {
					holder.setValue(new ExactMeasurement(0.0));
				}
			} else if (type == MeasurementType.INTERVAL) {
				if (oldBounds != null) {
					holder.setValue(oldBounds.deepCopy());					
				} else {
					holder.setValue(new Interval(0.0, 1.0));
				}
			} else if (type == MeasurementType.LOGNORMAL) {
				if (oldBounds != null) {
					holder.setValue(new LogNormalMeasurement(oldBounds.getMiddle(), 0.0));
				} else {
					holder.setValue(new LogNormalMeasurement(0.0, 0.0));
				}
			} else if (type == MeasurementType.GAUSSIAN) {
				if (oldBounds != null) {
					holder.setValue(new GaussianMeasurement(oldBounds.getMiddle(), 0.0));
				} else {
					holder.setValue(new GaussianMeasurement(1.0, 0.0));
				}
			}  else if (type == MeasurementType.BETA) {
				holder.setValue(new BetaMeasurement(2.0, 2.0, 0.0, 1.0));
			} else {
				throw new RuntimeException("unknown measurement type");
			}
		}
		
	}
}
