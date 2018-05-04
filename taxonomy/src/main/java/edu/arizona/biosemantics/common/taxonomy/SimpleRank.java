package edu.arizona.biosemantics.common.taxonomy;

import java.io.Serializable;

public enum SimpleRank implements Serializable {

	//Genus and Species only 
	
	GENUS(22, "Genus"),
	SPECIES(29, "Species"),
	UNRANKED(Integer.MAX_VALUE, "Unranked");



	
	private static final long serialVersionUID = 1L;

	private int id;

	private String displayName;

	SimpleRank() {
	}

	SimpleRank(int id, String displayName) {
		this.id = id;
		this.displayName = displayName;
	}

	public int getId() {
		return id;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public static boolean isValidParentChild(Rank parent, Rank child) {
		int parentLevelId = parent == null ? -1 : parent.getId();
		int childLevelId = child == null ? -1 : child.getId();
		// special case group allows children of itself as it is 'unranked'
		if (parent != null && child != null && parent.equals(UNRANKED)
				&& child.equals(UNRANKED))
			return true;
		return parentLevelId < childLevelId;
	}

	public static boolean equalOrBelowGenus(Rank level) {
		return level.getId() >= GENUS.getId();
	}

	public static boolean aboveGenus(Rank level) {
		return level.getId() < GENUS.getId();
	}
	
	public static Rank nextRank(Rank rank) {
		if(rank.getId() + 1 < Rank.values().length - 1)
			return Rank.values()[rank.getId() + 1];
		return null;
	}
}