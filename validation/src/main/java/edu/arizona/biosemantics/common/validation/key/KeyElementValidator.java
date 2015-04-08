/**
 * 
 */
package edu.arizona.biosemantics.common.validation.key;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import edu.arizona.biosemantics.common.log.LogLevel;

/**
 * @author hong cui
 *
 */
public class KeyElementValidator {
	static XPathFactory fac = null;
	
	static XPathExpression<Element> nextIdPath = null;
	static XPathExpression<Element> stateIdPath = null;
	static XPathExpression<Element> keyStatePath = null;
	static XPathExpression<Element> keyHeadPath = null;
	static XPathExpression<Element> detPath = null;
	
	static{
		fac = XPathFactory.instance();
		nextIdPath = fac.compile(".//next_statement_id", Filters.element()); //must use . to limit the selection to the current key element
		stateIdPath = fac.compile(".//statement_id", Filters.element());
		keyStatePath = fac.compile(".//key_statement", Filters.element());
		keyHeadPath = fac.compile(".//key_head", Filters.element());
		detPath = fac.compile(".//determination", Filters.element());
		/*nextIdPath = fac.compile("//key//next_statement_id", Filters.element());
		stateIdPath = fac.compile("//key//statement_id", Filters.element());
		keyStatePath = fac.compile("//key//key_statement", Filters.element());*/
	}

	/**
	 * 
	 */
	public KeyElementValidator() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * if any of the following is true, the key is not good
	 * 1. any next_statement_id is the same as an earlier statement_id on the same path (loop)
	 * [2. same next_statement_id appear more than once]
	 * 3. next_statement_id == first id used in statement ids (not possible)
	 * 4. some next_statement_ids can not be matched to a statement id
	 * 5. statement ids (except the firstId) are not referred by a next_statement_id
	 * 6. contains more than one key_head element
	 * 7. a determination contains unmatched brackets
	 * @param key
	 * @return
	 */

	public boolean validate(Element key, ArrayList<String> errors) throws KeyValidationException{
		if(key==null || errors ==null) return false; 
		boolean hasError = false;
		KeyValidationException exception = new KeyValidationException();
		if(keyHeadPath.evaluate(key).size()>1){
			errors.add("too many key_head in the key, allows only one");
			log(LogLevel.DEBUG, "too many key_head in the key, allows only one");
			//throw new KeyValidationException("Too many key_head in the key, allows only one.");
			exception.addError("Too many key_head in the key, allows only one.");
			hasError = true;
		}
		for(Element det: detPath.evaluate(key)){
			if(det.getTextNormalize().replaceAll("[^(){}\\[\\]]", "").length() % 2 !=0){
				errors.add("unmatched brackets () [] {} in determination: "+det.getTextNormalize());
				log(LogLevel.DEBUG,"unmatched brackets () [] {} in determination: "+det.getTextNormalize());
				//throw new KeyValidationException("unmatched brackets () [] {} in determination: "+det.getTextNormalize());
				exception.addError("unmatched brackets () [] {} in determination: "+det.getTextNormalize());
				hasError = true;
			}
		}
		
		Element stateId = stateIdPath.evaluateFirst(key);
		if(stateId == null){
			errors.add("key contains no statement");
			log(LogLevel.DEBUG,"key contains no statement");
			//throw new KeyValidationException("key contains no statement");
			exception.addError("key contains no statement");
			hasError = true;
		}
		
		String firstId = stateId.getTextNormalize();
		ArrayList<String> nextIds = new ArrayList<String>();
		
		//collect all ids
		HashSet<String> ids = new HashSet<String> ();
		for(Element id: this.stateIdPath.evaluate(key)){
			ids.add(id.getTextNormalize());
		}
		ids.remove(firstId);
		
		//check the conditions 
		for(Element nextStatement: nextIdPath.evaluate(key)){
			String nextId = nextStatement.getTextNormalize();
			//log(LogLevel.DEBUG, "checking nextid "+nextId);
			ids.remove(nextId);
			if(nextId.compareTo(firstId)==0){
				errors.add("key contains a loop, check "+" statement "+firstId);
				hasError = true;
				log(LogLevel.DEBUG, "key contains a loop, check "+" statement "+firstId);
				exception.addError("key contains a loop, check "+" statement "+firstId);
			}
			
			if(this.getKeyStatements(nextId, key).isEmpty()){
				errors.add("statement "+nextId+" can not be found");
				hasError = true;
				log(LogLevel.DEBUG, "statement "+nextId+" can not be found");
				exception.addError("statement "+nextId+" can not be found");
			}
			/*if(nextIds.contains(nextId)){
				log(LogLevel.DEBUG, "next id "+nextId+" is referred twice"); //this is a warning, having this doesn't always mean the key is bad
				//return false;
			}*/
			else nextIds.add(nextId);
		}

		if(!ids.isEmpty()){
			String extraIds = "";
			for(String id: ids){
				extraIds += id+", ";
			}
			hasError = true;
			errors.add("statement(s) "+extraIds.replaceFirst(", $", "")+" is/are not referenced");
			log(LogLevel.DEBUG, "statement(s) "+extraIds.replaceFirst(", $", "")+" is/are not referenced");
			exception.addError("statement(s) "+extraIds.replaceFirst(", $", "")+" is/are not referenced");
		}
		ArrayList<String> idsInPath = new ArrayList<String> ();
		if(containsLoop(this.getKeyStatements(firstId, key), idsInPath, key, exception)){
			hasError = true;
		}
		if(hasError){
			throw exception;
		}
		return true;
	}


