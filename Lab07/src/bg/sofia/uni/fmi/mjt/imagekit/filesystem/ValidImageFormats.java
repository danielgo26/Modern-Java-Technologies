package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import java.util.ArrayList;
import java.util.List;

public enum ValidImageFormats {
    JPEG,
    PNG,
    BMP;

    public static List<String> convertValuesToStringArray() {
        List<String> stringValues = new ArrayList<>();

        for (ValidImageFormats value : ValidImageFormats.values()) {
            stringValues.add(value.name());
        }

        return stringValues;
    }
}