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

package fi.smaa.jsmaa.gui;

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

import fi.smaa.jsmaa.model.Rank;

public class RankSelectorGroup {
	private ArrayList<JComboBox> components = new ArrayList<JComboBox>();
	private ListModel listModel;
	private List<Rank> boundRanks;
	public RankSelectorGroup(List<Rank> boundRanks) {
		int numBoxes = boundRanks.size();
		this.boundRanks = boundRanks;
		listModel = new ArrayListModel<Integer>(createObjects(numBoxes));
		createComponents();
	}
		
	private List<Integer> createObjects(int numBoxes) {
		ArrayList<Integer> ranks = new ArrayList<Integer>();
		for (int i=1;i<=numBoxes;i++) {
			ranks.add(i);
		}
		return ranks;
	}

	public List<JComboBox> getSelectors() {
		return components;
	}

	private void createComponents() {
		for (int i=0;i<boundRanks.size();i++) {
			components.add(createComboBox(boundRanks.get(i)));
		}
	}

	@SuppressWarnings("serial")
	private JComboBox createComboBox(Rank r) {
		ValueModel valueModel = new PropertyAdapter<Rank>(r, Rank.PROPERTY_RANK, true);
		JComboBox chooser = new JComboBox(new ComboBoxAdapter<Rank>(listModel, valueModel));
		chooser.setToolTipText("Rank in descending order, 1 is the best/most important, 2 the second, etc.");
		
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
		for (int i=1;i<=components.size();i++) {
			boolean found = false;			
			for (JComboBox box : components) {
				if (box.getSelectedItem().equals(new Integer(i))) {
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
