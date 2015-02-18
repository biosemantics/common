package edu.arizona.biosemantics.common.biology;

public enum TaxonGroup {
	
	ALGAE("Algae"), CNIDARIA("Cnidaria"), FOSSIL("Fossil"), GASTROPODS("Gastropods"), HYMENOPTERA("Hymenoptera"), PLANT("Plant"), PORIFERA("Porifera");
	
	private String displayName;
	
	TaxonGroup(String displayName) {
		this.displayName = displayName;
	}
	
	public TaxonGroup valueFromDisplayName(String displayName) {
		for(TaxonGroup taxonGroup : values())
			if(taxonGroup.getDisplayName().equals(displayName))
				return taxonGroup;
		return null;
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
