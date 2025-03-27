package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;

import java.time.LocalDateTime;
import java.time.ZoneId;

public abstract class Vehicle {

    private String id;
    private String model;
    private boolean isTaken;
    private LocalDateTime startRentTime;
    private Driver driver;

    public Vehicle(String id, String model) {
        this.id = id;
        this.model = model;
        isTaken = false;
        startRentTime = null;
        driver = null;
    }

    protected abstract int minHoursOfRent();

    protected abstract int maxHoursOfRent();

    public String getId() {
        return this.id;
    }
    public boolean isTaken() {
        return this.isTaken;
    }

    public LocalDateTime getStartRentTime(){
        return this.startRentTime;
    }

    public Driver getDriver(){
        return this.driver;
    }

    /**
     * Simulates rental of the vehicle. The vehicle now is considered rented by the provided driver and the start of the rental is the provided date.
     *
     * @param driver        the driver that wants to rent the vehicle.
     * @param startRentTime the start time of the rent
     * @throws VehicleAlreadyRentedException in case the vehicle is already rented by someone else or by the same driver.
     */
    public void rent(Driver driver, LocalDateTime startRentTime) {
        if (this.isTaken()) {
            throw new VehicleAlreadyRentedException(String.format("Vehicle with id %s is already taken by a driver", this.id));
        }
        this.isTaken = true;
        this.startRentTime = startRentTime;
        this.driver = driver;
    }

    /**
     * Simulates end of rental for the vehicle - it is no longer rented by a driver.
     *
     * @param rentalEnd time of end of rental
     * @throws IllegalArgumentException      in case @rentalEnd is null
     * @throws VehicleNotRentedException     in case the vehicle is not rented at all
     * @throws InvalidRentingPeriodException in case the rentalEnd is before the currently noted start date of rental or
     *                                       in case the Vehicle does not allow the passed period for rental, e.g. Caravans must be rented for at least a day
     *                                       and the driver tries to return them after an hour.
     */
    public void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException {
        if (rentalEnd == null)
            throw new IllegalArgumentException("Rent end date of vehicle was null");

        if (!this.isTaken)
            throw new VehicleNotRentedException("Vehicle was not rented!");

        ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
        long epoch1 = this.startRentTime.atZone(zoneId).toEpochSecond();
        long epoch2 = rentalEnd.atZone(zoneId).toEpochSecond();

        int days = (int) (epoch2 - epoch1) / (24 * 3600);
        int hoursOfRent = days * 24;

        if (this.startRentTime.compareTo(rentalEnd) >= 1 || hoursOfRent < this.minHoursOfRent() || hoursOfRent >= this.maxHoursOfRent()) {
            throw new InvalidRentingPeriodException("Invalid renting period of vehicle");
        }

        this.isTaken = false;
    }

    /**
     * Used to calculate potential rental price without the vehicle to be rented.
     * The calculation is based on the type of the Vehicle (Car/Caravan/Bicycle).
     *
     * @param startOfRent the beginning of the rental
     * @param endOfRent   the end of the rental
     * @return potential price for rent
     * @throws InvalidRentingPeriodException in case the vehicle cannot be rented for that period of time or
     *                                       the period is not valid (end date is before start date)
     */
    public abstract double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException;
}