package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GameBundle extends Item {
    Game[] games;

    public GameBundle(String title, BigDecimal price, LocalDateTime releaseDate, Game[] games) {
        super(title, price, releaseDate);
        this.setGames(games);
    }

    public Game[] getGames() {
        return this.games;
    }

    public void setGames(Game[] games) {
        //ToDo: check if null, then throw
        this.games = games;
    }
}