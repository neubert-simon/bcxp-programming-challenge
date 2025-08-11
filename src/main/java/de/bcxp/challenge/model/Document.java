package de.bcxp.challenge.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.bcxp.challenge.documentParsing.IDocumentParser;
import de.bcxp.challenge.exceptions.DocumentCreationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static de.bcxp.challenge.utility.ParameterValidationUtility.*;

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
     * @throws DocumentCreationException if the file path is invalid, or if an
     *         {@link IOException} or {@link ParseException} occurs during parsing
     *
     * @see IDocumentParser
     */
    public Document(List<DocumentEntry> entries) throws DocumentCreationException {

        if(entries == null) {
            logger.warn("Entries were null when trying to create Document.");
            throw new IllegalArgumentException("Entries can't be null.");
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