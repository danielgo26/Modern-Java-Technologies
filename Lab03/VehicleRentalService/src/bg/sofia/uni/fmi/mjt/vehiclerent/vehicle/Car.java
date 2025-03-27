package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Car extends Vehicle {

    private FuelType fuelType;
    private int numberOfSeats;
    private double pricePerWeek;
    private double pricePerDay;
    private double pricePerHour;
    private static final double pricePerSeat = 5;

    public Car(String id, String model, FuelType fuelType, int numberOfSeats, double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id, model);
        this.fuelType = fuelType;
        this.numberOfSeats = numberOfSeats;
        this.pricePerWeek = pricePerWeek;
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
    }


    @Override
    protected int minHoursOfRent() {
        return -1;
    }

    @Override
    protected int maxHoursOfRent() {
        return Integer.MAX_VALUE;
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
        long epoch1 = startOfRent.atZone(zoneId).toEpochSecond();
        long epoch2 = endOfRent.atZone(zoneId).toEpochSecond();

        int days = (int) (epoch2 - epoch1) / (24 * 3600);
        int weeks = days / 7;
        days -= weeks * 7;
        int hours = (int) Math.ceil(((epoch2 - epoch1) - (weeks * 7 + days) * (24 * 3600)) / 3600.0);

        return (this.pricePerWeek * weeks + this.pricePerDay * days + this.pricePerHour * hours)
                + (this.fuelType.getDailyTax() * days)
                + (pricePerSeat * this.numberOfSeats);
    }
}