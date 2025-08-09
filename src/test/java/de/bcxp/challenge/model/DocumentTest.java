package de.bcxp.challenge.model;

import de.bcxp.challenge.documentParsing.IDocumentParser;
import de.bcxp.challenge.exceptions.DocumentCreationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentTest {

    @Mock
    private IDocumentParser<TestEntry> mockParser;

    static class TestEntry extends DocumentEntry {
        public TestEntry(String id) {
            super(id);
        }
    }

    @Test
    void testDocumentCreationSuccess() throws Exception {

        List<TestEntry> entries = List.of(
                new TestEntry("entry1"),
                new TestEntry("entry2")
        );

        when(mockParser.parseDocument(anyString())).thenReturn(entries);
        Document<TestEntry> document = new Document<>("validFile.csv", mockParser);

        assertNotNull(document.getEntries());
        assertEquals(2, document.getEntries().size());
        assertEquals("entry1", document.getEntries().get(0).getId());
    }

    @Test
    void testDocumentCreationThrowsIOException() throws Exception {
        when(mockParser.parseDocument(anyString())).thenThrow(new IOException("IO problem"));
        assertThrows(DocumentCreationException.class,
                () -> new Document<>("file.csv", mockParser));
    }

    @Test
    void testDocumentCreationThrowsParseException() throws Exception {
        when(mockParser.parseDocument(anyString())).thenThrow(new ParseException("parse error", 0));
        assertThrows(DocumentCreationException.class,
                () -> new Document<>("file.csv", mockParser));
    }

    @Test
    void testDocumentCreationWithNullFilepath() {
        assertThrows(IllegalArgumentException.class,
                () -> new Document<>(null, mockParser));
    }

    @Test
    void testDocumentCreationWithEmptyFilepath() {
        assertThrows(IllegalArgumentException.class,
                () -> new Document<>("", mockParser));
    }
}
