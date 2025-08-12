package de.bcxp.challenge.model.csv;

/**
 * Interface providing a contract for an entry that can produce a numeric score
 * for comparison purposes.
 * <p>
 * Implementations of this interface provide a single numeric value,
 * which can be used in analytical operations such as
 * finding minimum or maximum values across a collection of entries.
 * This score must make the Object comparable via the {@link Comparable} interface.
 * </p>
 *
 */
public interface IEntryWithComparableNumericTuple extends Comparable<IEntryWithComparableNumericTuple> {

    /**
     * Returns the numeric score representing this entry's "best match" value.
     * <p>
     * This score should be used for comparisons when determining optimal entries
     * according to a given {@link java.util.Comparator}.
     * </p>
     *
     * @return the numeric score for this entry
     */
    double getBestMatchScore();

}