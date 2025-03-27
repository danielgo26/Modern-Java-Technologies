package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.HashSet;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateNotNull;

public class GenresOverlapSimilarityCalculator implements SimilarityCalculator {

    @Override
    public double calculateSimilarity(Book first, Book second) {
        validateNotNull(first, "The first given book is null");
        validateNotNull(second, "The second given book is null");

        Set<String> firstBookGenres = new HashSet<>(first.genres());
        Set<String> secondBookGenres = new HashSet<>(second.genres());

        int countWordsInMinSet = Math.min(firstBookGenres.size(), secondBookGenres.size());
        if (countWordsInMinSet == 0) {
            return 0.0;
        }

        firstBookGenres.retainAll(secondBookGenres);

        return (double)firstBookGenres.size() / countWordsInMinSet;
    }

}