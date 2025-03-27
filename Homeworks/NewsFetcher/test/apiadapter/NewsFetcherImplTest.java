package apiadapter;

import apiadapter.response.ErrorResponse;
import apiadapter.response.SuccessResponse;
import com.google.gson.Gson;
import exception.status.APIStatusException;
import exception.status.BadRequestException;
import exception.status.ServerErrorException;
import exception.status.TooManyRequestException;
import exception.status.UnauthorizedException;
import exception.status.UnsupportedException;
import httprequest.HttpRequestQuery;
import news.NewsHeadline;
import news.Source;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class NewsFetcherImplTest {

    private static final int OK_STATUS_CODE = 200;
    private static final int BAD_REQUEST_STATUS_CODE = 400;
    private static final int UNAUTHORIZED_STATUS_CODE = 401;
    private static final int TOO_MANY_REQUEST_STATUS_CODE = 429;
    private static final int SERVER_ERROR_STATUS_CODE = 500;
    private static final int UPGRADE_REQUIRED_STATUS_CODE = 426;

    private static List<NewsHeadline> newsHeadlines;

    private static NewsFetcherImpl newsFetcher;
    private static Gson jsonConverter;

    @BeforeAll
    static void setup() {
        newsHeadlines = List.of(
            new NewsHeadline(new Source("1", "a1"), "author1", "title1",
                "desc1", "url1", "urlImage1", "date1", "content1"),
            new NewsHeadline(new Source("2", "a2"), "author2", "title2",
                "desc2", "url2", "urlImage2", "date2", "content2"));

        jsonConverter = new Gson();
        newsFetcher = new NewsFetcherImpl();
    }

    @Test
    void testFetchThrowsWhenGivenNullHttpRequestQuery() {
        assertThrows(IllegalArgumentException.class, () -> newsFetcher.fetch(null),
            "Expected an IllegalArgumentException to be thrown when the given HttpRequestQuery is null.");
    }

    @Test
    void testFetchReturnsValidCollectionWhenResponseStatusIsOK() {
        SuccessResponse successResponse = new SuccessResponse("s1", newsHeadlines.size(), newsHeadlines);
        String successResponseJson = jsonConverter.toJson(successResponse);

        try {
            List<NewsHeadline> returnedNewsHeadlines = mockRestAPIResponse(successResponseJson, OK_STATUS_CODE);

            assertIterableEquals(newsHeadlines, returnedNewsHeadlines,
                "Expected a valid collection of news headlines to be returned, but the returned headlines did not match.");
        } catch (Exception e) {
            fail("Expected to not throw, but an exception was thrown: " + e.getMessage());
        }
    }

    @Test
    void testFetchThrowsTheCorrespondingExceptionDependingOnStatusCode() {
        Map<Integer, APIStatusException> expectedStatusCodeExceptionPairs = Map.of(
            BAD_REQUEST_STATUS_CODE, new BadRequestException(""),
            UNAUTHORIZED_STATUS_CODE, new UnauthorizedException(""),
            TOO_MANY_REQUEST_STATUS_CODE, new TooManyRequestException(""),
            SERVER_ERROR_STATUS_CODE, new ServerErrorException(""),
            UPGRADE_REQUIRED_STATUS_CODE, new UnsupportedException("")
        );

        for (var entry : expectedStatusCodeExceptionPairs.entrySet()) {
            ErrorResponse errorResponse = new ErrorResponse("", String.valueOf(entry.getKey()), "");
            String errorResponseJson = jsonConverter.toJson(errorResponse);

            assertThrows(entry.getValue().getClass(), () -> mockRestAPIResponse(errorResponseJson, entry.getKey()),
                "Expected the corresponding exception to be thrown when the response status code is not OK.");
        }

    }

    @Test
    void testGetAllNewsHeadlinesByKeywordsThrowsExceptionWhenInvalidDataIsGiven() {
        assertThrows(IllegalArgumentException.class, () ->
            newsFetcher.getAllNewsHeadlinesByKeywords(null, List.of("a")),
            "Expected an IllegalArgumentException to be thrown when the api key is null.");

        List<String> keywords = new ArrayList<>();
        keywords.add(null);
        assertThrows(IllegalArgumentException.class, () ->
            newsFetcher.getAllNewsHeadlinesByKeywords("apiKey", keywords),
            "Expected an IllegalArgumentException to be thrown when the given keywords are invalid.");
    }

    @Test
    void testGetAllNewsHeadlinesByKeywordsReturnsValidCollectionWhenGivenValidKeyAndKeywords() {
        NewsFetcher newsFetcherSpy = spy(new NewsFetcherImpl());

        try {
            doReturn(newsHeadlines).when(newsFetcherSpy).fetch(any(HttpRequestQuery.class));

            List<NewsHeadline> returnedNewsHeadlines =
                newsFetcherSpy.getAllNewsHeadlinesByKeywords("apiKey", List.of("word"));

            assertIterableEquals(newsHeadlines, returnedNewsHeadlines,
                "Expected a valid collection of news headlines to be returned, but the returned headlines did not match.");
        } catch (Exception e) {
            fail("Expected not to throw exception, but one was thrown: " + e.getMessage());
        }
    }

    private List<NewsHeadline> mockRestAPIResponse(String jsonResponse, int responseCode)
        throws IOException, InterruptedException, APIStatusException {
        HttpRequestQuery mockedQuery = mock();

        HttpRequest.Builder mockBuilder = mock();
        HttpClient.Builder mockedClientBuilder = mock();

        HttpRequest mockedRequest = mock();
        HttpResponse<String> mockedResponse = mock();
        when(mockedResponse.statusCode()).thenReturn(responseCode);
        when(mockedResponse.body()).thenReturn(jsonResponse);

        try (MockedStatic<HttpRequest> mockedRequestStatic = mockStatic(HttpRequest.class)) {
            when(HttpRequest.newBuilder()).thenReturn(mockBuilder);
            when(mockBuilder.uri(any())).thenReturn(mockBuilder);
            when(mockBuilder.build()).thenReturn(mockedRequest);

            try (MockedStatic<HttpClient> mockedClientStatic = mockStatic(HttpClient.class)) {
                when(HttpClient.newBuilder()).thenReturn(mockedClientBuilder);

                try (HttpClient mockedClient = mock()) {
                    when(mockedClientBuilder.build()).thenReturn(mockedClient);
                    when(mockedClient.send(mockedRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockedResponse);

                    return newsFetcher.fetch(mockedQuery);
                }
            }
        }
    }

}