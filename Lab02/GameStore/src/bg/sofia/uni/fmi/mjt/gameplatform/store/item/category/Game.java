package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Game extends Item{
    private String genre;

    public Game(String title, BigDecimal price, LocalDateTime releaseDate, String genre){
        super(title, price, releaseDate);
        this.setGenre(genre);
    }

    public String getGenre(){
        return this.genre;
    }

    public void setGenre(String genre){
        if (genre != null && !genre.isEmpty()) {
            this.genre = genre;
        }
        else{
            this.genre = "unknown";
        }
    }
}