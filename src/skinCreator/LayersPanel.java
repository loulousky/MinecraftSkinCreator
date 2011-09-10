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
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.TreePath;

public class LayersPanel extends JPanel implements ActionListener,
		ListSelectionListener {

	private static final long serialVersionUID = -3104941829477936137L;
	private JButton addButton;
	private Controller control;
	private int layerCounter = 0;
	private List<Layer> layers;
	private JList list;
	private final int LIST_WIDTH = 100;
	private JButton moveDownButton;
	private JButton moveUpButton;
	private JButton removeButton;

	public LayersPanel(Controller control) {
		this.control = control;
		initPanel();
	}

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
					"Are you sure you wish to delete this layer?",
					"Confirm Deletion of Layer", JOptionPane.YES_NO_OPTION);

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

	// wrapper for drawing image from list
	public void drawImage() {
		// System.out.println("draw image");
		drawImage(layers);
	}

	// prepares valid input for the image to be redrawn
	private void drawImage(List<Layer> layersToDraw) {
		List<File> files = new ArrayList<File>();

		File tempFile;
		TreePath path;
		int pathCount;
		String folderName;
		String fileName;
		String combinedFileName;

		for (Layer layer : layersToDraw) {
			path = layer.getPath();

			if (path == null) {
				break;
			}

			pathCount = path.getPathCount();
			folderName = path.getPathComponent(pathCount - 2).toString();
			fileName = path.getPathComponent(pathCount - 1).toString();
			// Check if file is a directory
			tempFile = new File(fileName);
			if (tempFile.exists()) {
				if (tempFile.isDirectory()) {
					break;
				}
			}

			combinedFileName = folderName + File.separator + fileName;
			// System.out.println(combinedFileName);

			tempFile = new File(combinedFileName);
			if (tempFile.exists() && tempFile.isFile()) {
				// System.out.println("added file:" + combinedFileName);
				files.add(tempFile);
			}
		}

		control.sendListToDraw(files);
	}

	private void enableControls(boolean enable) {
		addButton.setEnabled(true);
		removeButton.setEnabled(enable);
		moveDownButton.setEnabled(enable);
		moveUpButton.setEnabled(enable);
	}

	public Layer getCurrentLayer() {
		int index = list.getSelectedIndex();
		Layer layer = layers.get(index);
		return layer;
	}

	public List<Layer> getLayers() {
		return layers;

	}

	// called from controller - draws image with temporarily changed layer
	public void getTempPath(Layer tempLayer) {
		// System.out.println("draw temp image");

		List<Layer> tempLayers = new ArrayList<Layer>();
		tempLayers.addAll(layers);
		int index = list.getSelectedIndex();
		// System.out.println("Selected index: " + index);
		if (index != -1) {
			tempLayers.set(index, tempLayer);
		}
		// if (tempLayer.getPath() == null)
		// System.out.println("null");

		drawImage(tempLayers);
	}

	private void initPanel() {

		layers = new ArrayList<Layer>();
		this.setLayout(new BorderLayout());

		JScrollPane listScrollPane = new JScrollPane();
		list = new JList();
		listScrollPane.setViewportView(list);
		list.addListSelectionListener(this);
		list.setFixedCellWidth(LIST_WIDTH);

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

	public void receiveLayerItem(Layer layer) {
		int selectedIndex = list.getSelectedIndex();
		// Check item is selected before saving
		if (selectedIndex != -1) {
			layers.set(selectedIndex, layer);
		}

		updateList();
		list.setSelectedIndex(selectedIndex);
	}

	public void setLayers(List<Layer> newLayers) {
		this.layers = newLayers;
		System.out.println("new layers:" + newLayers);
		updateList(0);
		System.out.println("layers:" + layers);
	}

	private void updateList() {
		final String[] listText = new String[layers.size()];
		Layer tempLayer;
		String text;

		for (int i = 0; i < listText.length; i++) {
			text = "";
			tempLayer = layers.get(i);
			text = tempLayer.getLabel();
			listText[i] = text;
		}

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

	private void updateList(int index) {
		updateList();
		if (index >= layers.size()) {
			index = layers.size() - 1;
		} else if (layers.size() == 0) {
			index = -1;
		}

		list.setSelectedIndex(index);

	}

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
