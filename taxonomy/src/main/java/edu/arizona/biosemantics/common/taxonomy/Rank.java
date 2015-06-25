package edu.arizona.biosemantics.common.taxonomy;

import java.io.Serializable;

public enum Rank implements Serializable {
	LIFE(0), 
	SUPERDOMAIN(1), 
	DOMAIN(2), 
	SUBDOMAIN(3), 
	SUPERKINGDOM(4), 
	KINGDOM(5), 
	SUBKINGDOM(6), 
	SUPERPHYLUM(7), 
	PHYLUM(8), 
	SUBPHYLUM(9), 
	SUPERDIVISION(10),
	DIVISION(11),
	SUBDIVISION(12),
	SUPERCLASS(13),
	CLASS(14), 
	SUBCLASS(15),
	SUPERORDER(16), 
	ORDER(17), 
	SUBORDER(18), 
	SUPERFAMILY(19), 
	FAMILY(20), 
	SUBFAMILY(21), 
	SUPERTRIBE(22), 
	TRIBE(23), 
	SUBTRIBE(24), 
	SUPERGENUS(25), 
	GENUS(26), 
	SUBGENUS(27), 
	SUPERSECTION(28), 
	SECTION(29), 
	SUBSECTION(30), 
	SUPERSERIES(31), 
	SERIES(32), 
	SUBSERIES(33), 
	SUPERSPECIES(34), 
	SPECIES(35), 
	SUBSPECIES(36),
	SUPERSTRAIN(37),
	STRAIN(38),
	SUBSTRAIN(39),
	SUPERTYPESTRAIN(40),
	TYPESTRAIN(41),
	SUBTYPESTRAIN(42),
	SUPERVARIETY(43), 
	VARIETY(44), 
	SUBVARIETAS(45),
	SUPERFORMA(46), 
	FORMA(47), 
	SUBFORMA(48), 
	SUPERGROUP(49), 
	GROUP(50), 
	SUBGROUP(51),
	UNRANKED(Integer.MAX_VALUE);	
	
	private static final long serialVersionUID = 1L;

	private int id;

	Rank() {
	}

	Rank(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
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