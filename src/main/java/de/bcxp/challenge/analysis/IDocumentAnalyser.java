package de.bcxp.challenge.analysis;

import de.bcxp.challenge.model.Document;
import de.bcxp.challenge.model.DocumentEntry;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Functional interface that defines a contract for analyzing a {@link Document} containing {@link DocumentEntry} objects
 * to determine the best matching entry based on a specific criterion.
 *
 */
@FunctionalInterface
public interface IDocumentAnalyser {

    /**
     * Analyzes the provided {@link Document} and returns the single best matching entry.
     * <p>
     * The definition of "best match" is determined by the implementing class, which may
     * apply filtering, sorting, or scoring logic to select the optimal result.
     * </p>
     *
     * @param document the {@link Document} containing entries of type {@code T} to analyze;
     *                 must not be {@code null}
     * @return the entry of type {@code T} that best matches the implemented analysis criteria
     * @throws java.util.NoSuchElementException if no suitable entry can be found
     */
    Set<DocumentEntry> getBestMatches(final Document document) throws NoSuchElementException;

}