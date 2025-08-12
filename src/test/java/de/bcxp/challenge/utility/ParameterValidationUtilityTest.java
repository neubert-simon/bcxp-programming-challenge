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

        @Override
        public int compareTo(IEntryWithComparableNumericTuple o) {
            return Double.compare(getBestMatchScore(), o.getBestMatchScore());
        }
    }

    @Mock
    private Logger mockLogger;
    @Mock
    private IDocumentParser mockParser;

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
        final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validateString(
                        null, mockLogger,
                        STRING_LOG,
                        STRING_EXCEPTION));
        assertEquals(STRING_EXCEPTION, ex.getMessage());
        verify(mockLogger).warn(STRING_LOG);
    }

    @Test
    void validateStringEmpty() {
        final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
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
                List.of("one", "two"),
                mockLogger,
                COLLECTION_LOG,
                COLLECTION_EXCEPTION));
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
        assertDoesNotThrow(() ->
                validateCollection(
                        Collections.emptyList(),
                        mockLogger,
                        COLLECTION_LOG,
                        COLLECTION_EXCEPTION));
    }

    @Test
    void validateCollectionContainsNull() {
        final List<String> listWithNull = Arrays.asList("one", null);
        final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validateCollection(
                        listWithNull,
                        mockLogger,
                        COLLECTION_LOG,
                        COLLECTION_EXCEPTION));
        assertEquals("List parameter cannot be null or empty", ex.getMessage());
    }
    //endregion

    //region validateDocument() Tests
    @Test
    void validateDocumentValid() throws DocumentCreationException {
        when(mockParser
                .parseDocument(anyString()))
                .thenReturn(new Document(List.of(new TestEntry("d"))));
        final String MOCK_FILEPATH = "/mock/filepath";
        final Document doc = mockParser.parseDocument(MOCK_FILEPATH);

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
    void validateDocumentEmptyEntries() {
        final Document doc = new Document(List.of());
        assertDoesNotThrow(() ->
                validateDocument(
                        doc,
                        mockLogger,
                        DOCUMENT_LOG,
                        DOCUMENT_EXCEPTION));
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
        final List<DocumentEntry> listWithNull = Arrays.asList(new TestEntry("1"), null);
        final IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> validateNumericTupleDocumentEntries(listWithNull));
        assertEquals("Document entry is null", ex.getMessage());
    }

    @Test
    void validateNumericTupleDocumentEntriesInvalidType() {
        final class NotTupleEntry extends DocumentEntry {
            private NotTupleEntry(String id) {
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
