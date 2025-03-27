import apiadapter.NewsFetcherImpl;
import httprequest.get.HttpGetRequestQuery;
import httprequest.HttpRequestQuery;
import news.NewsHeadline;

import java.util.List;

public class Main {

    private static final String API_KEY = "480e29e2990848aba062c35ce805fb41";
    private static final List<String> KEYWORDS = List.of("trump");

    public static void main(String[] args) throws Exception {

        NewsFetcherImpl fetcher = new NewsFetcherImpl();


        List<NewsHeadline> headlines = fetcher.getAllNewsHeadlinesByKeywords(API_KEY, KEYWORDS);
        for (var a : headlines) {
            System.out.println(a.title());
        }

//        HttpRequestQuery query = HttpGetRequestQuery
//            .build(API_KEY, KEYWORDS)
//            .setPageSize(100)
//            .setPage(1)
//            .build();
//
//        List<NewsHeadline> headlines = fetcher.fetch(query);
//
//        for (var a : headlines) {
//            System.out.println(a.title());
//        }
//
//        HttpRequestQuery query2 = HttpGetRequestQuery
//            .build(API_KEY, KEYWORDS)
//            .setPageSize(100)
//            .setPage(2)
//            .build();
//
//        List<NewsHeadline> headlines2 = fetcher.fetch(query2);
//        System.out.println("-----------------------------");
//        for (var a : headlines2) {
//            System.out.println(a.title());
//        }
//
//        HttpRequestQuery query3 = HttpGetRequestQuery
//            .build(API_KEY, KEYWORDS)
//            .setPageSize(5)
//            .setPage(3)
//            .build();
//
//        List<NewsHeadline> headlines3 = fetcher.fetch(query3);
//        System.out.println("-----------------------------");
//        for (var a : headlines3) {
//            System.out.println(a.title());
//        }

    }

}