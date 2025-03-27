package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

import java.util.HashMap;
import java.util.Map;

public enum MapEntityType {
    ROAD('.'),
    WALL('#'),
    RESTAURANT('R'),
    CLIENT('C'),
    DELIVERY_GUY_CAR('A'),
    DELIVERY_GUY_BIKE('B');

    private final char representingSymbolOnTheMap;
    private static final Map<Character, MapEntityType> CHAR_TO_ENUM_MAP = new HashMap<>();

    MapEntityType(char representingSymbolOnTheMap) {
        this.representingSymbolOnTheMap = representingSymbolOnTheMap;
    }

    static {
        for (MapEntityType entity : MapEntityType.values()) {
            CHAR_TO_ENUM_MAP.put(entity.representingSymbolOnTheMap, entity);
        }
    }

    public static MapEntityType fromChar(char entityType) {
        MapEntityType result = CHAR_TO_ENUM_MAP.get(entityType);
        if (result == null) {
            throw new IllegalArgumentException("Unknown type: " + entityType);
        }
        return result;
    }

    public static boolean isPassable(MapEntityType entityType) {
        return !entityType.equals(MapEntityType.WALL);
    }

    public static boolean isValuable(MapEntityType entityType) {
        return !(entityType.equals(MapEntityType.WALL) || entityType.equals(MapEntityType.ROAD));
    }

    public static boolean isDeliveryGuy(MapEntityType entityType) {
        return entityType.equals(DELIVERY_GUY_BIKE) || entityType.equals(DELIVERY_GUY_CAR);
    }
}
