package de.bcxp.challenge.documentParsing;

import de.bcxp.challenge.exceptions.DocumentCreationException;
import de.bcxp.challenge.model.Document;

/**
 * Interface for parsing a document from a given file path. <br>
 * Implementations should handle the logic for reading the file.
 */
@FunctionalInterface
public interface IDocumentParser {

    /**
     * Parses a document from the specified file path.
     *
     * @param filepath the {@link String} representation of the documents filepath
     * @return a {@link Document} representing the parsed content
     * @throws DocumentCreationException if an I/O error or ParseError occurs while reading the file
     */
    Document parseDocument(final String filepath) throws DocumentCreationException;

}