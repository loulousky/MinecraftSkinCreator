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

import java.util.ArrayList;
import java.util.List;

/**
 * The Class FileTreeNode. Used to map the filesystem into a tree
 */
public class FileTreeNode {

	/** The Constant TYPE_DIRECTORY. */
	public static final int TYPE_DIRECTORY = 1;

	/** The Constant TYPE_FILE. */
	public static final int TYPE_FILE = 2;

	/** The child nodes of this node. */
	private List<FileTreeNode> childNodes;

	/** The name of this node. */
	private String nodeName;

	/** The node type - either file or directory (constants above). */
	private int nodeType;

	/**
	 * Instantiates a new file tree node.
	 */
	public FileTreeNode() {
		nodeName = "";
		childNodes = new ArrayList<FileTreeNode>();
	}

	/**
	 * Instantiates a new file tree node.
	 * 
	 * @param nodeName
	 *            the node name
	 */
	public FileTreeNode(String nodeName) {
		this.nodeName = nodeName;
		childNodes = new ArrayList<FileTreeNode>();
	}

	/**
	 * Instantiates a new file tree node.
	 * 
	 * @param nodeName
	 *            the node name
	 * @param nodeType
	 *            the node type
	 */
	public FileTreeNode(String nodeName, int nodeType) {
		this.nodeName = nodeName;
		childNodes = new ArrayList<FileTreeNode>();
		this.nodeType = nodeType;
	}

	/**
	 * Adds a child node.
	 * 
	 * @param child
	 *            the child to add
	 */
	public void addChildNode(FileTreeNode child) {
		childNodes.add(child);
	}

	/**
	 * Adds a list of child nodes
	 * 
	 * @param children
	 *            the children to add
	 */
	public void addChildNodeList(List<FileTreeNode> children) {
		childNodes.addAll(children);
	}

	/**
	 * Gets the list of child nodes.
	 * 
	 * @return the child nodes
	 */
	public List<FileTreeNode> getChildNodes() {
		return childNodes;
	}

	/**
	 * Gets the node name.
	 * 
	 * @return the node name
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * Gets the node type.
	 * 
	 * @return the node type
	 */
	public int getNodeType() {
		return nodeType;
	}

	/**
	 * Gets the number of children.
	 * 
	 * @return the number of children
	 */
	public int getNumberOfChildren() {
		if (childNodes == null) {
			return 0;
		}

		return childNodes.size();
	}

	/**
	 * Checks if node is a leaf node.
	 * 
	 * @return true, if is leaf
	 */
	public boolean isLeaf() {
		if (getNumberOfChildren() == 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Sets the child nodes.
	 * 
	 * @param childNodes
	 *            the new child nodes
	 */
	public void setChildNodes(List<FileTreeNode> childNodes) {
		this.childNodes = childNodes;
	}

	/**
	 * Sets the node name.
	 * 
	 * @param nodeName
	 *            the new node name
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * Sets the node type.
	 * 
	 * @param nodeType
	 *            the new node type
	 */
	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}
}
