package edu.ucdavis.gc.bm.descriptor2kind;

import java.util.List;

import edu.ucdavis.gc.pdb.PdbContact_light;

public class DescriptorII {
	private String domainName;
	// sequence segment 1;
	private String seqSegment1;

	private String ssSegment1;

	private Integer startSegment1;

	private Integer endSegment1;

	private String seqSegment2;

	private String ssSegment2;

	private Integer startSegment2;

	private Integer endSegment2;
	/**
	 * contact matrix which correspond to direct order of segments
	 */
	private int[][] matrix12 = null;
	/**
	 * contact matrix which correspond to reverse order of segments
	 */
	private int[][] matrix21 = null;
	/**
	 * slope of the regression line which correspond to direct order of segments
	 */
	private double slope12;
	/**
	 * slope of the regression line which correspond to reverse order of
	 * segments
	 */
	private double slope21;

	//private double[][] distMap12;

	private List<PdbContact_light> conts;

	public DescriptorII() {

	}

	public DescriptorII(List<PdbContact_light> conts, String targetSequence) {
		this.fillMatrix(conts);
		this.setSeqSegemensts(conts, targetSequence);
	}

	private int getMinIndexX(List<PdbContact_light> conts) {
		int minIndex = 9999;
		for (PdbContact_light c : conts) {
			if (c.getFirstResNum() < minIndex) {
				minIndex = c.getFirstResNum();
			}
		}
		startSegment1 = minIndex;
		return minIndex;
	}

	private int getMinIndexY(List<PdbContact_light> conts) {
		int minIndex = 9999;
		for (PdbContact_light c : conts) {
			if (c.getSecondResNum() < minIndex) {
				minIndex = c.getSecondResNum();
			}
		}
		startSegment2 = minIndex;
		return minIndex;
	}

	private int getMaxIndexX(List<PdbContact_light> conts) {
		int maxIndex = -1;
		for (PdbContact_light c : conts) {
			if (c.getFirstResNum() > maxIndex) {
				maxIndex = c.getFirstResNum();
			}
		}
		endSegment1 = maxIndex;
		return maxIndex;
	}

	private int getMaxIndexY(List<PdbContact_light> conts) {
		int maxIndex = -1;
		for (PdbContact_light c : conts) {
			if (c.getSecondResNum() > maxIndex) {
				maxIndex = c.getSecondResNum();
			}
		}
		endSegment2 = maxIndex;
		return maxIndex;
	}

	private void fillMatrix(List<PdbContact_light> conts) {
		int minX = getMinIndexX(conts);
		int maxX = getMaxIndexX(conts);
		int lX = maxX - minX + 1;
		int minY = getMinIndexY(conts);
		int maxY = getMaxIndexY(conts);
		int lY = maxY - minY + 1;
		matrix12 = new int[lX][lY];
		for (PdbContact_light c : conts) {
			matrix12[c.getFirstResNum() - minX][c.getSecondResNum() - minY] = 1;
		}
		/*
		 * for(int i = minX; i <= maxX; i++){ for(int j = minY; j<= maxY; j++){
		 * // matrix12[i - minX][j - minY] = } }
		 */
	}

	public int[][] getMatrix() {
		return this.matrix12;
	}

	public String getDomainName() {
		return this.domainName;
	}

	public String getSeqSegment1() {
		return this.seqSegment1;
	}

	public String getSeqSegment2() {
		return this.seqSegment2;
	}

	public void setSeqSegemensts(List<PdbContact_light> conts,
			String targetSequence) {
		int minX = getMinIndexX(conts);
		int maxX = getMaxIndexX(conts);
		int minY = getMinIndexY(conts);
		int maxY = getMaxIndexY(conts);
		this.seqSegment1 = targetSequence.substring(minX, maxX + 1);
		this.seqSegment2 = targetSequence.substring(minY, maxY + 1);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("");
		result.append(this.domainName + "\n");
		result.append(this.seqSegment1 + "\t" + this.startSegment1 + "-"
				+ this.endSegment1 + "\n");
		result.append(this.seqSegment2 + "\t" + this.startSegment2 + "-"
				+ this.endSegment2 + "\n");

		for (int i = 0; i < this.matrix12.length; i++) {
			for (int j = 0; j < this.matrix12[0].length; j++) {
				result.append(matrix12[i][j] + " ");
			}
			result.append("\n");
		}

		return result.toString();
	}
	
	public String toStringShort(){
		StringBuilder result = new StringBuilder("");
		result.append(this.domainName + " " + this.getBounds() + "\t" + this.seqSegment1 + "\t" + this.seqSegment2 + "\n");
		return result.toString();
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public void setSeqSegment1(String seqSegment1) {
		this.seqSegment1 = seqSegment1;
	}

	public void setSeqSegment2(String seqSegment2) {
		this.seqSegment2 = seqSegment2;
	}

	public void setMatrix12(int[][] matrix) {
		this.matrix12 = matrix;
	}

	public void setStartSegment1(int startSegment1) {
		this.startSegment1 = startSegment1;
	}

	public void setEndSegment1(int endSegment1) {
		this.endSegment1 = endSegment1;
	}

	public void setStartSegment2(int startSegment2) {
		this.startSegment2 = startSegment2;
	}

	public void setEndSegment2(int endSegment2) {
		this.endSegment2 = endSegment2;
	}

	public String getBounds() {
		StringBuilder result = new StringBuilder("");
		result.append(this.startSegment1 + "-" + this.endSegment1 + ";"
				+ this.startSegment2 + "-" + this.endSegment2);
		return result.toString();
	}

	public int getStartSegment1(){
		return this.startSegment1;
	}
	
	public int getEndSegment1(){
		return this.endSegment1;
	}
	
	public int getStartSegment2(){
		return this.startSegment2;
	}
	
	public int getEndSegment2(){
		return this.endSegment2;
	}	
	/*
	public void setDistMap(double[][] targetDistMap) {
		int lX = this.endSegment1 - this.startSegment1 + 1;
		int lY = this.endSegment2 - this.startSegment2 + 1;
		this.distMap12 = new double[lX][lY];
		for (int i = 0; i < lX; i++) {
			for (int j = 0; j < lY; j++) {
				if (this.startSegment1+i >= this.startSegment2+j) {
					this.distMap12[i][j] = targetDistMap[this.startSegment1+i][this.startSegment2+j];
				}else{
					this.distMap12[i][j] = targetDistMap[this.startSegment2+j][this.startSegment1+i];
				}
			}
		}
	}
	
	public double [][] getDistMap(){
		return this.distMap12;
	}
	*/
}
