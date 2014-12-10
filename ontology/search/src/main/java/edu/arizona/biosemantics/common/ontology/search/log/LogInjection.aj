package edu.arizona.biosemantics.common.taxonomy.log;

import edu.arizona.biosemantics.common.log.AbstractLogInjection;
import edu.arizona.biosemantics.common.log.ILoggable;

/**
 * LogInjectionAspect specifies ILoggables and adds them a log method
 * 
 * @author rodenhausen
 */
public aspect LogInjection extends AbstractLogInjection {

	/**
	 * ILoggable classes are specified below
	 */
	declare parents : edu.arizona.biosemantics.common.ontology.search.* implements ILoggable;
	declare parents : edu.arizona.biosemantics.common.ontology.search.log.TraceInjection implements ILoggable;

}
