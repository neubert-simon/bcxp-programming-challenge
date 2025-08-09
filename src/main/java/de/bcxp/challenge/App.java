package de.bcxp.challenge;

import de.bcxp.challenge.analysis.IDocumentAnalyser;
import de.bcxp.challenge.analysis.csv.CountryAnalyser;
import de.bcxp.challenge.analysis.csv.WeatherAnalyser;
import de.bcxp.challenge.documentParsing.IDocumentParser;
import de.bcxp.challenge.documentParsing.csv.CountryCsvParser;
import de.bcxp.challenge.documentParsing.csv.WeatherCsvParser;
import de.bcxp.challenge.model.Document;
import de.bcxp.challenge.model.DocumentEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The entry class for your solution. This class is only aimed as starting point and not intended as baseline for your
 * software design. Read: create your own classes and packages as appropriate.
 */
public final class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    private static final String WEATHER_CSV_PATH = "de/bcxp/challenge/weather.csv";
    private static final String COUNTRIES_CSV_PATH = "de/bcxp/challenge/countries.csv";

    /**
     * This is the main entry method of your program.
     * @param args The CLI arguments passed
     */
    public static void main(String... args) {

        String dayWithSmallestTempSpread = getBestMatchFromDocument(
                WEATHER_CSV_PATH,
                new WeatherCsvParser(','),
                new WeatherAnalyser()
        );
        System.out.printf("Day with smallest temperature spread: %s%n", dayWithSmallestTempSpread);


        String countryWithHighestPopulationDensity = getBestMatchFromDocument(
                COUNTRIES_CSV_PATH,
                new CountryCsvParser(';'),
                new CountryAnalyser()
        );
        System.out.printf("Country with highest population density: %s%n", countryWithHighestPopulationDensity);

    }

    /**
     * Loads a document from the given file path using the provided parser, analyzes its entries to find the best match
     * using the provided analyser, and prints the extracted result using the given result extractor function.
     *
     * <p>This method is generic and works with any type of {@link DocumentEntry}, as long as appropriate parser and analyser
     * implementations are provided.</p>
     *
     * @param path the file path to the document (typically a classpath resource)
     * @param parser the {@link IDocumentParser} that reads and parses the document into entries
     * @param analyser the {@link IDocumentAnalyser} that finds the best match from the parsed entries
     * @param <T> the type of {@link DocumentEntry} in the document
     */
    private static <T extends DocumentEntry> String getBestMatchFromDocument(final String path, final IDocumentParser<T> parser, final IDocumentAnalyser<T> analyser) {
        try {
            Document<T> document = new Document<>(path, parser);
            T bestMatch = analyser.getBestMatch(document);
            return bestMatch.getId();
        } catch (Exception e) {
            logger.fatal("Reading and analysis of document {} failed: {}", path, e.getMessage());
            System.err.println("Document analysis failed for: " + path);
            return "N/A";
        }
    }


}
