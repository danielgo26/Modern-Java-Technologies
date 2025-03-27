package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;

public class PriceItemFilter implements ItemFilter {
    private BigDecimal lowerBound;
    private BigDecimal upperBound;

    public PriceItemFilter(BigDecimal lowerBound, BigDecimal upperBound) {
        if (lowerBound != null && upperBound != null && lowerBound.compareTo(upperBound) > 0) {
            this.setUpperBound(lowerBound);
            this.setLowerBound(upperBound);
        } else {
            this.setUpperBound(upperBound);
            this.setLowerBound(lowerBound);
        }
    }

    public BigDecimal getLowerBound() {
        return this.lowerBound;
    }

    public BigDecimal getUpperBound() {
        return this.upperBound;
    }

    //if the new lower bound is greater than the actual upper bound, then both will change to the new lower bound value
    public void setLowerBound(BigDecimal lowerBound) {
        if (lowerBound != null && lowerBound.compareTo(new BigDecimal(0.00)) < 0) {
            lowerBound = new BigDecimal(0.00);
        }
        if (lowerBound != null && this.upperBound != null && lowerBound.compareTo(this.upperBound) > 0) {
            this.upperBound = lowerBound;
        }
        this.lowerBound = lowerBound;
    }

    //if the new upper bound is less than the actual lower bound, then both will change to the new upper bound value
    public void setUpperBound(BigDecimal upperBound) {
        if (upperBound != null && upperBound.compareTo(new BigDecimal(0.00)) < 0) {
            upperBound = new BigDecimal(0.00);
        }
        if (upperBound != null && this.lowerBound != null && upperBound.compareTo(this.lowerBound) < 0) {
            this.lowerBound = upperBound;
        }
        this.upperBound = upperBound;
    }

    @Override
    public boolean matches(StoreItem item) {
        if (this.getLowerBound() == null || this.getUpperBound() == null)
            return false;

        return (this.getLowerBound().compareTo(item.getPrice()) <= 0 &&
                this.getUpperBound().compareTo(item.getPrice()) >= 0);
    }
}