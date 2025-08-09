package de.bcxp.challenge.analysis.csv;

import java.util.Comparator;

/**
 * Enumeration defining strategies for comparing numeric values when determining
 * the "best" score in analytical operations.
 * <p>
 * This enum encapsulates a {@link Comparator} for {@link Double} values, allowing
 * algorithms to determine whether the optimal value is the minimum, maximum, or
 * another ordering strategy in the future.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * double best = scores.stream()
 *                     .min(NumericComparisonType.MIN.comparator)
 *                     .orElseThrow();
 * }</pre>
 *
 * <p>
 * Each constant stores a comparator that defines the ordering for comparison.
 * </p>
 */
public enum NumericComparisonType {

    /**
     * Selects the smallest numeric value as the best score.
     */
    MIN(Comparator.naturalOrder()),
    /**
     * Selects the largest numeric value as the best score.
     */
    MAX(Comparator.reverseOrder())
    ;


    public final Comparator<Double> comparator;

    NumericComparisonType(Comparator<Double> comparator) {
        this.comparator = comparator;
    }
}
