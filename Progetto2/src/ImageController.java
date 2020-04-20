import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import org.jtransforms.dct.DoubleDCT_2D;

public class ImageController {
	private double[][] image;
	private int width;
	private int height;
	private int f;
	private int d;

	public ImageController() {
	}

	public BufferedImage compressImage(BufferedImage image, int f, int d) {
		this.f = f;
		this.d = d;
		this.image = imageToPixels(image);
		
		// number of image's block 
		int nImgWidth = this.getWidth() / this.getF();
		int nImgHeight = this.getHeight() / this.getF();

		// divide the whole image in nImgWidth * nImgHeight images
		double[][][] img = new double[nImgWidth * nImgHeight][this.getF()][this.getF()];
		for (int k = 0; k < nImgWidth; k++) {
			for (int w = 0; w < nImgHeight; w++) {
				for (int i = 0; i < this.getF(); i++) {
					for (int j = 0; j < this.getF(); j++) {
						img[k * nImgHeight + w][i][j] = 
								this.image[k * this.getF() + i][w * this.getF() + j];
					}
				}
			}
		}

		DoubleDCT_2D idct2 = new DoubleDCT_2D(this.getF(), this.getF());
		
		// for every block
		for (int n = 0; n < nImgWidth * nImgHeight; n++) {
			// apply DCT
			idct2.forward(img[n], true);
			
			// delete frequencies where k + l >= d
			for (int k = 0; k < this.getF(); k++) {
				for (int l = 0; l < this.getF(); l++) {
					if (k + l >= this.getD())
						img[n][k][l] = 0;
				}
			}
			
			// apply reverse DCT
			idct2.inverse(img[n], true);
			for (int i = 0; i < this.getF(); i++) {
				for (int j = 0; j < this.getF(); j++) {
					// round and set consistent data
					Math.round(img[n][i][j]);
					if (img[n][i][j] > 255.0)
						img[n][i][j] = 255;
					else if (img[n][i][j] < 0.0) {
						img[n][i][j] = 0;
					}
				}
			}
		}

		// recompose the whole image
		for (int k = 0; k < nImgWidth; k++) {
			for (int w = 0; w < nImgHeight; w++) {
				for (int i = 0; i < this.getF(); i++) {
					for (int j = 0; j < this.getF(); j++) {
						this.image[k * this.getF() + i][w * this.getF() + j] =
								img[k * nImgHeight + w][i][j]; 
					}
				}
			}
		}
		
		return this.pixelsToImage(this.image);
	}

	public double[][] imageToPixels(BufferedImage image) {
		Raster raster = image.getData();
		this.width = raster.getWidth();
		this.height = raster.getHeight();
		double[][] pixels = new double[this.getWidth()][this.getHeight()];
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {
				pixels[i][j] = raster.getSample(i, j, 0);
			}
		}

		return pixels;
	}

	public BufferedImage pixelsToImage(double[][] pixels) {
		BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster wr = image.getRaster();
		for (int i = 0; i < this.getWidth(); i++) {
			for (int j = 0; j < this.getHeight(); j++) {
				wr.setSample(i, j, 0, pixels[i][j]);
			}
		}
		return image;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getF() {
		return f;
	}

	public int getD() {
		return d;
	}
}