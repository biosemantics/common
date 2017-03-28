/**
 * 
 */
package edu.arizona.biosemantics.common.ling.know;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author Hong Cui
 * one term  = a string + a category
 *
 */
public class Term implements Comparator<Term>, Serializable {
	
	private String label;
	private String category;

	public Term() { }
	
	public Term(String str, String category){
		this.label = str;
		this.category = category;
		
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLabel() {
		return label;
	}

	public String getCategory() {
		return category;
	}

	@Override
	public int compare(Term t1, Term t2) {
		if(t1.getLabel().compareTo(t2.getLabel())==0 &&
				t1.getCategory().compareTo(t2.getCategory())==0)
		return 0;
		
		return t1.getLabel().compareTo(t2.getLabel());
	}

	@Override
	public boolean equals(Object o) {
		return this.getLabel().compareTo(((Term)o).getLabel())==0 &&
				this.getCategory().compareTo(((Term)o).getCategory())==0;
	}
	
	@Override
	public int hashCode(){
		return (label+" "+category).hashCode();
	}
	
	@Override
	public String toString(){
		return label+"<"+category+">";
	}
}
