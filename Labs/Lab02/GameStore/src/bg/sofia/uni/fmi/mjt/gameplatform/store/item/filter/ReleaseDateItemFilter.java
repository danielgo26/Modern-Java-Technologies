package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.time.LocalDateTime;

public class ReleaseDateItemFilter implements ItemFilter {
    private LocalDateTime lowerBound;
    private LocalDateTime upperBound;

    public ReleaseDateItemFilter(LocalDateTime lowerBound, LocalDateTime upperBound){
        if (lowerBound != null && upperBound != null & lowerBound.compareTo(upperBound) > 0){
            this.setUpperBound(lowerBound);
            this.setLowerBound(upperBound);
        }
        else{
            this.setUpperBound(upperBound);
            this.setLowerBound(lowerBound);
        }
    }

    public LocalDateTime getLowerBound(){
        return this.lowerBound;
    }

    public LocalDateTime getUpperBound(){
        return this.upperBound;
    }

    //if the new lower bound is greater than the actual upper bound, then both will change to the new lower bound value
    public void setLowerBound(LocalDateTime lowerBound){
        if (lowerBound != null && this.upperBound != null && lowerBound.compareTo(this.upperBound) > 0){
            this.upperBound = lowerBound;
        }
        this.lowerBound = lowerBound;
    }

    //if the new upper bound is less than the actual lower bound, then both will change to the new upper bound value
    public void setUpperBound(LocalDateTime upperBound){
        if (upperBound != null && this.lowerBound != null && upperBound.compareTo(this.lowerBound) < 0){
            this.lowerBound = upperBound;
        }
        this.upperBound = upperBound;
    }

    @Override
    public boolean matches(StoreItem item) {
        if (this.getLowerBound() == null || this.getUpperBound() == null)
            return false;

        return (this.getLowerBound().compareTo(item.getReleaseDate()) <= 0 &&
                this.getUpperBound().compareTo(item.getReleaseDate()) >= 0);
    }
}