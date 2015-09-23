package edu.arizona.biosemantics.common.ling.know.lib;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;
import edu.arizona.biosemantics.common.ling.know.GlossaryInitializer;
import edu.arizona.biosemantics.common.ling.know.IGlossary;
import edu.arizona.biosemantics.common.ling.know.Term;
import edu.arizona.biosemantics.common.ling.transform.IInflector;
import edu.arizona.biosemantics.oto.model.lite.Decision;
import edu.arizona.biosemantics.oto.model.lite.Download;
import edu.arizona.biosemantics.oto.model.lite.Synonym;
import edu.arizona.biosemantics.oto.model.lite.UploadResult;

public class TermReviewFilesGlossaryInitializer extends OTO2GlossaryInitializer {

	public TermReviewFilesGlossaryInitializer(String oto2ClientUrl,
			UploadResult uploadResult, IInflector inflector) {
		super(oto2ClientUrl, uploadResult, inflector);
	}

	public void initialize(IGlossary glossary) throws Exception {
		List<Synonym> synonyms = new LinkedList<Synonym>();
		
		Set<String> hasSynonym = new HashSet<String>();
		List<Decision> decisions = new LinkedList<Decision>();
		
		try(CSVReader reader = new CSVReader(new FileReader("C:\\Users\\rodenhausen\\Desktop\\test-enhance\\"
				+ "Gordon_complexity_term_review\\category_mainterm_synonymterm-task-Gordon_complexity.csv"))) {
			List<String[]> lines = reader.readAll();
			int i=0;
			for(String[] line : lines) {
				synonyms.add(new Synonym(String.valueOf(i), line[1], line[0], line[2]));
				hasSynonym.add(line[1]);
			}	
		}
		try(CSVReader reader = new CSVReader(new FileReader("C:\\Users\\rodenhausen\\Desktop\\test-enhance\\"
				+ "Gordon_complexity_term_review\\category_term-task-Gordon_complexity.csv"))) {
			List<String[]> lines = reader.readAll();
			int i=0;
			for(String[] line : lines) {
				decisions.add(new Decision(String.valueOf(i), line[1], line[0], hasSynonym.contains(line[1]), ""));
			}
		}
		Download download = new Download(true, decisions, synonyms);
		
		
		initFromDownload(glossary, download);
	}

}
