package de.bcxp.challenge.documentParsing.csv;

import de.bcxp.challenge.model.DocumentEntry;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

class CsvParserTest {

    static class CsvParserTestImpl extends CsvParser<DocumentEntry> {
        public CsvParserTestImpl(final char delimiter, final Locale locale) {
            super(delimiter, locale);
        }
        public List<DocumentEntry> parseDocument(String filepath) {
            return List.of();
        }
    }

    private final CsvParserTestImpl parser = new CsvParserTestImpl(',', Locale.GERMANY);

    @Test
    void testReadFileWithHeader() throws IOException {
        Iterable<CSVRecord> records = parser.readFileWithHeader("csvParserTest.csv");
        assertNotNull(records);

        List<CSVRecord> recordList = (List<CSVRecord>) records;
        assertEquals(2, recordList.size());

        CSVRecord first = recordList.get(0);
        assertEquals("Alice", first.get("name"));
        assertEquals("30", first.get("age"));

        CSVRecord second = recordList.get(1);
        assertEquals("Bob", second.get("name"));
        assertEquals("25", second.get("age"));
    }

    @Test
    void testReadFileWithHeaderNullFilePath() {
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> parser.readFileWithHeader(null));
        assertTrue(e.getMessage().contains("Filepath can't be empty"));
    }

    @Test
    void testReadFileWithHeaderEmptyFilePath() {
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> parser.readFileWithHeader(""));
        assertEquals("Filepath can't be empty", e.getMessage());
    }

    @Test
    void testReadFileWithHeaderFileNotFound() {
        final String filepath = "nonexistent.csv";
        FileNotFoundException e = assertThrows(FileNotFoundException.class,
                () -> parser.readFileWithHeader(filepath));
        assertEquals("Resource not found: " + filepath, e.getMessage());
    }

    @Test
    void testReadFileWithHeaderIOException() {

        CsvParserTestImpl brokenParser = new CsvParserTestImpl(',', Locale.GERMANY) {
            @Override
            Iterable<CSVRecord> readFileWithHeader(String filepath) throws IOException {
                throw new IOException("Forced IO exception");
            }
        };

        IOException ex = assertThrows(IOException.class, () -> brokenParser.readFileWithHeader("test.csv"));
        assertTrue(ex.getMessage().contains("Forced IO exception"));
    }
}