	/**
	 * depth first, recursively check if the key contains a loop, return true immediately when a loop is detected.
	 * it contains a loop when a next_statement_id points to an earlier statement_id in the key 
	 *<key_statement>
	      <statement_id>1</statement_id>
	      <description type="morphology">Inflorescence of 1 — many flowers, if several-flowered corymbose, cymose or racemose, if slightly paniculate without strong prickles on the pedicels; crowns and canes biennial; prickles terete2.</description>
	      <next_statement_id>2</next_statement_id>
      </key_statement>
	 * @param key
	 * @return
	 */

	@SuppressWarnings("unchecked")
	private boolean containsLoop(ArrayList<Element> statements, ArrayList<String> idsInPath, Element key, KeyValidationException errors) throws KeyValidationException {
		boolean hasLoop = false;
		for(Element statement: statements){
			ArrayList<String> idsInThisPath = (ArrayList<String>) idsInPath.clone();
			idsInThisPath.add(statement.getChildTextNormalize("statement_id"));
			Element next = statement.getChild("next_statement_id");
			if(next!=null){
				String nextId = next.getTextNormalize();
				//log(LogLevel.DEBUG, "checking nextid "+nextId+" for any loop");
				if(idsInThisPath.contains(nextId)){
					errors.addError("key contains a loop, check "+ nextId);
					hasLoop = true;
					log(LogLevel.DEBUG, "key contains a loop, check "+ nextId);
					//return true;
				}else{
					idsInThisPath.add(nextId);				
					ArrayList<Element> nextStop = getKeyStatements(nextId, key);
					if(containsLoop(nextStop, idsInThisPath, key, errors)){
						hasLoop = true;
						//return true;
					}
				}
			}
		}
		return hasLoop? true : false;
	}

	/**
	 * 
	 * @param stmtid
	 * @param key
	 * @return key_statement elements with the specified statement_id
	 */
	private ArrayList<Element> getKeyStatements(String stmtid, Element key) {
		ArrayList<Element> results =  new ArrayList<Element>();
		for(Object state: keyStatePath.evaluate(key)){
			if(((Element)state).getChildTextNormalize("statement_id").compareTo(stmtid)==0){
				results.add((Element) state);
			}
		}
		return results;
	}
	/**
	 * @param args
	 * @throws KeyValidationException 
	 */
	
	public boolean validate(String filePath) throws KeyValidationException{
		SAXBuilder builder = new SAXBuilder();
		Document document;
		try {
			document = (Document) builder.build(new File(filePath));
			Element rootNode = document.getRootElement();
			XPathExpression<Element> keyPath = fac.compile("//bio:treatment/key", Filters.element(), null, Namespace.getNamespace("bio", "http://www.github.com/biosemantics"));
			for(Element key: keyPath.evaluate(rootNode)){
				ArrayList<String> errors = new ArrayList<String>();
				validate(key, errors);
			}
		} catch (JDOMException | IOException e) {
			return false;
		}
		return true;
	}
	public static void main(String[] args) {
		String filePath ="C:\\Users\\Aarthy\\etcsite\\Task-Docs\\Key Validation Task\\test\\asteraceae_V19_newHabPhenTaxHier_1.xml";
		SAXBuilder builder = new SAXBuilder();
		KeyElementValidator kev = new KeyElementValidator();
		try {
			Document document = (Document) builder.build(new File(filePath));
			Element rootNode = document.getRootElement();
			XPathExpression<Element> keyPath = fac.compile("//bio:treatment/key", Filters.element(), null, Namespace.getNamespace("bio", "http://www.github.com/biosemantics"));
			for(Element key: keyPath.evaluate(rootNode)){
				ArrayList<String> errors = new ArrayList<String>();
				if(!kev.validate(key, errors)){
					System.out.println("errors in the key: ");
					for(String error: errors){
						System.out.println(error);
					}
				}
			}
		  }catch(Exception e){
			  e.printStackTrace();
		  }

	}

}

