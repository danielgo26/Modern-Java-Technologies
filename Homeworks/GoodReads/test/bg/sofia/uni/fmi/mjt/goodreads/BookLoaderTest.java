package bg.sofia.uni.fmi.mjt.goodreads;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.b1;
import static bg.sofia.uni.fmi.mjt.goodreads.testcaseresources.Resources.b2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BookLoaderTest {

    @Test
    void testShouldValidCollectionOfLoadedBooksFromReader() {
        String toLoad ="N,Book,Author,Description,Genres,Avg_Rating,Num_Ratings,URL" +
            System.lineSeparator() +
            "1,To Kill a Mockingbird,Harper Lee,academy to superhero club a superhero," +
            "\"['Fiction', 'Classic', 'Historical']\",4.8,250000,https://example.com/tokillamockingbird" +
            System.lineSeparator() +
        "2,1984,George Orwell,superhero the mission to save club,\"['Fiction', 'Dystopian', 'Political']\"," +
            "4.7,320000,https://example.com/1984";

        Set<Book> resulted = BookLoader.load(new StringReader(toLoad));

        assertEquals(2, resulted.size(),
            "Expected to return set with size 2, but returned with size: " + resulted);
        assertTrue(resulted.contains(b1),
            "Expected resulted set to contains: " + b1 + ", but does not");
        assertTrue(resulted.contains(b2),
            "Expected resulted set to contains: " + b2 + ", but does not");

    }

}