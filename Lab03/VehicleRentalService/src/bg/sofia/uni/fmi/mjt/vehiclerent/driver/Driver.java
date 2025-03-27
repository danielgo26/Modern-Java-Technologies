package bg.sofia.uni.fmi.mjt.vehiclerent.driver;

public class Driver {

    private AgeGroup ageGroup;

    public Driver(AgeGroup group){
        this.ageGroup = group;
    }

    public int getExtraTax(){
        return this.ageGroup.getExtraTax();
    }
}
