package de.bcxp.challenge.utility;

import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Utility class providing methods to check method parameters for validity.
 */
public final class ParameterValidationUtility {

    //region Template log and exception messages
    public static final String STRING_LOG = "Invalid String parameter passed in.";
    public static final String STRING_EXCEPTION = "String parameter cannot be null or empty.";

    public static final String LIST_LOG = "Invalid List parameter passed in.";
    public static final String LIST_EXCEPTION = "List parameter cannot be null or empty.";
    //endregion

    /**
     * This is a utility class which provides only static methods, therefore it shouldn't be instantiated.
     */
    private ParameterValidationUtility() { throw new AssertionError("Cannot instantiate utility class."); }

    //region Validation methods
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
     * Validates that the provided list is non-null and not empty.
     * @param list             the list to validate
     * @param logger           the logger to use for warnings
     * @param logMessage       the message to log if validation fails
     * @param exceptionMessage the message to include in the thrown exception
     * @throws IllegalArgumentException if the list is {@code null} or empty
     */
    public static void validateList(final List<?> list, final Logger logger, final String logMessage, final String exceptionMessage) throws IllegalArgumentException {
        if (list == null || list.isEmpty()) {
            logger.warn(logMessage);
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
    //endregion
}