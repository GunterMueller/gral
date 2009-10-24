package openjchart.examples;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import openjchart.charts.BarChart;
import openjchart.data.DataSeries;
import openjchart.data.DataTable;

public class SimpleBarChart extends JFrame {

	public SimpleBarChart() {
		super("OpenJChartTest");
		final DataTable data = new DataTable(Integer.class, Integer.class, Integer.class);
		data.add(1, 1, 6);
		data.add(2, 3, 8);
		data.add(3, 2, 2);
		data.add(4, 6, 6);
		data.add(5, 4, 8);
		data.add(6, 8, 18);
		data.add(7, 9, 9);
		data.add(8, 11, 1);
		DataSeries series = new DataSeries();
		series.put("0", 0);
		series.put("1", 1);
		series.put("2", 2);
		BarChart chart = new BarChart(data, series);
		//chart.setBorder(new EmptyBorder(20, 20, 20, 20));
		getContentPane().add(chart, BorderLayout.CENTER);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
	}

	public static void main(String[] args) {
		SimpleBarChart test = new SimpleBarChart();
		test.setVisible(true);
	}
}