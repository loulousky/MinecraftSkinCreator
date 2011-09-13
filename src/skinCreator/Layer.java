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

import java.io.Serializable;

import javax.swing.tree.TreePath;

/**
 * The Layer Object. Designed to hold layer name and the tree path together for
 * easy access
 */
public class Layer implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9175719520038512883L;

	/** The layer name. */
	private String label;

	/** The path to the selected node. */
	private TreePath path;

	/**
	 * Instantiates a new layer.
	 */
	public Layer() {
		this.label = "";
		this.path = null;
	}

	/**
	 * Instantiates a new layer.
	 * 
	 * @param label
	 *            the name of the layer
	 * @param path
	 *            the tree path to the node
	 */
	public Layer(String label, TreePath path) {
		this.label = label;
		this.path = path;
	}

	/**
	 * Gets the label.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Gets the path.
	 * 
	 * @return the path
	 */
	public TreePath getPath() {
		return path;
	}

	/**
	 * Sets the label.
	 * 
	 * @param label
	 *            the new label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Sets the path.
	 * 
	 * @param path
	 *            the new path
	 */
	public void setPath(TreePath path) {
		this.path = path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// return "Layer [label=" + label + ", path=" + path + "]";
		return "Layer [label=" + label + "]";
	}

}
