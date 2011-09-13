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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The Button Panel Class. Used for adding buttons to the bottom right of the
 * main window to save/load layers, and to export the image as PNG
 */
public class ButtonPanel extends JPanel implements ActionListener {

	/**
	 * Version for saved layer file. Used to ensure we don't attempt to load an
	 * old version
	 */
	private static final String layerFileVersion = "1.1";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -324718791157821176L;

	/** Communication control. */
	private Controller control;

	/** The load layers button. */
	private JButton loadLayersButton;

	/** The root folder - paths are relative to this. */
	private File rootFolder;

	/** The save layers button. */
	private JButton saveLayersButton;

	/** The save PNG button. */
	private JButton savePNGButton;

	/**
	 * Instantiates a new button panel.
	 * 
	 * @param control
	 *            the communication controller
	 * @param rootFolder
	 *            the root folder
	 */
	public ButtonPanel(Controller control, File rootFolder) {
		this.control = control;
		this.rootFolder = rootFolder;
		initPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * Triggers method according to which button fired it.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == savePNGButton) {

			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"PNG Image (*.png)", "png");
			File fileToSave = saveFile(filter, "Save PNG Image");

			if (fileToSave != null) {
				control.saveImageAsPNG(fileToSave);
			}

		} else if (e.getSource() == saveLayersButton) {

			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Layer File (*.lay)", "lay");
			File fileToSave = saveFile(filter, "Save Layer File");

			saveLayersToFile(fileToSave);

		} else if (e.getSource() == loadLayersButton) {
			JFileChooser fileChooser = new JFileChooser(rootFolder);
			fileChooser.setDialogTitle("open Layer File");
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Layer File (*.lay)", "lay");
			fileChooser.setFileFilter(filter);
			int returnVal = fileChooser.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (file.exists() && file.isFile()) {
					loadLayersFromFile(file);

				}

			}
		}

	}

	/**
	 * Enable controls. Currently used for disabling/enabling save buttons when
	 * there are no layers
	 * 
	 * @param enable
	 *            true if buttons should be enabled
	 */
	public void enableControls(boolean enable) {
		saveLayersButton.setEnabled(enable);
		savePNGButton.setEnabled(enable);
	}

	/**
	 * Initialises the panel, buttons and adds action listeners
	 */
	private void initPanel() {
		savePNGButton = new JButton("Export current image as PNG");
		saveLayersButton = new JButton("Save layers");
		loadLayersButton = new JButton("Load layers");

		savePNGButton.addActionListener(this);
		saveLayersButton.addActionListener(this);
		loadLayersButton.addActionListener(this);

		// initial setting for buttons/controls
		enableControls(false);

		add(savePNGButton);
		add(saveLayersButton);
		add(loadLayersButton);

	}

	/**
	 * Load layers from a file.
	 * 
	 * Unchecked warnings suppressed for cast from object to List
	 * 
	 * @param fileToLoad
	 *            the file to load from
	 */
	@SuppressWarnings("unchecked")
	private void loadLayersFromFile(File fileToLoad) {

		try {
			FileInputStream fileInputStream = new FileInputStream(fileToLoad);
			ObjectInputStream objectInputStream = new ObjectInputStream(
					fileInputStream);

			// load version
			Object versionObject = objectInputStream.readObject();
			String versionString = "";

			// check valid
			if (versionObject instanceof String) {
				// file has string, likely right format
				versionString = (String) versionObject;
			} else {
				// invalid file
				JOptionPane.showMessageDialog(null,
						MessageText.ButtonInvalidLayer,
						MessageText.ButtonInvalidLayerTitle,
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (!versionString.equals(layerFileVersion)) {
				// Invalid version
				JOptionPane.showMessageDialog(null,
						MessageText.ButtonVersionMatchFailed,
						MessageText.ButtonVersionMatchFailedTitle,
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// load layer counter
			Object counterObj = objectInputStream.readObject();
			int counter = 0;
			if (counterObj instanceof Integer) {
				counter = (Integer) counterObj;
				control.setLayerCounter(counter);
			}

			// load list of paths
			Object listObj = objectInputStream.readObject();

			List<Layer> layers = new ArrayList<Layer>();
			if (listObj instanceof List) {
				layers = (List<Layer>) listObj;
				control.setLayers(layers);
			}

			objectInputStream.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (ClassNotFoundException e3) {
			e3.printStackTrace();
		}
	}

	/**
	 * Prepares a file for saving by another method using a file chooser
	 * 
	 * @param filter
	 *            The file filter to allow saving to
	 * @param title
	 *            Title of the dialog box
	 * @return The file to save to, else null if user cancelled
	 */
	private File saveFile(FileNameExtensionFilter filter, String title) {

		JFileChooser filechooser = new JFileChooser(rootFolder);
		filechooser.setFileFilter(filter);
		filechooser.setDialogTitle(title);

		int chooserVal = filechooser.showSaveDialog(this);
		if (chooserVal == JFileChooser.APPROVE_OPTION) {
			File file = filechooser.getSelectedFile();
			// System.out.println("Save to: " + file.getName());
			// System.out.println("Save path: " + file.getParent());

			// Check file name extension
			String extension = filter.getExtensions()[0];
			String currentFilename = file.getName();

			if (!currentFilename.toLowerCase().endsWith("." + extension)) {
				currentFilename += "." + extension;
			}

			// System.out.println(currentFilename);
			File fileToSave = new File(file.getParentFile(), currentFilename);

			if (fileToSave.exists()) {
				// confirm overwrite
				int n = JOptionPane.showConfirmDialog(this,
						MessageText.ButtonOverwriteFile,
						MessageText.ButtonOverwriteFileTitle,
						JOptionPane.YES_NO_OPTION);

				if (n == JOptionPane.YES_OPTION) { // user clicked yes
					return fileToSave;
				}

			} else {
				// save immediately
				return fileToSave;
			}

		}

		return null;

	}

	/**
	 * Save layers to file.
	 * 
	 * @param fileToSave
	 *            the file to save to
	 */
	private void saveLayersToFile(File fileToSave) {

		if (fileToSave == null) {
			return;
		}
		List<Layer> layers = new ArrayList<Layer>();
		layers.addAll(control.getLayers());
		// System.out.println("button layers:" + layers);

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(fileToSave);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(
					fileOutputStream);

			// save file version
			objectOutputStream.writeObject(layerFileVersion);
			// save layer counter
			int counter = control.getLayerCounter();
			objectOutputStream.writeObject(counter);
			// save list of paths
			objectOutputStream.writeObject(layers);

			objectOutputStream.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

	}
}
