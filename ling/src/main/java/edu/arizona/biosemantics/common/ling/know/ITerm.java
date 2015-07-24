/**
 * 
 */
package edu.arizona.biosemantics.common.ling.know;

import java.util.Comparator;

/**
 * @author Hong Cui
 *
 */
public interface ITerm {
	void setLabel(String label);
	void setCategory(String category);
	String getLabel();
	String getCategory();
		
}
