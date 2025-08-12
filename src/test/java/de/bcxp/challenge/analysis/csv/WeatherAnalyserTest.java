package de.bcxp.challenge.analysis.csv;

import de.bcxp.challenge.analysis.IDocumentAnalyser;
import de.bcxp.challenge.documentParsing.IDocumentParser;
import de.bcxp.challenge.exceptions.DocumentCreationException;
import de.bcxp.challenge.model.Document;
import de.bcxp.challenge.model.DocumentEntry;
import de.bcxp.challenge.model.csv.WeatherEntry;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import java.io.IOException;
import java.text.ParseException;
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
    private final String MOCK_FILEPATH = "/mock/filepath";

    //region Negative Tests
    @Test
    public void getBestMatchesTestEmptyDocument() throws IOException, ParseException, DocumentCreationException {

        final IDocumentAnalyser analyser = new WeatherAnalyser();
        when(mockParser.parseDocument(anyString())).thenReturn(new Document(List.of()));
        final Document emptyDocument = mockParser.parseDocument(MOCK_FILEPATH);

        assertThrows(NoSuchElementException.class, () -> analyser.getBestMatches(emptyDocument));

    }

    //endregion

    //region Positive Tests
    @Test
    public void getBestMatchesTest() throws DocumentCreationException, IOException, ParseException {

        //region Test 1
        final IDocumentAnalyser analyser = new WeatherAnalyser();

        List<DocumentEntry> testEntries = List.of(
                new WeatherEntry("1", 3, 2),   //best match
                new WeatherEntry("2", 50, -50)
        );
        when(mockParser.parseDocument(anyString())).thenReturn(new Document(testEntries));
        Document mockDocument = mockParser.parseDocument(MOCK_FILEPATH);
        
        Set<DocumentEntry> bestMatch = analyser.getBestMatches(mockDocument);
        assertEquals(Set.of(testEntries.get(0)), bestMatch);
        assertNotEquals(Set.of(testEntries.get(1)), bestMatch);
        //endregion

        //region Test 2
        testEntries = List.of(
                new WeatherEntry("1", 24, 12), //best match
                new WeatherEntry("2", 28, 9)
        );
        when(mockParser.parseDocument(anyString())).thenReturn(new Document(testEntries));
        mockDocument = mockParser.parseDocument(MOCK_FILEPATH);

        bestMatch = analyser.getBestMatches(mockDocument);
        assertEquals(Set.of(testEntries.get(0)), bestMatch);
        assertNotEquals(Set.of(testEntries.get(1)), bestMatch);
        //endregion
    }

    @Test
    public void getBestMatchesTestMultipleMatches() throws DocumentCreationException, IOException, ParseException {

        final IDocumentAnalyser analyser = new WeatherAnalyser();

        List<DocumentEntry> entries = List.of(
                new WeatherEntry("1", 10, 5),    // Spread = 5 -- best match 1
                new WeatherEntry("2", 7, 2),     // Spread = 5 -- best match 1
                new WeatherEntry("3", 50, 20),   // Spread = 30
                new WeatherEntry("3", 50, -50)   // Spread = 100
        );
        when(mockParser.parseDocument(anyString())).thenReturn(new Document(entries));
        final Document document = mockParser.parseDocument(MOCK_FILEPATH);

        Set<DocumentEntry> bestMatches = analyser.getBestMatches(document);

        assertEquals(2, bestMatches.size());
        assertEquals(bestMatches, Set.of(entries.get(0), entries.get(1)));
    }

    @Test
    public void getBestMatchesTestEmptyDocumentExtremeValues() throws IOException, ParseException, DocumentCreationException {
        final IDocumentAnalyser analyser = new WeatherAnalyser();
        List<DocumentEntry> testEntries = List.of(
                new WeatherEntry("5", Double.MAX_VALUE, Double.MAX_VALUE - 1),
                new WeatherEntry("8", 0, -Double.MAX_VALUE)
        );
        when(mockParser.parseDocument(anyString())).thenReturn(new Document(testEntries));
        final Document mockDocument = mockParser.parseDocument(MOCK_FILEPATH);

        assertEquals(Set.of(testEntries.get(0)), analyser.getBestMatches(mockDocument));
    }
    //endregion
}