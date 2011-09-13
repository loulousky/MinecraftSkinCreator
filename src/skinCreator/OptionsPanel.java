/**
 * Copyright (C) Matt Fisher 2011
 * This software is provided as-is. I am not responsible nor liable for 
 * any data loss, hardware damage, disaster nor any other damages from 
 * use of this code. Use of this software is at your own risk.
 * 
 * Feel free to use, modify and distribute the source code and programs 
 * containing parts of the source code, as long as I am credited 
 * where necessary.
 */

package skinCreator;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * The Class OptionsPanel. Used to display layer name and selected image to the
 * user
 */
public class OptionsPanel extends JPanel implements ActionListener,
		TreeSelectionListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1050867789757012196L;

	/** The cancel changes button. */
	private JButton cancel;

	/** The comms control. */
	private Controller control;

	/** The label field. */
	private JTextField labelField;

	/** The root folder. */
	private File rootFolder;

	/** The save button. */
	private JButton save;

	/** The tree. */
	private JTree tree;

	/**
	 * Instantiates a new options panel.
	 * 
	 * @param control
	 *            the comms control
	 */
	public OptionsPanel(Controller control) {
		this.control = control;
		this.rootFolder = new File(".");
		initPanel(this.rootFolder);
	}

	/**
	 * Instantiates a new options panel.
	 * 
	 * @param control
	 *            the comms control
	 * @param rootFolder
	 *            the root folder
	 */
	public OptionsPanel(Controller control, File rootFolder) {
		this.control = control;
		this.rootFolder = rootFolder;
		initPanel(this.rootFolder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * Action listener to trigger saving or resetting values, and then redraw
	 * the image
	 */
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

	/**
	 * Collapse all tree nodes.
	 */
	private void collapseAll() {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel()
				.getRoot();
		if (root != null) {
			((DefaultTreeModel) tree.getModel()).reload();
		}
	}

	/**
	 * Enable/disable controls.
	 * 
	 * @param enable
	 *            true if fields/buttons should be enabled, false otherwise
	 */
	private void enableControls(boolean enable) {
		labelField.setEnabled(enable);
		tree.setEnabled(enable);
		save.setEnabled(enable);
		cancel.setEnabled(enable);
	}

	/**
	 * Gets the item values.
	 * 
	 * @return the items in a layer object
	 */
	public Layer getItems() {
		Layer layer = new Layer();
		layer.setLabel(getLabelText());
		layer.setPath(getTreePath());
		return layer;
	}

	/**
	 * Gets the label text.
	 * 
	 * @return the label text
	 */
	private String getLabelText() {
		return labelField.getText();
	}

	/**
	 * Gets the tree path.
	 * 
	 * @return the tree path
	 */
	private TreePath getTreePath() {
		return tree.getSelectionPath();
	}

	/**
	 * Inits the panel.
	 * 
	 * @param dir
	 *            the root directory
	 */
	private void initPanel(File dir) {
		this.setLayout(new BorderLayout());

		labelField = new JTextField();
		TitledBorder idLabel = BorderFactory.createTitledBorder("Layer Name");
		labelField.setBorder(idLabel);
		add(BorderLayout.NORTH, labelField);

		DirectoryTree dirTree = new DirectoryTree();
		DefaultMutableTreeNode treeNodes = dirTree
				.getFileTreeAsMutableTreeNode(rootFolder);

		tree = new JTree(treeNodes);

		// Add a listener
		tree.addTreeSelectionListener(this);

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

	/**
	 * Sets the item values.
	 * 
	 * @param layer
	 *            the values for the fields
	 */
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

	/**
	 * Sets the label text.
	 * 
	 * @param text
	 *            the new label text
	 */
	private void setLabelText(String text) {
		labelField.setText(text);
	}

	/**
	 * Sets the tree path. Nested for loops are for searching for correct node
	 * to select - needed after loading a layers file
	 * 
	 * @param path
	 *            the tree path to select
	 */
	private void setTreePath(TreePath path) {

		collapseAll();
		if (path != null) {
			// System.out.println(path);

			List<Object> objectPath = new ArrayList<Object>();
			objectPath.add(tree.getModel().getRoot());

			// Drop first item in path as it is the root node (already found and
			// selected)
			boolean dropFirst = true;
			for (Object temp : path.getPath()) {
				if (dropFirst) {
					dropFirst = false;
					continue;
				}

				String searchForName = temp.toString();
				// System.out.println(searchForName);

				Object searchObj = new DefaultMutableTreeNode(searchForName);

				// Get last found node to search from - stops searching from the
				// top
				DefaultMutableTreeNode searchNode = (DefaultMutableTreeNode) objectPath
						.get(objectPath.size() - 1);

				boolean itemFound = false;
				for (Enumeration<?> e = searchNode.children(); e
						.hasMoreElements();) {
					Object tempNode = e.nextElement();
					Object userObject = ((DefaultMutableTreeNode) tempNode)
							.getUserObject();

					if (searchObj.toString().equals(userObject.toString())) {
						// System.out.println("found it");
						objectPath.add(tempNode);
						itemFound = true;
						break;
					}
				}
				if (itemFound == false) {
					JOptionPane.showMessageDialog(null,
							MessageText.OptionsFailedToFindNode,
							MessageText.OptionsFailedToFindNodeTitle,
							JOptionPane.ERROR_MESSAGE);
					return;
				}

			}

			Object[] pathObjectArray = objectPath.toArray();

			tree.expandPath(new TreePath(pathObjectArray));
			tree.setSelectionPath(new TreePath(pathObjectArray));
		} else {
			tree.clearSelection();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event
	 * .TreeSelectionEvent) Tree change listener. Triggers a redraw to show the
	 * selected item
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {

		control.saveTempPath(e.getNewLeadSelectionPath());
	}

}
