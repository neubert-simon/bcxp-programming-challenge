package de.bcxp.challenge.utility;

import de.bcxp.challenge.documentParsing.IDocumentParser;
import de.bcxp.challenge.exceptions.DocumentCreationException;
import de.bcxp.challenge.model.Document;
import de.bcxp.challenge.model.DocumentEntry;
import de.bcxp.challenge.model.csv.IEntryWithComparableNumericTuple;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static de.bcxp.challenge.utility.ParameterValidationUtility.*;

@ExtendWith(MockitoExtension.class)
class ParameterValidationUtilityTest {

    static class TestEntry extends DocumentEntry implements IEntryWithComparableNumericTuple {

        protected TestEntry(String id) {
            super(id);
        }

        @Override
        public double getBestMatchScore() {
            return 0;
        }
    }

    @Mock
    private Logger mockLogger;
    @Mock
    private IDocumentParser mockParser;
    private final String MOCK_FILEPATH = "/mock/filepath";

    //region validateString() Tests
    @Test
    void validateStringValid() {
        assertDoesNotThrow(() -> validateString(
                "hello", mockLogger,
                STRING_LOG,
                STRING_EXCEPTION));
        verifyNoInteractions(mockLogger);
    }

    @Test
    void validateStringNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validateString(
                        null, mockLogger,
                        STRING_LOG,
                        STRING_EXCEPTION));
        assertEquals(STRING_EXCEPTION, ex.getMessage());
        verify(mockLogger).warn(STRING_LOG);
    }

    @Test
    void validateStringEmpty() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validateString(
                        "", mockLogger,
                        STRING_LOG,
                        STRING_EXCEPTION));
        assertEquals(STRING_EXCEPTION, ex.getMessage());
        verify(mockLogger).warn(STRING_LOG);
    }
    //endregion

    //region validateCollection() Tests
    @Test
    void validateCollectionValid() {
        assertDoesNotThrow(() -> validateCollection(
                List.of("one", "two"), mockLogger,
                COLLECTION_LOG,
                COLLECTION_EXCEPTION));
        verifyNoInteractions(mockLogger);
    }

    @Test
    void validateCollectionNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validateCollection(
                        null, mockLogger,
                        COLLECTION_LOG,
                        COLLECTION_EXCEPTION));
        assertEquals(COLLECTION_EXCEPTION, ex.getMessage());
        verify(mockLogger).warn(COLLECTION_LOG);
    }

    @Test
    void validateCollectionEmpty() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validateCollection(
                        Collections.emptyList(), mockLogger,
                        COLLECTION_LOG,
                        COLLECTION_EXCEPTION));
        assertEquals(COLLECTION_EXCEPTION, ex.getMessage());
        verify(mockLogger).warn(COLLECTION_LOG);
    }

    @Test
    void validateCollection_containsNull() {
        List<String> listWithNull = Arrays.asList("one", null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validateCollection(
                        listWithNull, mockLogger,
                        COLLECTION_LOG,
                        COLLECTION_EXCEPTION));
        assertEquals("List contains null.", ex.getMessage());
    }
    //endregion

    //region validateDocument() Tests
    @Test
    void validateDocumentValid() throws IOException, ParseException, DocumentCreationException {
        when(mockParser.parseDocument(anyString())).thenReturn(List.of(new TestEntry("d")));
        Document doc = new Document(MOCK_FILEPATH, mockParser);

        assertDoesNotThrow(() -> validateDocument(
                doc, mockLogger,
                DOCUMENT_LOG,
                DOCUMENT_EXCEPTION));
    }

    @Test
    void validateDocumentNullDocument() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validateDocument(
                        null, mockLogger,
                        DOCUMENT_LOG,
                        DOCUMENT_EXCEPTION));
        assertEquals(DOCUMENT_EXCEPTION, ex.getMessage());
        verify(mockLogger).warn(DOCUMENT_LOG);
    }

    @Test
    void validateDocumentEmptyEntries() throws IOException, ParseException, DocumentCreationException {
        when(mockParser.parseDocument(anyString())).thenReturn(List.of());
        Document doc = new Document(MOCK_FILEPATH, mockParser);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validateDocument(
                        doc, mockLogger,
                        DOCUMENT_LOG,
                        DOCUMENT_EXCEPTION));
        assertEquals(DOCUMENT_EXCEPTION, ex.getMessage());
    }
    //endregion

    //region validateNumericTupleDocumentEntries() Tests
    @Test
    void validateNumericTupleDocumentEntriesValid() {
        assertDoesNotThrow(() -> validateNumericTupleDocumentEntries(
                List.of(new TestEntry("1"))));
    }

    @Test
    void validateNumericTupleDocumentEntriesNullEntry() {
        List<DocumentEntry> listWithNull = Arrays.asList(new TestEntry("1"), null);
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> validateNumericTupleDocumentEntries(listWithNull));
        assertEquals("Document entry is null", ex.getMessage());
    }

    @Test
    void validateNumericTupleDocumentEntries_invalidType() {
        class NotTupleEntry extends DocumentEntry {
            protected NotTupleEntry(String id) {
                super(id);
            }
        }
        List<DocumentEntry> invalidList = List.of(new NotTupleEntry("1"));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validateNumericTupleDocumentEntries(invalidList));
        assertEquals("Document entry is not of type IEntryWithComparableNumericTuple", ex.getMessage());
    }
    //endregion
}
