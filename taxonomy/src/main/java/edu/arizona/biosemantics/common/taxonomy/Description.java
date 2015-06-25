package edu.arizona.biosemantics.common.taxonomy;

import java.io.Serializable;

public enum Description implements Serializable {
	
	MORPHOLOGY(0),
	PHENOLOGY(1),
	HABITAT(2),
	DISTRIBUTION(5);

	private static final long serialVersionUID = 1L;
	private int id;

	Description() {
	}

	Description(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
