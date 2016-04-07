package edu.arizona.biosemantics.common.context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

public class FileIndex implements Index {
	
	private String baseDirectory;
	private FSDirectory directory;
	private StandardAnalyzer analyzer = new StandardAnalyzer();

	public FileIndex(String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}
	
	public void open() throws IOException {
		File file = new File(baseDirectory);
		if(!file.exists())
			file.mkdirs();
		directory = FSDirectory.open(file.toPath());
	}
	
	public void close() {
		directory.close();
	}
	
	public void add(List<Context> contexts) throws Exception {
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(directory, config);
		for(Context context : contexts)
			addDocument(context, writer);
		writer.close();
	}

	private void addDocument(Context context, IndexWriter writer) throws IOException {
		Document doc = new Document();
	    doc.add(new TextField("text", context.getText(), Field.Store.YES));
	    doc.add(new StringField("source", context.getSource(), Field.Store.YES));
	    writer.addDocument(doc);
	}

	public void add(Context context) throws IOException {
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(directory, config);
		addDocument(context, writer);
		writer.close();
	}

	public List<Context> search(String search, int maxHits) throws ParseException, IOException {
		List<Context> result = new LinkedList<Context>();
		Query q = new QueryParser("text", analyzer).parse(search);
		IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, maxHits);
        ScoreDoc[] hits = docs.scoreDocs;
        for(int i = 0; i < hits.length; i++) {
            int docId = hits[i].doc;
            Document document = searcher.doc(docId);
            result.add(new Context(document.get("source"), document.get("text")));
        }
        reader.close();
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		FileIndex index = new FileIndex("index");
		index.open();
		index.add(new Context("src1", "this is some example text"));
		index.add(new Context("src2", "this is weird text"));
		index.add(new Context("src3", "this is another example text"));
		index.add(new Context("src4", "hello it's me"));
		index.add(new Context("src5", "test"));
		List<Context> result = index.search("text", 5);
		System.out.println(result);
		index.close();
	}
}
