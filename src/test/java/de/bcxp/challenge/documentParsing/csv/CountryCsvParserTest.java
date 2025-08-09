package de.bcxp.challenge.documentParsing.csv;

import de.bcxp.challenge.model.csv.CountryEntry;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CountryCsvParserTest {

    private final CountryCsvParser parser = new CountryCsvParser(',', Locale.US);

    @Test
    void testParseDocument() throws IOException, ParseException {
        List<CountryEntry> entries = parser.parseDocument("countryCsvParserTest.csv");

        assertNotNull(entries);
        assertEquals(3, entries.size());

        CountryEntry first = entries.get(0);
        assertEquals("Germany", first.getCountry());
        assertEquals(8880, first.getPopulation());
        assertEquals(590, first.getArea());

        CountryEntry second = entries.get(1);
        assertEquals("Italy", second.getCountry());
        assertEquals(932_123, second.getPopulation());
        assertEquals(630.21, second.getArea());
    }

    @Test
    void testParseDocumentFileNotFound() {
        assertThrows(IOException.class,
                () -> parser.parseDocument("non_existent_file.csv"));
    }

    @Test
    void testParseDocumentMalformedNumber() {
        final CountryCsvParser parserNan = new CountryCsvParser(',', Locale.US) {
            @Override
            protected Iterable<org.apache.commons.csv.CSVRecord> readFileWithHeader(String filepath) throws IOException {
                String csv = "Name,Population,Area (km²)\nGermany,notANumber,59";
                return org.apache.commons.csv.CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setDelimiter(',')
                        .get()
                        .parse(new java.io.StringReader(csv))
                        .getRecords();
            }
        };

        assertThrows(ParseException.class,
                () -> parserNan.parseDocument("ignored.csv"));
    }

    @Test
    void testParseDocumentMissingField() {
        CountryCsvParser parser = new CountryCsvParser(',', Locale.US) {
            @Override
            protected Iterable<org.apache.commons.csv.CSVRecord> readFileWithHeader(String filepath) throws IOException {
                String csv = "Name,Population\nCanada,88";
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
