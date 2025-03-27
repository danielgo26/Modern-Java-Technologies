package bg.sofia.uni.fmi.mjt.goodreads.book.bookcomparator;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;

import java.util.Comparator;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateNotNull;

public class RecommendedBooksComparator implements Comparator<Book> {

    private final Map<Book, Double> bookRecommendations;

    public RecommendedBooksComparator(Map<Book, Double> bookRecommendations) {
        this.bookRecommendations = bookRecommendations;
    }

    @Override
    public int compare(Book o1, Book o2) {
        validateNotNull(o1, "The first given book is null");
        validateNotNull(o2, "The second given book is null");

        Double firstBookGrade = bookRecommendations.get(o1);
        Double secondBookGrade = bookRecommendations.get(o2);

        int result = secondBookGrade.compareTo(firstBookGrade);
        if (result == 0) {
            return Integer.compare(Integer.parseInt(o2.ID()), Integer.parseInt(o1.ID()));
        }
        return result;
    }

}