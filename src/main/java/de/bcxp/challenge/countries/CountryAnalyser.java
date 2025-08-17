package de.bcxp.challenge.countries;

import de.bcxp.challenge.common.analysis.IDocumentAnalyser;
import de.bcxp.challenge.common.model.DocumentEntry;
import de.bcxp.challenge.common.model.csv.NumericComparisonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.bcxp.challenge.common.model.Document;
import java.util.*;
import static de.bcxp.challenge.common.analysis.csv.CsvAnalysisUtility.getBestMatchesForNumericColumnComparison;
import static de.bcxp.challenge.common.utility.ParameterValidationUtility.*;

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
public class CountryAnalyser implements IDocumentAnalyser {
    private static final Logger logger = LogManager.getLogger(CountryAnalyser.class);

    /**
     * Instantiates stateless analyser.
     */
    public CountryAnalyser() {}

    /**
     * Finds the country with the highest population density from the provided document.
     *
     * <p>This method analyzes the list of {@link CountryEntry} entries in the given {@link Document}
     * and returns the country with the maximum value of population density. If the list is empty,
     * a {@link NoSuchElementException} is thrown.</p>
     *
     * @param document the document containing a list of {@link CountryEntry} entries
     * @return A set of {@link CountryEntry} objects with the highest population density
     * @throws NoSuchElementException if the document contains no matching entries
     */
    @Override
    public Set<DocumentEntry> getBestMatches(final Document document) throws NoSuchElementException {
        validateDocument(document, logger, DOCUMENT_LOG, DOCUMENT_EXCEPTION);
        return getBestMatchesForNumericColumnComparison(document, NumericComparisonType.MAX);
    }
}