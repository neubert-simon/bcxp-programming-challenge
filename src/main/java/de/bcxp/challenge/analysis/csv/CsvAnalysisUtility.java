package de.bcxp.challenge.analysis.csv;

import de.bcxp.challenge.model.Document;
import de.bcxp.challenge.model.DocumentEntry;
import de.bcxp.challenge.model.csv.IEntryWithComparableNumericTuple;
import de.bcxp.challenge.utility.NumericComparisonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.*;
import java.util.stream.Collectors;
import static de.bcxp.challenge.utility.ParameterValidationUtility.*;

/**
 * Utility class providing common analytical operations for {@link Document} objects.
 * <p>
 * This class contains static helper methods that perform reusable data analysis tasks,
 * such as identifying the best-matching document entries based on numeric column comparisons.
 * </p>
 * <p>
 * The utility class is declared {@code final} and has a private constructor to prevent instantiation.
 * All functionality is provided via static methods.
 * </p>
 *
 */
final class CsvAnalysisUtility {
    private static final Logger logger = LogManager.getLogger(CsvAnalysisUtility.class);

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private CsvAnalysisUtility() {
        throw new AssertionError("Cannot instantiate utility class.");
    }

    /**
     * Finds all entries in the given {@link Document} that share the "best" numeric score,
     * according to the specified {@link Comparator}.
     * <p>
     * The method performs the following steps:
     * </p>
     * <ol>
     *   <li>Validates that the document and its entries are not {@code null} and meet
     *       the required type constraints.</li>
     *   <li>Validates that all entries implement {@link IEntryWithComparableNumericTuple}.</li>
     *   <li>Calculates a numeric score for each entry via
     *       {@link IEntryWithComparableNumericTuple#getBestMatchScore()}.</li>
     *   <li>Determines the best score according to {@code type.comparator}.</li>
     *   <li>Collects and returns all entries that have this best score.</li>
     * </ol>
     *
     * @param document the {@link Document} containing entries to analyze
     * @param type the {@link NumericComparisonType} providing the Comparator for best score determination
     * @return a {@link Set} of entries with the best score according to the given comparator;
     *         never {@code null} but may be empty
     * @throws NoSuchElementException if the document contains no valid entries or no score could be computed
     * @throws IllegalArgumentException if the document entries are not of type {@link IEntryWithComparableNumericTuple}
     * @throws IllegalStateException if the document contains a {@code null} entry
     * @see Document
     * @see DocumentEntry
     * @see IEntryWithComparableNumericTuple
     */
    static Set<DocumentEntry> getBestMatchesForNumericColumnComparison(final Document document, final NumericComparisonType type) throws NoSuchElementException {

        validateDocument(document, logger, DOCUMENT_LOG, DOCUMENT_EXCEPTION);
        final Set<IEntryWithComparableNumericTuple> comparableEntries = getEntriesWithComparableNumericTuples(document);

        final double bestScore = comparableEntries.stream()
                .map(IEntryWithComparableNumericTuple::getBestMatchScore)
                .max(type.comparator)
                .orElseThrow(() -> {
                    logger.warn("No best match found in {}", comparableEntries);
                    return new NoSuchElementException("No best match found.");
                });

        return getAllDocumentEntriesWithBestScore(comparableEntries, bestScore);
    }

    /**
     * Maps all {@link DocumentEntry} objects from a {@link Document} to {@link IEntryWithComparableNumericTuple}
     * @param document Document with numeric tuples
     * @return a {@link Set} of {@link IEntryWithComparableNumericTuple} objects representing the entries from the given document
     * @throws IllegalArgumentException if the entries are empty, null, or not all the same concrete type implementing {@link IEntryWithComparableNumericTuple}
     */
    private static Set<IEntryWithComparableNumericTuple> getEntriesWithComparableNumericTuples(final Document document) throws IllegalArgumentException {
        final List<DocumentEntry> entries = document.getEntries();
        validateNumericTupleDocumentEntries(entries, logger);
        return entries.stream()
                .map(entry -> (IEntryWithComparableNumericTuple) entry)
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves all entries from the given set that have the determined score.
     * @param comparableEntries Set containing all entries from the document.
     * @param bestScore Score to filter for
     * @return Set of all {@link DocumentEntry} objects with the best score.
     */
    private static Set<DocumentEntry> getAllDocumentEntriesWithBestScore(final Set<IEntryWithComparableNumericTuple> comparableEntries, final double bestScore) {
        return comparableEntries.stream()
                .filter(entry -> entry.getBestMatchScore() == bestScore)
                .map(entry -> (DocumentEntry) entry)
                .collect(Collectors.toSet());
    }

}