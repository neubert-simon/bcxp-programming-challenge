package de.bcxp.challenge.common.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.text.DecimalFormatSymbols;

/**
 * Utility class providing methods for parsing Strings.
 */
public final class StringParsingUtility {
    private static final Logger logger = LogManager.getLogger(StringParsingUtility.class);

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
    public static long getLongFromString(String numberCandidate, final Locale locale) throws NumberFormatException, ParseException {
        numberCandidate = validateCandidateString(numberCandidate);
        logger.trace("Parsing long from String: {}", numberCandidate);
        Number parsed = parseNumber(numberCandidate, locale);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        final String decimalSeparator = String.valueOf(symbols.getDecimalSeparator());
        final String groupingSeparator = String.valueOf(symbols.getGroupingSeparator());
        final String numberCandidateStripped = 
                String.valueOf(parsed)
                .replace(decimalSeparator, "")
                .replace(groupingSeparator, "");

        if (!String.valueOf(parsed).equals(numberCandidateStripped)) {
            logger.warn("Parsing number {} failed. Long overflow likely. Parsed number: {}", numberCandidate, parsed);
            throw new NumberFormatException("Parsing failed. Absolute value of number" + numberCandidate + "might be too large.");
        }
        return parsed.longValue();

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
    public static double getDoubleFromString(String numberCandidate, final Locale locale) throws NumberFormatException, ParseException {
        numberCandidate = validateCandidateString(numberCandidate);
        logger.trace("Parsing double from String: {}", numberCandidate);
        return parseNumber(numberCandidate, locale).doubleValue();
    }

    /**
     * Parses the given string into a {@link Number} and returns it.
     *
     * @param numberCandidate the string to parse
     * @param locale Locale used when parsing number
     * @return the parsed number as a {@link Number}
     * @throws NumberFormatException if the input is not a valid number
     * @throws ParseException if the string cannot be parsed into a valid number
     */
    private static Number parseNumber(String numberCandidate, final Locale locale) throws NumberFormatException, ParseException {
        numberCandidate = validateCandidateString(numberCandidate);
        try {
            return NumberFormat.getInstance(locale).parse(numberCandidate);
        } catch (ParseException e) {
            logger.warn("Error during number parsing for {}: {}", numberCandidate, e);
            throw new ParseException("Error during number parsing of " + numberCandidate, e.getErrorOffset());
        }
    }

    /**
     * Validates that the provided string is non-null, not empty, and doesn't contain illegal characters that don't conform to number formatting.
     * @param numberCandidate  the string to validate
     * @return the validated {@link String} stripped of leading and trailing whitespaces
     * @throws IllegalArgumentException if the string is {@code null} or empty
     * @throws NumberFormatException if the string contains illegal characters
     */
    private static String validateCandidateString(String numberCandidate) {
        ParameterValidationUtility.validateString(
                numberCandidate,
                logger,
                ParameterValidationUtility.STRING_LOG,
                ParameterValidationUtility.STRING_EXCEPTION
        );
        numberCandidate = numberCandidate.strip();
        if(numberCandidate.matches(".*[a-zA-Z].*")) {
            throw new NumberFormatException("Number can't contain letters.");
        }
        return numberCandidate;
    }
    //endregion

}