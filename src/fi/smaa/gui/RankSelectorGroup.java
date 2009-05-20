package fi.smaa.gui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.ListModel;


import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.value.ValueModel;

import fi.smaa.OrdinalCriterion;
import fi.smaa.Rank;

public class RankSelectorGroup {
	private List<Integer> ranks;
	private OrdinalCriterion crit;
	private ArrayList<JComboBox> components = new ArrayList<JComboBox>();
	private ListModel listModel;
	
	public RankSelectorGroup(OrdinalCriterion crit) {
		this.crit = crit;
		this.ranks = createObjects(crit);
		listModel = new ArrayListModel<Integer>(ranks);				
		createComponents();
	}
	
	private List<Integer> createObjects(OrdinalCriterion crit) {
		ArrayList<Integer> ranks = new ArrayList<Integer>();
		for (int i=1;i<=crit.getAlternatives().size();i++) {
			ranks.add(i);
		}
		return ranks;
	}

	public List<JComboBox> getSelectors() {
		return components;
	}

	private void createComponents() {
		for (Rank r : crit.getRanks().values()){			
			components.add(createComboBox(r));
		}
	}

	private JComboBox createComboBox(Rank r) {
		ValueModel valueModel = new PropertyAdapter<Rank>(r, Rank.PROPERTY_RANK, true);
		JComboBox chooser = new JComboBox(new ComboBoxAdapter<Rank>(listModel, valueModel));
		
		chooser.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				comboBoxSelected((JComboBox)e.getSource());
			}			
		});
		return chooser;
	}
	
	protected void comboBoxSelected(JComboBox source) {
		Integer selected = (Integer) source.getSelectedItem();
		JComboBox other = findSelecter(selected, source);
		if (other != null) {
			other.setSelectedItem(findUnselectedItem());
		}
	}

	private Integer findUnselectedItem() {
		for (Integer i : ranks) {
			boolean found = false;			
			for (JComboBox box : components) {
				if (box.getSelectedItem() == i) {
					found = true;
					break;
				}
			}
			if (!found) {
				return i; 
			}
		}
		return null;
	}

	private JComboBox findSelecter(Integer selected, JComboBox source) {
		for (JComboBox b : components) {
			if (b == source) {
				continue;
			}
			if (b.getSelectedItem() == selected) {
				return b;
			}
		}
		return null;
	}		
}
