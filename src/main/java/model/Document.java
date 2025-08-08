package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import documentParsing.IDocumentParser;
import exceptions.DocumentCreationException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import static utility.ParameterValidationUtility.*;

/**
 * Represents an abstract document containing a list of {@link DocumentEntry}. <br>
 * Subclasses should define specific behavior for
 * different types of documents.
 */
public class Document<T extends DocumentEntry> {
    private static final Logger logger = LogManager.getLogger(Document.class);

    private final List<T> entries;

    public Document(final String filepath, IDocumentParser<T> parser) throws DocumentCreationException {

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