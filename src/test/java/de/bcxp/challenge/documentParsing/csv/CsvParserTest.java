package de.bcxp.challenge.documentParsing.csv;

import de.bcxp.challenge.exceptions.DocumentCreationException;
import de.bcxp.challenge.model.Document;
import de.bcxp.challenge.model.DocumentEntry;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

class CsvParserTest {

    static class TestEntryImpl extends DocumentEntry {
        protected TestEntryImpl(final String id, final int age) {
            super(id);
            this.age = age;
        }
        final int age;
    }

    static class CsvParserTestImpl extends CsvParser {

        public CsvParserTestImpl(final char delimiter, final Locale locale) {
            super(delimiter, locale);
        }
        @Override
        List<DocumentEntry> getEntriesFromRecords(Iterable<CSVRecord> records) throws NumberFormatException {
            List<DocumentEntry> entries = new ArrayList<>();
            records.forEach(record -> entries.add(new TestEntryImpl(record.get(0), Integer.parseInt(record.get(1)))));
            return entries;
        }
    }

    private final CsvParserTestImpl parser = new CsvParserTestImpl(',', Locale.GERMANY);

    @Test
    void testParseEmptyDocument() throws DocumentCreationException {

        final Document document = parser.parseDocument("parsingDocuments/csv/CsvParserTest.csv");
        assertNotNull(document);

        List<DocumentEntry> entries = document.getEntries();

        assertEquals(2, entries.size());
        assertEquals(entries.get(0).getId(), "Alice");
        assertEquals(((TestEntryImpl)entries.get(0)).age, 30);

        assertEquals(entries.get(1).getId(), "Bob");
        assertEquals(((TestEntryImpl)entries.get(1)).age, 25);
    }

}
