package edu.arizona.biosemantics.common.context;

import java.io.Serializable;

public class Context implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String source;
	private String text;
	
	public Context(int id, String source, String text) {
		this.id = id;
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
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return this.id + " " + this.source + ": " + this.text;
	}
}
