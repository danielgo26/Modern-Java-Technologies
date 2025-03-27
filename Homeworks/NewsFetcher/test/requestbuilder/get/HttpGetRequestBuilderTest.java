package requestbuilder.get;

import exception.syntax.APIRequestSyntaxException;
import httprequest.get.HttpGetRequestQuery;
import httprequest.get.utilitystructures.Category;
import httprequest.get.utilitystructures.Country;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import requestbuilder.get.utililystructures.KeywordsAddingOption;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HttpGetRequestBuilderTest {

    private static List<String> DEFAULT_KEYWORDS;
    private static HttpGetRequestBuilder builder;

    @BeforeAll
    static void setup() {
        String DEFAULT_APIKEY = "apiKey";
        DEFAULT_KEYWORDS = List.of("word1", "word2");
        builder = new HttpGetRequestBuilder(DEFAULT_APIKEY, DEFAULT_KEYWORDS);
    }

    @Test
    void testHttpGetRequestBuilderThrowsWhenTheGivenApiKeyIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new HttpGetRequestBuilder(null, List.of("word")),
            "Expected an IllegalArgumentException to be thrown when the given api key is null.");
    }

    @Test
    void testHttpGetRequestBuilderThrowsWhenTheGivenKeywordsCollectionIsNotValid() {
        assertThrows(IllegalArgumentException.class, () -> new HttpGetRequestBuilder("", null),
            "Expected an IllegalArgumentException to be thrown when the given keywords collection is null.");
    }

    @Test
    void testFilterByKeywordsThrowsWhenGivenInvalidKeywords() {
        assertThrows(IllegalArgumentException.class,
            () -> builder.filterByKeywords(List.of(""), KeywordsAddingOption.Append),
            "Expected an IllegalArgumentException to be thrown when the given keywords collection contains empty string.");
    }

    @Test
    void testFilterByKeywordsThrowsWhenTheGivenKeywordsAddingOptionIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> builder.filterByKeywords(List.of("word1"), null),
            "Expected an IllegalArgumentException to be thrown when the given keywords adding option is null.");
    }

    @Test
    void testFilterByKeywordsUpdatesKeywordsCollectionWithAppendOption() {
        String newKeyword = "word3";
        HttpGetRequestQuery query = builder.filterByKeywords(List.of(newKeyword), KeywordsAddingOption.Append).build();

        List<String> expectedKeywords = new ArrayList<>(DEFAULT_KEYWORDS);
        expectedKeywords.add(newKeyword);

        assertIterableEquals(expectedKeywords, query.getKeywords(),
            "Expected to return a valid collection of keywords, but the returned keywords did not match.");
    }

    @Test
    void testFilterByKeywordsUpdatesKeywordsCollectionWithReplaceOption() {
        String newKeyword = "word3";
        HttpGetRequestQuery query = builder.filterByKeywords(List.of(newKeyword), KeywordsAddingOption.Replace).build();

        List<String> expectedKeywords = List.of(newKeyword);

        assertIterableEquals(expectedKeywords, query.getKeywords(),
            "Expected to return a valid collection of keywords, but the returned keywords did not match.");
    }

    @Test
    void testFilterByCountryUpdatesCountry() {
        Country newCountry = Country.BULGARIA;
        HttpGetRequestQuery query = builder.filterByCountry(newCountry).build();

        assertEquals(newCountry, query.getCountry(),
            "Expected the country option to be updated after applying the builder.");
    }

    @Test
    void testFilterByCategoryUpdatesCategory() {
        Category newCategory = Category.Business;
        HttpGetRequestQuery query = builder.filterByCategory(newCategory).build();

        assertEquals(newCategory, query.getCategory(),
            "Expected the category option to be updated after applying the builder.");
    }

    @Test
    void testSetPageSizeThrowsWhenTheGivenPageSizeIsOutOfTheAllowedInterval() {
        int newPageSizeUnderMin = HttpGetRequestQuery.MIN_PAGE_SIZE - 1;
        assertThrows(APIRequestSyntaxException.class, () -> builder.setPageSize(newPageSizeUnderMin).build(),
            "Expected an APIRequestSyntaxException to be thrown when setting page size below limit.");

        int newPageSizeOverMax = HttpGetRequestQuery.MAX_PAGE_SIZE + 1;
        assertThrows(APIRequestSyntaxException.class, () -> builder.setPageSize(newPageSizeOverMax).build(),
            "Expected an APIRequestSyntaxException to be thrown when setting page size over limit.");
    }

    @Test
    void testSetPageSizeUpdatesPageSize() {
        int newPageSize = HttpGetRequestQuery.MIN_PAGE_SIZE;
        HttpGetRequestQuery query = builder.setPageSize(newPageSize).build();

        assertEquals(newPageSize, query.getPageSize(),
            "Expected the page size option to be updated after applying the builder.");
    }

    @Test
    void testSetPageThrowsWhenTheGivenPageBelowTheAllowedLimit() {
        int newPageCountUnderMin = HttpGetRequestQuery.MIN_PAGES - 1;
        assertThrows(APIRequestSyntaxException.class, () -> builder.setPage(newPageCountUnderMin).build(),
            "Expected an APIRequestSyntaxException to be thrown when setting page below limit.");
    }

    @Test
    void testSetPageUpdatesPageCount() {
        int newPageCount = HttpGetRequestQuery.MIN_PAGES;
        HttpGetRequestQuery query = builder.setPage(newPageCount).build();

        assertEquals(newPageCount, query.getPage(),
            "Expected the page option to be updated after applying the builder.");
    }

}