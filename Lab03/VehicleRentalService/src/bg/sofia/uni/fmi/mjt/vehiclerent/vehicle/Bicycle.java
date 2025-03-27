package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Bicycle extends Vehicle{
    private double pricePerDay;
    private double pricePerHour;

    public Bicycle(String id, String model, double pricePerDay, double pricePerHour){
        super(id, model);
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
    }

    @Override
    protected int minHoursOfRent() {
        return -1;
    }

    @Override
    protected int maxHoursOfRent() {
        return (7 * 24);
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
        long epoch1 = startOfRent.atZone(zoneId).toEpochSecond();
        long epoch2 = endOfRent.atZone(zoneId).toEpochSecond();

        int days = (int)((epoch2 - epoch1) / (24 * 3600));
        int hours = (int)((epoch2 - epoch1 - days * 24 * 3600) / 3600.0);

        return this.pricePerDay * days + this.pricePerHour * hours - this.getDriver().getExtraTax();
    }
}