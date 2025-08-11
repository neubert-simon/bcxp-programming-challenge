package de.bcxp.challenge.documentParsing.csv;

import de.bcxp.challenge.model.DocumentEntry;
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
        List<DocumentEntry> entries = parser.parseDocument("countryCsvParserTest.csv");

        assertNotNull(entries);
        assertEquals(3, entries.size());

        DocumentEntry first = entries.get(0);
        assertNotNull(first);
        assertInstanceOf(CountryEntry.class, first);
        CountryEntry firstCountry = (CountryEntry) first;
        assertEquals("Germany", firstCountry.getCountry());
        assertEquals(8880, firstCountry.getPopulation());
        assertEquals(590, firstCountry.getArea());

        DocumentEntry second = entries.get(1);
        assertNotNull(second);
        assertInstanceOf(CountryEntry.class, second);
        CountryEntry secondCountry = (CountryEntry) second;
        assertEquals("Italy", secondCountry.getCountry());
        assertEquals(932_123, secondCountry.getPopulation());
        assertEquals(630.21, secondCountry.getArea());
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

        assertThrows(NumberFormatException.class,
                () -> parserNan.parseDocument("ignored.csv"));

        final CountryCsvParser parserNum = new CountryCsvParser(',', Locale.US) {
            @Override
            protected Iterable<org.apache.commons.csv.CSVRecord> readFileWithHeader(String filepath) throws IOException {
                String csv = "Name,Population,Area (km²)\nGermany,1234ms,59";
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
