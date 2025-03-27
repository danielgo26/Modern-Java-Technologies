package bg.sofia.uni.fmi.mjt.glovo;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenter;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.ControlCenterApi;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.Delivery;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidMapEntityException;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidOrderException;
import bg.sofia.uni.fmi.mjt.glovo.exception.NoAvailableDeliveryGuyException;

import java.util.Set;

public class Glovo implements GlovoApi {

    private final ControlCenterApi controlCenter;

    public Glovo(char[][] mapLayout) {
        try {
            controlCenter = new ControlCenter(mapLayout);
        } catch (InvalidMapEntityException e) {
            throw new IllegalArgumentException("The given control center api cannot resolve current map layout", e);
        }
    }

    @Override
    public Delivery getCheapestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
        throws NoAvailableDeliveryGuyException {
        validateMapEntities(client, restaurant);
        validateFoodItem(foodItem);

        return finaliseDelivery(client, restaurant, foodItem, -1, -1, ShippingMethod.CHEAPEST);
    }

    @Override
    public Delivery getFastestDelivery(MapEntity client, MapEntity restaurant, String foodItem)
        throws NoAvailableDeliveryGuyException {
        validateMapEntities(client, restaurant);
        validateFoodItem(foodItem);

        return finaliseDelivery(client, restaurant, foodItem, -1, -1, ShippingMethod.FASTEST);
    }

    @Override
    public Delivery getFastestDeliveryUnderPrice(MapEntity client, MapEntity restaurant, String foodItem,
                                                 double maxPrice) throws NoAvailableDeliveryGuyException {
        validateMapEntities(client, restaurant);
        validateFoodItem(foodItem);

        return finaliseDelivery(client, restaurant, foodItem, maxPrice, -1, ShippingMethod.FASTEST);
    }

    @Override
    public Delivery getCheapestDeliveryWithinTimeLimit(MapEntity client, MapEntity restaurant, String foodItem,
                                                       int maxTime) throws NoAvailableDeliveryGuyException {
        validateMapEntities(client, restaurant);
        validateFoodItem(foodItem);

        return finaliseDelivery(client, restaurant, foodItem, -1, maxTime, ShippingMethod.CHEAPEST);
    }

    private void validateMapEntities(MapEntity client, MapEntity restaurant) {
        validateEntity(client, Set.of(MapEntityType.CLIENT));
        validateEntity(restaurant, Set.of(MapEntityType.RESTAURANT));
    }

    private void validateEntity(MapEntity entity, Set<MapEntityType> allowedEntityValues) {
        if (entity == null || entity.type() == null || entity.location() == null) {
            throw new NullPointerException("Given map entity is null or contains a null sub entity");
        }
        if (!allowedEntityValues.contains(entity.type())) {
            throw new IllegalArgumentException(
                String.format("The given map entity: %s cannot be used for the particular role", entity.type().name()));
        }

        verifyLocation(entity.location());
        verifyExistsAtLocation(entity, entity.location());
    }

    private void verifyLocation(Location location) {
        if (location == null) {
            throw new NullPointerException("Given location is null");
        }
        if (!Location.isLocationInMatrix(location, controlCenter.getLayout())) {
            throw new InvalidOrderException(String.format(
                "Map entity with coordinates x: %d , y: %d is outside the map boundaries", location.x(), location.y()));
        }
    }

    private void verifyExistsAtLocation(MapEntity entity, Location location) {
        if (!controlCenter.getLayout()[location.x()][location.y()].equals(entity)) {
            throw new InvalidOrderException(String.format(
                "Map entity with coordinates x: %d , y: %d does not exist at the given location",
                location.x(), location.y()));
        }
    }

    private void validateFoodItem(String foodItem) {
        if (foodItem == null) {
            throw new NullPointerException("The given food item is null");
        }
    }

    private Delivery finaliseDelivery(MapEntity client, MapEntity restaurant, String foodItem,
                                      double maxPrice, int maxTime, ShippingMethod shippingMethod)
        throws NoAvailableDeliveryGuyException {

        DeliveryInfo deliveryInfo = getDeliveryInfo(client, restaurant, maxPrice, maxTime, shippingMethod);

        return new Delivery(client.location(), restaurant.location(), deliveryInfo.deliveryGuyLocation(),
            foodItem, deliveryInfo.price(), deliveryInfo.estimatedTime());
    }

    private DeliveryInfo getDeliveryInfo(MapEntity client, MapEntity restaurant, double maxPrice, int maxTime,
                                         ShippingMethod shippingMethod) throws NoAvailableDeliveryGuyException {
        DeliveryInfo deliveryInfo = controlCenter.findOptimalDeliveryGuy(restaurant.location(),
            client.location(), maxPrice, maxTime, shippingMethod);

        if (deliveryInfo == null) {
            throw new NoAvailableDeliveryGuyException(
                "Could not find a suitable delivery guy for the current delivery task");
        }

        return deliveryInfo;
    }

}