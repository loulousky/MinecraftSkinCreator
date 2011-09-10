package skinCreator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

class MyTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 7739867529125369465L;

	// Only display last segment of nodes path in the tree
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, selected, expanded,
				leaf, row, hasFocus);

		String nodeText = value.toString();
		nodeText = nodeText.substring(nodeText.lastIndexOf("\\") + 1);
		setText(nodeText);

		return this;
	}

}

public class OptionsPanel extends JPanel implements ActionListener,
		TreeSelectionListener {

	private static final long serialVersionUID = -4262623153000104091L;
	private JButton cancel;
	private Controller control;
	private JTextField labelField;
	private JButton save;
	private JTree tree;

	public OptionsPanel(Controller control) {
		this.control = control;
		initPanel(new File("."));
	}

	public OptionsPanel(Controller control, File dir) {
		this.control = control;
		initPanel(dir);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == save) {
			control.sendOptionPanelItems();
			control.drawFromLayers();
		} else if (e.getSource() == cancel) {
			control.resetOptionPanelItems();
			control.drawFromLayers();
		}

	}

	/*
	 * The method below is used from
	 * http://www.java2s.com/Code/Java/File-Input-Output
	 * /DisplayafilesysteminaJTreeview.htm The copyright notice below is copied
	 * from the original source file. Copyright for this segment of code remains
	 * with the original author as stated below.
	 */

	/*
	 * Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 1996-2002. All
	 * rights reserved. Software written by Ian F. Darwin and others. $Id:
	 * LICENSE,v 1.8 2004/02/09 03:33:38 ian Exp $
	 * 
	 * Redistribution and use in source and binary forms, with or without
	 * modification, are permitted provided that the following conditions are
	 * met: 1. Redistributions of source code must retain the above copyright
	 * notice, this list of conditions and the following disclaimer. 2.
	 * Redistributions in binary form must reproduce the above copyright notice,
	 * this list of conditions and the following disclaimer in the documentation
	 * and/or other materials provided with the distribution.
	 * 
	 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
	 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
	 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
	 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE
	 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
	 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
	 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
	 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
	 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
	 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
	 * THE POSSIBILITY OF SUCH DAMAGE.
	 * 
	 * Java, the Duke mascot, and all variants of Sun's Java "steaming coffee
	 * cup" logo are trademarks of Sun Microsystems. Sun's, and James Gosling's,
	 * pioneering role in inventing and promulgating (and standardizing) the
	 * Java language and environment is gratefully acknowledged.
	 * 
	 * The pioneering role of Dennis Ritchie and Bjarne Stroustrup, of AT&T, for
	 * inventing predecessor languages C and C++ is also gratefully
	 * acknowledged.
	 */
	/** Add nodes from under "dir" into curTop. Highly recursive. */
	DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
		String curPath = dir.getPath();
		DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);
		if (curTop != null) { // should only be null at root
			curTop.add(curDir);
		}
		Vector<String> ol = new Vector<String>();

		String[] tmp = dir.list();
		for (int i = 0; i < tmp.length; i++)
			ol.addElement(tmp[i]);
		Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
		File f;
		Vector<String> files = new Vector<String>();
		// Make two passes, one for Dirs and one for Files. This is #1.
		for (int i = 0; i < ol.size(); i++) {
			String thisObject = ol.elementAt(i);
			String newPath;
			if (curPath.equals("."))
				newPath = thisObject;
			else
				newPath = curPath + File.separator + thisObject;

			if ((f = new File(newPath)).isDirectory())
				addNodes(curDir, f);
			else
				files.addElement(thisObject);
		}
		// Pass two: for files.
		for (int fnum = 0; fnum < files.size(); fnum++)
			curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
		return curDir;
	}

	private void collapseAll() {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel()
				.getRoot();
		if (root != null) {
			((DefaultTreeModel) tree.getModel()).reload();
		}
	}

	private void enableControls(boolean enable) {
		labelField.setEnabled(enable);
		tree.setEnabled(enable);
		save.setEnabled(enable);
		cancel.setEnabled(enable);
	}

	public Layer getItems() {
		Layer layer = new Layer();
		layer.setLabel(getLabelText());
		layer.setPath(getTreePath());
		return layer;
	}

	private String getLabelText() {
		return labelField.getText();
	}

	private TreePath getTreePath() {
		return tree.getSelectionPath();
	}

	private void initPanel(File dir) {
		this.setLayout(new BorderLayout());

		labelField = new JTextField();
		TitledBorder idLabel = BorderFactory.createTitledBorder("Layer Name");
		labelField.setBorder(idLabel);
		add(BorderLayout.NORTH, labelField);

		tree = new JTree(addNodes(null, dir));

		// Add a listener
		tree.addTreeSelectionListener(this);
		tree.setCellRenderer(new MyTreeCellRenderer());

		// Lastly, put the JTree into a JScrollPane.
		JScrollPane scrollpane = new JScrollPane();
		scrollpane.getViewport().add(tree);
		add(BorderLayout.CENTER, scrollpane);

		JPanel buttons = new JPanel();
		save = new JButton("Save Changes");
		cancel = new JButton("Cancel Changes");
		save.addActionListener(this);
		cancel.addActionListener(this);
		buttons.add(save);
		buttons.add(cancel);

		add(BorderLayout.SOUTH, buttons);
		enableControls(false);

	}

	public void setItems(Layer layer) {
		if (layer.getLabel() == "" && layer.getPath() == null) {
			enableControls(false);
			setTreePath(null);
			setLabelText("");
		} else {
			setLabelText(layer.getLabel());
			setTreePath(layer.getPath());
			enableControls(true);
		}

	}

	private void setLabelText(String text) {
		labelField.setText(text);
	}

	private void setTreePath(TreePath path) {
		// expandAll(tree, false);
		collapseAll();
		if (path != null) {
			tree.setSelectionPath(path);
		} else {
			tree.clearSelection();
		}

	}

	// Tree selection listener
	@Override
	public void valueChanged(TreeSelectionEvent e) {

		control.saveTempPath(e.getNewLeadSelectionPath());
	}

}
