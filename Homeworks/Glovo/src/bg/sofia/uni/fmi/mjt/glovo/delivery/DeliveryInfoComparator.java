package bg.sofia.uni.fmi.mjt.glovo.delivery;

import java.util.Comparator;

public class DeliveryInfoComparator implements Comparator<DeliveryInfo> {

    private final ShippingMethod shippingMethod;

    public DeliveryInfoComparator(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    @Override
    public int compare(DeliveryInfo o1, DeliveryInfo o2) {
        if (shippingMethod.equals(ShippingMethod.FASTEST)) {
            return Integer.compare(o1.estimatedTime(), o2.estimatedTime());
        } else if (shippingMethod.equals(ShippingMethod.CHEAPEST)) {
            return Double.compare(o1.price(), o2.price());
        } else {
            throw new UnsupportedOperationException("Cannot compare Deliveries with the given shipping method");
        }
    }

}