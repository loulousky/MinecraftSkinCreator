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
import java.awt.Container;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * The Class SkinGUI. Initialises all frames and displays them to the user
 */
public class SkinGUI extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1146800151202328550L;

	/** The version of this program. */
	private static final String version = "1.1.0";

	/**
	 * The main method. Launches the GUI
	 * 
	 * @param args
	 *            the command line arguments - not used
	 */
	public static void main(String[] args) {
		new SkinGUI();
	}

	/** The comms control. */
	private Controller control;

	/**
	 * Instantiates a new skin gui.
	 */
	public SkinGUI() {
		initGUI();
	}

	/**
	 * Inits the gui.
	 */
	private void initGUI() {
		setTitle("Skin Creator V" + version);
		setSize(800, 600);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());

		JFileChooser folderChooser = new JFileChooser(".");
		folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		folderChooser.setDialogTitle("Open base directory for layers");

		int returnVal = folderChooser.showOpenDialog(this);

		File rootFolder;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			rootFolder = folderChooser.getSelectedFile();

		} else {
			rootFolder = new File(".");
		}

		control = new Controller();

		OptionsPanel options = new OptionsPanel(control, rootFolder);
		control.setOptionsPanel(options);

		LayersPanel layers = new LayersPanel(control, rootFolder);
		control.setLayersPanel(layers);

		ImgPanel imagePanel = new ImgPanel();
		control.setImgPanel(imagePanel);

		ButtonPanel buttonPanel = new ButtonPanel(control, rootFolder);
		control.setButtonPanel(buttonPanel);

		leftPanel.add(layers, BorderLayout.CENTER);
		leftPanel.add(imagePanel, BorderLayout.SOUTH);

		rightPanel.add(options, BorderLayout.CENTER);
		rightPanel.add(buttonPanel, BorderLayout.SOUTH);

		panel.add(rightPanel, BorderLayout.CENTER);
		panel.add(leftPanel, BorderLayout.WEST);

		Container contentPane = getContentPane();
		contentPane.add(panel);
		setVisible(true);
		setLocationRelativeTo(null);

	}
}
