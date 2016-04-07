package edu.arizona.biosemantics.common.context;

import java.io.Serializable;

public class Context implements Serializable {

	private static final long serialVersionUID = 1L;
	private String source;
	private String text;
	
	public Context(String source, String text) {
		this.source = source;
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
		
	@Override
	public String toString() {
		return this.source + ": " + this.text;
	}
}
