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

public class ButtonPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -324718791157821176L;
	private Controller control;
	private JButton loadLayersButton;
	private JButton saveLayersButton;
	private JButton savePNGButton;

	// private List<Layer> layers;

	public ButtonPanel(Controller control) {
		this.control = control;
		initPanel();
	}

	@SuppressWarnings("unchecked")
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
			// TODO
			List<Layer> layers = new ArrayList<Layer>();
			layers.addAll(control.getLayers());
			System.out.println("button layers:" + layers);

			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Layer File (*.lay)", "lay");
			File fileToSave = saveFile(filter, "Save Layer File");

			if (fileToSave != null) {
				// output list to file
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(
							fileToSave);

					ObjectOutputStream objectOutputStream = new ObjectOutputStream(
							fileOutputStream);
					objectOutputStream.writeObject(layers);
					objectOutputStream.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}

		} else if (e.getSource() == loadLayersButton) {
			JFileChooser fileChooser = new JFileChooser(".");
			fileChooser.setDialogTitle("open Layer File");
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Layer File (*.lay)", "lay");
			fileChooser.setFileFilter(filter);
			int returnVal = fileChooser.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if (file.exists() && file.isFile()) {
					// Read object from file
					FileInputStream fileInputStream;
					try {
						fileInputStream = new FileInputStream(file);
						ObjectInputStream objectInputStream = new ObjectInputStream(
								fileInputStream);

						Object tempObj = objectInputStream.readObject();
						List<Layer> layers = new ArrayList<Layer>();
						if (tempObj instanceof List) {
							layers = (List<Layer>) tempObj;
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

			}
		}

	}

	public void enableControls(boolean enable) {
		saveLayersButton.setEnabled(enable);
	}

	private void initPanel() {
		// this.setLayout(new BorderLayout());
		savePNGButton = new JButton("Export current image as PNG");
		saveLayersButton = new JButton("Save layers");
		loadLayersButton = new JButton("Load layers");

		savePNGButton.addActionListener(this);
		saveLayersButton.addActionListener(this);
		loadLayersButton.addActionListener(this);

		saveLayersButton.setEnabled(false);
		// loadLayersButton.setEnabled(false);

		add(savePNGButton);
		add(saveLayersButton);
		add(loadLayersButton);

	}

	/**
	 * Prepares a file for saving by another method.
	 * 
	 * @param filter
	 *            The file filter
	 * @param title
	 *            Title of the dialog box
	 * @return The file to save to, else null if user cancelled
	 */
	private File saveFile(FileNameExtensionFilter filter, String title) {

		JFileChooser filechooser = new JFileChooser(".");
		filechooser.setFileFilter(filter);
		filechooser.setDialogTitle(title);

		int chooserVal = filechooser.showSaveDialog(this);
		if (chooserVal == JFileChooser.APPROVE_OPTION) {
			File file = filechooser.getSelectedFile();
			// System.out.println("Save to: " + file.getName());
			// System.out.println("Save path: " + file.getParent());

			// Check file name extension
			boolean append = false;
			String ext = filter.getExtensions()[0];

			String currentFilename = file.getName();
			if (currentFilename.length() < 5) {
				// append extension
				append = true;
			} else {
				// split around '.'
				String[] splitName = currentFilename.split("\\.");
				if (splitName.length == 1) {
					// has no extension, append it
					append = true;
				} else {
					String extension = splitName[splitName.length - 1];
					// check last segment to see if it has right extension, if
					// not, append
					if (!extension.equalsIgnoreCase(ext)) {
						append = true;
					}
				}
			}

			if (append) {
				// System.out.println("appending");

				currentFilename += "." + ext;
				// System.out.println(ext);

			}
			// System.out.println(currentFilename);
			File fileToSave = new File(file.getParentFile(), currentFilename);

			if (fileToSave.exists()) {
				// confirm overwrite
				int n = JOptionPane.showConfirmDialog(this,
						"Are you sure you wish to overwrite this file?",
						"Confirm Overwrite", JOptionPane.YES_NO_OPTION);

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

}
