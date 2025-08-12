package de.bcxp.challenge.documentParsing.csv;

import de.bcxp.challenge.documentParsing.IDocumentParser;
import de.bcxp.challenge.exceptions.DocumentCreationException;
import de.bcxp.challenge.model.Document;
import de.bcxp.challenge.model.DocumentEntry;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import static de.bcxp.challenge.utility.ParameterValidationUtility.validateString;

/**
 * An abstract base class for parsing CSV documents, implementing the {@link IDocumentParser} interface.
 * <p>
 * Provides common CSV reading and parsing functionality, including loading CSV files from the classpath,
 * applying a specified delimiter and locale, and delegating the conversion of CSV records into
 * {@link DocumentEntry} objects to subclasses.
 * </p>
 *
 * <p>
 * Subclasses must implement {@link #getEntriesFromRecords(Iterable)} to define how CSV records are transformed
 * into document entries specific to their domain.
 * </p>
 *
 * @see IDocumentParser
 * @see Document
 * @see DocumentEntry
 */
abstract class CsvParser implements IDocumentParser {
    private static final Logger logger = LogManager.getLogger(CsvParser.class);

    private final char delimiter;
    private final Locale locale;

    /**
     * Constructs a {@link CsvParser} with the specified delimiter character.
     *
     * @param delimiter the character used to separate values in the CSV file.
     * @param locale the locale used to determine the format of numbers that are parsed
     */
    protected CsvParser(final char delimiter, final Locale locale) {
        this.delimiter = delimiter;
        this.locale = locale;
    }


    /**
     * Reads a CSV file and converts its records into a {@link Document} by delegating
     * the record-to-entry conversion to getEntriesFromRecords(Iterable).
     * <p>
     * This method handles parsing errors, file not found errors, and general I/O issues,
     * wrapping them into a {@link DocumentCreationException}.
     * </p>
     *
     * @param filepath the relative path to the CSV file within the application's classpath
     * @return a {@link Document} containing the parsed entries from the CSV file
     * @throws DocumentCreationException if parsing fails due to invalid data formatting,
     *                                   missing file, or I/O errors
     */
    @Override
    public Document parseDocument(final String filepath) throws DocumentCreationException {
        try {

            final Iterable<CSVRecord> records = readFileWithHeader(filepath);
            final List<DocumentEntry> entries = getEntriesFromRecords(records);

            logger.debug("Parsed {} from {}", entries.toString() , filepath);
            return new Document(entries);

        } catch (NumberFormatException | ParseException e) {
            logger.warn("Parsing document failed: {}", filepath, e);
            throw new DocumentCreationException("Invalid formatting of numeric values in CSV.");
        } catch (FileNotFoundException e) {
            logger.error("File not found: {}, {}", filepath, e);
            throw new DocumentCreationException("File not found: " + filepath);
        } catch (IOException e) {
            logger.error("File reading failed: {}", filepath, e);
            throw new DocumentCreationException("Error during file reading of " + filepath);
        }
    }

    /**
     * Converts CSV records into a list of {@link DocumentEntry} objects.
     * <p>
     * This method must be implemented by subclasses to define how CSV records
     * are transformed into document entries relevant to the domain.
     * </p>
     *
     * @param records the CSV records to be converted
     * @return a list of {@link DocumentEntry} objects extracted from the CSV records
     * @throws NumberFormatException if a numeric value in the records cannot be parsed
     * @throws ParseException        if a value in the records cannot be parsed according to the locale
     */
    abstract List<DocumentEntry> getEntriesFromRecords(final Iterable<CSVRecord> records) throws NumberFormatException, ParseException;

    /**
     * Reads a CSV file from the application's classpath and parses its content into an {@link Iterable} of
     * {@link CSVRecord} objects.
     * <p>
     * The CSV file is located using the provided relative classpath {@code filepath}, and must exist under the
     * application's resources. The method applies the configured delimiter and treats the first record in the CSV as
     * the header row.
     * </p>
     *
     * <p><b>Behavior:</b></p>
     * <ul>
     *   <li>Validates that {@code filepath} is not {@code null} or empty.</li>
     *   <li>Loads the CSV file from the classpath using the current class loader.</li>
     *   <li>Parses the CSV using {@link CSVFormat} with the configured {@code delimiter}.</li>
     *   <li>Returns all parsed records as a fully materialized list (safe to iterate after method returns).</li>
     * </ul>
     * @param filepath the relative path to the CSV file within the classpath
     * @return an {@link Iterable} containing all CSV records from the file
     * @throws IllegalArgumentException if {@code filepath} is null or empty
     * @throws FileNotFoundException    if the CSV file cannot be found on the classpath
     * @throws IOException              if an I/O error occurs while reading or parsing the file
     */
    private Iterable<CSVRecord> readFileWithHeader(final String filepath) throws IOException, FileNotFoundException {

        validateString(filepath, logger, "Invalid filepath provided: " + filepath, "Filepath can't be empty");

        try (final InputStream input = getClass().getClassLoader().getResourceAsStream(filepath)) {

            if (input == null) throw new FileNotFoundException("Resource not found: " + filepath);

            final Reader in = new InputStreamReader(input);
            final CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setDelimiter(this.delimiter)
                    .get();

            return format.parse(in).getRecords();

        } catch (FileNotFoundException | NullPointerException e) {
            logger.error("\"Resource not found: {};\n{}", filepath, e);
            throw new FileNotFoundException("Resource not found: " + filepath);
        } catch (IOException e) {
            logger.error("Error during file reading of {}:\n{}", filepath, e);
            throw new IOException("Error during reading of CSV file of {}" + filepath);
        }

    }

    //region Getter
    public char getDelimiter() {
        return delimiter;
    }

    public Locale getLocale() {
        return locale;
    }
    //endregion
}