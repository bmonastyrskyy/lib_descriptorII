package edu.ucdavis.gc.bm.descriptor2kind_visualisation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import edu.ucdavis.gc.bm.descriptor2kind.DescriptorII;

public class DescriptorIIPairCanvas extends JPanel {

	private Integer edgeXSize = 10;

	private Integer fontSize = edgeXSize;

	private int edgeYSize = edgeXSize;

	private String fastaSeq = null;

	private String ssSeq = null;

	private int widthLine = 0;

	private int width = 0;

	private int height = 0;

	private int[][] map1 = null;

	private int[][] map2 = null;

	private int shiftH1 = 0;

	private int shiftV1 = 0;

	private int shiftH2 = 0;

	private int shiftV2 = 0;

	private boolean fVisualToFile = false;

	private String targetName = null;

	/**
	 * object used to write the image to file
	 */
	private BufferedImage image = null;

	public DescriptorIIPairCanvas() {

	}

	public DescriptorIIPairCanvas(int[][] map1, int[][] map2, String corr) {
		this.map1 = map1;
		this.map2 = map2;
		this.calcShifts(corr);
		this.calcDimensions();
		this.setPreferredSize(new Dimension(width, height));
	}

	/**
	 * corr = i1_j1:i2_j2
	 * 
	 * @param corr
	 */
	private void calcShifts(String corr) {
		String[] tokens = corr.split(":");
		String[] tokens1 = tokens[0].split("_");
		String[] tokens2 = tokens[1].split("_");
		if (Integer.valueOf(tokens1[0]) > Integer.valueOf(tokens2[0])) {
			shiftH1 = 0;
			shiftH2 = Integer.valueOf(tokens1[0]) - Integer.valueOf(tokens2[0]);
		} else {
			shiftH1 = -(Integer.valueOf(tokens1[0]) - Integer
					.valueOf(tokens2[0]));
			shiftH2 = 0;
		}
		if (Integer.valueOf(tokens1[1]) > Integer.valueOf(tokens2[1])) {
			shiftV1 = 0;
			shiftV2 = Integer.valueOf(tokens1[1]) - Integer.valueOf(tokens2[1]);
		} else {
			shiftV1 = -(Integer.valueOf(tokens1[1]) - Integer
					.valueOf(tokens2[1]));
			shiftV2 = 0;
		}
	}

	private void calcDimensions() {
		if (shiftH1 + map1.length > shiftH2 + map2.length) {
			width = (shiftH1 + map1.length) * this.edgeXSize;
		} else {
			width = (shiftH2 + map2.length) * this.edgeXSize;
		}
		if (shiftV1 + map1[0].length > shiftV2 + map2[0].length) {
			height = (shiftV1 + map1[0].length) * this.edgeYSize;
		} else {
			height = (shiftV2 + map2[0].length) * this.edgeYSize;
		}
	}

	public int getEdgeXSize() {
		return edgeXSize;
	}

	public void setEdgeXSize(int edgeXSize) {
		this.edgeXSize = edgeXSize;
	}

	public int getEdgeYSize() {
		return edgeYSize;
	}

	public void setEdgeYSize(int edgeYSize) {
		this.edgeYSize = edgeYSize;
	}

