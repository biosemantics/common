package edu.arizona.biosemantics.common.biology;

import java.io.Serializable;

public enum TaxonGroup implements Serializable {
	
	ALGAE("Algae"), CNIDARIA("Cnidaria"), FOSSIL("Fossil"), GASTROPODS("Gastropods"), HYMENOPTERA("Hymenoptera"), PLANT("Plant"), PORIFERA("Porifera"), 
	SPIDER("Spider");

	private static final long serialVersionUID = 1L;
	
	private String displayName;
	
	TaxonGroup() { }
	
	TaxonGroup(String displayName) {
		this.displayName = displayName;
	}
	
	public static TaxonGroup valueFromDisplayName(String displayName) {
		for(TaxonGroup taxonGroup : values())
			if(taxonGroup.getDisplayName().equals(displayName))
				return taxonGroup;
		return null;
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
