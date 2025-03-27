package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Map;

import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateEachNotNull;
import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateNotNull;
import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateNotNullNorEmpty;

public class CompositeSimilarityCalculator implements SimilarityCalculator {

    private final Map<SimilarityCalculator, Double> similarityCalculatorMap;

    public CompositeSimilarityCalculator(Map<SimilarityCalculator, Double> similarityCalculatorMap) {
        validateNotNullNorEmpty(similarityCalculatorMap, "The given map is null or empty");
        validateEachNotNull(similarityCalculatorMap, "The given map of calculators contains null elements");

        this.similarityCalculatorMap = similarityCalculatorMap;
    }

    @Override
    public double calculateSimilarity(Book first, Book second) {
        validateNotNull(first, "The first given book is null");
        validateNotNull(second, "The second given book is null");

        return this.similarityCalculatorMap.entrySet()
            .stream()
            .mapToDouble(es -> es.getKey().calculateSimilarity(first, second) * es.getValue())
            .sum();
    }

}