package index;

import cc.kave.commons.model.ssts.IStatement;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import utils.Configuration;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IndexHelperTest {

    private IndexHelper indexHelperUnderTest;

    @Before
    public void setUp() {
        indexHelperUnderTest = new IndexHelper();
    }


    @Test
    public void testIdentifierSanitization() {
        // Setup
        final String identifier = "iAmAnIdentifier";

        // Run the test
        final List<String> result = indexHelperUnderTest.identifierSanitization(identifier);

        // Verify the results
        if(Configuration.getRemoveStopWords()) {
            final List<String> expectedResult = Arrays.asList("Identifi");
            assertEquals(expectedResult, result);
        }else{
            final List<String> expectedResult = Arrays.asList("i","Am","An","Identifi");
            assertEquals(expectedResult, result);
        }
    }

    @Test
    public void testSplitCamelCase() {
        // Setup
        final String identifier = "newIdentifier";
        final List<String> expectedResult = Arrays.asList("new", "Identifier");

        // Run the test
        final List<String> result = indexHelperUnderTest.splitCamelCase(identifier);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testStemIdentifiers() {
        // Setup
        final List<String> identifiers = Arrays.asList();
        final List<String> expectedResult = Arrays.asList();

        // Run the test
        final List<String> result = indexHelperUnderTest.stemIdentifiers(identifiers);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testRemoveStopWords() {
        // Setup
        List<String> identifiers = new CopyOnWriteArrayList<>();
        identifiers.add("very");
        identifiers.add("Long");
        identifiers.add("identifier");

        List<String> expectedResult = new LinkedList<>();
        expectedResult.add("Long");
        expectedResult.add("identifier");

        // Run the test
        List<String> result = indexHelperUnderTest.removeStopWords(identifiers);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsStopWord() {
        // Setup
        final String identifier = "am";

        // Run the test
        final boolean result = indexHelperUnderTest.isStopWord(identifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    public void testNormalizeType() {
        // Setup
        final String type = "int`Something";

        final String expectedResult = "int";

        // Run the test
        final String result = indexHelperUnderTest.normalizeType(type);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
