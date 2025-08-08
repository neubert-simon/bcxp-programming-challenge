package documentParsing.csv;

import model.csv.CountryEntry;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import static utility.StringParsing.getDoubleFromString;
import static utility.StringParsing.getIntFromString;

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
public class CountryCsvParser extends CsvParser<CountryEntry> {
    private static final Logger logger = LogManager.getLogger(CountryCsvParser.class);

    private final static String NAME = "Name";
    private final static String POPULATION = "Population";
    private final static String AREA = "Area (km²)";

    /**
     * Constructs a {@link CountryCsvParser} with the specified CSV delimiter.
     *
     * @param delimiter the character used to separate values in the CSV file.
     */
    public CountryCsvParser(char delimiter) {
        super(delimiter);
    }

    /**
     * Parses a {@link List} of {@link CountryEntry} from the specified file path.
     * <p>
     * Reads a CSV file with headers and converts each record into a {@link CountryEntry}.
     * The expected headers include values for name, population, and area.
     * </p>
     *
     * @param filepath the {@link String} path to the CSV document resource
     * @return a {@link List} of {@link CountryEntry} objects representing the parsed content
     *
     * @throws IOException if an I/O error occurs while accessing or reading the file
     * @throws NumberFormatException if a numeric field (e.g., population) cannot be parsed as a {@link Number}
     * @throws ParseException if a parsing-related error occurs (e.g., malformed or missing fields)
     */
    @Override
    public List<CountryEntry> parseDocument(String filepath) throws IOException, NumberFormatException, ParseException {

        Iterable<CSVRecord> records = readFileWithHeader(filepath);
        List<CountryEntry> countryList = new ArrayList<>();

        for (CSVRecord record : records) {
            countryList.add(new CountryEntry(
                    record.get(NAME),
                    getIntFromString(record.get(POPULATION)),
                    getDoubleFromString(record.get(AREA))
                    )
            );
        }

        logger.debug("Parsed {} from {}", countryList.toString() , filepath);
        return countryList;
    }

}