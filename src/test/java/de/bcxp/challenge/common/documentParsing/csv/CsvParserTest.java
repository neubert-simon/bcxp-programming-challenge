package de.bcxp.challenge.common.documentParsing.csv;

import de.bcxp.challenge.common.exceptions.DocumentCreationException;
import de.bcxp.challenge.common.model.Document;
import de.bcxp.challenge.common.model.DocumentEntry;
import de.bcxp.challenge.common.utility.StringParsingUtility;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

final class CsvParserTest {

    final static class TestEntryImpl extends DocumentEntry {
        final double age;
        private TestEntryImpl(final String id, final double age) {
            super(id);
            this.age = age;
        }
    }

    final static class CsvParserTestImpl extends CsvParser {
        public CsvParserTestImpl(final char delimiter, final Locale locale) {
            super(delimiter, locale);
        }
        @Override
        protected List<DocumentEntry> getEntriesFromRecords(Iterable<CSVRecord> records) throws NumberFormatException {
            final List<DocumentEntry> entries = new ArrayList<>();
            records.forEach(record -> {
                try {
                    entries.add(new TestEntryImpl(record.get(0), StringParsingUtility.getDoubleFromString(record.get(1), getLocale())));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            });
            return entries;
        }
    }

    @Test
    void testParseDocumentLocaleAgnostic() throws DocumentCreationException {
        final CsvParserTestImpl parser = new CsvParserTestImpl(',', Locale.GERMANY);
        final Document document = parser.parseDocument("parsingDocuments/csv/CsvParserTest/CsvParserTest.csv");
        assertNotNull(document);

        final List<DocumentEntry> entries = document.getEntries();

        assertEquals(2, entries.size());
        assertEquals(entries.get(0).getId(), "Alice");
        assertEquals(((TestEntryImpl)entries.get(0)).age, 30);

        assertEquals(entries.get(1).getId(), "Bob");
        assertEquals(((TestEntryImpl)entries.get(1)).age, 25);
    }

    @Test
    void testParseDocumentWithUSLocale() throws DocumentCreationException {
        final CsvParserTestImpl parserUS = new CsvParserTestImpl(';', Locale.US);
        final Document document = parserUS.parseDocument("parsingDocuments/csv/CsvParserTest/CsvParserTestUS.csv");

        assertNotNull(document);
        final List<DocumentEntry> entries = document.getEntries();
        assertEquals(4, entries.size());

        assertEquals("Charlie", entries.get(0).getId());
        assertEquals(1234.56, ((TestEntryImpl) entries.get(0)).age);

        assertEquals("Diana", entries.get(1).getId());
        assertEquals(2045, ((TestEntryImpl) entries.get(1)).age);

        assertEquals("Ethan", entries.get(2).getId());
        assertEquals(3300.75, ((TestEntryImpl) entries.get(2)).age);

        assertEquals("Fiona", entries.get(3).getId());
        assertEquals(4100.10, ((TestEntryImpl) entries.get(3)).age);
    }

    @Test
    void testParseDocumentWithFranceLocale() throws DocumentCreationException {
        CsvParserTestImpl parserFR = new CsvParserTestImpl(';', Locale.FRANCE);
        final Document document = parserFR.parseDocument("parsingDocuments/csv/CsvParserTest/CsvParserTestFR.csv");

        assertNotNull(document);
        final List<DocumentEntry> entries = document.getEntries();
        assertEquals(4, entries.size());

        assertEquals("Émile", entries.get(0).getId());
        assertEquals(1234.56, ((TestEntryImpl) entries.get(0)).age);

        assertEquals("François", entries.get(1).getId());
        assertEquals(2045, ((TestEntryImpl) entries.get(1)).age);

        assertEquals("Claire", entries.get(2).getId());
        assertEquals(3300.75, ((TestEntryImpl) entries.get(2)).age);

        assertEquals("Sophie", entries.get(3).getId());
        assertEquals(4100.10, ((TestEntryImpl) entries.get(3)).age);
    }

    @Test
    void testParseDocumentWithGermanyLocale() throws DocumentCreationException {
        CsvParserTestImpl parserDE = new CsvParserTestImpl(';', Locale.GERMANY);
        final Document document = parserDE.parseDocument("parsingDocuments/csv/CsvParserTest/CsvParserTestDE.csv");

        assertNotNull(document);
        final List<DocumentEntry> entries = document.getEntries();
        assertEquals(4, entries.size());

        assertEquals("Günther", entries.get(0).getId());
        assertEquals(1234.56, ((TestEntryImpl) entries.get(0)).age);

        assertEquals("Heidi", entries.get(1).getId());
        assertEquals(2045, ((TestEntryImpl) entries.get(1)).age);

        assertEquals("Lukas", entries.get(2).getId());
        assertEquals(3300.75, ((TestEntryImpl) entries.get(2)).age);

        assertEquals("Anna", entries.get(3).getId());
        assertEquals(4100.10, ((TestEntryImpl) entries.get(3)).age);
    }

}