package bg.sofia.uni.fmi.mjt.glovo.delivery;

public enum DeliveryType {
    CAR(5, 3),
    BIKE(3, 5);

    private double pricePerKm;
    private int estimatedTimePerKm;

    DeliveryType(double pricePerKm, int estimatedTimePerKm) {
        this.pricePerKm = pricePerKm;
        this.estimatedTimePerKm = estimatedTimePerKm;
    }

    public double getPricePerKm() {
        return this.pricePerKm;
    }

    public int getEstimatedTimePerKm() {
        return this.estimatedTimePerKm;
    }
}
