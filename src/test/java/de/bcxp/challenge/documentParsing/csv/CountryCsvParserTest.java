package de.bcxp.challenge.documentParsing.csv;

import de.bcxp.challenge.exceptions.DocumentCreationException;
import de.bcxp.challenge.model.Document;
import de.bcxp.challenge.model.DocumentEntry;
import de.bcxp.challenge.model.csv.CountryEntry;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CountryCsvParserTest {

    private final CountryCsvParser parser = new CountryCsvParser(',', Locale.US);

    @Test
    void testParseDocument() throws DocumentCreationException {
        Document document = parser.parseDocument("parsingDocuments/csv/CountryCsvParserTest.csv");
        final List<DocumentEntry> entries = document.getEntries();

        assertNotNull(entries);
        assertEquals(3, entries.size());

        final DocumentEntry first = entries.get(0);
        assertNotNull(first);
        assertInstanceOf(CountryEntry.class, first);
        CountryEntry firstCountry = (CountryEntry) first;
        assertEquals("Germany", firstCountry.getCountry());
        assertEquals(8880, firstCountry.getPopulation());
        assertEquals(590, firstCountry.getArea());

        final DocumentEntry second = entries.get(1);
        assertNotNull(second);
        assertInstanceOf(CountryEntry.class, second);
        CountryEntry secondCountry = (CountryEntry) second;
        assertEquals("Italy", secondCountry.getCountry());
        assertEquals(932_123, secondCountry.getPopulation());
        assertEquals(630.21, secondCountry.getArea());
    }

    @Test
    void testParseDocumentFileNotFound() {
        assertThrows(DocumentCreationException.class,
                () -> parser.parseDocument("non_existent_file.csv"));
    }

    @Test
    void testParseDocumentMalformedNumber() {
        final CsvParser parserNan = new CountryCsvParser(',', Locale.US);
        assertThrows(DocumentCreationException.class,
                () -> parserNan.parseDocument("ignored.csv"));

        final CsvParser parserNum = new CountryCsvParser(',', Locale.US);
        assertThrows(DocumentCreationException.class,
                () -> parserNum.parseDocument("ignored.csv"));
    }

    @Test
    void testParseDocumentMissingField() {
        final CsvParser parser = new CountryCsvParser(',', Locale.US);
        assertThrows(DocumentCreationException.class,
                () -> parser.parseDocument("ignored.csv"));
    }
}
