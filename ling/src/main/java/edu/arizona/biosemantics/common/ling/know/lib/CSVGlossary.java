package edu.arizona.biosemantics.common.ling.know.lib;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

/**
 * A CSVGlossary creates an IGlossary from a CSV file; expected CSV file format: word;category
 * @author rodenhausen
 */
public class CSVGlossary extends InMemoryGlossary {
	
	/**
	 * @param filePath
	 * @throws IOException
	 */
	public CSVGlossary(String filePath) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(filePath));
		
		List<String[]> lines = reader.readAll();
		for(String[] line : lines) {
			
			String term = line[1];
			String category = line[2];
			
			if(category.compareTo("quantity")==0) category = "count";
			//if(term.equals("rhizome")) 
			//	System.out.println("here");
			
			this.addEntry(term, category);
			
			//if(category.equalsIgnoreCase("structure")) 
			//	System.out.println(reverseGlossary.get(category));
		}
		
		reader.close();
	}
}
