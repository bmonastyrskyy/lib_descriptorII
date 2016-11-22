package edu.ucdavis.gc.bm.descriptor2kind_visualisation;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class VisualisatorDescriptorIIPair {
	
	private int [][] map1 ;
	
	private int [][] map2;
	
	private String corr; 
	
	private String title;
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setMapsCorr(int [][] map1, int[][] map2, String corr){
		this.map1 = map1;
		this.map2 = map2;
		this.corr =corr;
	}
	public void createAndShowGUI() {

		JFrame frame = new JFrame("DescriptorII comparison: " + title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DescriptorIIPairCanvas canvas = new DescriptorIIPairCanvas(map1, map2, corr);
		JScrollPane scrollPane = new JScrollPane(canvas);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame.setContentPane(scrollPane);
		frame.setPreferredSize(canvas.getPreferredSize());
		frame.pack();
		frame.setVisible(true);
	}

}
