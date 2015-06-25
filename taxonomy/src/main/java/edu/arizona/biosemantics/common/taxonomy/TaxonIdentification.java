package edu.arizona.biosemantics.common.taxonomy;

import java.io.Serializable;
import java.util.LinkedList;

public class TaxonIdentification implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private LinkedList<RankData> rankData;
	private String author;
	private String date;

	public TaxonIdentification(LinkedList<RankData> rankData, String author, String date) {
		super();
		this.rankData = rankData;
		this.author = author;
		this.date = date;
	}

	public LinkedList<RankData> getRankData() {
		return rankData;
	}

	public String getAuthor() {
		return author;
	}

	public String getDate() {
		return date;
	}

	public String getDisplayName() {
		return rankData.toString() + ":" + author + "," + date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((rankData == null) ? 0 : rankData.hashCode());
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
		TaxonIdentification other = (TaxonIdentification) obj;
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
		if (rankData == null) {
			if (other.rankData != null)
				return false;
		} else if (!rankData.equals(other.rankData))
			return false;
		return true;
	}
	
	
}