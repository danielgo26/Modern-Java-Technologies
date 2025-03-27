package bg.sofia.uni.fmi.mjt.goodreads.book;

import java.util.List;
import java.util.Objects;

import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateEachNotNull;
import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateEqualTo;
import static bg.sofia.uni.fmi.mjt.goodreads.objectvalidator.ObjectValidator.validateNotNull;

public record Book(
    String ID,
    String title,
    String author,
    String description,
    List<String> genres,
    double rating,
    int ratingCount,
    String URL
) {
    private static final int EXPECTED_TOKENS_COUNT = 8;
    private static final int ID_POS = 0;
    private static final int TITLE_POS = 1;
    private static final int AUTHOR_POS = 2;
    private static final int DESCRIPTION_POS = 3;
    private static final int GENRES_POS = 4;
    private static final int RATING_POS = 5;
    private static final int RATING_COUNT_POS = 6;
    private static final int URL_POS = 7;

    public static Book of(String[] tokens) {
        validateNotNull(tokens, "The given tokens array is null");
        validateEqualTo(tokens.length, EXPECTED_TOKENS_COUNT,
            "Expected the given tokens array to have length 8, but instead: " + tokens.length);
        validateEachNotNull(tokens);

        String genresStr = tokens[GENRES_POS];
        List<String> genres = List.of(genresStr.substring(1, genresStr.length() - 1)
            .replaceAll("[' ]", "")
            .split(","));
        validateEachNotNull(genres);

        String ratingCountStr = tokens[RATING_COUNT_POS].replaceAll(",", "");

        return new Book(tokens[ID_POS], tokens[TITLE_POS], tokens[AUTHOR_POS], tokens[DESCRIPTION_POS],
            genres, Double.parseDouble(tokens[RATING_POS]), Integer.parseInt(ratingCountStr), tokens[URL_POS]);
    }

    public String getKeywordsString() {
        return title + " " + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(ID, book.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ID);
    }
}