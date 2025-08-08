package analysis;

import model.Document;
import model.DocumentEntry;

public interface IDocumentAnalyser<T extends DocumentEntry> {

    T getBestMatch(Document<T> document);

}