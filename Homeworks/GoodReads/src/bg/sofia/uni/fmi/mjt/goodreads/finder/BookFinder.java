package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateEachNotNull;
import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateNotNull;
import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateNotNullNorEmpty;

public class BookFinder implements BookFinderAPI {

    private final Set<Book> books;
    private final TextTokenizer textTokenizer;

    public BookFinder(Set<Book> books, TextTokenizer tokenizer) {
        validateNotNullNorEmpty(books, "The given collection of books is null or empty");
        validateEachNotNull(books);
        validateNotNull(tokenizer, "The given tokenizer is null");

        this.books = books;
        this.textTokenizer = tokenizer;
    }

    public Set<Book> allBooks() {
        return this.books;
    }

    @Override
    public List<Book> searchByAuthor(String authorName) {
        validateNotNull(authorName, "The given author name is null");

        return this.books
            .stream()
            .filter(book -> book.author().equals(authorName))
            .toList();
    }

    @Override
    public Set<String> allGenres() {
        return this.books
            .stream()
            .map(Book::genres)
            .filter(l -> !l.isEmpty())
            .flatMap(Collection::stream)
            .filter(g -> !g.isEmpty() && !g.isBlank())
            .collect(Collectors.toSet());
    }

    @Override
    public List<Book> searchByGenres(Set<String> genres, MatchOption option) {
        Function<Book, Set<String>> searchInto =
            book -> new HashSet<>(textTokenizer.tokenizeWithoutRemovingStopwords(book.genres()));

        return searchBy(searchInto, genres, option);
    }

    @Override
    public List<Book> searchByKeywords(Set<String> keywords, MatchOption option) {
        Function<Book, Set<String>> searchInto = this::getBookPossibleKeywords;

        return searchBy(searchInto, keywords, option);
    }

    private List<Book> searchBy(Function<Book, Set<String>> searchInto, Set<String> searchedFor,
                                MatchOption matchOption) {
        validateNotNull(searchedFor, "The given collection to search for is null");
        if (searchedFor.isEmpty()) {
            return new ArrayList<>();
        }
        validateEachNotNull(searchedFor);

        Set<String> normalizedCollectionToSearchFor =
            new HashSet<>(textTokenizer.tokenizeWithoutRemovingStopwords(searchedFor));

        return this.books.stream()
            .filter(b -> matchBook(searchInto.apply(b), normalizedCollectionToSearchFor, matchOption))
            .toList();
    }

    private boolean matchBook(Set<String> searchInto, Set<String> searchedFor, MatchOption matchOption) {
        BiPredicate<Stream<String>, Predicate<String>> matchFunction = MatchOption.getMatchFunction(matchOption);

        return matchFunction.test(searchedFor.stream(), searchInto::contains);
    }

    private Set<String> getBookPossibleKeywords(Book book) {
        return new HashSet<>(this.textTokenizer.tokenize(book.getKeywordsString()));
    }

}