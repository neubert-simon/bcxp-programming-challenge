package de.bcxp.challenge.exceptions;

import de.bcxp.challenge.model.Document;

/**
 * Custom exception that should be thrown if {@link Document} creation fails.
 */
public class DocumentCreationException extends Exception {

    /**
     * Creates custom exception that should be thrown if {@link Document} creation fails.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
     *                method.
     */
    public DocumentCreationException(final String message) {
        super(message);
    }

}
