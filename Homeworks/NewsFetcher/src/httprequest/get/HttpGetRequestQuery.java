package httprequest.get;

import exception.syntax.APIRequestSyntaxException;
import httprequest.get.utilitystructures.Category;
import httprequest.get.utilitystructures.Country;
import httprequest.HttpRequestQuery;
import requestbuilder.get.HttpGetRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static validator.object.ObjectValidator.validateCollection;
import static validator.object.ObjectValidator.validateNotNull;

public class HttpGetRequestQuery implements HttpRequestQuery {

    public static final int MIN_PAGE_SIZE = 0;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MIN_PAGES = 0;

    //the required parameters
    private final String apiKey;
    private List<String> keywords;

    //the optional parameters
    private Country country;
    private Category category;
    private Integer pageSize;
    private Integer page;

    public HttpGetRequestQuery(String apiKey, List<String> keywords) {
        validateNotNull(apiKey, "The given api key is null!");
        validateCollection(keywords, "keywords");

        this.apiKey = apiKey;
        this.keywords = keywords;
    }

    public static HttpGetRequestBuilder build(String apiKey, List<String> keywords) {
        validateNotNull(apiKey, "The given api key is null!");
        validateCollection(keywords, "keywords");

        return new HttpGetRequestBuilder(apiKey, keywords);
    }

    public String getRequestQuery() throws APIRequestSyntaxException {
        String countryQuery = country == null ? "" : "country=" + Country.getISOCodeString(country);
        String categoryQuery = category == null ? "" : "category=" + Category.getCategoryString(category);
        String keywordsQuery = "q=" + String.join(" ", keywords);
        int pageResultsCount = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
        String pageSizeQuery = "pageSize=" + pageResultsCount;
        String pageQuery = page == null ? "" : "page=" + page;
        String apiKeyQuery = "apiKey=" + apiKey;

        List<String> validatedQueryString = removeEmptyElements(
            List.of(countryQuery, categoryQuery, keywordsQuery, pageSizeQuery, pageQuery, apiKeyQuery));

        return String.join("&", validatedQueryString);
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        validateCollection(keywords, "keywords");

        this.keywords = keywords;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    private List<String> removeEmptyElements(List<String> entities) {
        List<String> notEmptyValues = new ArrayList<>();
        for (String curr : entities) {
            if (!curr.isEmpty()) {
                notEmptyValues.add(curr);
            }
        }

        return notEmptyValues;
    }

}