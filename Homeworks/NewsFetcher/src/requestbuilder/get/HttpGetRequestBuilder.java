package requestbuilder.get;

import httprequest.get.HttpGetRequestQuery;
import httprequest.get.utilitystructures.Category;
import httprequest.get.utilitystructures.Country;
import requestbuilder.HttpRequestBuilderAPI;
import requestbuilder.get.utililystructures.KeywordsAddingOption;

import java.util.ArrayList;
import java.util.List;

import static validator.object.ObjectValidator.validateCollection;
import static validator.object.ObjectValidator.validateNotNull;
import static validator.object.ObjectValidator.validateRequestEntityGreaterThan;
import static validator.object.ObjectValidator.validateRequestEntityWithinInterval;

public class HttpGetRequestBuilder implements HttpRequestBuilderAPI {

    private final HttpGetRequestQuery query;

    public HttpGetRequestBuilder(String apiKey, List<String> keywords) {
        validateNotNull(apiKey, "The given api key is null!");
        validateCollection(keywords, "keywords");

        this.query = new HttpGetRequestQuery(apiKey, keywords);
    }

    public HttpGetRequestBuilder filterByKeywords(List<String> keywords, KeywordsAddingOption addingOption) {
        validateCollection(keywords, "keywords");
        validateNotNull(addingOption, "The adding option is null!");

        List<String> newKeywordsSet = switch (addingOption) {
            case KeywordsAddingOption.Replace -> keywords;
            case KeywordsAddingOption.Append -> {
                List<String> current = new ArrayList<>(query.getKeywords());
                current.addAll(keywords);
                yield current;
            }
            default -> keywords;
        };

        query.setKeywords(newKeywordsSet);
        return this;
    }

    public HttpGetRequestBuilder filterByCountry(Country country) {
        query.setCountry(country);
        return this;
    }

    public HttpGetRequestBuilder filterByCategory(Category category) {
        query.setCategory(category);
        return this;
    }

    public HttpGetRequestBuilder setPageSize(int count) {
        int min = HttpGetRequestQuery.MIN_PAGE_SIZE;
        int max = HttpGetRequestQuery.MAX_PAGE_SIZE;

        validateRequestEntityWithinInterval(count, min, max,
            String.format("The given page size must be within: %d and %d!", min, max));

        query.setPageSize(count);
        return this;
    }

    public HttpGetRequestBuilder setPage(int numberOfPage) {
        int min = HttpGetRequestQuery.MIN_PAGES;

        validateRequestEntityGreaterThan(numberOfPage, min,
            String.format("The given page index must be greater: %d!", min));

        query.setPage(numberOfPage);
        return this;
    }

    @Override
    public HttpGetRequestQuery build() {
        return this.query;
    }

}