package edu.arizona.biosemantics.common.taxonomy;

import java.util.LinkedList;
import java.util.List;

public class Taxon {

	private TaxonIdentification taxonIdentification;
	private Taxon parent;
	private List<Taxon> children = new LinkedList<Taxon>();
	
	public Taxon(TaxonIdentification taxonIdentification) {
		this.taxonIdentification = taxonIdentification;
	}
	
	public List<Taxon> getChildren() {
		return children;
	}
	
	public void addChild(Taxon taxon) {
		taxon.setParent(this);
		children.add(taxon);
	}
	
	public void removeChild(Taxon taxon) {
		taxon.setParent(null);
		children.remove(taxon);
	}
	
	public void addChild(int index, Taxon child) {
		child.setParent(this);
		children.add(index, child);
	}

	public void setParent(Taxon parent) {
		this.parent = parent;
	}
	
	public Taxon getParent() {
		return parent;
	}

	public TaxonIdentification getTaxonIdentification() {
		return taxonIdentification;
	}
}
