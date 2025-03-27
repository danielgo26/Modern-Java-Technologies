package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.category.DLC;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.category.Game;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.category.GameBundle;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.category.Item;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args){
        //items
        LocalDateTime dt =  LocalDateTime.now();
        StoreItem item1 = new Game("Avatar", new BigDecimal(1000.12), LocalDateTime.now().minusMonths(4), "action");
        StoreItem item2 = new Game("Minecraft", new BigDecimal(99.55), LocalDateTime.now().minusYears(2), "adventure");
        StoreItem item3 = new DLC("My DLC", new BigDecimal(24.65), dt, (Game)item1);
        Game[] games = new Game[]{(Game)item1, (Game)item2};
        StoreItem item4 = new GameBundle("Bundle", new BigDecimal(12.55), LocalDateTime.now().minusHours(11).minusYears(3), games);
        StoreItem[] items = new StoreItem[]{item1, item2, item3, item4};

        //store
        GameStore store = new GameStore(items);

        store.applyDiscount("VAN40");

        store.applyDiscount("VAN40");

        //filters
        ItemFilter filter1 = new PriceItemFilter(new BigDecimal(24.65), new BigDecimal(101));
        ItemFilter filter2 = new RatingItemFilter(3);
        ItemFilter filter3 = new ReleaseDateItemFilter(LocalDateTime.now().minusYears(2), LocalDateTime.now().plusHours(2));
        ItemFilter filter4 = new TitleItemFilter("d", false);
        ItemFilter[] filters = new ItemFilter[]{filter4, filter3};

        store.rateItem(item2,4);
        StoreItem[] rest = store.findItemByFilters(filters);

        for (StoreItem item : rest){
            System.out.println(item.getTitle());
        }

    }
}