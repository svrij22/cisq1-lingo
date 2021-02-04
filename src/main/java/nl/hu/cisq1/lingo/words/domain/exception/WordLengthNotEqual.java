package nl.hu.cisq1.lingo.words.domain.exception;

public class WordLengthNotEqual extends RuntimeException {
    public WordLengthNotEqual() {
        super("Word lengths not equal");
    }
}
