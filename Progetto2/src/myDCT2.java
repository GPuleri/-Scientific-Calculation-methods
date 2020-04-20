public class myDCT2 {

	public myDCT2() {}

	public void DCT1(double[] f) {
		double[] app = new double [f.length];
		double alphak;
		for(int k = 0; k < f.length; k++) {
			if(k == 0)
				alphak = Math.sqrt(1.0) / Math.sqrt(f.length);
			else
				alphak = Math.sqrt(2.0) / Math.sqrt(f.length);
			double sum = 0;
			for (int i = 0; i < f.length; i++)
				sum += f[i] * Math.cos(k * Math.PI * ((2 * i + 1) / (2.0 * f.length)));
			app[k] = alphak * sum;
		}
		
		for(int i = 0; i < f.length; i++)
			f[i] = app[i];
	}

	public void DCT2(double[][] f) {
		double[][] app = new double [f[0].length][f.length];

		// transpose the matrix
		for(int i = 0; i < f[0].length; i++) 
			for(int j = 0; j < f.length; j++) 
				app[i][j] = f[j][i];

		// calculate monodimensional DCT on "column"
		for(int i = 0; i < f[0].length; i++)
			DCT1(app[i]);

		// retranspose the matrix
		for(int i = 0; i < f.length; i++) 
			for(int j = 0; j < f[0].length; j++) 
				f[i][j] = app[j][i];

		// calculate monodimensional DCT on rows
		for(int i = 0; i < f.length; i++)
			DCT1(f[i]);
	}
}