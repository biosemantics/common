package edu.arizona.biosemantics.common.log;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

/**
 * A singleton ObjectStringifier transforms objects into String representations
 * by using Jackson
 * 
 * @author rodenhausen
 */
public class ObjectStringifier {

	private static ObjectStringifier instance;

	/**
	 * @return the singleton instance of ObjectStringifier
	 */
	public static ObjectStringifier getInstance() {
		if (instance == null)
			instance = new ObjectStringifier();
		return instance;
	}

	private ObjectMapper mapper;
	private ObjectWriter writer;

	/**
*
*/
	private ObjectStringifier() {
		this.mapper = new ObjectMapper();
		this.writer = mapper.writerWithDefaultPrettyPrinter();
	}

	/**
	 * synchronization necessary, even though ObjectMapper and ObjectWriter
	 * JavaDoc state that they are thread-safe It might not be used in the right
	 * way then in this case. If removed and more threads print
	 * JSonMappingException is thrown for DescriptionTreatmentElement['name']
	 * 
	 * @param object
	 * @return stringified object
	 */
	public synchronized String stringify(Object object) {
		try {
			return writer.writeValueAsString(object);
		} catch (Exception e) {
			log(LogLevel.ERROR, "Problem writing object as String", e);
			return null;
		}
	}
	
	public static String stringify(Object[] objects) {
		StringBuilder result = new StringBuilder();
		for(Object object : objects)
			try {
				result.append(object.toString() + "\n");
			} catch(Exception e) {
				//no big deal
			}
		String out = result.toString();
		if(out.length() == 0)
			return out;
		return out.substring(0, out.length() - 1);
	}

}