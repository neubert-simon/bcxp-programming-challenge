package de.bcxp.challenge.common.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import static de.bcxp.challenge.common.utility.ParameterValidationUtility.*;

/**
 * Utility class providing methods for parsing Strings.
 */
public final class StringParsingUtility {
    private static final Logger logger = LogManager.getLogger(StringParsingUtility.class);
    private static final String localeLogMsg = "Passed in Locale was null ", localeExceptionMsg = "Locale can't be null.";

    /**
     * This is a utility class which provides only static methods, therefore it shouldn't be instantiated.
     */
    private StringParsingUtility() {
        throw new AssertionError("Cannot instantiate utility class.");
    }

    //region Parsing numbers
    /**
     * Parses int from the given {@link String}.
     *
     * @param numberCandidate the string to parse as an integer
     * @param locale Locale used when parsing number
     * @return the parsed integer value
     * @throws ParseException if the string cannot be parsed into a valid number
     * @throws NumberFormatException if the String doesn't contain a valid number
     * @throws IllegalArgumentException if the input string is null, empty, or invalid
     */
    public static long getLongFromString(final String numberCandidate, final Locale locale) throws NumberFormatException, ParseException {
        final Number parsed = getParsedNumber(numberCandidate, locale, NumberFormat.getIntegerInstance(locale));
        final long value = parsed.longValue();
        if (!parsed.equals(value)) {
            logger.warn("Parsing number {} failed. Long overflow likely. Parsed number: {}", numberCandidate, parsed);
            throw new NumberFormatException("Parsing failed. Absolute value of number" + numberCandidate + "might be too large.");
        }
        return value;
    }

    /**
     * Parses double from the given {@link String}.
     *
     * @param numberCandidate the string to parse as a double
     * @param locale Locale used when parsing number
     * @return the parsed double value
     * @throws ParseException if the string cannot be parsed into a valid number
     * @throws NumberFormatException if the String doesn't contain a valid number
     * @throws IllegalArgumentException if the input string is null, empty, or invalid
     */
    public static double getDoubleFromString(final String numberCandidate, final Locale locale) throws NumberFormatException, ParseException {
        final double value = getParsedNumber(numberCandidate, locale, NumberFormat.getInstance(locale)).doubleValue();
        if (!Double.isFinite(value)) {
            logger.warn("Parsing number {} as double failed.", numberCandidate);
            throw new NumberFormatException("Invalid double: " + numberCandidate);
        }
        return value;
    }

    /**
     * Parses a numeric string into a {@link Number} instance using the specified {@link Locale}.
     *
     * @param numberCandidate the string representation of the number to parse;
     *                        must not be {@code null} or empty
     * @param locale          the {@link Locale} whose formatting rules should be used
     *                        for parsing; must not be {@code null}
     * @return a {@link Number} representing the parsed value
     * @throws ParseException if the string cannot be parsed as a number according to the given locale
     * @throws NullPointerException if {@code numberCandidate} or {@code locale} is {@code null}
     * @see NumberFormat#getInstance(Locale)
     */
    private static Number getParsedNumber(String numberCandidate, final Locale locale, final NumberFormat numberFormat) throws ParseException {
        nullCheck(locale, logger, localeLogMsg + numberCandidate, localeExceptionMsg);
        numberCandidate = validateCandidateString(numberCandidate);
        return numberFormat.parse(numberCandidate);
    }

    /**
     * Validates that the provided string is non-null, not empty, and doesn't contain illegal characters that don't conform to number formatting.
     * @param numberCandidate  the string to validate
     * @return the validated {@link String} stripped of leading and trailing whitespaces
     * @throws IllegalArgumentException if the string is {@code null} or empty
     * @throws NumberFormatException if the string contains illegal characters
     */
    private static String validateCandidateString(String numberCandidate) {
        validateString(
                numberCandidate,
                logger,
                ParameterValidationUtility.STRING_LOG,
                ParameterValidationUtility.STRING_EXCEPTION
        );

        numberCandidate = numberCandidate.strip();
        if(numberCandidate.matches(".*[a-zA-Z].*")) {
            throw new NumberFormatException("Number can't contain letters.");
        }
        return numberCandidate.replace(" ", "");
    }
    //endregion

}