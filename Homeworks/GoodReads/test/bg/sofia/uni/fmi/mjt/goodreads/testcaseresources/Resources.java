package bg.sofia.uni.fmi.mjt.goodreads.testcaseresources;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.io.StringReader;
import java.util.List;
import java.util.Set;

public class Resources {
    public static Book b1 = new Book("1", "To Kill a Mockingbird", "Harper Lee",
        "academy to superhero club a superhero",
        List.of("Fiction", "Classic", "Historical"), 4.8, 250000, "https://example.com/tokillamockingbird");
    public static Book b2 = new Book("2", "1984", "George Orwell",
        "superhero the mission to save club",
        List.of("Fiction", "Dystopian", "Political"), 4.7, 320000, "https://example.com/1984");
    public static Book b3 = new Book("3", "Pride and Prejudice", "Jane Austen",
        "crime a a murder to mystery a  club",
        List.of("Fiction", "Romance", "Classic"), 4.5, 150000, "https://example.com/prideandprejudice");
    public static Book complBook1 = new Book("1", "To Kill a Mockingbird", "Harper Lee",
        "A novel about the serious issues of rape and racial inequality.",
        List.of("Fiction", "Classic", "Historical"), 4.8, 250000, "https://example.com/tokillamockingbird");
    public static Book complBook2 = new Book("2", "1984", "George Orwell",
        "A dystopian novel set in a totalitarian society ruled by Big Brother.",
        List.of("Fiction", "Dystopian", "Political"), 4.7, 320000, "https://example.com/1984");
    public static Book complBook3 = new Book("3", "Pride and Prejudice", "Jane Austen",
        "A romantic novel that critiques the British landed gentry at the end of the 18th century.",
        List.of("Fiction", "Romance", "Classic"), 4.5, 150000, "https://example.com/prideandprejudice");
    public static Book complBook4 =
        new Book("4", "The Great Gatsby", "F. Scott Fitzgerald", "A critique of the American Dream in the 1920s.",
            List.of("Fiction", "Classic", "Drama"), 4.4, 100000, "https://example.com/gatsby");
    public static Book complBook5 = new Book("5", "Moby Dick", "Herman Melville",
        "A story of Captain Ahab’s obsession with hunting the white whale.",
        List.of("Fiction", "Adventure", "Classic"), 4.3, 85000, "https://example.com/mobydick");
    public static Book complBook6 = new Book("6", "War and Peace", "Leo Tolstoy",
        "A historical novel that intertwines the lives of several aristocratic families during the Napoleonic wars.",
        List.of("Fiction", "Historical", "Epic"), 4.6, 120000, "https://example.com/warandpeace");
    public static Book complBook7 = new Book("7", "The Catcher in the Rye", "J.D. Salinger",
        "The novel explores complex themes of teenage angst and alienation.",
        List.of("Fiction", "Coming-of-age", "Classic"), 4.2, 200000, "https://example.com/catcherintherye");
    public static Book complBook8 = new Book("8", "The Hobbit", "J.R.R. Tolkien",
        "A fantasy novel about a hobbit’s adventure to reclaim stolen treasure.",
        List.of("Fiction", "Fantasy", "Adventure"), 4.7, 350000, "https://example.com/thehobbit");
    public static Book complBook9 = new Book("9", "Animal Farm", "George Orwell",
        "A satirical novel",
        List.of("Dystopian", "Political"), 4.9, 300000, "https://example.com/animalfarm");
    public static Book complBook10 = new Book("10", "Brave New World", "Aldous Huxley",
        "A dystopian society where happiness is maintained through the use of technology.",
        List.of("Fiction", "Dystopian", "Science Fiction"), 4.4, 175000,
        "https://example.com/bravenewworld");

    public static String basicStopWordsText = "to" + System.lineSeparator() + "a" + System.lineSeparator() + "the";

    public static TextTokenizer basicTextTokenizer = new TextTokenizer(new StringReader(basicStopWordsText));

    public static Set<Book> basicBookSet = Set.of(b1, b2, b3);

    public static Set<Book> complexBookSet = Set.of(complBook1, complBook2, complBook3, complBook4, complBook5,
        complBook6, complBook7, complBook8, complBook9, complBook10);
}