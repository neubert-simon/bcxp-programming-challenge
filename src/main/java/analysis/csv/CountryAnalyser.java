package analysis.csv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import analysis.IDocumentAnalyser;
import model.Document;
import model.csv.CountryEntry;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Analyzes a document of {@link CountryEntry} objects to extract the country with the highest population density.
 * <p>
 * Implements the {@link IDocumentAnalyser} interface for {@link CountryEntry} entries.
 * </p>
 *
 * @see IDocumentAnalyser
 * @see CountryEntry
 * @see Document
 */
public class CountryAnalyser implements IDocumentAnalyser<CountryEntry> {
    private static final Logger logger = LogManager.getLogger(CountryAnalyser.class);

    /**
     * Finds the country with the highest population density from the provided document.
     *
     * <p>This method analyzes the list of {@link CountryEntry} entries in the given {@link Document}
     * and returns the country with the maximum value of population density. If the list is empty,
     * a {@link NoSuchElementException} is thrown.</p>
     *
     * @param document the document containing a list of {@link CountryEntry} entries
     * @return the {@link CountryEntry} with the highest population density
     * @throws NoSuchElementException if the document contains no entries
     */
    @Override
    public CountryEntry getBestMatch(Document<CountryEntry> document) {

        List<CountryEntry> entries = document.getEntries();
        logger.debug("Analyzing {} country entries for population density.", entries.size());

        Optional<CountryEntry> highestPopulationDensity = entries.stream()
                .max(Comparator.comparingDouble(CountryEntry::getPopulationDensity));

        if (highestPopulationDensity.isPresent()) {
            CountryEntry result = highestPopulationDensity.get();
            logger.info("Country with highest population density: {} ({})", result.getCountry(), result.getPopulationDensity());
            return result;
        }

        logger.warn("No country entries found in document matching the filter criteria: {}", document.toString());
        throw new NoSuchElementException("Unable to extract population density from provided list.");
    }

}