package bg.sofia.uni.fmi.mjt.glovo.controlcenter;

import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.Location;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntity;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfo;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryInfoComparator;
import bg.sofia.uni.fmi.mjt.glovo.delivery.DeliveryType;
import bg.sofia.uni.fmi.mjt.glovo.delivery.ShippingMethod;
import bg.sofia.uni.fmi.mjt.glovo.controlcenter.map.MapEntityType;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidLocationInMapLayout;
import bg.sofia.uni.fmi.mjt.glovo.exception.InvalidMapEntityException;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class ControlCenter implements ControlCenterApi {

    MapEntity[][] mapLayout;
    Map<Location, Integer> connectedComponentsInMap;
    static final int NEUTRAL_COMPONENT_INDEX = -1;
    static final Location NEUTRAL_LOCATION_IN_MAP = new Location(-1, -1);

    public ControlCenter(char[][] mapLayout) {
        setTransformedMapLayout(mapLayout);
        setConnectedComponentsInMap();
    }

    private void setTransformedMapLayout(char[][] mapLayout) {
        validateNotNullOrEmptyArray(mapLayout, "Given map layout is null or empty");
        this.mapLayout = new MapEntity[mapLayout.length][];

        for (int i = 0; i < mapLayout.length; i++) {
            validateNotNullOrEmptyArray(mapLayout[i], "Row number %n in the map layout is null or empty");
            this.mapLayout[i] = new MapEntity[mapLayout[i].length];

            for (int j = 0; j < mapLayout[i].length; j++) {
                addEntityToArrayAtPosition(this.mapLayout, new int[] {i, j}, mapLayout[i][j]);
            }
        }
    }

    private <T> void validateNotNullOrEmptyArray(T array, String errorMessage) {
        if (array == null || Array.getLength(array) == 0) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void addEntityToArrayAtPosition(MapEntity[][] matrix, int[] positionInMatrix, char value) {
        int row = positionInMatrix[0];
        int col = positionInMatrix[1];
        MapEntityType entityType;
        try {
            entityType = MapEntityType.fromChar(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidMapEntityException("Cannot add value to array", e);
        }
        matrix[row][col] = new MapEntity(new Location(row, col), entityType);
    }

    private void setConnectedComponentsInMap() {
        connectedComponentsInMap = new HashMap<>();
        for (MapEntity[] row : mapLayout) {
            for (MapEntity el : row) {
                if (MapEntityType.isValuable(el.type())) {
                    connectedComponentsInMap.put(el.location(), ControlCenter.NEUTRAL_COMPONENT_INDEX);
                }
            }
        }

        int indexOfComponent = 1;
        for (Location location : connectedComponentsInMap.keySet()) {
            if (connectedComponentsInMap.get(location) == ControlCenter.NEUTRAL_COMPONENT_INDEX) {
                fillConnectedComponentsFrom(location, indexOfComponent++, connectedComponentsInMap);
            }
        }
    }

    private void fillConnectedComponentsFrom(Location start, int indexOfComponent,
                                             Map<Location, Integer> connectedComponentsInMap) {
        Set<Location> traversedPositions = new HashSet<>();
        Queue<Location> toTraverse = new ArrayDeque<>(Collections.singletonList(start));

        while (!toTraverse.isEmpty()) {
            Location current = toTraverse.poll();
            traversedPositions.add(current);

            if (MapEntityType.isValuable(getEntityTypeByLocation(current))) {
                connectedComponentsInMap.put(current, indexOfComponent);
            }

            Set<Location> possibleLocations = generatePossibleLocationsNearBy(current);
            for (Location locationNearBy : possibleLocations) {
                if (!traversedPositions.contains(locationNearBy) &&
                    MapEntityType.isPassable(getEntityTypeByLocation(locationNearBy))) {
                    toTraverse.add(locationNearBy);
                }
            }
        }
    }

    private MapEntityType getEntityTypeByLocation(Location location) {
        return mapLayout[location.x()][location.y()].type();
    }

    private Set<Location> generatePossibleLocationsNearBy(Location location) {
        List<Location> possibleLocations = new ArrayList<>(Arrays.asList(
            new Location(location.x() - 1, location.y()),
            new Location(location.x() + 1, location.y()),
            new Location(location.x(), location.y() - 1),
            new Location(location.x(), location.y() + 1)));

        Set<Location> realLocations = new HashSet<>();
        for (Location current : possibleLocations) {
            if (Location.isLocationInMatrix(current, mapLayout)) {
                realLocations.add(current);
            }
        }

        return realLocations;
    }

    @Override
    public DeliveryInfo findOptimalDeliveryGuy(Location restaurantLocation, Location clientLocation, double maxPrice,
                                               int maxTime, ShippingMethod shippingMethod) {
        verifyValidLocations(Set.of(restaurantLocation, clientLocation));
        maxPrice = (maxPrice < 0 && maxPrice != -1) ? -1 : maxPrice;
        maxTime = Math.max(maxTime, -1);

        if (!verifyInOneConnectedComponent(new HashSet<>(Arrays.asList(restaurantLocation, clientLocation)))) {
            return null;
        }

        int kmFromRestaurantToClient = findKmFromRestaurantToClient(restaurantLocation, clientLocation);
        if (kmFromRestaurantToClient == -1) {
            return null;
        }

        return findOptimalDeliveryFrom(restaurantLocation,
            new DeliveryInfoComparator(shippingMethod), maxPrice, maxTime, kmFromRestaurantToClient);
    }

    private void verifyValidLocations(Set<Location> locations) {
        for (Location location : locations) {
            if (location == null) {
                throw new NullPointerException("Given location is null");
            }
            if (!Location.isLocationInMatrix(location, this.mapLayout)) {
                throw new InvalidLocationInMapLayout(
                    String.format("Map entity with coordinates x: %d , y: %d is outside the map boundaries",
                        location.x(), location.y()));
            }
        }
    }

    private boolean verifyInOneConnectedComponent(Set<Location> locations) {
        int indexOfFirstConnectedComponent = ControlCenter.NEUTRAL_COMPONENT_INDEX;

        for (Location location : locations) {
            if (indexOfFirstConnectedComponent == ControlCenter.NEUTRAL_COMPONENT_INDEX) {
                indexOfFirstConnectedComponent = connectedComponentsInMap.get(location);
                continue;
            }

            if (indexOfFirstConnectedComponent != connectedComponentsInMap.get(location)) {
                return false;
            }
        }

        return true;
    }

    private int findKmFromRestaurantToClient(Location restaurant, Location client) {
        Set<Location> traversedPositions = new HashSet<>();
        Queue<Location> toTraverse = new ArrayDeque<>(Arrays.asList(restaurant, NEUTRAL_LOCATION_IN_MAP));
        int kmFromRestaurant = 0;

        while (!toTraverse.isEmpty()) {
            Location current = toTraverse.poll();
            if (current.equals(NEUTRAL_LOCATION_IN_MAP)) {
                kmFromRestaurant++;
                if (!toTraverse.isEmpty()) {
                    toTraverse.add(NEUTRAL_LOCATION_IN_MAP);
                }
                continue;
            }
            traversedPositions.add(current);
            if (current.equals(client)) {
                return kmFromRestaurant;
            }
            Set<Location> possibleLocations = generatePossibleLocationsNearBy(current);
            for (Location locationNearBy : possibleLocations) {
                if (traversedPositions.contains(locationNearBy)) {
                    continue;
                }
                if (MapEntityType.isPassable(getEntityTypeByLocation(locationNearBy))) {
                    toTraverse.add(locationNearBy);
                }
            }
        }
        return -1;
    }

    private DeliveryInfo findOptimalDeliveryFrom(Location restaurant, Comparator<DeliveryInfo> deliveryComparator,
                                                 double maxPrice, int maxTime, int kmFromRestaurantToClient) {
        PriorityQueue<DeliveryInfo> optimalDeliveries = new PriorityQueue<>(deliveryComparator);
        Set<Location> traversedPositions = new HashSet<>();
        Queue<Location> toTraverse = new ArrayDeque<>(Arrays.asList(restaurant, NEUTRAL_LOCATION_IN_MAP));
        int kmFromRestaurantToCurrentGroupOfLocations = 0;
        while (!toTraverse.isEmpty()) {
            Location current = toTraverse.poll();
            if (current == NEUTRAL_LOCATION_IN_MAP) {
                kmFromRestaurantToCurrentGroupOfLocations++;
                if (!toTraverse.isEmpty()) {
                    toTraverse.add(NEUTRAL_LOCATION_IN_MAP);
                }
                continue;
            }
            traversedPositions.add(current);
            if (MapEntityType.isDeliveryGuy(getEntityTypeByLocation(current))) {
                DeliveryInfo currentDeliveryGuyOffer = calculateDeliveryInfo(current,
                    kmFromRestaurantToCurrentGroupOfLocations + kmFromRestaurantToClient);
                if (checkPriceAndTimeUnderLimits(currentDeliveryGuyOffer.price(), maxPrice,
                    currentDeliveryGuyOffer.estimatedTime(), maxTime)) {
                    optimalDeliveries.add(currentDeliveryGuyOffer);
                }
            }
            for (Location locationNearBy : generatePossibleLocationsNearBy(current)) {
                addIfShouldBeTraversed(locationNearBy, toTraverse, traversedPositions);
            }
        }
        return optimalDeliveries.peek();
    }

    private DeliveryInfo calculateDeliveryInfo(Location deliveryGuy, int totalKmFromDeliveryGuyToClient) {
        DeliveryType deliveryType = getDeliveryTypeByMapEntityType(getEntityTypeByLocation(deliveryGuy));

        double price = deliveryType.getPricePerKm();
        int estimatedTimePerKm = deliveryType.getEstimatedTimePerKm();

        return new DeliveryInfo(deliveryGuy, price * totalKmFromDeliveryGuyToClient,
            estimatedTimePerKm * totalKmFromDeliveryGuyToClient, deliveryType);
    }

    private DeliveryType getDeliveryTypeByMapEntityType(MapEntityType type) {
        return switch (type) {
            case DELIVERY_GUY_CAR -> DeliveryType.CAR;
            case DELIVERY_GUY_BIKE -> DeliveryType.BIKE;
            default -> throw new UnsupportedOperationException(
                String.format("MapEntityType %s cannot be linked with any DeliveryType", type));
        };
    }

    private boolean checkPriceAndTimeUnderLimits(double price, double maxPrice, int time, int maxTime) {
        return checkInLimits(price, maxPrice, (double) -1) && checkInLimits(time, maxTime, -1);
    }

    private <T extends Comparable<T>> boolean checkInLimits(T value, T maxValue, T neutralValue) {
        return maxValue.compareTo(neutralValue) == 0 || value.compareTo(maxValue) <= 0;
    }

    private void addIfShouldBeTraversed(Location location, Queue<Location> toTraverse,
                                        Set<Location> traversedPositions) {
        if (!traversedPositions.contains(location) &&
            MapEntityType.isPassable(getEntityTypeByLocation(location))) {
            toTraverse.add(location);
        }
    }

    @Override
    public MapEntity[][] getLayout() {
        return this.mapLayout;
    }

}