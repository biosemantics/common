package edu.arizona.biosemantics.common.taxonomy;

import java.io.Serializable;

public class RankData implements Comparable<RankData>, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Rank rank = Rank.UNRANKED;
	private String name;
	private RankData parent;
	private String author;
	private String date;
	
	public RankData() { }
	
	public RankData(Rank rank, String name, RankData parent, String author, String date) {
		this(rank, name, author, date);
		this.parent = parent;
	}

	public RankData(Rank rank, String name, String author, String date) {
		this(rank, name);
		this.author = author;
		this.date = date;
	}
	
	public RankData(Rank rank, String name, RankData parent) {
		this(rank, name);
		this.parent = parent;
	}
	
	public RankData(Rank rank, String name) {
		this.rank = rank;
		this.name = name;
	}

	public Rank getRank() {
		return rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RankData getParent() {
		return parent;
	}

	public void setParent(RankData parent) {
		this.parent = parent;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RankData other = (RankData) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (rank != other.rank)
			return false;
		return true;
	}

	@Override
	public int compareTo(RankData o) {
		return rank.getId() - o.rank.getId();
	}

	public String displayName() {
		return rank.toString() + "=" + name + "," + author + "," + date;
	}
	
	public String fullDisplayName() {
		if(parent == null)
			return displayName();
		else 
			return parent.toString() + ";" + this.displayName();
	}	
}