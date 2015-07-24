package edu.arizona.biosemantics.common.ling.transform;

import java.util.List;

import edu.arizona.biosemantics.common.ling.pos.POS;



public interface IStemmer {
	
	public List<String> getStems(String word);

	public List<String> getStems(String word, POS pos);
	
}
