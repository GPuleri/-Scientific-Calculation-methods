import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;

public class View extends JFrame {
	private static final long serialVersionUID = 1L;

	private final Controller controller;

	private JPanel northPanel;
	private BufferedImage originalImage;
	private JPanel originalImagePanel;
	private JScrollPane originalImageScrollPane;
	private BufferedImage compressedImage;
	private final JSplitPane splitPane;
	private JPanel compressedImagePanel;
	private JScrollPane compressedImageScrollPane;
	private JPanel southPanel;

	private JTextField nField;
	private JButton executeDCT2Button;
	private JButton executeDCT2ComparisonButton;

	private JButton selectImageButton;
	private JTextField fField;
	private JTextField dField;
	private JButton compressButton;

	private JLabel originalImageLabel;
	private JLabel compressedImageLabel;	
	private JLabel compressionRateLabel;

	public View() {
		this.controller = new Controller();
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Progetto 2");
		this.createNorthPanel();
		this.createOriginalImagePanel();
		this.createCompressedImagePanel();
		this.createSouthPanel();
		this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		this.splitPane.setLeftComponent(this.originalImageScrollPane);
		this.splitPane.setRightComponent(this.compressedImageScrollPane);
		this.add(this.northPanel, BorderLayout.NORTH);
		this.add(this.splitPane, BorderLayout.CENTER);
		this.add(this.southPanel, BorderLayout.SOUTH);
		this.splitPane.setResizeWeight(0.5);
		this.createActionListener();
		this.setVisible(true);
		this.executeDCT2Button.setEnabled(true);
		this.executeDCT2ComparisonButton.setEnabled(true);
		this.compressButton.setEnabled(false);
	}

	private void createActionListener() {
		this.selectImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectImage();
			}
		});
		this.compressButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				compressImage();
			}
		});
		this.executeDCT2Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DCT2();
			}
		});
		this.executeDCT2ComparisonButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comparisonDCT2();
			}
		});
	}

	private void createNorthPanel() {
		this.northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.nField = new JTextField(3);
		this.northPanel.add(new JLabel("N: "));
		this.northPanel.add(this.nField);

		this.executeDCT2Button = new JButton("Esegui DCT2");
		this.northPanel.add(this.executeDCT2Button);

		this.northPanel.add(Box.createRigidArea(new Dimension(30, 0)));

		this.executeDCT2ComparisonButton = new JButton("Compara algoritmi DCT2");
		this.northPanel.add(this.executeDCT2ComparisonButton);

		this.northPanel.add(Box.createRigidArea(new Dimension(30, 0)));

		this.selectImageButton = new JButton("Carica immagine");
		this.northPanel.add(this.selectImageButton);

		this.fField = new JTextField(4);
		this.northPanel.add(new JLabel("F: "));
		this.northPanel.add(this.fField);

		this.dField = new JTextField(4);
		this.northPanel.add(new JLabel("d: "));
		this.northPanel.add(this.dField);

		this.compressButton = new JButton("Confronta immagini");
		this.northPanel.add(this.compressButton);
	}

	private void createOriginalImagePanel() {
		this.originalImagePanel = new JPanel(new FlowLayout());
		this.originalImageScrollPane = new JScrollPane(this.originalImagePanel);
		this.originalImagePanel.setBorder(BorderFactory.createEtchedBorder());
		this.originalImageLabel = new JLabel();
		//this.originalImageLabel.setPreferredSize(new Dimension(800, 800));
		this.originalImagePanel.add(this.originalImageLabel, BorderLayout.CENTER);
	}

	private void createCompressedImagePanel() {
		this.compressedImagePanel = new JPanel(new FlowLayout());
		this.compressedImageScrollPane = new JScrollPane(this.compressedImagePanel);
		this.compressedImagePanel.setBorder(BorderFactory.createEtchedBorder());
		this.compressedImageLabel = new JLabel();
		//this.compressedImageLabel.setPreferredSize(new Dimension(800, 800));
		this.compressedImagePanel.add(this.compressedImageLabel, BorderLayout.CENTER);
	}

	private void createSouthPanel() {
		this.southPanel = new JPanel();
		this.compressionRateLabel = new JLabel();
		this.southPanel.add(this.compressionRateLabel);
	}

	private void selectImage() {
		JFileChooser input = new JFileChooser(System.getProperty("user.dir") + 
				File.separator + "immagini");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Immagini BMP", "bmp");
		input.setFileFilter(filter);
		if (input.showOpenDialog(this.selectImageButton) == JFileChooser.APPROVE_OPTION) {
			File file = input.getSelectedFile();
			try {
				FileInputStream stream = new FileInputStream(file);
				this.originalImage = ImageIO.read(stream);
				this.controller.setOriginalSize(file.length());
				this.showImage(this.originalImage);
				this.showCompressedImage(this.originalImage);
				this.compressButton.setEnabled(true);
				this.compressionRateLabel.setText("Immagine caricata");
			} catch (IOException ex) { 
				this.compressionRateLabel.setText("Impossibile caricare questa immagine"); 
			}			
		}
	}

	private void showImage(BufferedImage image) {
		this.originalImageLabel.setIcon(new ImageIcon(image));
		this.originalImageLabel.setSize(image.getWidth(), image.getHeight());
		this.originalImagePanel.repaint();
	}

	private void showCompressedImage(BufferedImage image) {
		this.compressedImage = this.controller.getDCT2Image(image);
		this.compressedImageLabel.setIcon(new ImageIcon(image));
		this.compressedImageLabel.setSize(image.getWidth(), image.getHeight());
		this.compressedImagePanel.repaint();
	}

	private void compressImage() {
		try {
			int f = Integer.parseInt(this.fField.getText());
			int d = Integer.parseInt(this.dField.getText());

			if (f >= 0 && f <= this.originalImage.getWidth() && f <= this.originalImage.getHeight()
					&& d >= 0 && d <= ((2 * f) - 2)) {
				this.compressedImage = this.controller.compressImage(this.originalImage, f, d);
				this.compressedImageLabel.setIcon(new ImageIcon(this.compressedImage));
				this.compressedImageLabel.setSize(this.compressedImage.getWidth(),
						this.compressedImage.getHeight());
				this.compressedImagePanel.repaint();

				this.compressionRateLabel.setText("Confronto eseguito. ");
			}
			else {
				this.compressionRateLabel.setText("Inserire dei numeri validi");
			}
		} catch (NumberFormatException nfe) {
			this.compressionRateLabel.setText("Inserire dei numeri validi"); 
		}
	}

	private void DCT2() {
		try {
			int n = Integer.parseInt(this.nField.getText());
			this.controller.DCT2(n, null);
		} catch(NumberFormatException nfe) {
			this.compressionRateLabel.setText("Inserire un numero valido"); 
		}
		this.compressionRateLabel.setText("DCT2 eseguito");
	}

	private void comparisonDCT2 () {
		this.controller.comparisonDCT2();
		this.compressionRateLabel.setText("Comparazione DCT2 eseguita");
	}
}