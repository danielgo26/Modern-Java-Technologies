package apiadapter;

import apiadapter.response.SuccessResponse;
import exception.status.APIStatusException;
import exception.syntax.APIRequestSyntaxException;
import httprequest.HttpRequestQuery;
import httprequest.get.HttpGetRequestQuery;
import news.NewsHeadline;
import validator.statuscode.ResponseStatusCodeValidator;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static validator.object.ObjectValidator.validateCollection;
import static validator.object.ObjectValidator.validateNotNull;

public class NewsFetcherImpl implements NewsFetcher {

    private static final String API_ENDPOINT_SCHEME = "https";
    private static final String API_ENDPOINT_HOST = "newsapi.org";
    private static final String API_ENDPOINT_PATH = "/v2/top-headlines";

    public NewsFetcherImpl() { }

    @Override
    public List<NewsHeadline> fetch(HttpRequestQuery query)
        throws IOException, InterruptedException, APIStatusException {

        validateNotNull(query, "The given HTTP query to fetch is null!");

        try (HttpClient client = HttpClient.newBuilder().build()) {
            URI uri = new URI(API_ENDPOINT_SCHEME, API_ENDPOINT_HOST, API_ENDPOINT_PATH, query.getRequestQuery(), null);

            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ResponseStatusCodeValidator.validateStatusCode(response);

            return SuccessResponse.of(response.body()).articles();
        } catch (URISyntaxException e) {
            throw new APIRequestSyntaxException("The given URI is invalid: ", e);
        }
    }

    @Override
    public List<NewsHeadline> getAllNewsHeadlinesByKeywords(String apiKey, List<String> keywords)
        throws IOException, InterruptedException, APIStatusException {

        validateNotNull(apiKey, "The given api key is null!");
        validateCollection(keywords, "keywords");

        int currPage = 1;
        List<NewsHeadline> allNewsHeadlines = new ArrayList<>();

        while (true) {
            HttpRequestQuery getCurrPageRecords = HttpGetRequestQuery
                .build(apiKey, keywords)
                .setPageSize(HttpGetRequestQuery.MAX_PAGE_SIZE)
                .setPage(currPage++)
                .build();

            List<NewsHeadline> currentNewsHeadlines = this.fetch(getCurrPageRecords);
            allNewsHeadlines.addAll(currentNewsHeadlines);

            if (currentNewsHeadlines.size() < HttpGetRequestQuery.MAX_PAGE_SIZE) {
                break;
            }
        }

        return allNewsHeadlines;
    }

}