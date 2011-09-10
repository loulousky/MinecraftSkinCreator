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
