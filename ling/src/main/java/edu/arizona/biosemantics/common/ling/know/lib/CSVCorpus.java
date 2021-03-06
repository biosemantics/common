package edu.arizona.biosemantics.common.ling.know.lib;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

import edu.arizona.biosemantics.common.ling.know.ICorpus;

/**
 * A CSVCorpus creates an ICorpus from a CSV file
 * @author rodenhausen
 */
public class CSVCorpus implements ICorpus {

	private HashMap<String, Integer> frequencies = new HashMap<String, Integer>();
	
	/**
	 * @param filePath
	 * @throws IOException
	 */
	public CSVCorpus(InputStream corpus) throws IOException {
		CSVReader reader = new CSVReader(new InputStreamReader(corpus));
		List<String[]> lines = reader.readAll();
		for(String[] line : lines) {
			frequencies.put(line[0].toLowerCase(), Integer.parseInt(line[1]));
		}
		reader.close();
	}
	
	@Override
	public int getFrequency(String word) {
		word = word.toLowerCase();
		if(!frequencies.containsKey(word))
			return 0;
		return frequencies.get(word);
	}
}
