package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Caravan extends Vehicle {

    private FuelType fuelType;
    private int numberOfSeats;
    private int numberOfBeds;
    private double pricePerWeek;
    private double pricePerDay;
    private double pricePerHour;
    private static final double pricePerSeat = 5;
    private static final double pricePerBed = 10;

    public Caravan(String id, String model, FuelType fuelType, int numberOfSeats, int numberOfBeds, double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id, model);
        this.fuelType = fuelType;
        this.numberOfSeats = numberOfSeats;
        this.numberOfBeds = numberOfBeds;
        this.pricePerWeek = pricePerWeek;
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
    }


    @Override
    protected int minHoursOfRent() {
        return 24;
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

        int days = (int)(epoch2 - epoch1) / (24 * 3600);
        int weeks = days / 7;
        days -= weeks * 7;
        int hours = (int)Math.ceil(((epoch2 - epoch1) - (weeks * 7 + days) * (24 * 3600)) / 3600.0);

        if (days == 0 && hours < 24)
            throw new InvalidRentingPeriodException("Caravan cannot be rented for less than 24 hours");

        return (this.pricePerWeek * weeks + this.pricePerDay * days + this.pricePerHour * hours)
                + (this.fuelType.getDailyTax() * days)
                + (pricePerSeat * this.numberOfSeats)
                + (pricePerBed * this.numberOfBeds);
    }
}