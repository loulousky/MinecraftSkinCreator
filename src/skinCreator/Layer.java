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

public class Layer implements Serializable {

	private static final long serialVersionUID = -9175719520038512883L;
	private String label;
	private TreePath path;

	public Layer() {
		this.label = "";
		this.path = null;
	}

	public Layer(String label, TreePath path) {
		this.label = label;
		this.path = path;
	}

	public String getLabel() {
		return label;
	}

	public TreePath getPath() {
		return path;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setPath(TreePath path) {
		this.path = path;
	}

	@Override
	public String toString() {
		// return "Layer [label=" + label + ", path=" + path + "]";
		return "Layer [label=" + label + "]";
	}

}
