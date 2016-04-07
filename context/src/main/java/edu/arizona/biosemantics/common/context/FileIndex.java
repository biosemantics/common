package edu.arizona.biosemantics.common.context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
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
	
	public void destory() throws IOException {
		this.close();
		File file = new File(baseDirectory);
		FileUtils.deleteDirectory(file);
	}
	
	public void open() throws IOException {
		if(this.isClosed()) {
			File file = new File(baseDirectory);
			if(!file.exists())
				file.mkdirs();
			directory = FSDirectory.open(file.toPath());
		}
	}
	
	private boolean isClosed() {
		return directory == null;
	}

	public void close() {
		if(this.isOpen())
			directory.close();
	}
	
	private boolean isOpen() {
		return directory != null;
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
		doc.add(new IntField("id", context.getId(), Field.Store.YES));
	    doc.add(new StringField("source", context.getSource(), Field.Store.YES));
	    doc.add(new TextField("text", context.getText(), Field.Store.YES));
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
            result.add(new Context(Integer.valueOf(document.get("id")), document.get("source"), document.get("text")));
        }
        reader.close();
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		FileIndex index = new FileIndex("index");
		index.destory();
		index.open();
		index.add(new Context(1, "src1", "this is some example text"));
		index.add(new Context(2, "src2", "this is weird text"));
		index.add(new Context(3, "src3", "this is another example text"));
		index.add(new Context(4, "src4", "hello it's me"));
		index.add(new Context(5, "src5", "test"));
		List<Context> result = index.search("text", 5);
		System.out.println(result);
		index.close();
	}
}
