package de.bcxp.challenge.utility;

import de.bcxp.challenge.model.Document;
import de.bcxp.challenge.model.DocumentEntry;
import de.bcxp.challenge.model.csv.IEntryWithComparableNumericTuple;
import org.apache.logging.log4j.Logger;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * <p>
 * Utility class providing methods to check method parameters for validity.
 * Used instead of Objects.requireNonNull() for logging purposes, and to throw IllegalArgumentException
 * instead of NullPointerException.
 * </p>
 */
public final class ParameterValidationUtility {

    //region Template log and exception messages
    public static final String STRING_LOG = "Invalid String parameter passed in";
    public static final String STRING_EXCEPTION = "String parameter cannot be null or empty";

    public static final String COLLECTION_LOG = "Invalid List parameter passed in";
    public static final String COLLECTION_EXCEPTION = "List parameter cannot be null or empty";

    public static final String DOCUMENT_LOG = "Invalid Document parameter passed in";
    public static final String DOCUMENT_EXCEPTION = "Document parameter cannot be null or empty";
    //endregion

    /**
     * This is a utility class which provides only static methods, therefore it shouldn't be instantiated.
     */
    private ParameterValidationUtility() {
        throw new AssertionError("Cannot instantiate utility class.");
    }

    //region Validation methods

    //region Validation for built-ins
    /**
     * Validates that the provided string is non-null and not empty.
     * @param string           the string to validate
     * @param logger           the logger to use for warnings
     * @param errorLogMessage       the message to log if validation fails
     * @param exceptionMessage the message to include in the thrown exception.
     * @throws IllegalArgumentException if the string is {@code null} or empty
     */
    public static void validateString(final String string, final Logger logger, final String errorLogMessage, final String exceptionMessage) throws IllegalArgumentException {
        if (string == null || string.isEmpty()) {
            logger.warn(errorLogMessage);
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    //endregion

    //region Validation for self-rolled objects
    /**
     * Validates that the provided document is non-null and not empty.
     * @param document         The {@link Document} to validate
     * @param logger           the logger to use for warnings
     * @param logMessage       the message to log if validation fails
     * @param exceptionMessage the message to include in the thrown exception
     */
    public static void validateDocument(final Document document, final Logger logger, final String logMessage, final String exceptionMessage) throws IllegalArgumentException {
        if (document == null) {
            logger.warn(logMessage);
            throw new IllegalArgumentException(exceptionMessage);
        }
        validateEntries(document.getEntries(), true, logger, logMessage, exceptionMessage);
    }

    /**
     * <p>
     * Validates that the provided {@link Collection} and its contents are non-null.
     * </p>
     * @param collection       the collection to validate
     * @param allowEmpty       if the entries can be empty
     * @param logger           the logger to use for warnings
     * @param logMessage       the message to log if validation fails
     * @param exceptionMessage the message to include in the thrown exception
     * @throws IllegalArgumentException if the list is {@code null} or empty
     */
    public static void validateEntries(final Collection<? extends DocumentEntry> collection, final boolean allowEmpty, final Logger logger, final String logMessage, final String exceptionMessage) throws IllegalArgumentException, NoSuchElementException, IllegalStateException {
        if (collection == null) {
            logger.warn(logMessage);
            throw new IllegalArgumentException(exceptionMessage);
        }
        if((collection.isEmpty() && !allowEmpty)) {
            logger.warn(logMessage);
            throw new NoSuchElementException("Collection empty.");
        }
        for (Object element : new HashSet<>(collection)) {
            if(element == null) {
                logger.warn(logMessage);
                throw new IllegalStateException(exceptionMessage);
            }
        }
    }

    /**
     * Checks if the list of {@link DocumentEntry} entries from a {@link Document} all implement the {@link IEntryWithComparableNumericTuple} interface and are all of the same type.
     * @param entries {@link DocumentEntry} objects contained in a {@link Document}
     * @param logger Logger to log possible error messages to
     */
    public static void validateNumericTupleDocumentEntries(final List<DocumentEntry> entries, final Logger logger) throws IllegalArgumentException {

        validateEntries(entries, false, logger, "Entries were null or empty when checking for numeric tuple.", "Entries can't be null or empty.");
        final Class<? extends DocumentEntry> type = entries.get(0).getClass();

        for (final DocumentEntry entry : new HashSet<>(entries)) {

            if(!(entry instanceof IEntryWithComparableNumericTuple)) {
                logger.warn("Document entry is not of type IEntryWithComparableNumericTuple {}", entry);
                throw new IllegalArgumentException("Document entries are not all of type IEntryWithComparableNumericTuple");
            }

            if(!type.isInstance(entry)) {
                logger.warn("Different DocumentEntry types present {} - {}", entry.getClass(), type);
                throw new IllegalArgumentException("Entries must all be of the same type.");
            }

        }
    }
    //endregion

    //endregion
}