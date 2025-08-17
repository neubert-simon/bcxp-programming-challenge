package de.bcxp.challenge.common.model.csv;

import java.util.Comparator;

/**
 * Enumeration defining strategies for comparing numeric values when determining
 * the "best" score in analytical operations.
 * <p>
 * This enum encapsulates a {@link Comparator} for {@link Double} values, allowing
 * algorithms to determine whether the optimal value is the minimum or maximum score.
 * </p>
 * <p>
 * Each constant stores a comparator that defines the ordering for comparison.
 * </p>
 */
public enum NumericComparisonType {

    /**
     * Selects the biggest numeric value as the best score.
     */
    MAX(Comparator.naturalOrder()),
    /**
     * Selects the smallest numeric value as the best score.
     */
    MIN(Comparator.reverseOrder());

    /**
     * Comparator used with max() function applied to a double stream.
     * <h2>Usage Example:</h2>
     *  <pre>{@code
     *  final double bestScore = comparableEntries.stream()
     *                 .map(IEntryWithComparableNumericTuple::getBestMatchScore)
     *                 .max(type.comparator)
     *  }</pre>
     */
    public final Comparator<Double> comparator;

    NumericComparisonType(final Comparator<Double> comparator) {
        this.comparator = comparator;
    }
}