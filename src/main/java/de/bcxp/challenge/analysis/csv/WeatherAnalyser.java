package de.bcxp.challenge.analysis.csv;

import de.bcxp.challenge.analysis.IDocumentAnalyser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.bcxp.challenge.model.Document;
import de.bcxp.challenge.model.csv.WeatherEntry;
import java.util.*;
import static de.bcxp.challenge.utility.ParameterValidationUtility.*;
import static de.bcxp.challenge.analysis.AnalysisUtility.getBestMatchesForNumericColumnComparison;
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
    public Set<WeatherEntry> getBestMatches(final Document<WeatherEntry> document) throws NoSuchElementException {
        validateDocument(document, logger, DOCUMENT_LOG, DOCUMENT_EXCEPTION);
        return getBestMatchesForNumericColumnComparison(document, NumericComparisonType.MIN);
    }

}
