import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import org.jtransforms.dct.DoubleDCT_2D;

public class Controller {
	private ImageController imageController;
	private final myDCT2 dct2Controller;
	private long originalSize;
	private long compressedSize;

	public Controller() {
		this.imageController = new ImageController();
		this.dct2Controller = new myDCT2();
	}

	public BufferedImage getDCT2Image(BufferedImage originalImage) {
		imageController = new ImageController();
		double[][] newImage = imageController.imageToPixels(originalImage);
		DoubleDCT_2D jTransforms = new DoubleDCT_2D(newImage.length, newImage[0].length);
		jTransforms.forward(newImage, true);
	
		return imageController.pixelsToImage(newImage);
	}

	public BufferedImage compressImage(BufferedImage image, int f, int d) {
		BufferedImage output = this.imageController.compressImage(image, f, d);
		File outputFile = new File("CompressedImage.bmp");
		try {
			ImageIO.write(output, "bmp" , outputFile);
		} catch (IOException e)
		{ 
			System.out.println("Errore nella compressione dell'immagine");
		}
		this.compressedSize = outputFile.length();
		return output;
	}
	
	public long getOriginalSize() {
		return originalSize;
	}
	
	public long getCompressedSize() {
		return compressedSize;
	}
	
	public void setOriginalSize(long originalSize) {
		this.originalSize = originalSize;
	}
	
	public double[][] DCT2(double[][] matrix) {
		this.dct2Controller.DCT2(matrix);
		return matrix;
	}
	
	public double[][] DCT2(int n, FileWriter file) {
		double matrix[][] = new double[n][n];
		double matrix2[][] = new double[n][n];
		DoubleDCT_2D jTransforms = new DoubleDCT_2D(n, n);
		
		Random rand = new Random();
		double rValue;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				rValue = rand.nextInt(255);
				matrix[i][j] = rValue;
				matrix2[i][j] = rValue;
			}
		}
		
		// calculate myDCT2 execution time in seconds
		long startMyDCT2 = System.nanoTime();
		this.dct2Controller.DCT2(matrix);
		long endMyDCT2 = System.nanoTime();
		double durationMyDCT2 = 
				Double.parseDouble(Long.toString(endMyDCT2 - startMyDCT2)) / Math.pow(10, 9);

		// calculate jTransforms execution time in seconds
		long startJTransform = System.nanoTime();
		jTransforms.forward(matrix2, true);
		long endJTransform = System.nanoTime();
		double durationJTransform = 
				Double.parseDouble(Long.toString(endJTransform - startJTransform)) / Math.pow(10, 9);

		// print TimeGraph or write on file
		if(file == null)
			TimeGraph.Graph3D(durationMyDCT2, durationJTransform);
		else {
			try {
				file.append(Integer.toString(n) + ",");
				file.append(Double.toString(durationMyDCT2) + ",");  
				file.append(Double.toString(durationJTransform) + "\n");  
			} catch (IOException e) {
				System.out.println("Errore nella scrittura del file");
			}
		}
		return matrix;
	}
	
	// calculate both DCT2 and write on file
	public void comparisonDCT2() {
		int[] dim = {10, 50, 100, 200, 300,
				500, 750, 1000, 1500, 2000
				};
		FileWriter csvWriter;
		try {
			csvWriter = new FileWriter("ComparisonDCT2.csv");
			csvWriter.append("Dimensione,myDCT2,jTransforms\n");
			for(int i = 0; i < dim.length; i++)
				this.DCT2(dim[i], csvWriter);
			csvWriter.flush();  
			csvWriter.close();
		} catch (IOException e) {
			System.out.println("Errore nella creazione del file");
		}
	}
}