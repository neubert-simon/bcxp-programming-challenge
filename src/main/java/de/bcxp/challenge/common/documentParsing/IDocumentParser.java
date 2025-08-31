package de.bcxp.challenge.common.documentParsing;

import de.bcxp.challenge.common.exceptions.DocumentCreationException;
import de.bcxp.challenge.common.model.Document;

/**
 * Interface for parsing a document from a given file path. <br>
 * Implementations should handle the logic for reading the file.
 */
@FunctionalInterface
public interface IDocumentParser {

    /**
     * Parses a document in accordance with the specific parser implementation.
     *
     * @return a {@link Document} representing the parsed content
     * @throws DocumentCreationException if parsing the file fails
     */
    Document parseDocument() throws DocumentCreationException;

}