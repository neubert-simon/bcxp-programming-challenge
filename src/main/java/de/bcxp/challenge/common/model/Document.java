package de.bcxp.challenge.common.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.bcxp.challenge.common.documentParsing.IDocumentParser;
import java.util.List;
import java.util.Set;
import static de.bcxp.challenge.common.utility.ParameterValidationUtility.validateEntries;

/**
 * Represents an abstract document containing a list of {@link DocumentEntry}. <br>
 * Subclasses should define specific behavior for
 * different types of documents.
 */
public class Document {
    private static final Logger logger = LogManager.getLogger(Document.class);

    /**
     * A list of {@link DocumentEntry} objects present in the represented document.
     * Consideration was given to represent entries as a {@link Set} to avoid saving duplicate entries,
     * but a {@link List} was chosen to represent the entries since there might be documents with identical
     * entries that could be valid.
     */
    private final List<DocumentEntry> entries;

    /**
     * Constructs a new {@code Document} by parsing the specified file using the given parser.
     * <p>
     * This constructor first validates the provided file path, ensuring it is neither
     * {@code null} nor empty. If validation passes, it attempts to parse the document
     * using the supplied {@link IDocumentParser}. The parsed content is stored in
     * {@code entries}.
     * </p>
     *
     * @param entries a {@link List} of {@link DocumentEntry} objects that represent the Document contents
     *
     * @see IDocumentParser
     */
    public Document(final List<DocumentEntry> entries) {
        validateEntries(entries, true, logger, "List of entries was null or contained null when trying to create Document.", "Entries can't be null.");
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