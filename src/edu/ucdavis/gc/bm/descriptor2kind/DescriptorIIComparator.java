package edu.ucdavis.gc.bm.descriptor2kind;

import java.util.List;

/**
 * The class compares two descriptors of the 2nd kind. The comparison is
 * performed on the basis of contact maps. If the contact maps have the common
 * pattern, it determines the alignment of the descriptors' segments.
 * 
 * @author bohdan
 * 
 */
public class DescriptorIIComparator {

	private DescriptorII desc1;

	private DescriptorII desc2;

	private double score;

	/**
	 * minimal size in horizontal direction of the common pattern
	 */
	private int minSizeH = 9;

	/**
	 * minimal size in vertical direction of the common pattern
	 */
	private int minSizeV = 9;

	/**
	 * threshold for the value of the scoring function of the similarity
	 */
	private double threshold;

	/**
	 * correspondence of pair of residues indexI1_indexJ1:indexI2_indexJ2
	 */
	private String corres;

	public DescriptorIIComparator(DescriptorII desc1, DescriptorII desc2) {
		this.desc1 = desc1;
		this.desc2 = desc2;
	}

	public DescriptorII getDesc1() {
		return this.desc1;
	}

	public DescriptorII getDesc2() {
		return desc2;
	}

	public void setMinSizeH(int minSizeH) {
		this.minSizeH = minSizeH;
	}

