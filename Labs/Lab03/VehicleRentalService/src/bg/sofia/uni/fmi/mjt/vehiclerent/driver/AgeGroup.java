package bg.sofia.uni.fmi.mjt.vehiclerent.driver;

public enum AgeGroup {
    JUNIOR(10),
    EXPERIENCED(0),
    SENIOR(15);

    private int extraTax;

    private AgeGroup(int extraTax){
        this.extraTax = extraTax;
    }

    public int getExtraTax(){
        return this.extraTax;
    }
}