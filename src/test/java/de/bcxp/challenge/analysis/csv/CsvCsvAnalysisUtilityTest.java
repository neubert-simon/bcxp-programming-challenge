package de.bcxp.challenge.analysis.csv;

import de.bcxp.challenge.documentParsing.IDocumentParser;
import de.bcxp.challenge.exceptions.DocumentCreationException;
import de.bcxp.challenge.model.Document;
import de.bcxp.challenge.model.DocumentEntry;
import de.bcxp.challenge.model.csv.IEntryWithComparableNumericTuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvCsvAnalysisUtilityTest {

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

        @Override
        public int compareTo(IEntryWithComparableNumericTuple o) {
            return Double.compare(score, o.getBestMatchScore());
        }
    }

    @Mock
    private IDocumentParser mockParser;
    private final String MOCK_FILEPATH = "/mock/filepath";

    //region Positive Tests
    @Test
    void testReturnsMaxScoreEntry() throws DocumentCreationException {
        TestEntry e1 = new TestEntry("A", 10);
        TestEntry e2 = new TestEntry("B", 30);
        TestEntry e3 = new TestEntry("C", 20);
        when(mockParser.parseDocument(anyString())).thenReturn(new Document(List.of(e1, e2, e3)));

        final Set<DocumentEntry> result = CsvAnalysisUtility
                .getBestMatchesForNumericColumnComparison(
                        mockParser.parseDocument(MOCK_FILEPATH)
                );

        assertEquals(Set.of(e2), result);
    }

    @Test
    void testHandlesTiesForBestScore() throws DocumentCreationException {
        TestEntry e1 = new TestEntry("A", 10);
        TestEntry e2 = new TestEntry("B", 10);
        TestEntry e3 = new TestEntry("C", 5);
        when(mockParser.parseDocument(anyString())).thenReturn(new Document(List.of(e1, e2, e3)));

        final Set<DocumentEntry> result = CsvAnalysisUtility
                .getBestMatchesForNumericColumnComparison(
                        mockParser.parseDocument(MOCK_FILEPATH)
                );

        assertEquals(Set.of(e1, e2), result);
    }
    //endregion

    //region Negative Tests
    @Test
    void testThrowsForEmptyDocument() throws DocumentCreationException {
        when(mockParser.parseDocument(anyString())).thenReturn(new Document(List.of()));

        assertThrows(NoSuchElementException.class, () ->
                CsvAnalysisUtility.getBestMatchesForNumericColumnComparison(
                        mockParser.parseDocument(MOCK_FILEPATH)
                ));
    }

    @Test
    void testThrowsForNullDocument() {
        assertThrows(IllegalArgumentException.class, () ->
                CsvAnalysisUtility
                        .getBestMatchesForNumericColumnComparison(
                                null
                        ));
    }
    //endregion
}
