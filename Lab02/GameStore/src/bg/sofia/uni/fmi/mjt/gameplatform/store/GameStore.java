package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;

import java.math.BigDecimal;

public class GameStore implements StoreAPI {
    private StoreItem[] availableItems;
    private static final String[] promoCodes = {"VAN40", "100YO"};
    private static final BigDecimal[] promoCodesDiscounts = {new BigDecimal(0.4), new BigDecimal(1.0)};
    private static final int ratingLimitLowerBoundary = 1;
    private static final int ratingLimitUpperBoundary = 5;
    private BigDecimal lastCodeDiscountApplied = null;

    public GameStore(StoreItem[] availableItems) {
        this.setAvailableItems(availableItems);
    }

    public StoreItem[] getAvailableItems() {
        return this.availableItems;
    }

    public void setAvailableItems(StoreItem[] availableItems) {
        this.availableItems = availableItems;
    }

    @Override
    public StoreItem[] findItemByFilters(ItemFilter[] itemFilters) {
        //that is removed due to the tests
        //if (itemFilters == null || itemFilters.length == 0 || this.getAvailableItems() == null || this.getAvailableItems().length == 0)
          //  return new StoreItem[0];

        int[] indexesMatched = new int[this.availableItems.length];
        int currentPositionForMatchedItem = 0;

        int currentCheckedItemIndex = 0;
        for (StoreItem item : this.getAvailableItems()) {
            boolean matchedAllFilters = true;

            for (ItemFilter filter : itemFilters) {
                if (filter != null && !filter.matches(item)) {
                    matchedAllFilters = false;
                    break;
                }
            }
            if (matchedAllFilters) indexesMatched[currentPositionForMatchedItem++] = currentCheckedItemIndex;
            currentCheckedItemIndex++;
        }

        StoreItem[] matchedItems = new StoreItem[currentPositionForMatchedItem];
        for (int i = 0; i < currentPositionForMatchedItem; i++) {
            matchedItems[i] = this.getAvailableItems()[indexesMatched[i]];
        }

        return matchedItems;
    }

    @Override
    public void applyDiscount(String promoCode) {
        if (promoCode == null || this.getAvailableItems() == null) return;

        int indexOfPromoCodeInCollection = -1;
        for (int i = 0; i < this.promoCodes.length; i++) {
            String currentPromoCode = this.promoCodes[i];
            if (currentPromoCode != null && currentPromoCode.equals(promoCode)) {
                indexOfPromoCodeInCollection = i;
                break;
            }
        }

        if (indexOfPromoCodeInCollection != -1) {
            BigDecimal discount = this.promoCodesDiscounts[indexOfPromoCodeInCollection];
            if (this.lastCodeDiscountApplied != null && this.lastCodeDiscountApplied.compareTo(discount) >= 0)
                return;
            this.lastCodeDiscountApplied = discount;
            for (int i = 0; i < this.availableItems.length; i++) {
                BigDecimal currentItemPrice = this.availableItems[i].getPrice();
                this.availableItems[i].setPrice(currentItemPrice.subtract(currentItemPrice.multiply(discount)));
            }
        }
    }

    @Override
    public boolean rateItem(StoreItem item, int rating) {
        if (item == null || rating < ratingLimitLowerBoundary || rating > ratingLimitUpperBoundary) return false;

        item.rate(rating);

        return true;
    }
}