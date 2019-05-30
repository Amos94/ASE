package index;

import utils.Configuration;
import org.apache.lucene.store.Directory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvertedIndexTest {
    @Mock
    private InvertedIndex invertedIndex;

    @Mock
    private IndexDocument indexDocument;

    @Mock
    private DataSource ds;

    @Mock
    private Connection c;

    @Mock
    private PreparedStatement prepStmt;

    @Mock
    private ResultSet rs;

    @Before
    public void setup() throws Exception {
        invertedIndex = new InvertedIndex(Configuration.INDEX_STORAGE);
        MockitoAnnotations.initMocks(this);

        when(c.prepareStatement(any(String.class))).thenReturn(prepStmt);
        when(ds.getConnection()).thenReturn(c);

        when(rs.first()).thenReturn(true);
        when(rs.getString(1)).thenReturn("eeb89fc164b54d1837ce360bc630b6107b72e045c508e289f863dc364c4a2d84");
        when(rs.getString(2)).thenReturn("Enumerable");
        when(rs.getString(3)).thenReturn("TSource");
        when(rs.getInt(4)).thenReturn(1);
        when(rs.getString(5)).thenReturn("FirstOrDefault");
        when(rs.getString(6)).thenReturn("FirstOrDefault`1[[TSource -> p:string]] identifier=static [TSource] [System.Linq.Enumerable, System.Core, 4.0.0.0].FirstOrDefault`1[[TSource -> p:string]](this [i:System.Collections.Generic.IEnumerable`1[[T -> TSource]], mscorlib, 4.0.0.0] source, [d:[TResult] [System.Func`2[[T -> TSource],[TResult -> p:bool]], mscorlib, 4.0.0.0].([T] arg)] predicate");
        when(rs.getInt(7)).thenReturn(0);
        when(rs.getInt(8)).thenReturn(0);
        when(rs.getString(9)).thenReturn("8,<return>3,End4,Line4,Read4,bool");
        when(rs.getString(10)).thenReturn("VersioningTasks.sln-contexts");
        when(prepStmt.executeQuery()).thenReturn(rs);
    }


    @Test
    public void getIndexDirectory() throws IOException {
        invertedIndex = new InvertedIndex(Configuration.INDEX_STORAGE);
        Directory dir = invertedIndex.getIndexDirectory();
        assertNotNull(dir);
    }


    @Test
    public void startIndexing() {
        invertedIndex = new InvertedIndex(Configuration.INDEX_STORAGE);
        invertedIndex.startIndexing();
    }

    @Test
    public void isIndexed() {
        assertFalse(invertedIndex.isIndexed(indexDocument));
    }

    @Test
    public void serializeIndexDocument() throws IOException {
        invertedIndex.serializeIndexDocument(indexDocument);
        verify(invertedIndex).serializeIndexDocument(indexDocument);

        invertedIndex.serializeIndexDocument(indexDocument);
    }

    @Test
    public void deserializeIndexDocument() throws IOException {
        invertedIndex.deserializeIndexDocument("0");
        verify(invertedIndex).deserializeIndexDocument("0");
    }

    @Test
    public void deserializeAll() {
        invertedIndex.deserializeAll();
        verify(invertedIndex).deserializeAll();
    }

    @Test
    public void finishIndexing() {
        invertedIndex.finishIndexing();
        verify(invertedIndex).finishIndexing();
    }


}