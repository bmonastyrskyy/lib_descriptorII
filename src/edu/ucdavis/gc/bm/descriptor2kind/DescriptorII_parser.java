package edu.ucdavis.gc.bm.descriptor2kind;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DescriptorII_parser {

	private File file;
	
	public DescriptorII_parser(String fileName){
		file = new File(fileName);
	}
	
	public DescriptorII_parser(File file){
		this.file = file;
	}
	
	public DescriptorII parse() throws IOException{
		DescriptorII result = new DescriptorII();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		// read first line
		line = reader.readLine();
		result.setDomainName(line.trim());
		// read second line
		line = reader.readLine();
		String [] tokens = line.trim().split("\\s+");
		result.setSeqSegment1(tokens[0]);
		tokens = tokens[1].split("-");
		result.setStartSegment1(Integer.valueOf(tokens[0]));
		result.setEndSegment1(Integer.valueOf(tokens[1]));
		//read third line
		line = reader.readLine();
		tokens = line.trim().split("\\s+");
		result.setSeqSegment2(tokens[0]);
		tokens = tokens[1].split("-");
		result.setStartSegment2(Integer.valueOf(tokens[0]));
		result.setEndSegment2(Integer.valueOf(tokens[1]));
		List<String> lines = new ArrayList<String>();
		while ((line = reader.readLine()) != null) {
		    lines.add(line.trim());
		}
		reader.close();
		int [][] matrix = new int [lines.size()] [];
		for (int i = 0; i < lines.size(); i++){
			tokens = lines.get(i).split("\\s+");
			matrix[i] = new int [tokens.length];
			for (int j = 0; j < tokens.length; j++){
				matrix[i][j] = Integer.valueOf(tokens[j]);
			}
		}
		result.setMatrix12(matrix);
		return result;
	}
}
