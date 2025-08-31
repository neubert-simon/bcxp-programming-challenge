package de.bcxp.challenge.weather;

import de.bcxp.challenge.common.analysis.IDocumentAnalyser;
import de.bcxp.challenge.common.documentParsing.IDocumentParser;
import de.bcxp.challenge.common.exceptions.DocumentCreationException;
import de.bcxp.challenge.common.model.Document;
import de.bcxp.challenge.common.model.DocumentEntry;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WeatherAnalyserTest {

    @Mock
    private IDocumentParser mockParser;

    //region Negative Tests
    @Test
    public void getBestMatchesTestEmptyDocument() throws DocumentCreationException {

        final IDocumentAnalyser analyser = new WeatherAnalyser();
        when(mockParser.parseDocument()).thenReturn(new Document(List.of()));
        final Document emptyDocument = mockParser.parseDocument();

        assertThrows(NoSuchElementException.class, () -> analyser.getBestMatches(emptyDocument));

    }
    //endregion

    //region Positive Tests
    @Test
    public void getBestMatchesTest() throws DocumentCreationException {

        //region Test 1
        final IDocumentAnalyser analyser = new WeatherAnalyser();

        List<DocumentEntry> testEntries = List.of(
                new WeatherEntry("1", 3, 2),   //best match
                new WeatherEntry("2", 50, -50)
        );
        when(mockParser.parseDocument()).thenReturn(new Document(testEntries));
        Document mockDocument = mockParser.parseDocument();
        
        Set<DocumentEntry> bestMatch = analyser.getBestMatches(mockDocument);
        assertEquals(Set.of(testEntries.get(0)), bestMatch);
        assertNotEquals(Set.of(testEntries.get(1)), bestMatch);
        //endregion

        //region Test 2
        testEntries = List.of(
                new WeatherEntry("1", 24, 12), //best match
                new WeatherEntry("2", 28, 9)
        );
        when(mockParser.parseDocument()).thenReturn(new Document(testEntries));
        mockDocument = mockParser.parseDocument();

        bestMatch = analyser.getBestMatches(mockDocument);
        assertEquals(Set.of(testEntries.get(0)), bestMatch);
        assertNotEquals(Set.of(testEntries.get(1)), bestMatch);
        //endregion
    }

    @Test
    public void getBestMatchesTestMultipleMatches() throws DocumentCreationException {

        final IDocumentAnalyser analyser = new WeatherAnalyser();

        final List<DocumentEntry> entries = List.of(
                new WeatherEntry("1", 10, 5),    // Spread = 5 -- best match 1
                new WeatherEntry("2", 7, 2),     // Spread = 5 -- best match 1
                new WeatherEntry("3", 50, 20),   // Spread = 30
                new WeatherEntry("3", 50, -50)   // Spread = 100
        );
        when(mockParser.parseDocument()).thenReturn(new Document(entries));
        final Document document = mockParser.parseDocument();

        final Set<DocumentEntry> bestMatches = analyser.getBestMatches(document);

        assertEquals(2, bestMatches.size());
        assertEquals(bestMatches, Set.of(entries.get(0), entries.get(1)));
    }

    @Test
    public void getBestMatchesTestEmptyDocumentExtremeValues() throws DocumentCreationException {
        final IDocumentAnalyser analyser = new WeatherAnalyser();
        final List<DocumentEntry> testEntries = List.of(
                new WeatherEntry("5", Double.MAX_VALUE, Double.MAX_VALUE - 1),
                new WeatherEntry("8", 0, -Double.MAX_VALUE)
        );
        when(mockParser.parseDocument()).thenReturn(new Document(testEntries));
        final Document mockDocument = mockParser.parseDocument();

        assertEquals(Set.of(testEntries.get(0)), analyser.getBestMatches(mockDocument));
    }
    //endregion
}