package de.bcxp.challenge.countries;

import de.bcxp.challenge.common.analysis.IDocumentAnalyser;
import de.bcxp.challenge.common.documentParsing.IDocumentParser;
import de.bcxp.challenge.common.exceptions.DocumentCreationException;
import de.bcxp.challenge.common.model.Document;
import de.bcxp.challenge.common.model.DocumentEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountryAnalyserTest {

    @Mock
    private IDocumentParser mockParser;

    //region Negative Tests
    @Test
    public void getBestMatchesTestEmptyDocument() throws DocumentCreationException {

        final IDocumentAnalyser analyser = new CountryAnalyser();
        when(mockParser.parseDocument()).thenReturn(new Document(List.of()));

        assertThrows(NoSuchElementException.class,
                () -> analyser.getBestMatches(mockParser.parseDocument()));

    }
    //endregion

    //region Positive Tests
    @Test
    public void getBestMatchesTest() throws DocumentCreationException {

        //region Test 1
        final IDocumentAnalyser analyser = new CountryAnalyser();
        List<DocumentEntry> testEntries = List.of(
                new CountryEntry("Germany", 1_463_865_525, 3_287_000),
                new CountryEntry("Italy", 50, 4)  //best match
        );
        when(mockParser.parseDocument()).thenReturn(new Document(testEntries));
        Document mockDocument = mockParser.parseDocument();

        Set<DocumentEntry> bestMatch = analyser.getBestMatches(mockDocument);
        assertEquals(Set.of(testEntries.get(0)), bestMatch);
        assertNotEquals(Set.of(testEntries.get(1)), bestMatch);
        //endregion

        //region Test 2
        testEntries = List.of(
                new CountryEntry("Albania", 28, 20), //best match
                new CountryEntry("Algeria", 17, 120)
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

        final IDocumentAnalyser analyser = new CountryAnalyser();

        final List<DocumentEntry> entries = List.of(
                new CountryEntry("Angola", 100_00, 10),     // Density = 1.000 people/km² -- best match 1
                new CountryEntry("China", 100_00, 10),      // Density = 1.000 people/km² -- best match 1
                new CountryEntry("Japan", 123_123, 800),    // Density = 152,9 people/km²
                new CountryEntry("Uruguay", 654, 30)        // Density = 21,8 people/km²
        );
        when(mockParser.parseDocument()).thenReturn(new Document(entries));
        final Document document = mockParser.parseDocument();

        Set<DocumentEntry> bestMatches = analyser.getBestMatches(document);

        assertEquals(2, bestMatches.size());
        assertEquals(bestMatches, Set.of(entries.get(0), entries.get(1)));
    }

    @Test
    public void getBestMatchesTestEmptyDocumentExtremeValues() throws DocumentCreationException {

        final IDocumentAnalyser analyser = new CountryAnalyser();
        final List<DocumentEntry> testEntries = List.of(
                new CountryEntry("Germany", Long.MAX_VALUE, Double.MAX_VALUE),
                new CountryEntry("Germany", 0, Double.MAX_VALUE)
        );

        when(mockParser.parseDocument()).thenReturn(new Document(testEntries));
        final Document mockDocument = mockParser.parseDocument();

        assertEquals(Set.of(testEntries.get(0)), analyser.getBestMatches(mockDocument));
    }
    //endregion
}