package apiadapter;

import exception.status.APIStatusException;
import httprequest.HttpRequestQuery;
import news.NewsHeadline;

import java.io.IOException;
import java.util.List;

//Note: for more implementation details reasoning see the ReadMe file
public interface NewsFetcher {

    List<NewsHeadline> fetch(HttpRequestQuery query)
        throws IOException, InterruptedException, APIStatusException;

    List<NewsHeadline> getAllNewsHeadlinesByKeywords(String apiKey, List<String> keywords)
        throws IOException, InterruptedException, APIStatusException;

}
