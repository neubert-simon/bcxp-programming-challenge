package exceptions;

public class DocumentCreationException extends Exception {

    /**
     * Custom exception that should be thrown if {@link model.Document} creation fails.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
     *                method.
     */
    public DocumentCreationException(final String message) {
        super(message);
    }

}
