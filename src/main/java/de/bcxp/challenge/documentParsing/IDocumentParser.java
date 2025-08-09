package de.bcxp.challenge.documentParsing;

import de.bcxp.challenge.model.DocumentEntry;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Interface for parsing a document from a given file path. <br>
 * Implementations should handle the logic for reading the file.
 * @param <T> the type of {@link DocumentEntry} produced by the parser
 */
@FunctionalInterface
public interface IDocumentParser<T extends DocumentEntry> {

    /**
     * Parses a document from the specified file path.
     *
     * @param filepath the {@link String} representation of the documents filepath
     * @return a List of {@link DocumentEntry} representing the parsed content
     * @throws IOException if an I/O error occurs while reading the file
     * @throws ParseException if an error occurs while trying to parse the file
     */
    List<T> parseDocument(final String filepath) throws IOException, ParseException;

}