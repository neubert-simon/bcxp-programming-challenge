package de.bcxp.challenge.documentParsing.csv;

import de.bcxp.challenge.model.csv.WeatherEntry;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class WeatherCsvParserTest {

    private final WeatherCsvParser parser = new WeatherCsvParser(',', Locale.GERMANY);

    @Test
    void testParseDocument() throws IOException, ParseException {
        List<WeatherEntry> entries = parser.parseDocument("weatherCsvParserTest.csv");

        assertNotNull(entries);
        assertEquals(3, entries.size());

        WeatherEntry first = entries.get(0);
        assertEquals("1", first.getDay());
        assertEquals(88.0, first.getMaxTemp());
        assertEquals(59.0, first.getMinTemp());

        WeatherEntry second = entries.get(1);
        assertEquals("2", second.getDay());
        assertEquals(79.0, second.getMaxTemp());
        assertEquals(63.0, second.getMinTemp());
    }

    @Test
    void testParseDocumentFileNotFound() {
        assertThrows(IOException.class,
                () -> parser.parseDocument("non_existent_file.csv"));
    }

    @Test
    void testParseDocumentMalformedNumber() {
        final WeatherCsvParser parserNan = new WeatherCsvParser(',', Locale.GERMANY) {
            @Override
            protected Iterable<org.apache.commons.csv.CSVRecord> readFileWithHeader(String filepath) throws IOException {
                String csv = "Day,MxT,MnT\n1,notANumber,59";
                return org.apache.commons.csv.CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setDelimiter(',')
                        .get()
                        .parse(new java.io.StringReader(csv))
                        .getRecords();
            }
        };

        assertThrows(NumberFormatException.class,
                () -> parserNan.parseDocument("ignored.csv"));

        final WeatherCsvParser parserNum = new WeatherCsvParser(',', Locale.GERMANY) {
            @Override
            protected Iterable<org.apache.commons.csv.CSVRecord> readFileWithHeader(String filepath) throws IOException {
                String csv = "Day,MxT,MnT\n1,1234sn,59";
                return org.apache.commons.csv.CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setDelimiter(',')
                        .get()
                        .parse(new java.io.StringReader(csv))
                        .getRecords();
            }
        };

        assertThrows(NumberFormatException.class,
                () -> parserNum.parseDocument("ignored.csv"));
    }

    @Test
    void testParseDocumentMissingField() {
        WeatherCsvParser parser = new WeatherCsvParser(',', Locale.GERMANY) {
            @Override
            protected Iterable<org.apache.commons.csv.CSVRecord> readFileWithHeader(String filepath) throws IOException {
                String csv = "Day,MxT\n1,88"; // MnT missing
                return org.apache.commons.csv.CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setDelimiter(',')
                        .get()
                        .parse(new java.io.StringReader(csv))
                        .getRecords();
            }
        };

        assertThrows(IllegalArgumentException.class,
                () -> parser.parseDocument("ignored.csv"));
    }
}
