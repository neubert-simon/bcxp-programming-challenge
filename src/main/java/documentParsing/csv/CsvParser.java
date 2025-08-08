package documentParsing.csv;

import documentParsing.IDocumentParser;
import model.DocumentEntry;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

import static utility.ParameterValidationUtility.validateString;

/**
 * An abstract parser class for CSV documents that implements {@link IDocumentParser}.
 */
public abstract class CsvParser<T extends DocumentEntry> implements IDocumentParser<T> {
    private static final Logger logger = LogManager.getLogger(CsvParser.class);

    private final char delimiter;

    /**
     * Constructs a {@link CsvParser} with the specified delimiter character.
     *
     * @param delimiter the character used to separate values in the CSV file.
     */
    public CsvParser(char delimiter) {
        this.delimiter = delimiter;
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

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(filepath)) {

            if (input == null) throw new FileNotFoundException("Resource not found: " + filepath);

            Reader in = new InputStreamReader(input);
            CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setDelimiter(this.delimiter)
                    .get();
            return format.parse(in).getRecords();
        } catch (FileNotFoundException e) {
            logger.error("\"Resource not found: {};\n{}", filepath, e.getMessage());
            throw new FileNotFoundException("Resource not found: " + filepath);
        } catch (IOException e) {
            logger.error("Error during file reading of {}:\n{}", filepath, e.getMessage());
            throw new IOException("Error during reading of CSV file of {}" + filepath);
        }

    }
}