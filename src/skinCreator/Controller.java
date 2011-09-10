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

import java.io.File;
import java.util.List;

import javax.swing.tree.TreePath;

public class Controller {

	private ButtonPanel buttonPanel;
	private ImgPanel imgPanel;
	private LayersPanel layersPanel;
	private OptionsPanel optionsPanel;

	public Controller() {

	}

	// called from optionspanel
	public void drawFromLayers() {
		layersPanel.drawImage();
	}

	// called from layerspanel
	public void enableButtonControls(boolean enable) {
		buttonPanel.enableControls(enable);
	}

	public ButtonPanel getButtonPanel() {
		return buttonPanel;
	}

	public ImgPanel getImgPanel() {
		return imgPanel;
	}

	// called from ButtonPanel to retrieve current layers
	public List<Layer> getLayers() {
		List<Layer> layers = layersPanel.getLayers();
		return layers;

	}

	public LayersPanel getLayersPanel() {
		return layersPanel;
	}

	public OptionsPanel getOptionsPanel() {
		return optionsPanel;
	}

	// called from layerspanel
	public Layer getOptionsPanelItems() {
		Layer layer = optionsPanel.getItems();

		return layer;
	}

	public void resetImage() {
		imgPanel.resetImage();
	}

	// called from optionspanel
	public void resetOptionPanelItems() {
		// get layer from layerspanel
		Layer layer = layersPanel.getCurrentLayer();
		setOptionsPanelItems(layer);
	}

	// called from buttonPanel
	public void saveImageAsPNG(File file) {
		imgPanel.savePNG(file);
	}

	// called from optionspanel
	public void saveTempPath(TreePath path) {
		// System.out.println(path.toString());
		Layer layer = new Layer("Temp", path);
		layersPanel.getTempPath(layer);
	}

	// called from layerspanel
	public void sendListToDraw(List<File> files) {
		imgPanel.drawList(files);
	}

	// called from optionspanel
	public void sendOptionPanelItems() {
		Layer layer = getOptionsPanelItems();

		// send to what every objects need it
		layersPanel.receiveLayerItem(layer);
	}

	public void setButtonPanel(ButtonPanel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}

	public void setImgPanel(ImgPanel imgPanel) {
		this.imgPanel = imgPanel;
	}

	// called form ButtonPanel to replace current layers
	public void setLayers(List<Layer> layers) {
		layersPanel.setLayers(layers);
	}

	public void setLayersPanel(LayersPanel layersPanel) {
		this.layersPanel = layersPanel;
	}

	public void setOptionsPanel(OptionsPanel optionsPanel) {
		this.optionsPanel = optionsPanel;
	}

	// called from layerspanel
	public void setOptionsPanelItems(Layer layer) {
		optionsPanel.setItems(layer);
	}
}
