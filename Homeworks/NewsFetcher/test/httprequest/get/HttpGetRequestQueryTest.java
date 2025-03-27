package httprequest.get;

import httprequest.get.utilitystructures.Category;
import httprequest.get.utilitystructures.Country;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HttpGetRequestQueryTest {

    private static String DEFAULT_APIKEY;
    private static List<String> DEFAULT_KEYWORDS;
    private static HttpGetRequestQuery query;

    @BeforeAll
    static void setup() {
        DEFAULT_APIKEY = "apiKey";
        DEFAULT_KEYWORDS = List.of("word1", "word2");
        query = new HttpGetRequestQuery(DEFAULT_APIKEY, DEFAULT_KEYWORDS);
    }

    @Test
    void testHttpGetRequestQueryThrowsWhenTheGivenApiKeyIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new HttpGetRequestQuery(null, List.of("word")),
            "Expected an IllegalArgumentException to be thrown when the given api key is null.");
    }

    @Test
    void testHttpGetRequestQueryThrowsWhenTheGivenKeywordsCollectionIsNotValid() {
        assertThrows(IllegalArgumentException.class, () -> new HttpGetRequestQuery("", null),
            "Expected an IllegalArgumentException to be thrown when the given keywords collection is null.");
    }

    @Test
    void testBuildThrowsWhenTheGivenApiKeyIsNull() {
        assertThrows(IllegalArgumentException.class, () -> HttpGetRequestQuery.build(null, List.of("word")),
            "Expected an IllegalArgumentException to be thrown when the given api key is null.");
    }

    @Test
    void testBuildThrowsWhenTheGivenKeywordsCollectionIsNotValid() {
        assertThrows(IllegalArgumentException.class, () -> HttpGetRequestQuery.build("", null),
            "Expected an IllegalArgumentException to be thrown when the given keywords collection is null.");
    }

    @Test
    void testSetKeywordsThrowWhenGivenInvalidKeywords() {
        assertThrows(IllegalArgumentException.class, () -> query.setKeywords(List.of()),
            "Expected an IllegalArgumentException to be thrown when the given keywords collection is null.");
    }

    @Test
    void testGetRequestQueryReturnsValidQueryWithOnlyApiKeyAndKeywordsGiven() {
        String expectedQueryStr = String.format("q=%s&pageSize=%s&apiKey=%s",
            String.join(" " , DEFAULT_KEYWORDS),
            HttpGetRequestQuery.DEFAULT_PAGE_SIZE,
            DEFAULT_APIKEY);

        String resulted = query.getRequestQuery();

        assertEquals(expectedQueryStr, resulted,
            "Expected to return a valid collection of news headlines, but the returned headlines did not match.");
    }

    @Test
    void testGetRequestQueryReturnsValidQueryWhenAllPropertiesAreSet() {
        Country country = Country.UNITED_STATES;
        Category category = Category.Business;
        List<String> keywords = List.of("a", "b");
        Integer pageSize = 10;
        Integer page = 1;

        query.setCountry(country);
        query.setCategory(category);
        query.setKeywords(keywords);
        query.setPageSize(pageSize);
        query.setPage(page);

        String expectedQueryStr = String.format("country=%s&category=%s&q=%s&pageSize=%s&page=%s&apiKey=%s",
            Country.getISOCodeString(country),
            Category.getCategoryString(category),
            String.join(" " , keywords),
            pageSize,
            page,
            DEFAULT_APIKEY);

        String resulted = query.getRequestQuery();

        assertEquals(expectedQueryStr, resulted,
            "Expected to return a valid collection of news headlines, but the returned headlines did not match.");
    }

}