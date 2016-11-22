package edu.ucdavis.gc.bm.descriptor2kind;

import java.util.ArrayList;
import java.util.List;

public class DescriptorII_Util {

	private DescriptorII desc;

	private List<String> centerDots = null;

	private int marginX = 4;

	private int marginY = 4;

	private int minNumberContactsCutOff = 2;

	public DescriptorII_Util(DescriptorII desc) {
		this.desc = desc;
	}

	public void setmarginX(int marginX) {
		this.marginX = marginX;
	}

	public void setMarginY(int marginY) {
		this.marginY = marginY;
	}

	public List<String> getCenterDots(){
		if (centerDots == null){
			centerDots = new ArrayList<String>();
			//System.out.println("inside getCenterDots");
			checkCenterDots();
		}
		return centerDots;
	}
	
	private void checkCenterDots(){
		for(int i = marginX; i < desc.getMatrix().length - marginX; i++){
			for (int j = marginY; j < desc.getMatrix()[0].length - marginY ; j++){
				//System.out.println("inSide checkCenter+++");
				if(checkCenterDot(desc.getMatrix(), i, j)){
					//System.out.println("after return true");
					centerDots.add(i+"_"+j);
				}
			}
		}
	}
	
	private boolean checkCenterDot(int map[][], int indexI, int indexJ) {
		int numberConts = 0;
		double centerGravityI = 0;
		double centerGravityJ = 0;
		for (int i = indexI - marginX; i <= indexI + marginX; i++) {
			for (int j = indexJ - marginY; j <= indexJ + marginY; j++) {
				if (desc.getMatrix()[i][j] > 0) {
					numberConts++;
					centerGravityI += i * desc.getMatrix()[i][j];
					centerGravityJ += j * desc.getMatrix()[i][j];
				}
			}
		}
		if (numberConts > 0) {
			centerGravityI /= numberConts;
			centerGravityJ /= numberConts;
		}
		//System.out.println("Inside center==============");
		if (numberConts < minNumberContactsCutOff
				|| Math.abs(centerGravityI - indexI) > 1
				|| Math.abs(centerGravityJ - indexJ) > 1){
			//System.out.println("return false");
			return false;			
		}
		//System.out.println("return true");
		return true;
		
	}

	
}
