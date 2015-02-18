package edu.arizona.biosemantics.common.validation.log;

import edu.arizona.biosemantics.common.log.AbstractStringifyInjection;
import edu.arizona.biosemantics.common.log.IPrintable;

/**
 * StringifyInjectionAspect specifies IPrintables and adds them a toString
 * method
 * 
 * @author rodenhausen, cui
 */
public aspect StringifyInjection extends AbstractStringifyInjection {

	/**
	 * IPrintables are specified
	 */
	declare parents : edu.arizona.biosemantics.common.validation.* implements IPrintable;

}