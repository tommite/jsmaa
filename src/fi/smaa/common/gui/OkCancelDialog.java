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

package fi.smaa.common.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.ButtonBarBuilder2;

@SuppressWarnings("serial")
public abstract class OkCancelDialog extends JDialog {
	private JPanel d_userPanel;

	protected abstract void commit();

	protected abstract void cancel();
	
	private void construct() {
		setContentPane(createPanel());
		pack();
	}

	private JComponent createPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		
		createUserPanel();
		panel.add(d_userPanel, BorderLayout.CENTER);
		
		JButton okButton = createOkButton();
		JButton cancelButton = createCancelButton();
		ButtonBarBuilder2 builder = new ButtonBarBuilder2();
		builder.addGlue();
		builder.addButton(okButton);
		builder.addButton(cancelButton);
		
		panel.add(builder.getPanel(), BorderLayout.SOUTH);
	
		return panel;
	}

	private void createUserPanel() {
		d_userPanel = new JPanel();
	}
	
	protected JPanel getUserPanel() {
		return d_userPanel;
	}

	private JButton createCancelButton() {
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				cancel();
			}
		});
		return cancelButton;
	}

	private JButton createOkButton() {
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				commit();
			}
		});
		return okButton;
	}
	
	public OkCancelDialog() {
		super();
		construct();
	}

	public OkCancelDialog(Frame owner) {
		super(owner);
		construct();
	}

	public OkCancelDialog(Dialog owner) {
		super(owner);
		construct();
	}

	public OkCancelDialog(Frame owner, boolean modal) {
		super(owner, modal);
		construct();
	}

	public OkCancelDialog(Frame owner, String title) {
		super(owner, title);
		construct();
	}

	public OkCancelDialog(Dialog owner, boolean modal) {
		super(owner, modal);
		construct();
	}

	public OkCancelDialog(Dialog owner, String title) {
		super(owner, title);
		construct();
	}

	public OkCancelDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		construct();
	}

	public OkCancelDialog(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
		construct();
	}
}