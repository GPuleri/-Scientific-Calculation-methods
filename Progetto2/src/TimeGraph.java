import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class TimeGraph extends JFrame {
	private static final long serialVersionUID = 1L;

	public static void Graph3D(double durationMyDCT2, double durationJTransform) {
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(durationJTransform, "JTransforms", "");
		dataset.addValue(durationMyDCT2, "myDCT2", "");
		
		JFreeChart chart = ChartFactory.createBarChart3D(
				"Tempo d'esecuzione DCT2", "", "Tempo (secondi)", dataset,
				PlotOrientation.VERTICAL, true, true, true);
		
		ChartFrame chartFrame = new ChartFrame("Tempo d'esecuzione DCT2", chart);
		chartFrame.setVisible(true);
		chartFrame.setSize(450, 650);
	}
}