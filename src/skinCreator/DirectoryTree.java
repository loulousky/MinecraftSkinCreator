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
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The DirectoryTree Class. Creates the nodes from a directory to be displayd in
 * a tree.
 */
public class DirectoryTree {

	/**
	 * Instantiates a new directory tree.
	 */
	public DirectoryTree() {

	}

	/**
	 * Creates a tree mapping a folder with each file/folder name in a node.
	 * public wrapper for getFileTree(File, FileTreeNode)
	 * 
	 * @param rootFolder
	 *            the folder to start mapping from
	 * @return the built file tree
	 */
	public FileTreeNode getFileTree(File rootFolder) {
		String rootName = rootFolder.getName();
		FileTreeNode root = new FileTreeNode(rootName,
				FileTreeNode.TYPE_DIRECTORY);
		root = getFileTree(rootFolder, root);

		return root;
	}

	/**
	 * Creates the tree for a directory. Only returns folders and files ending
	 * in ".png"
	 * 
	 * 
	 * This method is recursive!
	 * 
	 * @param currFolder
	 *            the current folder
	 * @param rootNode
	 *            the root node for this folder
	 * @return the built tree
	 */
	private FileTreeNode getFileTree(File currFolder, FileTreeNode rootNode) {

		// list files in current directory
		String[] files = currFolder.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				// Check if dir + name is a directory
				if (new File(dir, name).isDirectory())
					return true;

				// check name is a png
				if (name.toLowerCase().endsWith(".png"))
					return true;

				return false;
			}
		});

		// create list of child nodes
		List<FileTreeNode> childList = new ArrayList<FileTreeNode>();
		for (String listedFile : files) {
			// System.out.println(listedFile);
			// Make new file to open child
			File childFile = new File(currFolder, listedFile);

			// check if directory or file
			int type;
			if (childFile.isDirectory()) {
				type = FileTreeNode.TYPE_DIRECTORY;
			} else {
				type = FileTreeNode.TYPE_FILE;
			}

			// make and add node
			childList.add(new FileTreeNode(listedFile, type));
		}

		// for every child node, if directory add children
		// Sort into order at the same time (directories first)
		List<FileTreeNode> listOfDirectories = new ArrayList<FileTreeNode>();
		List<FileTreeNode> listOfFiles = new ArrayList<FileTreeNode>();

		for (int i = 0; i < childList.size(); i++) {
			FileTreeNode child = childList.get(i);
			// if its a directory
			if (child.getNodeType() == FileTreeNode.TYPE_DIRECTORY) {
				// recursively call this method to list children
				// call getFileTree(currfolder + childName, childNode)
				child = getFileTree(new File(currFolder, child.getNodeName()),
						child);

				// Check if directory is empty
				if (child.isLeaf() == false) {
					// set the node as it now has children
					listOfDirectories.add(child);
				}

			} else {
				listOfFiles.add(child);
			}
		}
		childList.clear();
		childList.addAll(listOfDirectories);
		childList.addAll(listOfFiles);

		// add children to node
		rootNode.addChildNodeList(childList);

		return rootNode;
	}

	/**
	 * Converts the tree to use mutable tree nodes, so it can be used by a JTree
	 * 
	 * This method is recursive!
	 * 
	 * @param node
	 *            the current node to convert
	 * @return the converted tree
	 */
	private DefaultMutableTreeNode getFileTreeAsMTN(FileTreeNode node) {
		// make root node
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(
				node.getNodeName());

		// get children
		List<FileTreeNode> children = node.getChildNodes();

		// convert and add children
		for (FileTreeNode childNode : children) {
			DefaultMutableTreeNode newChild;
			if (childNode.isLeaf()) {
				newChild = new DefaultMutableTreeNode(childNode.getNodeName());
			} else {
				newChild = getFileTreeAsMTN(childNode);
			}

			root.add(newChild);

		}

		// return
		return root;
	}

	/**
	 * Gets the file tree, but returns as a mutable tree node.
	 * 
	 * @param currFolder
	 *            the folder to map
	 * @return the built tree
	 */
	public DefaultMutableTreeNode getFileTreeAsMutableTreeNode(File currFolder) {
		FileTreeNode tree = getFileTree(currFolder);

		DefaultMutableTreeNode dmtNode = getFileTreeAsMTN(tree);

		return dmtNode;
	}

}
