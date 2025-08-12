package de.bcxp.challenge.documentParsing.csv;

import de.bcxp.challenge.exceptions.DocumentCreationException;
import de.bcxp.challenge.model.Document;
import de.bcxp.challenge.model.DocumentEntry;
import de.bcxp.challenge.model.csv.WeatherEntry;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

class WeatherCsvParserTest {

    private final WeatherCsvParser parser = new WeatherCsvParser(',', Locale.GERMANY);

    @Test
    void testParseDocument() throws DocumentCreationException {
        final Document document = parser.parseDocument("parsingDocuments/csv/WeatherCsvParserTest.csv");
        final List<DocumentEntry> entries = document.getEntries();

        assertNotNull(entries);
        assertEquals(3, entries.size());

        final DocumentEntry first = entries.get(0);
        assertNotNull(first);
        assertInstanceOf(WeatherEntry.class, first);
        final WeatherEntry firstWeather = (WeatherEntry) first;
        assertEquals("1", firstWeather.getDay());
        assertEquals(88.0, firstWeather.getMaxTemp());
        assertEquals(59.0, firstWeather.getMinTemp());

        final DocumentEntry second = entries.get(1);
        assertNotNull(second);
        assertInstanceOf(WeatherEntry.class, second);
        final WeatherEntry secondWeather = (WeatherEntry) second;
        assertEquals("2", secondWeather.getDay());
        assertEquals(79.0, secondWeather.getMaxTemp());
        assertEquals(63.0, secondWeather.getMinTemp());
    }

    @Test
    void testParseDocumentFileNotFound() {
        assertThrows(DocumentCreationException.class,
                () -> parser.parseDocument("non_existent_file.csv"));
    }

    @Test
    void testParseDocumentMalformedNumber() {
        final CsvParser parserNan = new WeatherCsvParser(',', Locale.GERMANY);
        assertThrows(DocumentCreationException.class,
                () -> parserNan.parseDocument("ignored.csv"));

        final CsvParser parserNum = new WeatherCsvParser(',', Locale.GERMANY);
        assertThrows(DocumentCreationException.class,
                () -> parserNum.parseDocument("ignored.csv"));
    }

    @Test
    void testParseDocumentMissingField() {
        final CsvParser parser = new WeatherCsvParser(',', Locale.GERMANY);
        assertThrows(DocumentCreationException.class,
                () -> parser.parseDocument("ignored.csv"));
    }
}
