package de.bcxp.challenge.common.utility;

import de.bcxp.challenge.common.model.Document;
import de.bcxp.challenge.common.model.DocumentEntry;
import de.bcxp.challenge.common.model.csv.IEntryWithComparableNumericTuple;
import org.apache.logging.log4j.Logger;

import java.util.*;

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
     * @param logMessage       the message to log if validation fails
     * @param exceptionMessage the message to include in the thrown exception.
     * @throws IllegalArgumentException if the string is {@code null} or empty
     */
    public static void validateString(final String string, final Logger logger, final String logMessage, final String exceptionMessage) throws IllegalArgumentException {
        validateLoggerAndMessages(logger, logMessage, exceptionMessage);
        nullCheck(string, logger, logMessage, exceptionMessage);
        if (string.isEmpty() || string.isBlank()) {
            logger.warn(logMessage);
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    /**
     * Validates that the provided object reference is not {@code null}.
     * <p>
     * If the object is {@code null}, this method logs a warning message using the provided
     * {@link Logger} and then throws an {@link IllegalArgumentException} with the specified
     * exception message.
     * </p>
     *
     * @param o                the object reference to validate
     * @throws IllegalArgumentException if {@code o} is {@code null}
     */
    public static void nullCheck(final Object o, final Logger logger, final String logMessage, final String exceptionMessage) {
        validateLoggerAndMessages(logger, logMessage, exceptionMessage);
        if(o == null) {
            logger.warn(logMessage);
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
        validateLoggerAndMessages(logger, logMessage, exceptionMessage);
        nullCheck(document, logger, logMessage, exceptionMessage);
        validateEntries(document.getEntries(), true, logger, logMessage, exceptionMessage);
    }

    /**
     * <p>
     * Validates that the provided {@link Collection} and its contents are non-null.
     * </p>
     * @param entries       the entries to validate
     * @param allowEmpty       if the entries can be empty
     * @param logger           the logger to use for warnings
     * @param logMessage       the message to log if validation fails
     * @param exceptionMessage the message to include in the thrown exception
     * @throws IllegalArgumentException if the list is {@code null} or empty
     */
    public static void validateEntries(final Collection<? extends DocumentEntry> entries, final boolean allowEmpty, final Logger logger, final String logMessage, final String exceptionMessage) throws IllegalArgumentException, NoSuchElementException, IllegalStateException {
        validateLoggerAndMessages(logger, logMessage, exceptionMessage);
        nullCheck(entries, logger, logMessage, exceptionMessage);
        if((entries.isEmpty() && !allowEmpty)) {
            logger.warn(logMessage);
            throw new NoSuchElementException("Collection empty.");
        }
        new HashSet<>(entries).forEach(element -> nullCheck(element, logger, logMessage, exceptionMessage));
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

    //region Auxiliary
    private static void validateLoggerAndMessages(final Logger logger, final String logMessage, final String exceptionMessage) {
        try {
            Objects.requireNonNull(logger);
            Objects.requireNonNull(logMessage);
            Objects.requireNonNull(exceptionMessage);
        } catch (NullPointerException e) {
            logger.warn("Null parameter passed into validator-method: {}, {}, {}", logger, logMessage, exceptionMessage, e);
            throw new IllegalArgumentException("Logger and exception messages can't be Null.");
        }
    }
    //endregion

    //endregion
}