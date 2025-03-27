package bg.sofia.uni.fmi.mjt.glovo.controlcenter.map;

public record Location(int x, int y) {

    public static <T> boolean isLocationInMatrix(Location location, T[][] matrix) {
        return location != null && matrix != null &&
            location.x() >= 0 && location.x() < matrix.length &&
            location.y() >= 0 && location.y() < matrix[location.x()].length;
    }

}
