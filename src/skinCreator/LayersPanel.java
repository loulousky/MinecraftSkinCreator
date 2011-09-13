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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.TreePath;

/**
 * The Class LayersPanel.
 */
public class LayersPanel extends JPanel implements ActionListener,
		ListSelectionListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3104941829477936137L;

	/** The add layer button. */
	private JButton addButton;

	/** The communication controller. */
	private Controller control;

	/** The layer counter. */
	private int layerCounter = 0;

	/** The layers. */
	private List<Layer> layers;

	/** The list to show layers. */
	private JList list;

	/** The move layer down button. */
	private JButton moveDownButton;

	/** The move layer up button. */
	private JButton moveUpButton;

	/** The remove layer button. */
	private JButton removeButton;

	/** The root folder. */
	private File rootFolder;

	/**
	 * Instantiates a new layers panel.
	 * 
	 * @param control
	 *            the comms control
	 * @param rootFolder
	 *            the root folder
	 */
	public LayersPanel(Controller control, File rootFolder) {
		this.control = control;
		this.rootFolder = rootFolder;
		initPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			layerCounter++;
			Layer layer = new Layer("Layer " + layerCounter, null);
			layers.add(layer);
			updateList();
			list.setSelectedIndex(layers.size() - 1);
			// control.setOptionsPanelItems(layer);

		} else if (e.getSource() == removeButton) {
			int index = list.getSelectedIndex();

			int n = JOptionPane.showConfirmDialog(this,
					MessageText.LayersDeleteLayer,
					MessageText.LayersDeleteLayerTitle,
					JOptionPane.YES_NO_OPTION);

			if (n == JOptionPane.YES_OPTION) { // user clicked yes
				layers.remove(index);
				updateList(index);
			}

		} else if (e.getSource() == moveUpButton) {
			int index = list.getSelectedIndex();
			moveLayer(index, index - 1);
		} else if (e.getSource() == moveDownButton) {
			int index = list.getSelectedIndex();
			moveLayer(index, index + 1);
		}

	}

	/**
	 * Draws the image from the saved list of layers. Public wrapper to use
	 * saved list without specifying
	 */
	public void drawImage() {
		// System.out.println("draw image");
		drawImage(layers);
	}

	/**
	 * Prepares valid files ready for imgPanel to draw the image from a list of
	 * layers.
	 * 
	 * @param layersToDraw
	 *            the layers to be drawn
	 */
	private void drawImage(List<Layer> layersToDraw) {
		List<File> files = new ArrayList<File>();

		File tempFile;
		// TreePath path;
		Object[] path = null; // Holds strings to file from root
		String relativeFileName;

		for (Layer layer : layersToDraw) {
			// Check and stop any null pointers
			TreePath tempPath = layer.getPath();
			if (tempPath != null) {
				path = tempPath.getPath();
				if (path == null) {
					continue;
				}
			} else {
				continue;
			}

			// Get relative path to the image
			relativeFileName = "";
			boolean dropFirstEntry = true;
			for (Object tempOb : path) {
				if (dropFirstEntry) {
					dropFirstEntry = false;
				} else {
					relativeFileName += File.separator;
					relativeFileName += tempOb.toString();
				}

			}

			tempFile = new File(rootFolder + relativeFileName);

			// System.out.println(rootFolder + relativeFileName);

			// if its a directory, don't add to the list
			if (tempFile.exists()) {
				// System.out.println("exists");
				if (tempFile.isDirectory()) {
					// System.out.println("is directory");
					continue; // exit this iteration
				}

				// if the file exists and is actually a file, add to the list
				if (tempFile.isFile()) {
					// System.out.println("is file");
					files.add(tempFile);
				}
			} else {
				// System.out.println("doesnt exist");
			}

		}

		control.sendListToDraw(files);
	}

	/**
	 * Called from Comms control - causes a redraw of the image using a
	 * temporarily changed layer. Used to preview each item when clicked on.
	 * Works by temporarily changing the selected layer to contain the path to
	 * newly selected item
	 * 
	 * @param tempLayer
	 *            the temporary layer to draw
	 */
	public void drawImageTempPath(Layer tempLayer) {
		// System.out.println("draw temp image");

		List<Layer> tempLayers = new ArrayList<Layer>();
		tempLayers.addAll(layers);
		int index = list.getSelectedIndex();
		// System.out.println("Selected index: " + index);
		if (index != -1) {
			tempLayers.set(index, tempLayer);
		}

		drawImage(tempLayers);
	}

	/**
	 * Enable/disable button controls.
	 * 
	 * @param enable
	 *            true if buttons should be enabled, false otherwise
	 */
	private void enableControls(boolean enable) {
		addButton.setEnabled(true);
		removeButton.setEnabled(enable);
		moveDownButton.setEnabled(enable);
		moveUpButton.setEnabled(enable);
	}

	/**
	 * Gets the currently selected layer.
	 * 
	 * @return the current layer
	 */
	public Layer getCurrentLayer() {
		int index = list.getSelectedIndex();
		Layer layer = layers.get(index);
		return layer;
	}

	/**
	 * Gets the layer counter.
	 * 
	 * @return the layer counter
	 */
	public int getLayerCounter() {
		return layerCounter;
	}

	/**
	 * Gets the layers.
	 * 
	 * @return the layers
	 */
	public List<Layer> getLayers() {
		return layers;

	}

	/**
	 * Inits the panel.
	 */
	private void initPanel() {

		layers = new ArrayList<Layer>();
		this.setLayout(new BorderLayout());

		list = new JList();
		JScrollPane listScrollPane = new JScrollPane(list,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		listScrollPane.setPreferredSize(new Dimension(100, 200));
		listScrollPane.setMaximumSize(new Dimension(100, 500));
		list.addListSelectionListener(this);

		add(listScrollPane, BorderLayout.CENTER);

		JPanel buttons = new JPanel(new BorderLayout());
		addButton = new JButton("Add layer");
		removeButton = new JButton("Remove layer");
		moveUpButton = new JButton("Move up");
		moveDownButton = new JButton("Move down");

		addButton.addActionListener(this);
		removeButton.addActionListener(this);
		moveUpButton.addActionListener(this);
		moveDownButton.addActionListener(this);

		JPanel buttonsTop = new JPanel();
		buttonsTop.add(addButton);
		buttonsTop.add(removeButton);

		JPanel buttonsBottom = new JPanel();
		buttonsBottom.add(moveUpButton);
		buttonsBottom.add(moveDownButton);
		buttons.add(buttonsTop, BorderLayout.NORTH);
		buttons.add(buttonsBottom, BorderLayout.SOUTH);
		add(buttons, BorderLayout.SOUTH);

		enableControls(false);

	}

	/**
	 * Move layer to a new location in the list. Contains checks to ensure new
	 * location will not be out of bounds
	 * 
	 * @param currentIndex
	 *            the current index of the layer
	 * @param newIndex
	 *            the new index for the layer
	 */
	private void moveLayer(int currentIndex, int newIndex) {

		// Ensure newIndex will not be out of bounds
		if (newIndex < 0)
			newIndex = 0;
		if (newIndex > layers.size() - 1)
			newIndex = layers.size() - 1;

		// System.out.println("curr: " + currentIndex + " new: " + newIndex);

		// System.out.println("Before swaping, ArrayList contains : " + layers);
		Collections.swap(layers, currentIndex, newIndex);
		updateList(newIndex);

		// System.out.println("After swaping, ArrayList contains : " + layers);
	}

	/**
	 * Receive new layer item. Called from optionsPanel when save is clicked
	 * 
	 * @param layer
	 *            the new layer
	 */
	public void receiveLayerItem(Layer layer) {
		int selectedIndex = list.getSelectedIndex();
		// Check item is selected before saving
		if (selectedIndex != -1) {
			layers.set(selectedIndex, layer);
		}

		updateList();
		list.setSelectedIndex(selectedIndex);
	}

	/**
	 * Sets the layer counter.
	 * 
	 * @param layerCounter
	 *            the new layer counter
	 */
	public void setLayerCounter(int layerCounter) {
		this.layerCounter = layerCounter;
	}

	/**
	 * Sets the layers. Used when loading a layers file
	 * 
	 * @param newLayers
	 *            the new layers
	 */
	public void setLayers(List<Layer> newLayers) {
		this.layers = newLayers;
		// System.out.println("new layers:" + newLayers);
		updateList(0);
		// System.out.println("layers:" + layers);
	}

	/**
	 * Update list to display the layers
	 */
	private void updateList() {
		final String[] listText = new String[layers.size()];
		Layer tempLayer;
		String text;
		TreePath path;
		File tempFile;

		for (int i = 0; i < listText.length; i++) {
			text = "";
			tempLayer = layers.get(i);
			text = tempLayer.getLabel();

			path = tempLayer.getPath();
			listText[i] = text;

			if (path != null) {
				// System.out.println("not null");
				String relativeFileName = "";
				boolean dropFirstEntry = false;
				for (Object tempOb : path.getPath()) {
					if (dropFirstEntry) {
						dropFirstEntry = false;
					} else {
						relativeFileName += File.separator;
						relativeFileName += tempOb.toString();
					}

				}

				// Check if file is a directory
				tempFile = new File(rootFolder + relativeFileName);
				if (tempFile.exists()) {
					if (tempFile.isDirectory()) {
						continue;
					}
				}

				// only run this if last item in the path
				// is not a directory, ie, last item
				// contains file name
				listText[i] = text + " - " + relativeFileName;
				// System.out.println(combinedFileName);

			}
		}

		/*
		 * Set the text for items in the list
		 */
		list.setModel(new AbstractListModel() {
			private static final long serialVersionUID = 224156126562699821L;
			String[] strings = listText;

			@Override
			public Object getElementAt(int i) {
				return strings[i];
			}

			@Override
			public int getSize() {
				return strings.length;
			}
		});

		list.setSelectedIndex(-1);
	}

	/**
	 * Update list to display the layers, and then select the required index
	 * 
	 * @param index
	 *            the index to select
	 */
	private void updateList(int index) {
		updateList();
		if (index >= layers.size()) {
			index = layers.size() - 1;
		} else if (layers.size() == 0) {
			index = -1;
		}

		list.setSelectedIndex(index);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 * .ListSelectionEvent) List change listener, sets the optionsPanel to
	 * display the correct values
	 */
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		if (list.getSelectedIndex() != -1) {
			enableControls(true);
			int index = list.getSelectedIndex();

			control.setOptionsPanelItems(layers.get(index));
		} else {
			enableControls(false);
			control.setOptionsPanelItems(new Layer());
		}

		// check number of layers, if zero, then disable controls and
		// reset image
		if (layers.size() == 0) {
			control.enableButtonControls(false);
			control.resetImage();
		} else {
			control.enableButtonControls(true);
		}

	}
}