	public void setMinSizeV(int minSizeV) {
		this.minSizeV = minSizeV;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public void compare(List<String> centers1, List<String> centers2) {
		score = 0;
		corres = "";
		for (String c1 : centers1) {
			String[] tokens1 = c1.split("_");
			int centerI1 = Integer.valueOf(tokens1[0]);
			int centerJ1 = Integer.valueOf(tokens1[1]);
			for (String c2 : centers2) {
				String[] tokens2 = c2.split("_");
				int centerI2 = Integer.valueOf(tokens2[0]);
				int centerJ2 = Integer.valueOf(tokens2[1]);
				double curScore = compare(desc1.getMatrix(), centerI1,
						centerJ1, desc2.getMatrix(), centerI2, centerJ2);
				if (score < curScore) {
					score = curScore;
					corres = c1 + ":" + c2;
				}
			}
		}

	}

	private double compare(int[][] map1, int centerI1, int centerJ1,
			int[][] map2, int centerI2, int centerJ2) {
		double result = 0;
		int shiftLeft = 0;
		int shiftRight = 0;
		int shiftUp = 0;
		int shiftDown = 0;
		shiftLeft = min(centerI1, centerI2);
		shiftUp = min(centerJ1, centerJ2);
		shiftRight = min(map1.length - centerI1, map2.length - centerI2);
		shiftDown = min(map1[0].length - centerJ1, map2[0].length - centerJ2);
		for (int i = -shiftLeft; i < shiftRight; i++) {
			for (int j = -shiftUp; j < shiftDown; j++) {
				try {
					result += map1[centerI1 + i][centerJ1 + j]
							* map2[centerI2 + i][centerJ2 + j];
				} catch (ArrayIndexOutOfBoundsException e) {
					// Systemerr.println(" "+ (-shiftLeft) + " " + shiftRight +
					// " : " + (-shiftDown) + " " + shiftUp );
					// Systemerr.println((centerI1 + i) + " " + (centerJ1 + j) +
					// " " + (centerI2 + i) + " " + (centerJ2 + j));
					throw e;
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @param distMap1
	 * @param centers1
	 * @param distMap2
	 * @param centers2
	 * @return
	 */
	public void compare(double[][] distMap1, List<String> centers1,
			double[][] distMap2, List<String> centers2, int flag) {
		if (flag == 1) {
			score = Double.POSITIVE_INFINITY;
		}
		if (flag == 2) {
			score = Double.NEGATIVE_INFINITY;
		}
		corres = "";
		for (String c1 : centers1) {
			String[] tokens1 = c1.split("_");
			int centerI1 = Integer.valueOf(tokens1[0]);
			int centerJ1 = Integer.valueOf(tokens1[1]);
			for (String c2 : centers2) {
				String[] tokens2 = c2.split("_");
				int centerI2 = Integer.valueOf(tokens2[0]);
				int centerJ2 = Integer.valueOf(tokens2[1]);
				double curScore = compare(desc1.getMatrix(), distMap1,
						centerI1, centerJ1, desc2.getMatrix(), distMap2,
						centerI2, centerJ2, flag);
				if (flag == 1) {
					if (score > curScore) {
						score = curScore;
						corres = c1 + ":" + c2;
					}
				}
				if (flag == 2) {
					if (score < curScore) {
						score = curScore;
						corres = c1 + ":" + c2;
					}
				}
			}
		}
	}

	/**
	 * calc RMSD of the differences of distances over the contacts of the first
	 * contact map
	 * 
	 * @param map1
	 * @param distMap1
	 * @param centerI1
	 * @param centerJ1
	 * @param map2
	 * @param distMap2
	 * @param centerI2
	 * @param centerJ2
	 * @return
	 */
	private double compare(int[][] map1, double[][] distMap1, int centerI1,
			int centerJ1, int[][] map2, double[][] distMap2, int centerI2,
			int centerJ2, int flag) {
		double result = 0;
		int shiftLeft = 0;
		int shiftRight = 0;
		int shiftUp = 0;
		int shiftDown = 0;
		shiftLeft = min(centerI1, centerI2);
		shiftUp = min(centerJ1, centerJ2);
		shiftRight = min(map1.length - centerI1, map2.length - centerI2);
		shiftDown = min(map1[0].length - centerJ1, map2[0].length - centerJ2);
		int count = 0;
		for (int i = -shiftLeft; i < shiftRight; i++) {
			for (int j = -shiftUp; j < shiftDown; j++) {
				if (map1[centerI1 + i][centerJ1 + j] > 0
						|| map2[centerI2 + i][centerJ2 + j] > 0) {
					try {
						// RMSD
						if (flag == 1) {
							result += Math.pow(distMap1[centerI1 + i][centerJ1
									+ j]
									- distMap2[centerI2 + i][centerJ2 + j], 2);
						}
						// S function
						if (flag == 2) {
							result += 1 / (1 + Math.pow(
									distMap1[centerI1 + i][centerJ1 + j]
											- distMap2[centerI2 + i][centerJ2
													+ j], 2));
						}
						count++;
					} catch (ArrayIndexOutOfBoundsException e) {
						// Systemerr.println(" "+ (-shiftLeft) + " " +
						// shiftRight + " : " + (-shiftDown) + " " + shiftUp );
						// Systemerr.println((centerI1 + i) + " " + (centerJ1 +
						// j) + " " + (centerI2 + i) + " " + (centerJ2 + j));
						throw e;
					}
				}
			}
		}
		if (flag == 1) {
			result = Math.sqrt(result / count);
		}
		if (flag == 2) {
			result /= count;
		}
		return result;
	}

	public double getScore() {
		return score;
	}

	public String getCorres() {
		return corres;
	}

	private int min(int i, int j) {
		if (i < j) {
			return i;
		} else {
			return j;
		}
	}

	private int max(int i, int j) {
		if (i > j) {
			return i;
		} else {
			return j;
		}
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("");
		/*
		 * result.append(desc1.getDomainName() + " " + desc1.getBounds() + "\t"
		 * + desc1.getSeqSegment1() + "\t" + desc1.getSeqSegment2() + "\n");
		 */
		result.append(desc2.getDomainName() + " " + desc2.getBounds() + "\t"
				+ calcShifts(corres) + "\t" + this.score + "\n");
		return result.toString();
	}

	private String calcShifts(String corr) {
		String[] tokens = corr.split(":");
		String[] tokens1 = tokens[0].split("_");
		String[] tokens2 = tokens[1].split("_");
		int shiftH1;
		int shiftH2;
		int shiftV1;
		int shiftV2;
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
		int lengthOverlapH;
		int lengthOverlapV;
		lengthOverlapH = min(desc1.getSeqSegment1().length() - shiftH1, desc2
				.getSeqSegment1().length() - shiftH2);
		lengthOverlapV = min(desc1.getSeqSegment2().length() - shiftV1, desc2
				.getSeqSegment2().length() - shiftV2);
		StringBuilder segmOverlap1 = new StringBuilder("");
		for (int i = 0; i < desc1.getSeqSegment1().length(); i++) {
			if (i + shiftH1 - shiftH2 < desc2.getSeqSegment1().length()
					&& i + shiftH1 - shiftH2 >= 0) {

				segmOverlap1.append(desc2.getSeqSegment1().charAt(
						i + shiftH1 - shiftH2));

			} else {
				segmOverlap1.append("-");
			}
		}

		StringBuilder segmOverlap2 = new StringBuilder("");
		for (int i = 0; i < desc1.getSeqSegment2().length(); i++) {
			if (i + shiftV1 - shiftV2 < desc2.getSeqSegment2().length()
					&& i + shiftV1 - shiftV2 >= 0) {

				segmOverlap2.append(desc2.getSeqSegment2().charAt(
						i + shiftV1 - shiftV2));

			} else {
				segmOverlap2.append("-");
			}
		}
		return segmOverlap1.toString() + "\t" + segmOverlap2.toString() + "\t"
				+ 0 + "_" + 0 + ":" + (shiftH2>0 ? shiftH2 : -shiftH1) + "_" + (shiftV2 > 0 ? shiftV2 : -shiftV1) ;
	}
}
