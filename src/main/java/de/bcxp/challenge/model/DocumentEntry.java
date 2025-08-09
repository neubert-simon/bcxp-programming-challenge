package de.bcxp.challenge.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static de.bcxp.challenge.utility.ParameterValidationUtility.*;

/**
 * Represents an abstract entry within a {@link Document}. <br>
 * Subclasses can extend this to represent various types of document
 * entries.
 */
public abstract class DocumentEntry {
    private static final Logger logger = LogManager.getLogger(DocumentEntry.class);

    private final String id;

    /**
     * Constructs a new DocumentEntry with the specified ID.
     * @param id the string representation of the entry's unique identifier
     */
    protected DocumentEntry(final String id) {
        validateString(id, logger, STRING_LOG, STRING_EXCEPTION);
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

}