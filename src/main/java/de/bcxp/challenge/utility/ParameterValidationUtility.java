package de.bcxp.challenge.utility;

import de.bcxp.challenge.model.Document;
import de.bcxp.challenge.model.DocumentEntry;
import de.bcxp.challenge.model.csv.IEntryWithComparableNumericTuple;
import org.apache.logging.log4j.Logger;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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

    /**
     * <p>
     * Validates that the provided {@link Collection} and its contents are non-null.
     * THIS METHOD ALLOWS FOR EMPTY LISTS.
     * </p>
     * @param collection       the collection to validate
     * @param logger           the logger to use for warnings
     * @param logMessage       the message to log if validation fails
     * @param exceptionMessage the message to include in the thrown exception
     * @throws IllegalArgumentException if the list is {@code null} or empty
     */
    public static void validateCollection(final Collection<?> collection, final Logger logger, final String logMessage, final String exceptionMessage) throws IllegalArgumentException {
        if (collection == null) {
            logger.warn(logMessage);
            throw new IllegalArgumentException(exceptionMessage);
        }
        for (Object element : new HashSet<>(collection)) {
            if(element == null) {
                logger.warn(logMessage);
                throw new IllegalArgumentException(exceptionMessage);
            }
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
    public static void validateDocument(final Document document, final Logger logger, final String logMessage, final String exceptionMessage) {
        if (document == null) {
            logger.warn(logMessage);
            throw new IllegalArgumentException(exceptionMessage);
        }
        validateCollection(document.getEntries(), logger, logMessage, exceptionMessage);
    }

    /**
     * Checks if {@link DocumentEntry} in provided {@link Document} implements the {@link IEntryWithComparableNumericTuple} interface
     * @param entries {@link DocumentEntry} objects contained in a {@link Document}
     */
    public static void validateNumericTupleDocumentEntries(List<DocumentEntry> entries) {
        for (DocumentEntry entry : new HashSet<>(entries)) {
            if (entry == null) {
                throw new IllegalStateException("Document entry is null");
            }
            if(!(entry instanceof IEntryWithComparableNumericTuple)) {
                throw new IllegalArgumentException("Document entry is not of type IEntryWithComparableNumericTuple");
            }
        }
    }
    //endregion

    //endregion
}