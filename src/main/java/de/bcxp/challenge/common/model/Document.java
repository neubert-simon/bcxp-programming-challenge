package de.bcxp.challenge.common.model;

import de.bcxp.challenge.common.exceptions.DocumentCreationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Set;
import static de.bcxp.challenge.common.utility.ParameterValidationUtility.validateEntries;

/**
 * Represents an abstract document containing a list of {@link DocumentEntry} objects. <br>
 * Subclasses should define specific behavior for
 * different types of documents.
 */
public class Document {
    private static final Logger logger = LogManager.getLogger(Document.class);

    /**
     * A list of {@link DocumentEntry} objects present in the represented document.
     * <p>
     * Consideration was given to represent entries as a {@link Set} to avoid saving duplicate entries.
     * A {@link List} was chosen to represent the entries since there might be documents with identical
     * entries that could be valid.
     * </p>
     */
    private final List<DocumentEntry> entries;

    /**
     * Constructs a new {@code Document}.
     * @param entries a {@link List} of {@link DocumentEntry} objects that represent the Document contents
     *
     */
    public Document(final List<DocumentEntry> entries) throws DocumentCreationException {
        try {
            validateEntries(entries, true, logger, "List of entries was null or contained null when trying to create Document.", "Entries can't be null.");
        } catch (Exception e) {
            throw new DocumentCreationException(e.getMessage());
        }
        this.entries = entries;
        logger.debug("Created Document with {}", entries);
    }

    public List<DocumentEntry> getEntries() {
        return entries;
    }

    //region java.lang.Object Overrides
    @Override
    public String toString() {
        return "Document: " +
                "entries: " + entries;
    }
    //endregion
}