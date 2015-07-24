package edu.arizona.biosemantics.common.ling.pos.lib;

import java.util.ArrayList;
import java.util.List;

import edu.arizona.biosemantics.common.ling.Token;
import edu.arizona.biosemantics.common.ling.know.IPOSKnowledgeBase;
import edu.arizona.biosemantics.common.ling.pos.IPOSTagger;
import edu.arizona.biosemantics.common.ling.pos.POS;
import edu.arizona.biosemantics.common.ling.pos.POSedToken;

/**
 * OrganCharacterPOSTagger taggs tokens using an IPOSKnowledgeBase
 * @author rodenhausen
 */
public class WordNetPOSTagger implements IPOSTagger {

	private IPOSKnowledgeBase posKnowledgeBase;

	/**
	 * @param posKnowledgeBase
	 */
	public WordNetPOSTagger(IPOSKnowledgeBase posKnowledgeBase) {
		this.posKnowledgeBase = posKnowledgeBase;
	}

	public List<Token> tag(List<Token> sentence) {
		List<Token> taggedSentence = new ArrayList<Token>();
		for(Token token : sentence) {
			POS pos = posKnowledgeBase.getMostLikleyPOS(token.getContent());
			if(pos == null)
				pos = POS.NONE;
			taggedSentence.add(new POSedToken(token.getContent(), pos));
		}
		return taggedSentence;
	}


	/*@Override
	public List<Token> tag(List<Token> sentence) {
		List<Token> taggedSentence = new ArrayList<Token>();
		for(Token token : sentence) {
			POS pos = posKnowledgeBase.getMostLikleyPOS(token.getContent());
			if(pos == null)
				pos = POS.NONE;
			taggedSentence.add(new POSedToken(token.getContent(), pos));
		}
		return taggedSentence;
	}*/

}
