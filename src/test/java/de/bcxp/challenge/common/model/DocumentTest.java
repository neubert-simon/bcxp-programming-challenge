package de.bcxp.challenge.common.model;

import de.bcxp.challenge.common.documentParsing.IDocumentParser;
import de.bcxp.challenge.common.exceptions.DocumentCreationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentTest {

    @Mock
    private IDocumentParser mockParser;

    static class TestEntry extends DocumentEntry {
        public TestEntry(String id) {
            super(id);
        }
    }

    //region Positive Tests
    @Test
    void testDocumentCreation() throws Exception {

        final List<DocumentEntry> entries = List.of(
                new TestEntry("entry1"),
                new TestEntry("entry2")
        );

        when(mockParser.parseDocument()).thenReturn(new Document(entries));
        final Document document = mockParser.parseDocument();

        assertNotNull(document.getEntries());
        assertEquals(2, document.getEntries().size());
        assertEquals("entry1", document.getEntries().get(0).getId());
        assertEquals("entry2", document.getEntries().get(1).getId());
    }

    @Test
    void testDocumentCreationWithEmptyEntries() throws DocumentCreationException {
        when(mockParser.parseDocument()).thenReturn(new Document(List.of()));
        assertDoesNotThrow(() -> mockParser.parseDocument());
    }
    //endregion

    //region Negative Tests
    @Test
    void testDocumentCreationWithNullEntries() {
        assertThrows(DocumentCreationException.class,
                () -> new Document(null));

        List<DocumentEntry> list = new ArrayList<>();
        list.add(null);
        assertThrows(DocumentCreationException.class,
                () -> new Document(list));
    }
    //endregion

}
