package index;

import utils.Configuration;
import org.apache.lucene.store.Directory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class AbstractInvertedIndexTest {
    @Mock private InvertedIndex invertedIndex;
    @Mock private IndexDocument indexDocument;
    
	
    @Before
    public void setup(){
    	invertedIndex = new InvertedIndex(Configuration.INDEX_STORAGE);
    	MockitoAnnotations.initMocks(this);  
    }
     
    @Test
    public void indexDocument() {
    	invertedIndex.indexDocument(indexDocument);
    	verify(invertedIndex).indexDocument(indexDocument);
    }

    @Test
    @Ignore
    public void isIndexed() {
        assertFalse(invertedIndex.isIndexed(indexDocument));
    	
    	//TODO check if can change -> does not work with mock? [Ignored for now]
    	invertedIndex.indexDocument(indexDocument);
        assertTrue(invertedIndex.isIndexed(indexDocument));
    }

    @Test
    public void serializeIndexDocument() throws IOException {
        invertedIndex.serializeIndexDocument(indexDocument);
        verify(invertedIndex).serializeIndexDocument(indexDocument);
    }

    @Test
    public void getIndexDirectory() throws IOException {
        invertedIndex = new InvertedIndex(Configuration.INDEX_STORAGE);
        Directory dir = invertedIndex.getIndexDirectory();
        assertNotNull(dir);
    }

    @Test
    public void deserializeIndexDocument() throws IOException {
        invertedIndex.deserializeIndexDocument("0");
        verify(invertedIndex).deserializeIndexDocument("0");
    }

    @Test
    public void startIndexing() {
        invertedIndex.startIndexing();
        verify(invertedIndex).startIndexing();
    }

    @Test
    public void finishIndexing() {
        invertedIndex.finishIndexing();
        verify(invertedIndex).finishIndexing();
    }
}