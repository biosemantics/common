package edu.arizona.biosemantics.common.ling.transform.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.arizona.biosemantics.common.ling.Token;
import edu.arizona.biosemantics.common.ling.transform.ITokenizer;

/**
 * WhitespaceTokenizer tokenizes a text by whitespace.
 * @author rodenhausen
 */
public class WhitespaceTokenizer implements ITokenizer {

	private Pattern wordPunctuationPattern = Pattern.compile("(.+)([\\!,\\.:;\\?])"); //don't put brackets in here
	
	@Override
	public List<Token> tokenize(String text) {
		List<Token> tokens = new ArrayList<Token>();
		for(String token : text.split("\\s+")) {
			
			Matcher matcher = wordPunctuationPattern.matcher(token);
			if(matcher.matches()) {
				String word = matcher.group(1);
			 	String punctuation = matcher.group(2);
			 	tokens.add(new Token(word));
			 	tokens.add(new Token(punctuation));
			} else {
				tokens.add(new Token(token));
			}			
		}
		return tokens;
	}

}
