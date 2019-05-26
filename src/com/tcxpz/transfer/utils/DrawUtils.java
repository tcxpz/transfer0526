package com.tcxpz.transfer.utils;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DrawUtils {
	//在series对象中添加数据
	public static void add(XYSeries series,double x,double y){
		series.add(new XYDataItem(x, y));
	}
	//绘制图形
	public static void draw(XYSeries series,String curveName,String xAxis,String yAxis){
		XYSeriesCollection dataset = new XYSeriesCollection();		
		dataset.addSeries(series);		
		JFreeChart chart = ChartFactory.createXYLineChart(				
				curveName, // 图形名称				
				xAxis, // 横坐标				
				yAxis, // 纵坐标				
				dataset, // data				
				PlotOrientation.VERTICAL,				
				false, // include legend				
				false, // tooltips				
				false // urls				
				); 		
		ChartFrame frame = new ChartFrame("my picture", chart);		
		frame.pack();		
		frame.setVisible(true);		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
	}
}
