package edu.arizona.biosemantics.common.biology.log;

import edu.arizona.biosemantics.common.log.AbstractStringifyInjection;
import edu.arizona.biosemantics.common.log.IPrintable;

/**
 * StringifyInjectionAspect specifies IPrintables and adds them a toString
 * method
 * 
 * @author rodenhausen
 */
public aspect StringifyInjection extends AbstractStringifyInjection {

	/**
	 * IPrintables are specified
	 */
	declare parents : edu.arizona.biosemantics.common.biology.* implements IPrintable;

}