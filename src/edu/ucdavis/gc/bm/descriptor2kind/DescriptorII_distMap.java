package edu.ucdavis.gc.bm.descriptor2kind;

public class DescriptorII_distMap {

	private DescriptorII desc;

	private double[][] distMap;

	public DescriptorII_distMap(DescriptorII desc, double[][] targetDistMap) {
		this.desc = desc;
		this.setDistMap(targetDistMap);
	}

	private  void setDistMap(double[][] targetDistMap) {
		int lX = desc.getEndSegment1() - desc.getStartSegment1() + 1;
		int lY = desc.getEndSegment2() - desc.getStartSegment2() + 1;
		this.distMap = new double[lX][lY];
		for (int i = 0; i < lX; i++) {
			for (int j = 0; j < lY; j++) {
				if (desc.getStartSegment1() + i >= desc.getStartSegment2() + j) {
					this.distMap[i][j] = targetDistMap[desc.getStartSegment1() + i][desc.getStartSegment2()
							+ j];
				} else {
					this.distMap[i][j] = targetDistMap[desc.getStartSegment2() + j][desc.getStartSegment1()
							+ i];
				}
			}
		}
	}

	public double[][] getDistMap(){
		return this.distMap;
	}
}