	public void paintImage() {
		// this part of code handles with writing to file
		Graphics2D g2 = image.createGraphics();
		super.paintComponent(g2);
		this.drawFilledCells(g2);
		this.drawGrid(g2);
		g2.drawImage(image, null, 0, 0);

		try {
			writeImageToFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		this.drawFilledCells(g2);
		this.drawGrid(g2);
	}


	private void drawFastaSeq(Graphics2D g) {
		int y = 4 * this.fontSize;
		int x = 0;
		int xEnd = this.widthLine;
		Font f = new Font("Arrial", Font.BOLD, 10);
		g.setFont(f);
		char[] arrFasta = this.fastaSeq.toUpperCase().toCharArray();
		int index = 0;
		while (x < xEnd) {
			Character curLetter = arrFasta[index];
			g.drawString(curLetter.toString(), x, y);
			index++;
			x += this.edgeXSize;
		}
	}

	private void drawSsSeq(Graphics2D g) {
		int y = 5 * this.fontSize;
		int x = 0;
		int xEnd = this.widthLine;
		Font f = new Font("Arrial", Font.BOLD, this.fontSize);
		g.setFont(f);
		char[] arrSS = this.ssSeq.toUpperCase().toCharArray();
		int index = 0;
		while (x < xEnd) {
			Character curLetter = arrSS[index];
			g.drawString(curLetter.toString(), x, y);
			index++;
			x += this.edgeXSize;
		}
	}

	private void drawGrid(Graphics2D g) {
		// draw map1
		int xStart = shiftH1 * this.edgeXSize;
		int yStart = shiftV1 * this.edgeYSize;
		int width = this.edgeXSize;
		int height = this.edgeYSize;
		int Width = map1.length * this.edgeXSize;
		int Height = map1[0].length * this.edgeYSize;
		g.setColor(Color.GRAY);
		while (width <= Width) {
			g.drawRect(xStart, yStart, width, Height);
			width += this.edgeXSize;
		}
		while (height <= Height) {
			g.drawRect(xStart, yStart, Width, height);
			height += this.edgeYSize;
		}
		// draw map2
		g.setColor(Color.GRAY);
		xStart = shiftH2 * this.edgeXSize;
		yStart = shiftV2 * this.edgeYSize;
		width = this.edgeXSize;
		height = this.edgeYSize;
		Width = map2.length * this.edgeXSize;
		Height = map2[0].length * this.edgeYSize;
		while (width <= Width) {
			g.drawRect(xStart, yStart, width, Height);
			width += this.edgeXSize;
		}
		while (height <= Height) {
			g.drawRect(xStart, yStart, Width, height);
			height += this.edgeYSize;
		}
		// draw overlap
		xStart = (shiftH2 > shiftH1 ? shiftH2 : shiftH1) * this.edgeXSize;
		yStart = (shiftV2 > shiftV1 ? shiftV2 : shiftV1) * this.edgeYSize;
		width = (min(shiftH1 + map1.length, shiftH2 + map2.length) - max(
				shiftH1, shiftH2)) * this.edgeXSize;
		height = (min(shiftV1 + map1[0].length, shiftV2 + map2[0].length) - max(
				shiftV1, shiftV2)) * this.edgeYSize;
		g.setColor(Color.RED);
		g.drawRect(xStart, yStart, width, height);

	}

	private void drawFilledCells(Graphics2D g) {

		for (int i = 0; i < width / this.edgeXSize; i++) {
			int xStart = i * this.edgeXSize;
			for (int j = 0; j < height / this.edgeYSize; j++) {
				int yStart = j * this.edgeYSize;
				if (i - shiftH1 >= 0 && i - shiftH1 < map1.length
						&& j - shiftV1 >= 0 && j - shiftV1 < map1[0].length) {
					if (map1[i - shiftH1][j - shiftV1] > 0) {
						g.setColor(Color.BLUE);
						g.drawRect(xStart, yStart, this.edgeXSize,
								this.edgeYSize);
						g.fill(new Rectangle2D.Double((double) xStart, (double) yStart,
								(double) this.edgeXSize, (double) this.edgeYSize));
					}
				}
				if (i - shiftH2 >= 0 && i - shiftH2 < map2.length
						&& j - shiftV2 >= 0 && j - shiftV2 < map2[0].length) {
					if (map2[i - shiftH2][j - shiftV2] > 0) {
						g.setColor(Color.GREEN);
						g.drawRect(xStart, yStart, this.edgeXSize,
								this.edgeYSize);
						g.fill(new Rectangle2D.Double((double) xStart, (double) yStart,
								(double) this.edgeXSize, (double) this.edgeYSize));
					}
				}
				if (i - shiftH1 >= 0 && i - shiftH1 < map1.length
						&& j - shiftV1 >= 0 && j - shiftV1 < map1[0].length
						&& i - shiftH2 >= 0 && i - shiftH2 < map2.length
						&& j - shiftV2 >= 0 && j - shiftV2 < map2[0].length) {
					if (map1[i - shiftH1][j - shiftV1] > 0
							&& map2[i - shiftH2][j - shiftV2] > 0) {
						g.setColor(Color.RED);
						g.drawRect(xStart, yStart, this.edgeXSize,
								this.edgeYSize);
						g.fill(new Rectangle2D.Double((double) xStart, (double) yStart,
								(double) this.edgeXSize, (double) this.edgeYSize));
					}
				}
			}

		}


	}

	public void writeImageToFile() throws IOException {

		String fileName = targetName + "_" + "contmap.png";
		File file = new File(fileName);
		ImageIO.write(image, "png", file);

	}

	private int min(int a, int b) {
		if (a < b) {
			return a;
		} else {
			return b;
		}
	}

	private int max(int a, int b) {
		if (a > b) {
			return a;
		} else {
			return b;
		}
	}

}
