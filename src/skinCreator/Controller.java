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

/**
 * The communication Controller. Allows communication between each of the
 * different panels without every panel knowing about all the others.
 */
public class Controller {

	/** The button panel. */
	private ButtonPanel buttonPanel;

	/** The img panel - for displaying the current image. */
	private ImgPanel imgPanel;

	/** The layers panel. */
	private LayersPanel layersPanel;

	/** The options panel. */
	private OptionsPanel optionsPanel;

	/**
	 * Instantiates a new controller. Empty as currently nothing to initialise
	 * on construction
	 */
	public Controller() {

	}

	/**
	 * Draws the image using the current saved layers. This is called from
	 * optionsPanel when save changes is clicked
	 */
	public void drawFromLayers() {
		layersPanel.drawImage();
	}

	/**
	 * Toggle if buttons can be clicked or not. Used to disable both save
	 * buttons when there are no layers loaded This is called from layersPanel
	 * (when there is a change to the selection)
	 * 
	 * @param enable
	 *            true if buttons should be enabled, false otherwise
	 */
	public void enableButtonControls(boolean enable) {
		buttonPanel.enableControls(enable);
	}

	/**
	 * Gets the current path to the selected file/folder in the JTree. This is
	 * called from LayersPanel when adding a new layer to preselect the last
	 * used file.
	 * 
	 * @return
	 */
	public TreePath getCurrentPath() {
		TreePath currPath = optionsPanel.getTreePath();
		return currPath;
	}

	/**
	 * Gets the current counter for the number of layers. Used to give each new
	 * layer a unique name. Called from buttonPanel when saving a layers file
	 * 
	 * @return the layer counter
	 */
	public int getLayerCounter() {
		return layersPanel.getLayerCounter();
	}

	/**
	 * Gets the current layers from the layersPanel. Called from buttonPanel
	 * when saving the layers file
	 * 
	 * @return the list of layers
	 */
	public List<Layer> getLayers() {
		List<Layer> layers = layersPanel.getLayers();
		return layers;

	}

	/**
	 * Resets the visible image to be invisible. Called from layersPanel when
	 * there are no layers to draw
	 */
	public void resetImage() {
		imgPanel.resetImage();
	}

	/**
	 * Reset any changes on option panel items to match the saved layer. Called
	 * from optionsPanel when cancel changes is clicked
	 */
	public void resetOptionPanelItems() {
		// get layer from layerspanel
		Layer layer = layersPanel.getCurrentLayer();
		setOptionsPanelItems(layer);
	}

	/**
	 * Saves the visible image as PNG. Called from buttonPanel when save as PNG
	 * is clicked. buttonPanel also handles file selection
	 * 
	 * @param file
	 *            the file to save the image to
	 */
	public void saveImageAsPNG(File file) {
		imgPanel.savePNG(file);
	}

	/**
	 * Forces redraw of the image by making a temporary change to the selected
	 * layer. Works by grabbing the currently selected node, then passing it to
	 * layersPanel (which handles preparing the layers for drawing) Called from
	 * optionsPanel when a change is made in the tree
	 * 
	 * @param path
	 *            the path to the newly selected node
	 */
	public void saveTempPath(TreePath path) {
		// System.out.println(path.toString());
		Layer layer = new Layer("Temp", path);
		layersPanel.drawImageTempPath(layer);
	}

	/**
	 * Sends list of files to the image panel to draw. Called from layersPanel
	 * once it has processed the layers
	 * 
	 * @param files
	 *            the files which are to be drawn
	 */
	public void sendListToDraw(List<File> files) {
		imgPanel.drawList(files);
	}

	/**
	 * Send option panel items to the layersPanel to save the current layer.
	 * Called from optionsPanel when save is clicked
	 */
	public void sendOptionPanelItems() {
		Layer layer = optionsPanel.getItems();

		// send to what every objects need it
		layersPanel.receiveLayerItem(layer);
	}

	/**
	 * Sets the button panel. Called form main window during initialisation
	 * 
	 * @param buttonPanel
	 *            the new button panel
	 */
	public void setButtonPanel(ButtonPanel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}

	/**
	 * Sets the current path to the selected file/folder in the JTree. This is
	 * called from LayersPanel when adding a new layer to preselect the last
	 * used file.
	 * 
	 * @return
	 */
	public void setCurrentPath(TreePath newPath) {
		optionsPanel.setTreePath(newPath);
	}

	/**
	 * Sets the img panel. Called form main window during initialisation
	 * 
	 * @param imgPanel
	 *            the new img panel
	 */
	public void setImgPanel(ImgPanel imgPanel) {
		this.imgPanel = imgPanel;
	}

	/**
	 * Sets the current layer counter. Used to create unique name for layers
	 * Called from buttonPanel when loading a layers file
	 * 
	 * @param layerCounter
	 *            the new layer counter
	 */
	public void setLayerCounter(int layerCounter) {
		layersPanel.setLayerCounter(layerCounter);
	}

	/**
	 * Replaces the current layers. Called from buttonPanel when loading a
	 * layers file
	 * 
	 * @param layers
	 *            the new layers
	 */
	public void setLayers(List<Layer> layers) {
		layersPanel.setLayers(layers);
	}

	/**
	 * Sets the layers panel. Called from main window during initialisation
	 * 
	 * @param layersPanel
	 *            the new layers panel
	 */
	public void setLayersPanel(LayersPanel layersPanel) {
		this.layersPanel = layersPanel;
	}

	/**
	 * Sets the options panel. Called from main window during initialisation
	 * 
	 * @param optionsPanel
	 *            the new options panel
	 */
	public void setOptionsPanel(OptionsPanel optionsPanel) {
		this.optionsPanel = optionsPanel;
	}

	/**
	 * Sets the options panel items. Called from layersPanel to set the fields
	 * to the correct values
	 * 
	 * @param layer
	 *            the layer to display
	 */
	public void setOptionsPanelItems(Layer layer) {
		optionsPanel.setItems(layer);
	}
}
