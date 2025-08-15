package de.bcxp.challenge.common.utility;

import de.bcxp.challenge.common.documentParsing.IDocumentParser;
import de.bcxp.challenge.common.exceptions.DocumentCreationException;
import de.bcxp.challenge.common.model.Document;
import de.bcxp.challenge.common.model.DocumentEntry;
import de.bcxp.challenge.countries.CountryEntry;
import de.bcxp.challenge.common.model.csv.IEntryWithComparableNumericTuple;
import de.bcxp.challenge.weather.WeatherEntry;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static de.bcxp.challenge.common.utility.ParameterValidationUtility.*;

@ExtendWith(MockitoExtension.class)
class ParameterValidationUtilityTest {

    static final class TestEntry extends DocumentEntry implements IEntryWithComparableNumericTuple {

        private TestEntry(String id) {
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

    private final DocumentEntry testEntry = new WeatherEntry("one", 3, 2);

    //region validateString() Tests
    @Test
    void validateStringValidTest() {
        assertDoesNotThrow(() -> validateString(
                "hello", mockLogger,
                STRING_LOG,
                STRING_EXCEPTION));
        verifyNoInteractions(mockLogger);
    }

    @Test
    void validateStringNullTest() {
        final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validateString(
                        null, mockLogger,
                        STRING_LOG,
                        STRING_EXCEPTION));
        assertEquals(STRING_EXCEPTION, ex.getMessage());
        verify(mockLogger).warn(STRING_LOG);
    }

    @Test
    void validateStringEmptyTest() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validateString(
                        "", mockLogger,
                        STRING_LOG,
                        STRING_EXCEPTION));
        assertEquals(STRING_EXCEPTION, ex.getMessage());
        verify(mockLogger).warn(STRING_LOG);
    }

    @Test
    void validateStringBlankTest() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validateString(
                        "        ", mockLogger,
                        STRING_LOG,
                        STRING_EXCEPTION));
        assertEquals(STRING_EXCEPTION, ex.getMessage());
        verify(mockLogger).warn(STRING_LOG);
    }

    //endregion

    //region nullCheck() Tests
    @Test
    void nullCheckObjectIsNotNull() {
        assertDoesNotThrow(() ->
                nullCheck("value", mockLogger, STRING_LOG, STRING_EXCEPTION)
        );
        verify(mockLogger, never()).warn(anyString());
    }

    @Test
    void nullCheckObjectIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                nullCheck(null, mockLogger, STRING_LOG, STRING_EXCEPTION)
        );
        assertEquals(STRING_EXCEPTION, ex.getMessage());
        verify(mockLogger).warn(STRING_LOG);
    }
    //endregion

    //region validateEntries() Tests
    @Test
    void validateEntriesValidTest() {
        assertDoesNotThrow(() -> validateEntries(
                List.of(testEntry),
                true,
                mockLogger,
                COLLECTION_LOG,
                COLLECTION_EXCEPTION));
    }

    @Test
    void validateEntriesNullTest() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validateEntries(
                        null,
                        true,
                        mockLogger,
                        COLLECTION_LOG,
                        COLLECTION_EXCEPTION));
        assertEquals(COLLECTION_EXCEPTION, ex.getMessage());
        verify(mockLogger).warn(COLLECTION_LOG);
    }

    @Test
    void validateEntriesEmptyTest() {
        assertDoesNotThrow(() ->
                validateEntries(
                        Collections.emptyList(),
                        true,
                        mockLogger,
                        COLLECTION_LOG,
                        COLLECTION_EXCEPTION));
        assertThrows(NoSuchElementException.class, () ->
                validateEntries(
                        Collections.emptyList(),
                        false,
                        mockLogger,
                        COLLECTION_LOG,
                        COLLECTION_EXCEPTION));
    }

    @Test
    void validateEntriesContainsNullTest() {
        final List<DocumentEntry> listWithNull = Arrays.asList(testEntry, null);
        assertThrows(IllegalStateException.class,
                () -> validateEntries(
                        listWithNull,
                        true,
                        mockLogger,
                        COLLECTION_LOG,
                        COLLECTION_EXCEPTION));
    }
    //endregion

    //region validateDocument() Tests
    @Test
    void validateDocumentValidTest() throws DocumentCreationException {
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
    void validateDocumentNullDocumentTest() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validateDocument(
                        null, mockLogger,
                        DOCUMENT_LOG,
                        DOCUMENT_EXCEPTION));
        assertEquals(DOCUMENT_EXCEPTION, ex.getMessage());
        verify(mockLogger).warn(DOCUMENT_LOG);
    }

    @Test
    void validateDocumentEmptyEntriesTest() {
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
    void validateNumericTupleDocumentEntriesValidTest() {
        assertDoesNotThrow(() -> validateNumericTupleDocumentEntries(
                List.of(new TestEntry("1")), mockLogger));
    }

    @Test
    void validateNumericTupleDocumentEntriesNullEntryTest() {
        final List<DocumentEntry> listWithNull = Arrays.asList(new TestEntry("1"), null);
        assertThrows(IllegalStateException.class,
                () -> validateNumericTupleDocumentEntries(listWithNull, mockLogger));
    }

    @Test
    void validateNumericTupleDocumentEntriesInvalidTypeTest() {
        final class NotTupleEntry extends DocumentEntry {
            private NotTupleEntry(String id) {
                super(id);
            }
        }
        List<DocumentEntry> invalidList = List.of(new NotTupleEntry("1"));
        assertThrows(IllegalArgumentException.class,
                () -> validateNumericTupleDocumentEntries(invalidList, mockLogger));
    }

    @Test
    void validateNumericTupleDocumentEntriesDifferentTypesTest() {
        List<DocumentEntry> invalidList = List.of(
                new WeatherEntry("1", 1, 1),
                new CountryEntry("1", 1, 1));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> validateNumericTupleDocumentEntries(invalidList, mockLogger));
        assertEquals("Entries must all be of the same type.", ex.getMessage());
    }
    //endregion
}
