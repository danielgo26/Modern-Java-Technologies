package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateEachNotNull;
import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateNotNull;
import static java.lang.Math.log10;

public class TFIDFSimilarityCalculator implements SimilarityCalculator {

    private final Set<Book> books;
    private final TextTokenizer tokenizer;
    private final Map<String, Integer> wordsRepetitionsFromAllGivenBooks;
    private final Map<Book, Map<String, Double>> hashedBooksTfIdfs;

    public TFIDFSimilarityCalculator(Set<Book> books, TextTokenizer tokenizer) {
        validateNotNull(books, "The given collection of books is null");
        validateEachNotNull(books);
        validateNotNull(tokenizer, "The given tokenizer is null");

        this.books = books;
        this.tokenizer = tokenizer;
        this.wordsRepetitionsFromAllGivenBooks = populateWordsRepetitionsFromAllGivenBooks(books);
        this.hashedBooksTfIdfs = new HashMap<>();
    }

    @Override
    public double calculateSimilarity(Book first, Book second) {
        Map<String, Double> tfIdfScoresFirst = computeTFIDF(first);
        Map<String, Double> tfIdfScoresSecond = computeTFIDF(second);

        return cosineSimilarity(tfIdfScoresFirst, tfIdfScoresSecond);
    }

    public Map<String, Double> computeTFIDF(Book book) {
        validateNotNull(book, "The given book is null");

        if (this.hashedBooksTfIdfs.containsKey(book)) {
            return this.hashedBooksTfIdfs.get(book);
        }

        Map<String, Double> tfs = computeTF(book);
        Map<String, Double> idfs = computeIDF(book);

        Map<String, Double> currentBookTfIdfs = tfs.entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                es -> es.getValue() * idfs.getOrDefault(es.getKey(), 0.0)
            ));
        this.hashedBooksTfIdfs.put(book, currentBookTfIdfs);

        return currentBookTfIdfs;
    }

    public Map<String, Double> computeTF(Book book) {
        validateNotNull(book, "The given book is null");

        List<String> tokenizedWords = this.tokenizer.tokenize(book.description());

        Map<String, Integer> wordsRepetitionsCurrentBook = tokenizedWords
            .stream()
            .collect(Collectors.toMap(
                word -> word,
                word -> 1,
                Integer::sum
            ));

        return wordsRepetitionsCurrentBook.entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                es -> (es.getValue() / (double) tokenizedWords.size())
            ));
    }

    public Map<String, Double> computeIDF(Book book) {
        validateNotNull(book, "The given book is null");

        Set<String> tokenizedWords = new HashSet<>(this.tokenizer.tokenize(book.description()));

        Map<String, Integer> wordsRepetitionsInAllBooks = tokenizedWords
            .stream()
            .collect(Collectors.toMap(
                word -> word,
                this.wordsRepetitionsFromAllGivenBooks::get
            ));

        int allBooksCount = this.books.size();

        return wordsRepetitionsInAllBooks
            .entrySet()
            .parallelStream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                es -> log10((double) allBooksCount / es.getValue())
            ));
    }

    private Map<String, Integer> populateWordsRepetitionsFromAllGivenBooks(Set<Book> books) {
        return books
            .stream()
            .map(b -> this.tokenizer.tokenize(b.description()))
            .map(HashSet::new)
            .flatMap(Set::stream)
            .collect(Collectors.toMap(
                w -> w,
                w -> 1,
                Integer::sum
            ));
    }

    private double cosineSimilarity(Map<String, Double> first, Map<String, Double> second) {
        double magnitudeFirst = magnitude(first.values());
        double magnitudeSecond = magnitude(second.values());

        return dotProduct(first, second) / (magnitudeFirst * magnitudeSecond);
    }

    private double dotProduct(Map<String, Double> first, Map<String, Double> second) {
        Set<String> commonKeys = new HashSet<>(first.keySet());
        commonKeys.retainAll(second.keySet());

        return commonKeys.stream()
            .mapToDouble(word -> first.get(word) * second.get(word))
            .sum();
    }

    private double magnitude(Collection<Double> input) {
        double squaredMagnitude = input.stream()
            .map(v -> v * v)
            .reduce(0.0, Double::sum);

        return Math.sqrt(squaredMagnitude);
    }

}