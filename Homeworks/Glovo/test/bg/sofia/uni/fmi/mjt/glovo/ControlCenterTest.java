package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenter;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControlCenterTest {
    char[][] simpleLayout = {
        {'C', '#', '#', '.', '#', '#'},
        {'#', '.', 'B', 'R', '.', '#'},
        {'.', '.', '#', '.', '#', '#'},
        {'#', 'C', '.', 'A', '#', '#'},
        {'#', '.', '#', '#', 'R', '#'}
    };

    char[][] complexLayout = {
        {'.', '.', '#', '.', '#'},
        {'#', '.', 'R', 'A', '.'},
        {'.', '.', '#', '.', '#'},
        {'#', 'C', '.', '.', '.'},
        {'#', '.', '#', '#', 'B'}
    };

    MapEntity accessibleClient = new MapEntity(new Location(3, 1), MapEntityType.CLIENT);
    MapEntity accessibleRestaurant = new MapEntity(new Location(1, 3), MapEntityType.RESTAURANT);

    @Test
    void findCheapestDeliveryGuyNoTimeAndPriceConstraints() {
        ControlCenter controlCenter = new ControlCenter(simpleLayout);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            accessibleRestaurant.location(), accessibleClient.location(), -1, -1, ShippingMethod.CHEAPEST
        );

        assertNotNull(deliveryInfo, "DeliveryInfo is null");
        assertEquals(15, deliveryInfo.price(), "Price isn't valid");
        assertEquals(25,deliveryInfo.estimatedTime(),  "Estimated time isn't valid");
        assertEquals(DeliveryType.BIKE, deliveryInfo.deliveryType());
        assertEquals(new Location(1, 2), deliveryInfo.deliveryGuyLocation());
    }

    @Test
    void findFastestDeliveryGuyNoTimeAndPriceConstraints() {
        ControlCenter controlCenter = new ControlCenter(simpleLayout);
        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            accessibleRestaurant.location(), accessibleClient.location(), -1, -1, ShippingMethod.FASTEST
        );

        assertNotNull(deliveryInfo, "DeliveryInfo is null");
        assertEquals(30, deliveryInfo.price(), "Price isn't valid");
        assertEquals(18, deliveryInfo.estimatedTime(), "Estimated time isn't valid");
        assertEquals(DeliveryType.CAR, deliveryInfo.deliveryType());
        assertEquals(new Location(3, 3), deliveryInfo.deliveryGuyLocation());
    }

    @Test
    void findFastestDeliveryGuyWithMoneyConstraint() {
        ControlCenter controlCenter = new ControlCenter(simpleLayout);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            accessibleRestaurant.location(), accessibleClient.location(), 25, -1, ShippingMethod.FASTEST
        );

        assertNotNull(deliveryInfo, "DeliveryInfo is null");
        assertEquals(15, deliveryInfo.price(), "Price isn't valid");
        assertEquals(25,deliveryInfo.estimatedTime(),  "Estimated time isn't valid");
        assertEquals(DeliveryType.BIKE, deliveryInfo.deliveryType());
        assertEquals(new Location(1, 2), deliveryInfo.deliveryGuyLocation());
    }

    @Test
    void findCheapestDeliveryGuyWithTimeConstraint() {
        ControlCenter controlCenter = new ControlCenter(simpleLayout);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            accessibleRestaurant.location(), accessibleClient.location(), -1, 20, ShippingMethod.CHEAPEST
        );

        assertNotNull(deliveryInfo, "DeliveryInfo is null");
        assertEquals(30, deliveryInfo.price(), "Price isn't valid");
        assertEquals(18, deliveryInfo.estimatedTime(), "Estimated time isn't valid");
        assertEquals(DeliveryType.CAR, deliveryInfo.deliveryType());
        assertEquals(new Location(3, 3), deliveryInfo.deliveryGuyLocation());
    }

    @Test
    void noAccessToClient() {
        ControlCenter controlCenter = new ControlCenter(simpleLayout);
        MapEntity client = new MapEntity(new Location(0, 0), MapEntityType.CLIENT);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            accessibleRestaurant.location(), client.location(), -1, -1, ShippingMethod.CHEAPEST
        );

        assertNull(deliveryInfo, "Delivery info should be null!");
    }

    @Test
    void noAccessRestaurant() {
        ControlCenter controlCenter = new ControlCenter(simpleLayout);
        MapEntity restaurant = new MapEntity(new Location(4, 4), MapEntityType.RESTAURANT);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            restaurant.location(), accessibleClient.location(), -1, -1, ShippingMethod.CHEAPEST
        );

        assertNull(deliveryInfo, "Delivery info should be null!");
    }

    @Test
    void findCheapestNoPriceConstraintFulfilled() {
        ControlCenter controlCenter = new ControlCenter(simpleLayout);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            accessibleRestaurant.location(), accessibleClient.location(), 10, -1, ShippingMethod.CHEAPEST
        );

        assertNull(deliveryInfo, "Delivery info should be null!");
    }

    @Test
    void findCheapestNoTimeConstraintFulfilled() {
        ControlCenter controlCenter = new ControlCenter(simpleLayout);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            accessibleRestaurant.location(), accessibleClient.location(), -1, 14, ShippingMethod.CHEAPEST
        );

        assertNull(deliveryInfo, "Delivery info should be null!");
    }

    @Test
    void findCheapestWithTimeConstraint_GetSecondOption() {
        ControlCenter controlCenter = new ControlCenter(simpleLayout);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            accessibleRestaurant.location(), accessibleClient.location(), -1, 18, ShippingMethod.CHEAPEST
        );

        assertNotNull(deliveryInfo, "Delivery info should get the second cheapest because of the time constraint");
        assertEquals(30, deliveryInfo.price(), "Price isn't valid");
        assertEquals(18, deliveryInfo.estimatedTime(), "Estimated time isn't valid");
        assertEquals(DeliveryType.CAR, deliveryInfo.deliveryType());
        assertEquals(new Location(3, 3), deliveryInfo.deliveryGuyLocation());
    }

    @Test
    void findCheapestWithPriceAndTimeConstraintsNotFulfilled() {
        ControlCenter controlCenter = new ControlCenter(simpleLayout);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            accessibleRestaurant.location(), accessibleClient.location(), 20, 18, ShippingMethod.CHEAPEST
        );

        assertNull(deliveryInfo, "Delivery info should be null!");
    }

    @Test
    void findCheapestWithPriceAndTimeConstraints() {
        ControlCenter controlCenter = new ControlCenter(simpleLayout);

        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            accessibleRestaurant.location(), accessibleClient.location(), 30, 20, ShippingMethod.FASTEST
        );

        assertNotNull(deliveryInfo, "Delivery info should be null!");
    }

    @Test
    void findCheapestDeliveryWhenCarIsCheaper() {
        ControlCenter controlCenter = new ControlCenter(complexLayout);
        MapEntity restaurant = new MapEntity(new Location(1, 2), MapEntityType.RESTAURANT);
        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(
            restaurant.location(), accessibleClient.location(), -1, -1, ShippingMethod.CHEAPEST
        );

        assertNotNull(deliveryInfo, "DeliveryInfo is null");
        assertEquals(20, deliveryInfo.price(), "Price isn't valid");
        assertEquals(12, deliveryInfo.estimatedTime(), "Estimated time isn't valid");
        assertEquals(DeliveryType.CAR, deliveryInfo.deliveryType());
        assertEquals(new Location(1, 3), deliveryInfo.deliveryGuyLocation());
    }

}