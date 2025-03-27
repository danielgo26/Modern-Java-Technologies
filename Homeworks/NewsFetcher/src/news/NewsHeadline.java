package news;

public record NewsHeadline(Source source, String author, String title, String description,
                            String url, String urlToImage, String publishedAt, String content) {
}