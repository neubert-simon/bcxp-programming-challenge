package de.bcxp.challenge.countries;

import de.bcxp.challenge.common.documentParsing.csv.CsvParser;
import de.bcxp.challenge.common.exceptions.DocumentCreationException;
import de.bcxp.challenge.common.model.Document;
import de.bcxp.challenge.common.model.DocumentEntry;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CountryCsvParserTest {


    @Test
    void testParseDocument() throws DocumentCreationException {
        final CountryCsvParser parser = new CountryCsvParser(',', Locale.US, "parsingDocuments/csv/CountryCsvParserTest.csv");
        Document document = parser.parseDocument();
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
        final CountryCsvParser parser = new CountryCsvParser(',', Locale.US, "non_existent_file.csv");
        assertThrows(DocumentCreationException.class, parser::parseDocument);
    }

    @Test
    void testParseDocumentMalformedNumber() {
        final CsvParser parserNan = new CountryCsvParser(',', Locale.US, "ignored.csv");
        assertThrows(DocumentCreationException.class, parserNan::parseDocument);

        final CsvParser parserNum = new CountryCsvParser(',', Locale.US, "ignored.csv");
        assertThrows(DocumentCreationException.class, parserNum::parseDocument);
    }

    @Test
    void testParseDocumentMissingField() {
        final CsvParser parser = new CountryCsvParser(',', Locale.US, "ignored.csv");
        assertThrows(DocumentCreationException.class, parser::parseDocument);
    }
}
