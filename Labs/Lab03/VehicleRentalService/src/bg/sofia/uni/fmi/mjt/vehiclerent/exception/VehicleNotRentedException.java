package bg.sofia.uni.fmi.mjt.vehiclerent.exception;

public class VehicleNotRentedException extends RuntimeException{
    public VehicleNotRentedException(String errorMessage){
        super(errorMessage);
    }
}