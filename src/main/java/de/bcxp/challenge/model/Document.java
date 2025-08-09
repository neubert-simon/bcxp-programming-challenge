package de.bcxp.challenge.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.bcxp.challenge.documentParsing.IDocumentParser;
import de.bcxp.challenge.exceptions.DocumentCreationException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import static de.bcxp.challenge.utility.ParameterValidationUtility.*;

/**
 * Represents an abstract document containing a list of {@link DocumentEntry}. <br>
 * Subclasses should define specific behavior for
 * different types of documents.
 * @param <T> the concrete type of {@link DocumentEntry} contained in the entries {@link List}
 */
public class Document<T extends DocumentEntry> {
    private static final Logger logger = LogManager.getLogger(Document.class);

    private final List<T> entries;

    /**
     * Constructs a new {@code Document} by parsing the specified file using the given parser.
     * <p>
     * This constructor first validates the provided file path, ensuring it is neither
     * {@code null} nor empty. If validation passes, it attempts to parse the document
     * using the supplied {@link IDocumentParser}. The parsed content is stored in
     * {@code entries}.
     * </p>
     *
     * @param filepath the path to the file to be parsed; must be non-null and non-empty
     * @param parser   the {@link IDocumentParser} implementation to use for parsing
     *                 the document; must not be {@code null}
     * @throws DocumentCreationException if the file path is invalid, or if an
     *         {@link IOException} or {@link ParseException} occurs during parsing
     *
     * @see IDocumentParser
     */
    public Document(final String filepath, final IDocumentParser<T> parser) throws DocumentCreationException {

        validateString(filepath, logger, "Invalid filepath passed in when trying to create document.", "Filepath mustn't be empty");

        try {
            this.entries = parser.parseDocument(filepath);
            logger.debug("Created Document from {} with {}", filepath, entries);
        } catch (IOException | ParseException e) {
            logger.error("Document creation failed for {};\n{}", filepath, e.getMessage());
            throw new DocumentCreationException("Document couldn't be created from: " + filepath);
        }
    }

    public List<T> getEntries() {
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