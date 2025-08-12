package de.bcxp.challenge.documentParsing.csv;

import de.bcxp.challenge.model.DocumentEntry;
import de.bcxp.challenge.model.csv.CountryEntry;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static de.bcxp.challenge.utility.StringParsingUtility.getDoubleFromString;
import static de.bcxp.challenge.utility.StringParsingUtility.getLongFromString;

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
     * Constructs a {@link CountryCsvParser} with the specified CSV delimiter.
     *
     * @param delimiter the character used to separate values in the CSV file.
     * @param locale Locale used when parsing number
     */
    public CountryCsvParser(final char delimiter, final Locale locale) {
        super(delimiter, locale);
    }

    @Override
    List<DocumentEntry> getEntriesFromRecords(final Iterable<CSVRecord> records) throws NumberFormatException, ParseException {
        List<DocumentEntry> countryList = new ArrayList<>();

        for (final CSVRecord record : records) {
            countryList.add(new CountryEntry(
                            record.get(NAME),
                            getLongFromString(record.get(POPULATION), this.getLocale()),
                            getDoubleFromString(record.get(AREA), this.getLocale())
                    )
            );
        }

        return countryList;
    }

}