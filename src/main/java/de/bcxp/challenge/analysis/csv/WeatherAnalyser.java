package de.bcxp.challenge.analysis.csv;

import de.bcxp.challenge.analysis.IDocumentAnalyser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.bcxp.challenge.model.Document;
import de.bcxp.challenge.model.csv.WeatherEntry;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Analyzes a weather document to find the entry with the smallest temperature spread.
 * <p>
 * Implements the {@link IDocumentAnalyser} interface for {@link WeatherEntry} objects.
 * </p>
 *
 * @see IDocumentAnalyser
 * @see WeatherEntry
 * @see Document
 */
public class WeatherAnalyser implements IDocumentAnalyser<WeatherEntry> {
    private static final Logger logger = LogManager.getLogger(WeatherAnalyser.class);

    /**
     * Instantiates stateless analyser.
     */
    public WeatherAnalyser() {}

    /**
     * Finds the day with the smallest temperature spread from the provided weather document.
     * <p>
     * This method analyzes the list of {@link WeatherEntry} entries in the given {@link Document}
     * and returns the one with the smallest temperature spread (i.e., maxTemp - minTemp).
     * </p>
     * @param document the document containing a list of {@link WeatherEntry} entries
     * @return the {@link WeatherEntry} entry with the smallest temperature spread
     * @throws NoSuchElementException if the document contains no entries
     */
    @Override
    public WeatherEntry getBestMatch(final Document<WeatherEntry> document) throws NoSuchElementException {
        final List<WeatherEntry> entries = document.getEntries();
        logger.debug("Analyzing {} weather entries for temperature spread.", entries.size());

        final Optional<WeatherEntry> lowestTemperatureSpread = entries.stream()
                .min(Comparator.comparingDouble(WeatherEntry::getTemperatureSpread));

        if (lowestTemperatureSpread.isPresent()) {
            final WeatherEntry result = lowestTemperatureSpread.get();
            logger.info("Day with smallest temperature spread: {} (Spread: {})", result.getDay(), result.getTemperatureSpread());
            return result;
        }

        logger.warn("No weather entries found in document matching the filter criteria: {}", document.toString());
        throw new NoSuchElementException("Unable to extract temperature spread from provided list.");
    }

}
