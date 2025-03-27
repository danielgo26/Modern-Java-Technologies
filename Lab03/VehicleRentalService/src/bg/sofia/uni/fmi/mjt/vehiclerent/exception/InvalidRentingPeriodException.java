package bg.sofia.uni.fmi.mjt.vehiclerent.exception;

public class InvalidRentingPeriodException extends Exception{
    public InvalidRentingPeriodException(String errorMessage){
        super(errorMessage);
    }
    public InvalidRentingPeriodException(String errorMessage, Exception e){
        super(errorMessage, e);
    }
}