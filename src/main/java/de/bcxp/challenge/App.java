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
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;

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
                new WeatherCsvParser(',', Locale.GERMANY),
                new WeatherAnalyser()
        );
        System.out.printf("Day with smallest temperature spread: %s%n", dayWithSmallestTempSpread);


        String countryWithHighestPopulationDensity = getBestMatchFromDocument(
                COUNTRIES_CSV_PATH,
                new CountryCsvParser(';', Locale.GERMANY),
                new CountryAnalyser()
        );
        System.out.printf("Country with highest population density: %s%n", countryWithHighestPopulationDensity);

    }

    /**
     * Loads a document from the given file path using the provided parser, analyzes its entries to find the best match
     * using the provided analyser, and prints the extracted result using the given result extractor function.
     * <p>
     * Since the provided main()-Method in App.java calls for only one value, an arbitrary value is pulled from the set of results.
     * For example, if there are multiple days with equal temperature spreads, only one arbitrary day be returned.
     * </p>
     *
     * @param path the file path to the document
     * @param parser the {@link IDocumentParser} that reads and parses the document into entries
     * @param analyser the {@link IDocumentAnalyser} that finds the best match from the parsed entries
     */
    private static String getBestMatchFromDocument(final String path, final IDocumentParser parser, final IDocumentAnalyser analyser) {
        try {
            final Document document = parser.parseDocument(path);
            final Set<DocumentEntry> bestMatches = analyser.getBestMatches(document);
            return bestMatches.stream()
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException("Unable to find best match"))
                    .getId();
        } catch (Exception e) {
            logger.fatal("Reading and analysis of document {} failed: {}", path, e);
            System.err.println("Document analysis failed for: " + path);
            return "N/A";
        }
    }


}
