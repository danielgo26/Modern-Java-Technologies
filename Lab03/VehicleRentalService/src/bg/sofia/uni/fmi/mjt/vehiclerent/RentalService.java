package bg.sofia.uni.fmi.mjt.vehiclerent;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.AgeGroup;
import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.Bicycle;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.Car;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.FuelType;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.Vehicle;

import java.time.LocalDateTime;

public class RentalService {

    /**
     * Simulates renting of the vehicle. Makes all required validations and then the provided driver "rents" the provided
     * vehicle with start time @startOfRent
     *
     * @param driver      the designated driver of the vehicle
     * @param vehicle     the chosen vehicle to be rented
     * @param startOfRent the start time of the rental
     * @throws IllegalArgumentException      if any of the passed arguments are null
     * @throws VehicleAlreadyRentedException in case the provided vehicle is already rented
     */
    public void rentVehicle(Driver driver, Vehicle vehicle, LocalDateTime startOfRent) {
        if (driver == null || vehicle == null || startOfRent == null) {
            throw new IllegalArgumentException("Driver, vehicle or start of rent were null");
        }
        if (vehicle.isTaken()) {
            throw new VehicleAlreadyRentedException(String.format("Vehicle with id %s was already taken", vehicle.getId()));
        }

        vehicle.rent(driver, startOfRent);
    }

    /**
     * This method simulates rental return - it includes validation of the arguments that throw the listed exceptions
     * in case of errors. The method returns the expected total price for the rental - price for the vehicle plus
     * additional tax for the driver, if it is applicable
     *
     * @param vehicle   the rented vehicle
     * @param endOfRent the end time of the rental
     * @return
     * @throws InvalidRentingPeriodException in case the endOfRent is before the start of rental, or the vehicle
     *                                       does not allow the passed period for rental, e.g. Caravans must be rented for at least a day.
     */
    public double returnVehicle(Vehicle vehicle, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        if (!vehicle.isTaken())
            throw new VehicleNotRentedException("Vehicle cannot be returned, because it was not previously rent");

        vehicle.returnBack(endOfRent);

        return vehicle.calculateRentalPrice(vehicle.getStartRentTime(), endOfRent) + vehicle.getDriver().getExtraTax();
    }
}