package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

public class RatingItemFilter implements ItemFilter {
    private double rating;

    public RatingItemFilter(double rating) {
        this.setRating(rating);
    }

    public double getRating() {
        return this.rating;
    }

    public void setRating(double rating) {
        if (rating < 0.0) {
            rating = 0.0;
        }
        this.rating = rating;
    }

    @Override
    public boolean matches(StoreItem item) {
        if (item == null)
            return false;

        return item.getRating() >= this.getRating();
    }
}