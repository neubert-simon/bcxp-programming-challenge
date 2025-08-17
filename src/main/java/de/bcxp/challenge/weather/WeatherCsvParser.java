package de.bcxp.challenge.weather;

import de.bcxp.challenge.common.documentParsing.csv.CsvParser;
import de.bcxp.challenge.common.model.DocumentEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.csv.CSVRecord;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static de.bcxp.challenge.common.utility.StringParsingUtility.*;

/**
 * A CSV parser specifically for weather data entries.
 * <p>
 * {@link WeatherCsvParser} extends {@link CsvParser} to parse CSV files containing daily weather information.
 * Each record is expected to contain fields such as day, maximum temperature, and minimum temperature,
 * which are mapped to {@link WeatherEntry} objects.
 * </p>
 *
 * <p>
 * This parser assumes that the CSV file includes a header row.
 * </p>
 *
 * @see WeatherEntry
 * @see CsvParser
 */
public class WeatherCsvParser extends CsvParser {
    private static final Logger logger = LogManager.getLogger(WeatherCsvParser.class);

    private final static String NAME = "Day", MAX_TEMP = "MxT", MIN_TEMP = "MnT";

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
     * Converts a collection of CSV records into a list of {@link DocumentEntry} objects,
     * specifically {@link WeatherEntry} instances. Each record is expected to contain
     * weather-related data such as the name, maximum temperature, and minimum temperature.
     *
     * @param records an {@link Iterable} of {@link CSVRecord} objects containing weather data.
     *                Each record must provide values for {@code NAME}, {@code MAX_TEMP},
     *                and {@code MIN_TEMP}.
     *
     * @return a {@link List} of {@link DocumentEntry} objects, where each entry corresponds
     *         to a parsed {@link WeatherEntry} from the CSV records.
     *
     * @throws NumberFormatException if temperature values cannot be parsed as doubles.
     * @throws ParseException if a parsing error occurs during number conversion
     *                        (e.g., due to locale-specific formatting issues).
     */
    @Override
    protected List<DocumentEntry> getEntriesFromRecords(final Iterable<CSVRecord> records) throws NumberFormatException, ParseException {
        final List<DocumentEntry> weatherList = new ArrayList<>();

        for (final CSVRecord record : records) {
            weatherList.add(new WeatherEntry(
                record.get(NAME),
                getDoubleFromString(record.get(MAX_TEMP), super.getLocale()),
                getDoubleFromString(record.get(MIN_TEMP), super.getLocale())
                )
            );
        }

        return weatherList;
    }
}
