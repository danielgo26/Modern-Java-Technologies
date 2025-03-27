package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

public class Main {
    @SuppressWarnings("checkstyle:MagicNumber")
    public static void main(String[] args) {

        char[][] layout3 = {
            {'#', '#', '#', '.', '#'},
            {'#', '.', 'B', 'R', '.', 'R', '#'},
            {'.', '.', '#', '.', '.', '#'},
            {'#', 'C', '.', 'A', '.'},
            {'#', '.', '#', '#', '#'}
        };

        Glovo glovo3 = new Glovo(layout3);
        MapEntity client3 = new MapEntity(new Location(3,1), MapEntityType.CLIENT);
        MapEntity restaurant3 = new MapEntity(new Location(1,5), MapEntityType.RESTAURANT);
        Delivery fastest3;
        Delivery cheapest3;
        Delivery fastestUnderPrise3;
        Delivery cheapestUnderTime3;

        try {

            fastest3 = glovo3.getFastestDelivery(client3, restaurant3, "apple");
            cheapest3 = glovo3.getCheapestDelivery(client3, restaurant3, "Tonik");
            fastestUnderPrise3 = glovo3.getFastestDeliveryUnderPrice(client3, restaurant3, "Jin", 30);
            cheapestUnderTime3 = glovo3.getCheapestDeliveryWithinTimeLimit(client3, restaurant3, "Morena", 40);
        } catch (NoAvailableDeliveryGuyException e) {
            System.out.println(e.toString());
            return;
        }

        char[][] layout2 = {
            {'.', '.', '#', '.', '.', '.', '.', '#', '.', '.'},
            {'.', '.', 'R', '#', '.', '.', '.', 'C', '#', '.'},
            {'.', '.', '#', '.', '#', '.', '.', '#', '.', '.'},
            {'.', '.', '.', '#', 'A', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'}
        };

        try {
            Glovo glovo2 = new Glovo(layout2);
            MapEntity client2 = new MapEntity(new Location(1,7), MapEntityType.CLIENT);
            MapEntity restaurant2 = new MapEntity(new Location(1,2), MapEntityType.RESTAURANT);
            Delivery fastest2;
            Delivery cheapest2;
            Delivery fastestUnderPrise2;
            Delivery cheapestUnderTime2;
            fastest2 = glovo2.getFastestDelivery(client2, restaurant2, "apple");
            cheapest2 = glovo2.getCheapestDelivery(client2, restaurant2, "Tonik");
            fastestUnderPrise2 = glovo2.getFastestDeliveryUnderPrice(client2, restaurant2, "Jin", 20);
            cheapestUnderTime2 = glovo2.getCheapestDeliveryWithinTimeLimit(client2, restaurant2, "Morena", 20);
        } catch (NoAvailableDeliveryGuyException e) {
            System.out.println(e.toString());
            return;
        }

        char[][] layout = {
            {'#', '#', '#', '.', '#'},
            {'#', '.', 'B', 'R', '.'},
            {'.', '.', '#', 'A', '#'},
            {'#', '.', '.', 'A', 'C'},
            {'#', '.', '#', '#', '#'}
        };

        //first layout

        try {
            Glovo glovo = new Glovo(layout);
            MapEntity client = new MapEntity(new Location(3,4), MapEntityType.CLIENT);
            MapEntity restaurant = new MapEntity(new Location(1,3), MapEntityType.RESTAURANT);
            Delivery fastest;
            Delivery cheapest;
            Delivery fastestUnderPrise;
            Delivery cheapestUnderTime;
            fastest = glovo.getFastestDelivery(client, restaurant, "apple");
            cheapest = glovo.getCheapestDelivery(client, restaurant, "Tonik");
            fastestUnderPrise = glovo.getFastestDeliveryUnderPrice(client, restaurant, "Jin", 20);
            cheapestUnderTime = glovo.getCheapestDeliveryWithinTimeLimit(client, restaurant, "Morena", 20);
        } catch (NoAvailableDeliveryGuyException e) {
            System.out.println(e.getStackTrace());
            return;
        }
        int a = 1;
    }
}