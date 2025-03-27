package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.basicTextTokenizer;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.complexBookSet;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.complBook1;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.complBook2;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.complBook3;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.complBook4;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.complBook5;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.complBook6;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.complBook7;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.complBook8;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.complBook9;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.complBook10;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookFinderTest {

    private static final BookFinder bookFinder =
        new BookFinder(complexBookSet, basicTextTokenizer);

    @Test
    void testAllBooksShouldReturnTheSameBookCollectionAsGivenInTheConstructor() {
        Set<Book> resulted = bookFinder.allBooks();

        assertIterableEquals(resulted, complexBookSet,
            "Expected to return: " + complexBookSet + ", but returned: " + resulted);
    }

    @Test
    void testSearchByAuthorShouldThrowWhenAuthorNameIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> bookFinder.searchByAuthor(null),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testSearchByAuthorShouldReturnValidListOfBooksForAGivenAuthor() {
        String authorName = "George Orwell";
        List<Book> resulted = bookFinder.searchByAuthor(authorName);

        List<Book> expected = List.of(complBook2, complBook9);

        assertEquals(resulted.size(), expected.size(),
            "Expected to return collection with size: " + expected.size() +
            ", but returned collection with size: " + resulted.size());
        assertTrue(resulted.containsAll(expected),
            "Expected to return: " + expected + ", but returned: " + resulted);
    }

    @Test
    void testAllGenresShouldReturnValidSetOfTheContainingGenres() {
        Set<String> resulted = bookFinder.allGenres();

        Set<String> expected = Set.of("Dystopian", "Coming-of-age", "Political", "Fantasy", "Historical", "Adventure",
            "Fiction", "Classic", "Drama", "Romance", "Science Fiction", "Epic");

        assertEquals(resulted.size(), expected.size(),
            "Expected to return collection with size: " + expected.size() +
            ", but returned collection with size: " + resulted.size());
        assertTrue(resulted.containsAll(expected),
            "Expected to return: " + expected + ", but returned: " + resulted);
    }

    @Test
    void testSearchByGenresShouldThrowWhenGivenGenresCollectionIsNull() {
       assertThrows(IllegalArgumentException.class,
           () -> bookFinder.searchByGenres(null, MatchOption.MATCH_ALL),
           "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testSearchByGenresShouldThrowWhenGivenGenresCollectionContainsNullElement() {
        Set<String> genres = new HashSet<>();
        genres.add(null);

        assertThrows(IllegalArgumentException.class,
            () -> bookFinder.searchByGenres(genres, MatchOption.MATCH_ALL),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testSearchByGenresShouldReturnEmptyCollectionOfGenresWhenGivenNoGenres() {
        List<Book> resulted = bookFinder.searchByGenres(Set.of(), MatchOption.MATCH_ALL);

        assertTrue(resulted.isEmpty(),
            "Expected to return an empty collection, but returned: " + resulted);
    }

    @Test
    void testSearchByGenresShouldReturnValidCollectionOfGenresWithOptionMatchAny() {
        Set<String> searchedGenres = Set.of("Dystopian", "Romance");
        List<Book> resulted = bookFinder.searchByGenres(searchedGenres, MatchOption.MATCH_ANY);

        List<Book> expected = List.of(complBook2, complBook3, complBook9, complBook10);

        assertEquals(resulted.size(), expected.size(),
            "Expected to return collection with size: " + expected.size() +
                ", but returned collection with size: " + resulted.size());
        assertTrue(resulted.containsAll(expected),
            "Expected to return: " + expected + ", but returned: " + resulted);
    }

    @Test
    void testSearchByGenresShouldReturnValidCollectionOfGenresWithOptionMatchAll() {
        Set<String> searchedGenres = Set.of("Fiction", "Classic");
        List<Book> resulted = bookFinder.searchByGenres(searchedGenres, MatchOption.MATCH_ALL);

        List<Book> expected = List.of(complBook1, complBook3, complBook4, complBook5, complBook7);

        assertEquals(resulted.size(), expected.size(),
            "Expected to return collection with size: " + expected.size() +
                ", but returned collection with size: " + resulted.size());
        assertTrue(resulted.containsAll(expected),
            "Expected to return: " + expected + ", but returned: " + resulted);
    }

    @Test
    void testSearchByKeywordsShouldThrowWhenGivenKeywordsCollectionIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> bookFinder.searchByKeywords(null, MatchOption.MATCH_ALL),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testSearchByKeywordsShouldThrowWhenGivenKeywordsCollectionContainsNullElement() {
        Set<String> keywords = new HashSet<>();
        keywords.add(null);

        assertThrows(IllegalArgumentException.class,
            () -> bookFinder.searchByKeywords(keywords, MatchOption.MATCH_ALL),
            "Expected an IllegalArgumentException to be thrown");
    }

    @Test
    void testSearchByKeywordsShouldReturnEmptyCollectionOfKeywordsWhenGivenNoKeywords() {
        List<Book> resulted = bookFinder.searchByKeywords(Set.of(), MatchOption.MATCH_ALL);

        assertTrue(resulted.isEmpty(),
            "Expected to return an empty collection, but returned: " + resulted);
    }

    @Test
    void testSearchByKeywordsShouldReturnValidCollectionOfKeywordsWithOptionMatchAny() {
        Set<String> searchedKeywords = Set.of("novel", "about");
        List<Book> resulted = bookFinder.searchByKeywords(searchedKeywords, MatchOption.MATCH_ANY);

        List<Book> expected = List.of(complBook1, complBook2, complBook3, complBook6,
            complBook7, complBook8, complBook9);

        assertEquals(resulted.size(), expected.size(),
            "Expected to return collection with size: " + expected.size() +
                ", but returned collection with size: " + resulted.size());
        assertTrue(resulted.containsAll(expected),
            "Expected to return: " + expected + ", but returned: " + resulted);
    }

    @Test
    void testSearchByKeywordsShouldReturnValidCollectionOfKeywordsWithOptionMatchAll() {
        Set<String> searchedKeywords = Set.of("novel", "about");
        List<Book> resulted = bookFinder.searchByKeywords(searchedKeywords, MatchOption.MATCH_ALL);

        List<Book> expected = List.of(complBook1, complBook8);

        assertEquals(resulted.size(), expected.size(),
            "Expected to return collection with size: " + expected.size() +
                ", but returned collection with size: " + resulted.size());
        assertTrue(resulted.containsAll(expected),
            "Expected to return: " + expected + ", but returned: " + resulted);
    }

}