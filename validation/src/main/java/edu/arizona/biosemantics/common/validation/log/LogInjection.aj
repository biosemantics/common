package edu.arizona.biosemantics.common.validation.log;

import edu.arizona.biosemantics.common.log.AbstractLogInjection;
import edu.arizona.biosemantics.common.log.ILoggable;

/**
 * LogInjectionAspect specifies ILoggables and adds them a log method
 * 
 * @author rodenhausen, cui
 */
public aspect LogInjection extends AbstractLogInjection {

	/**
	 * ILoggable classes are specified below
	 */
	declare parents : edu.arizona.biosemantics.common.validation.* implements ILoggable;
	declare parents : edu.arizona.biosemantics.common.validation.key.* implements ILoggable;
	declare parents : edu.arizona.biosemantics.common.validation.log.TraceInjection implements ILoggable;

}
