package edu.arizona.biosemantics.common.context.server;

import java.util.List;

import edu.arizona.biosemantics.common.context.shared.Context;

public interface Index {

	public void add(Context context) throws Exception;
	
	public void add(List<Context> context) throws Exception;
	
	public List<Context> search(String search, int maxHits) throws Exception;
	
}
