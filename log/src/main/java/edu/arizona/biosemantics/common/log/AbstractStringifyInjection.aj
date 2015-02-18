package edu.arizona.biosemantics.common.log;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * StringifyInjectionAspect specifies IPrintables and adds them a toString
 * method
 * 
 * @author rodenhausen
 */
public abstract aspect AbstractStringifyInjection {

	@XmlTransient
	@Transient
	@JsonIgnore
	private IPrintable IPrintable.thisObject;

	/**
	 * toString method is defined for IPrintables
	 */
	public String IPrintable.toString() {
		return ObjectStringifier.getInstance().stringify(thisObject);
	}

	/**
	 * Pointcut specification for object construction of an IPrintable
	 * 
	 * @param object
	 */
	pointcut objectConstruction(IPrintable object) :
		initialization(IPrintable+.new(..)) && this(object);

	/**
	 * Advice for after object construction lets an IPrintable have a reference
	 * of his own for use in toString()
	 * 
	 * @param object
	 */
	after(IPrintable object) : objectConstruction(object) {
		object.thisObject = object;
	}

}