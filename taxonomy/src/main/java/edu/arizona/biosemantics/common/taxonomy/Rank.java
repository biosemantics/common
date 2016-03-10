package edu.arizona.biosemantics.common.taxonomy;

import java.io.Serializable;

public enum Rank implements Serializable {
	/*LIFE(0), 
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
	UNRANKED(Integer.MAX_VALUE);	*/
	
	
	//taken from GBIF's http://rs.gbif.org/vocabulary/gbif/rank.xml
	DOMAIN(1, "Domain"), 
	KINGDOM(2, "Kingdom"), 
	SUBKINGDOM(3, "Subkingdom"),
	SUPERPHYLUM(4, "Superphylum"),
	PHYLUM(5, "Phylum"),
	SUBPHYLUM(6, "Subphylum"),
	SUPERCLASS(7, "Superclass"),
	CLASS(8, "Class"),
	SUBCLASS(9, "Subclass"),
	SUPERCOHORT(10, "Supercohort"),
	COHORT(11, "Cohort"),
	SUBCOHORT(12, "Subcohort"),
	SUPERORDER(13, "Superorder"),
	ORDER(14, "Order"),
	SUBORDER(15, "Suborder"),
	INFRAORDER(16, "Infraorder"),
	SUPERFAMILY(17, "Superfamily"),
	FAMILY(18, "Family"),
	SUBFAMILY(19, "Subfamily"),
	TRIBE(20, "Tribe"),
	SUBTRIBE(21, "Subtribe"),
	GENUS(22, "Genus"),
	SUBGENUS(23, "Subgenus"),
	SECTION(24, "Section"),
	SUBSECTION(25, "Subsection"),
	SERIES(26, "Series"),
	SUBSERIES(27, "Subseries"),
	SPECIES_AGGREGATE(28, "Species-Aggregate"),
	SPECIES(29, "Species"),
	SUBSPECIFIC_AGGREGATE(30, "Subspecific-Aggregate"),
	SUBSPECIES(31, "Subspecies"),
	VARIETY(32, "Variety"),
	SUBVARIETY(33, "Subvariety"),
	FORM(34, "Form"),
	SUBFORM(35, "Subform"),
	CULTIVAR_GROUP(36, "Cultivar-Group"),
	CULTIVAR(37, "Cultivar"),
	STRAIN(38, "Strain"),
	UNRANKED(Integer.MAX_VALUE, "Unranked");
	
	
	private static final long serialVersionUID = 1L;

	private int id;

	private String displayName;

	Rank() {
	}

	Rank(int id, String displayName) {
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