package de.bcxp.challenge.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import static de.bcxp.challenge.utility.ParameterValidationUtility.*;

/**
 * Utility class providing methods for parsing Strings.
 */
public final class StringParsing {
    private static final Logger logger = LogManager.getLogger(StringParsing.class);

    /**
     * This is a utility class which provides only static methods, therefore it shouldn't be instantiated.
     */
    private StringParsing() {
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
        validateString(numberCandidate, logger, STRING_LOG, STRING_EXCEPTION);
        logger.trace("Parsing int from String: {}", numberCandidate);
        return parseNumber(numberCandidate, locale).longValue();
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
        validateString(numberCandidate, logger, STRING_LOG, STRING_EXCEPTION);
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
    private static Number parseNumber(final String numberCandidate, final Locale locale) throws NumberFormatException, ParseException {

        validateString(numberCandidate, logger, STRING_LOG, STRING_EXCEPTION);

        try {
            logger.trace("Parsing {} as number.", numberCandidate);
            return NumberFormat.getInstance(locale).parse(numberCandidate.strip());
        } catch (NumberFormatException e) {
            logger.warn("Value {} is not a number: {}", numberCandidate, e.getMessage());
            throw new NumberFormatException("Value isn't a valid number.");
        } catch (ParseException e) {
            logger.warn("Error during number parsing for {}: {}", numberCandidate, e.getMessage());
            throw new ParseException("Error during number parsing of " + numberCandidate, e.getErrorOffset());
        }
    }
    //endregion

}