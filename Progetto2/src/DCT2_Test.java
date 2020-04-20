import static org.junit.jupiter.api.Assertions.*;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.jtransforms.dct.DoubleDCT_1D;
import org.jtransforms.dct.DoubleDCT_2D;
import org.junit.jupiter.api.Test;

class DCT2_Test {

	DoubleDCT_2D jTransforms = new DoubleDCT_2D(8, 8);
	myDCT2 _myDCT2 = new myDCT2();

	double[][] matrix = new double[][] {
		{ 231 , 32 , 233 , 161 , 24 , 71 , 140 , 245 },
		{ 247 , 40 , 248 , 245 , 124 , 204 , 36 , 107 },
		{ 234 , 202 , 245 , 167 , 9 , 217 , 239 , 173 },
		{ 193 , 190 , 100 , 167 , 43 , 180 , 8 , 70 },
		{ 11 , 24 , 210 , 177 , 81 , 243 , 8 , 112 },
		{ 97 , 195 , 203 , 47 , 125 , 114 , 165 , 181 },
		{ 193 , 70 , 174 , 167 , 41 , 30 , 127 , 245 },
		{ 87 , 149 , 57 , 192 , 65 , 129 , 178 , 228 }
	};

	String[][] result = new String[][] {
		{"1,11e03", "4,40e01", "7,59e01", "-1,38e02", "3,50e00", "1,22e02", "1,95e02", "-1,01e02"},
		{"7,71e01", "1,14e02", "-2,18e01", "4,13e01", "8,77e00", "9,90e01", "1,38e02", "1,09e01"},
		{"4,48e01", "-6,27e01", "1,11e02", "-7,63e01", "1,24e02", "9,55e01", "-3,98e01", "5,85e01"},
		{"-6,99e01", "-4,02e01", "-2,34e01", "-7,67e01", "2,66e01", "-3,68e01", "6,61e01", "1,25e02"},
		{"-1,09e02", "-4,33e01", "-5,55e01", "8,17e00", "3,02e01", "-2,86e01", "2,44e00", "-9,41e01"},
		{"-5,38e00", "5,66e01", "1,73e02", "-3,54e01", "3,23e01", "3,34e01", "-5,81e01", "1,90e01"},
		{"7,88e01", "-6,45e01", "1,18e02", "-1,50e01", "-1,37e02", "-3,06e01", "-1,05e02", "3,98e01"},
		{"1,97e01", "-7,81e01", "9,72e-01", "-7,23e01", "-2,15e01", "8,12e01", "6,37e01", "5,90e00"},
	};

	DoubleDCT_1D jTransforms1D = new DoubleDCT_1D(8);
	double[] array = new double[]{231, 32, 233, 161, 24, 71, 140, 245};
	double[][] myArray = new double[][]{ {231, 32, 233, 161, 24, 71, 140, 245} };

	String[] result1D = new String[] { 
			"4,01e02", "6,60e00", "1,09e02", "-1,12e02", "6,54e01", "1,21e02", "1,16e02", "2,88e01"
	};


	@Test
	void testArrayJTransforms() {
		// set exponential format and truncate from 3rd decimal number 
		DecimalFormat df = new DecimalFormat("0.00E00");
		df.setRoundingMode(RoundingMode.DOWN);
		
		jTransforms1D.forward(array, true);
		for (int i = 0; i < array.length; i++) {
			assertEquals(result1D[i], df.format(array[i]).toLowerCase());
		}
	}

	@Test
	void testMatriceJTransforms() {
		DecimalFormat df = new DecimalFormat("0.00E00");
		df.setRoundingMode(RoundingMode.DOWN);
		
		jTransforms.forward(matrix, true);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				assertEquals(result[i][j], df.format(matrix[i][j]).toLowerCase());
			}
		}
	}

	@Test
	void testArrayMyDCT() {
		DecimalFormat df = new DecimalFormat("0.00E00");
		df.setRoundingMode(RoundingMode.DOWN);
		
		_myDCT2.DCT1(array);
		for (int i = 0; i < myArray.length; i++) {
				assertEquals(result1D[i], df.format(array[i]).toLowerCase());
		}
		
		_myDCT2.DCT2(myArray);
		for (int i = 0; i < myArray.length; i++) {
			for (int j = 0; j < myArray[0].length; j++) {
				assertEquals(result1D[j], df.format(myArray[i][j]).toLowerCase());
			}
		}
	}

	@Test
	void testMatriceMyDCT2() {
		DecimalFormat df = new DecimalFormat("0.00E00");
		df.setRoundingMode(RoundingMode.DOWN);
		
		_myDCT2.DCT2(matrix);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				assertEquals(result[i][j], df.format(matrix[i][j]).toLowerCase());
			}
		}
	}
}