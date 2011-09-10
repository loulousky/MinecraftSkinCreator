package skinCreator;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class SkinGUI extends JFrame {
	private static final long serialVersionUID = -1146800151202328550L;
	private Controller control;

	public static void main(String[] args) {
		new SkinGUI();
	}

	public SkinGUI() {
		initGUI();
	}

	private void initGUI() {
		setTitle("Skin Creator");
		setSize(800, 600);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());

		control = new Controller();

		JFileChooser folderChooser = new JFileChooser(".");
		folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		folderChooser.setDialogTitle("Open base directory for layers");

		int returnVal = folderChooser.showOpenDialog(this);

		File file;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = folderChooser.getSelectedFile();

		} else {
			file = new File(".");
		}

		// OptionsPanel options = new OptionsPanel(control, new
		// File("./Delmark"));
		// OptionsPanel options = new OptionsPanel(control, new File("."));
		OptionsPanel options = new OptionsPanel(control, file);
		control.setOptionsPanel(options);
		rightPanel.add(options, BorderLayout.CENTER);

		LayersPanel layers = new LayersPanel(control);
		control.setLayersPanel(layers);
		leftPanel.add(layers, BorderLayout.CENTER);

		ImgPanel imagePanel = new ImgPanel();
		control.setImgPanel(imagePanel);
		leftPanel.add(imagePanel, BorderLayout.SOUTH);

		ButtonPanel buttonPanel = new ButtonPanel(control);
		control.setButtonPanel(buttonPanel);
		rightPanel.add(buttonPanel, BorderLayout.SOUTH);

		panel.add(rightPanel, BorderLayout.CENTER);
		panel.add(leftPanel, BorderLayout.WEST);

		Container contentPane = getContentPane();
		contentPane.add(panel);
		setVisible(true);
		setLocationRelativeTo(null);

	}
}
