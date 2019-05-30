package index;

import cc.kave.commons.model.ssts.IStatement;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IndexHelperTest {

    private IndexHelper indexHelperUnderTest;

    @Before
    public void setUp() {
        indexHelperUnderTest = new IndexHelper();
    }

    @Test
    @Ignore
    public void testGetLastNStatementsBeforeStatement() {
        // Setup
        final List<IStatement> statements = Arrays.asList();
        final int indexOfStatement = 0;
        final int lastNConsideredStatements = 0;
        final List<IStatement> expectedResult = Arrays.asList();

        // Run the test
        final List<IStatement> result = indexHelperUnderTest.getLastNStatementsBeforeStatement(statements, indexOfStatement, lastNConsideredStatements);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIdentifierSanitization() {
        // Setup
        final String identifier = "identifier";
        final List<String> expectedResult = Arrays.asList();

        // Run the test
        final List<String> result = indexHelperUnderTest.identifierSanitization(identifier);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSplitCamelCase() {
        // Setup
        final String identifier = "identifier";
        final List<String> expectedResult = Arrays.asList();

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
        final List<String> identifiers = Arrays.asList();
        final List<String> expectedResult = Arrays.asList();

        // Run the test
        final List<String> result = indexHelperUnderTest.removeStopWords(identifiers);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsStopWord() {
        // Setup
        final String identifier = "identifier";

        // Run the test
        final boolean result = indexHelperUnderTest.isStopWord(identifier);

        // Verify the results
        assertTrue(result);
    }

    @Test
    public void testNormalizeType() {
        // Setup
        final String type = "type";
        final String expectedResult = "result";

        // Run the test
        final String result = indexHelperUnderTest.normalizeType(type);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
