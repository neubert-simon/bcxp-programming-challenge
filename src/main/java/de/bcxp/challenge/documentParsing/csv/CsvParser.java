package de.bcxp.challenge.documentParsing.csv;

import de.bcxp.challenge.documentParsing.IDocumentParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Locale;

import static de.bcxp.challenge.utility.ParameterValidationUtility.validateString;

/**
 * An abstract parser class for CSV documents that implements {@link IDocumentParser}.
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
     * @throws IllegalArgumentException if {@code filepath} is {@code null} or empty
     * @throws FileNotFoundException    if the CSV file is not found on the classpath
     * @throws IOException              if an I/O error occurs while reading or parsing the file
     */
    Iterable<CSVRecord> readFileWithHeader(final String filepath) throws IOException, FileNotFoundException {

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
            logger.error("\"Resource not found: {};\n{}", filepath, e.getMessage());
            throw new FileNotFoundException("Resource not found: " + filepath);
        } catch (IOException e) {
            logger.error("Error during file reading of {}:\n{}", filepath, e.getMessage());
            throw new IOException("Error during reading of CSV file of {}" + filepath);
        }

    }

    public char getDelimiter() {
        return delimiter;
    }

    public Locale getLocale() {
        return locale;
    }
}