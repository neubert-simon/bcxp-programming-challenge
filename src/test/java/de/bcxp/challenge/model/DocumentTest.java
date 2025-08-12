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
    private IDocumentParser mockParser;
    private final String MOCK_FILEPATH = "/mock/filepath";

    static class TestEntry extends DocumentEntry {
        public TestEntry(String id) {
            super(id);
        }
    }

    //region Positive Tests
    @Test
    void testDocumentCreation() throws Exception {

        List<DocumentEntry> entries = List.of(
                new TestEntry("entry1"),
                new TestEntry("entry2")
        );

        when(mockParser.parseDocument(anyString())).thenReturn(new Document(entries));
        Document document = mockParser.parseDocument(MOCK_FILEPATH);

        assertNotNull(document.getEntries());
        assertEquals(2, document.getEntries().size());
        assertEquals("entry1", document.getEntries().get(0).getId());
    }

    @Test
    void testDocumentCreationWithEmptyEntries() throws IOException, ParseException, DocumentCreationException {
        when(mockParser.parseDocument(anyString())).thenReturn(new Document(List.of()));
        assertDoesNotThrow(() -> mockParser.parseDocument(MOCK_FILEPATH));
    }
    //endregion

    //region Negative Tests
    @Test
    void testDocumentCreationThrowsIOException() throws Exception {
        when(mockParser.parseDocument(anyString())).thenThrow(new IOException("IO problem"));
        assertThrows(IOException.class,
                () -> mockParser.parseDocument(MOCK_FILEPATH));
    }

    @Test
    void testDocumentCreationThrowsParseException() throws Exception {
        when(mockParser.parseDocument(anyString())).thenThrow(new ParseException("parse error", 0));
        assertThrows(ParseException.class,
                () -> mockParser.parseDocument(MOCK_FILEPATH));
    }

    @Test
    void testDocumentCreationWithNullFilepath() {
        assertThrows(IllegalArgumentException.class,
                () -> new Document(null));
    }
    //endregion

}
