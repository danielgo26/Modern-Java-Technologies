package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateEachNotNull;
import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateNotNull;

public class TextTokenizer {

    private static final String PUNCTUATION_PATTERN = "\\p{Punct}";
    private static final String WHITESPACE_PATTERN = "\\s+";
    private final Set<String> stopwords;

    public TextTokenizer(Reader stopwordsReader) {
        validateNotNull(stopwordsReader, "The given stop words reader is null");

        try (var br = new BufferedReader(stopwordsReader)) {
            stopwords = br.lines()
                .collect(Collectors.toSet())
                .parallelStream()
                .map(sw -> normalizeWith(sw, PUNCTUATION_PATTERN + WHITESPACE_PATTERN))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not load dataset", ex);
        }
    }

    public List<String> tokenize(String input) {
        validateNotNull(input, "The given string input is null");

        return removeStopwordsFrom(separateWordsFrom(normalizeWith(input, PUNCTUATION_PATTERN)));
    }

    public List<String> tokenizeWithoutRemovingStopwords(Collection<String> input) {
        validateNotNull(input, "The given string input is null");
        validateEachNotNull(input);

        return input.stream()
            .map(w -> normalizeWith(w, "[" + PUNCTUATION_PATTERN + WHITESPACE_PATTERN + "]"))
            .toList();
    }

    public Set<String> stopwords() {
        return stopwords;
    }

    private String normalizeWith(String str, String pattern) {
        return str.replaceAll(pattern, "").toLowerCase();
    }

    private List<String> separateWordsFrom(String input) {
        return Arrays.stream(input.split(WHITESPACE_PATTERN))
                .toList();
    }

    private List<String> removeStopwordsFrom(Collection<String> words) {
        return words
            .stream()
            .filter(w -> !stopwords.contains(w))
            .toList();
    }

}