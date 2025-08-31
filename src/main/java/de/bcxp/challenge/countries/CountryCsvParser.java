package de.bcxp.challenge.countries;

import de.bcxp.challenge.common.documentParsing.csv.CsvParser;
import de.bcxp.challenge.common.model.DocumentEntry;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static de.bcxp.challenge.common.utility.StringParsingUtility.getDoubleFromString;
import static de.bcxp.challenge.common.utility.StringParsingUtility.getLongFromString;

/**
 * A CSV parser specialized for parsing country data entries.
 * <p>
 * {@code CountryParser} extends {@link CsvParser} to read CSV files containing country information,
 * including country name, population, and area. Each CSV record is mapped to a {@link CountryEntry}.
 * </p>
 * <p>
 * The parser expects the CSV file to contain headers matching the field names:
 * "Name", "Population", and "Area (km²)".
 * </p>
 *
 * @see CsvParser
 * @see CountryEntry
 */
public class CountryCsvParser extends CsvParser {
    private static final Logger logger = LogManager.getLogger(CountryCsvParser.class);

    private final static String NAME = "Name", POPULATION = "Population", AREA = "Area (km²)";

    /**
     * Constructs a {@link CountryCsvParser} with the specified CSV delimiter and Locale.
     *
     * @param delimiter the character used to separate values in the CSV file.
     * @param locale Locale used when parsing number
     * @param filepath Filepath to the csv
     */
    public CountryCsvParser(final char delimiter, final Locale locale, final String filepath) {
        super(delimiter, locale, filepath);
    }

    /**
     * Converts a collection of CSV records into a list of {@link DocumentEntry} objects,
     * specifically {@link CountryEntry} instances. Each record is expected to contain
     * country-related data such as the name, population, and area.
     *
     * @param records an {@link Iterable} of {@link CSVRecord} objects containing country data.
     *                Each record must provide values for {@code NAME}, {@code POPULATION},
     *                and {@code AREA}.
     *
     * @return a {@link List} of {@link DocumentEntry} objects, where each entry corresponds
     *         to a parsed {@link CountryEntry} from the CSV records.
     *
     * @throws NumberFormatException if numeric values (population or area) cannot be parsed.
     * @throws ParseException if a parsing error occurs during number conversion
     *                        (e.g., due to locale-specific formatting issues).
     */
    @Override
    protected List<DocumentEntry> getEntriesFromRecords(final Iterable<CSVRecord> records) throws NumberFormatException, ParseException {
        final List<DocumentEntry> countryList = new ArrayList<>();

        for (final CSVRecord record : records) {
            countryList.add(new CountryEntry(
                            record.get(NAME),
                            getLongFromString(record.get(POPULATION), super.getLocale()),
                            getDoubleFromString(record.get(AREA), super.getLocale())
                    )
            );
        }

        return countryList;
    }

}