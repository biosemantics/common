package edu.arizona.biosemantics.common.taxonomy;

import java.io.Serializable;
import java.util.LinkedList;

public class TaxonIdentification implements Serializable {
	
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
}