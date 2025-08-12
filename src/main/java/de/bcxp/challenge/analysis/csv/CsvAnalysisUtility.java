package de.bcxp.challenge.analysis.csv;

import de.bcxp.challenge.model.Document;
import de.bcxp.challenge.model.DocumentEntry;
import de.bcxp.challenge.model.csv.IEntryWithComparableNumericTuple;
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
     * <p>
     * This method will throw a {@link NoSuchElementException} if the document contains
     * no entries or if no score could be determined.
     * </p>
     *
     * @param document the {@link Document} containing entries to analyze; must not be {@code null}
     * @return a {@link Set} of entries with the best score according to the given comparator;
     *         never {@code null} but may be empty
     * @throws NoSuchElementException if the document contains no valid entries or no score could be computed
     * @throws IllegalArgumentException if the document entries are not of type {@link IEntryWithComparableNumericTuple}
     * @throws IllegalStateException if the document contains a {@code null} entry
     * @see Document
     * @see DocumentEntry
     * @see IEntryWithComparableNumericTuple
     */
    static Set<DocumentEntry> getBestMatchesForNumericColumnComparison(final Document document) throws NoSuchElementException {

        validateDocument(document, logger, DOCUMENT_LOG, DOCUMENT_EXCEPTION);
        final List<DocumentEntry> entries = document.getEntries();

        validateNumericTupleDocumentEntries(entries);
        final Set<IEntryWithComparableNumericTuple> comparableEntries =
                entries.stream()
                        .map(entry -> (IEntryWithComparableNumericTuple) entry)
                        .collect(Collectors.toSet());

        logger.debug("Analyzing {} weather entries for temperature spread.", entries.size());

        final IEntryWithComparableNumericTuple bestEntry = comparableEntries.stream()
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> {
                    logger.warn("No best match found in {}", comparableEntries);
                    return new NoSuchElementException("No best match found.");
                });

        return comparableEntries.stream()
                .filter(entry -> entry.compareTo(bestEntry) == 0)
                .map(entry -> (DocumentEntry) entry)
                .collect(Collectors.toSet());
    }

}
