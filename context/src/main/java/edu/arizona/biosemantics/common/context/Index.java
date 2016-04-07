package edu.arizona.biosemantics.common.context;

import java.util.List;

public interface Index {

	public void add(Context context) throws Exception;
	
	public void add(List<Context> context) throws Exception;
	
	public List<Context> search(String search, int maxHits) throws Exception;
	
}
