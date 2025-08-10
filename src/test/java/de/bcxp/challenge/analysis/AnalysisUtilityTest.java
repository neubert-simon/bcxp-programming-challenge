package de.bcxp.challenge.analysis;

import de.bcxp.challenge.analysis.csv.NumericComparisonType;
import de.bcxp.challenge.documentParsing.IDocumentParser;
import de.bcxp.challenge.exceptions.DocumentCreationException;
import de.bcxp.challenge.model.Document;
import de.bcxp.challenge.model.DocumentEntry;
import de.bcxp.challenge.model.csv.IEntryWithComparableNumericTuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalysisUtilityTest {

    static class TestEntry extends DocumentEntry implements IEntryWithComparableNumericTuple {

        private final double score;

        TestEntry(final String name, final double score) {
            super(name);
            this.score = score;
        }

        @Override
        public double getBestMatchScore() {
            return score;
        }
    }

    @Mock
    private IDocumentParser<TestEntry> mockParser;
    private final String MOCK_FILEPATH = "/mock/filepath";

    //region Positive Tests
    @Test
    void testReturnsMaxScoreEntry() throws DocumentCreationException, IOException, ParseException {
        TestEntry e1 = new TestEntry("A", 10);
        TestEntry e2 = new TestEntry("B", 30);
        TestEntry e3 = new TestEntry("C", 20);
        when(mockParser.parseDocument(anyString())).thenReturn(List.of(e1, e2, e3));
        Document<TestEntry> doc = new Document<>(MOCK_FILEPATH, mockParser);

        Set<TestEntry> result = AnalysisUtility.getBestMatchesForNumericColumnComparison(doc, NumericComparisonType.MAX);

        assertEquals(Set.of(e2), result);
    }

    @Test
    void testReturnsMinScoreEntry() throws DocumentCreationException, IOException, ParseException {
        TestEntry e1 = new TestEntry("A", 10);
        TestEntry e2 = new TestEntry("B", 5);
        TestEntry e3 = new TestEntry("C", 8);
        when(mockParser.parseDocument(anyString())).thenReturn(List.of(e1, e2, e3));
        Document<TestEntry> doc = new Document<>(MOCK_FILEPATH, mockParser);

        Set<TestEntry> result = AnalysisUtility.getBestMatchesForNumericColumnComparison(doc, NumericComparisonType.MIN);

        assertEquals(Set.of(e2), result);
    }

    @Test
    void testHandlesTiesForBestScore() throws IOException, ParseException, DocumentCreationException {
        TestEntry e1 = new TestEntry("A", 10);
        TestEntry e2 = new TestEntry("B", 10);
        TestEntry e3 = new TestEntry("C", 20);
       when(mockParser.parseDocument(anyString())).thenReturn(List.of(e1, e2, e3));
        Document<TestEntry> doc = new Document<>(MOCK_FILEPATH, mockParser);

        Set<TestEntry> result = AnalysisUtility.getBestMatchesForNumericColumnComparison(doc, NumericComparisonType.MIN);

        assertEquals(Set.of(e1, e2), result);
    }
    //endregion

    //region Negative Tests
    @Test
    void testThrowsForEmptyDocument() throws DocumentCreationException, IOException, ParseException {
        when(mockParser.parseDocument(anyString())).thenReturn(List.of());
        Document<TestEntry> doc = new Document<>(MOCK_FILEPATH, mockParser);

        assertThrows(IllegalArgumentException.class, () ->
                AnalysisUtility.getBestMatchesForNumericColumnComparison(doc, NumericComparisonType.MAX));
    }

    @Test
    void testThrowsForNullDocument() {
        assertThrows(IllegalArgumentException.class, () ->
                AnalysisUtility.getBestMatchesForNumericColumnComparison(null, NumericComparisonType.MIN));
    }
    //endregion
}
