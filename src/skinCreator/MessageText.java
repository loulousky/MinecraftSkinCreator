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

/**
 * Used to hold any strings for messages. Makes it easier to add/change when
 * needed.
 */
public class MessageText {

	/** ButtonPanel - loadLayersFromFile */
	public static final String ButtonInvalidLayer = "This is not a valid Layer file (or an old incompatible version).";

	/** ButtonPanel - loadLayersFromFile */
	public static final String ButtonInvalidLayerTitle = "Invalid layer File";
	/** ButtonPanel - saveFile */
	public static final String ButtonOverwriteFile = "Are you sure you wish to overwrite this file?";
	/** ButtonPanel - saveFile */
	public static final String ButtonOverwriteFileTitle = "Confirm Overwrite";
	/** ButtonPanel - loadLayersFromFile */
	public static final String ButtonVersionMatchFailed = "Layers file version number does not match what was expected.";
	/** ButtonPanel - loadLayersFromFile */
	public static final String ButtonVersionMatchFailedTitle = "File Version Error";

	/** LayersPanel - actionPerformed */
	public static final String LayersDeleteLayer = "Are you sure you wish to delete this layer?";
	/** LayersPanel - actionPerformed */
	public static final String LayersDeleteLayerTitle = "Confirm Deletion of Layer";

	/** Options Panel - setTreePath */
	public static final String OptionsFailedToFindNode = "Failed to find tree node.\nThings to check:\n\n"
			+ "Have you set the same base directory as when you saved the layers file?\n"
			+ "The first part of the path in the label should match the root node in the tree.\n\n"
			+ "Does the file still exist?\nBrowse through the tree to ensure it is still there.";
	/** Options Panel - setTreePath */
	public static final String OptionsFailedToFindNodeTitle = "Error finding tree node";

}
