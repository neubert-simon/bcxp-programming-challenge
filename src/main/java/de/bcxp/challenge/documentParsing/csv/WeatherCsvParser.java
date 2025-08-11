package de.bcxp.challenge.documentParsing.csv;

import de.bcxp.challenge.model.DocumentEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.bcxp.challenge.model.csv.WeatherEntry;
import org.apache.commons.csv.CSVRecord;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static de.bcxp.challenge.utility.StringParsingUtility.*;

/**
 * A CSV parser specifically for weather data entries.
 * <p>
 * {@link WeatherCsvParser} extends {@link CsvParser} to parse CSV files containing daily weather information.
 * Each record is expected to contain fields such as day, maximum temperature, and minimum temperature,
 * which are mapped to {@link WeatherEntry} objects.
 * </p>
 *
 * <p>
 * This parser assumes that the CSV file includes a header row, and uses a configurable delimiter.
 * </p>
 *
 * @see WeatherEntry
 * @see CsvParser
 */
public class WeatherCsvParser extends CsvParser {
    private static final Logger logger = LogManager.getLogger(WeatherCsvParser.class);

    private final static String NAME = "Day";
    private final static String MAX_TEMP = "MxT";
    private final static String MIN_TEMP = "MnT";

    /**
     * Constructs a {@link WeatherCsvParser} with the specified CSV delimiter.
     *
     * @param delimiter the character used to separate values in the CSV file.
     * @param locale Locale used when parsing number
     */
    public WeatherCsvParser(final char delimiter, final Locale locale) {
        super(delimiter, locale);
    }

    /**
     * Parses a {@link List} of {@link WeatherEntry} from the specified file path.
     * <p>
     * Reads a CSV file with headers and converts each record into a {@link WeatherEntry}.
     * The expected headers include values for name, maximum temperature, and minimum temperature.
     * </p>
     *
     * @param filepath the {@link String} path to the CSV document resource
     * @return a {@link List} of {@link WeatherEntry} objects representing the parsed content
     *
     * @throws IOException if an I/O error occurs while accessing or reading the file
     * @throws NumberFormatException if a numeric field (e.g., temperature) cannot be parsed as a {@code double}
     * @throws ParseException if a parsing-related error occurs (e.g., malformed or missing fields)
     */
    @Override
    public List<DocumentEntry> parseDocument(final String filepath) throws IOException, NumberFormatException, ParseException {

        final Iterable<CSVRecord> records = readFileWithHeader(filepath);
        final List<DocumentEntry> weatherList = new ArrayList<>();

        for (final CSVRecord record : records) {
            weatherList.add(new WeatherEntry(
                    record.get(NAME),
                    getDoubleFromString(record.get(MAX_TEMP), this.getLocale()),
                    getDoubleFromString(record.get(MIN_TEMP), this.getLocale())
                    )
            );
        }

        logger.debug("Parsed {} from {}", weatherList.toString() , filepath);

        return weatherList;
    }
}
