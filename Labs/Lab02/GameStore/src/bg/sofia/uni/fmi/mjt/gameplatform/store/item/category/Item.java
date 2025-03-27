package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

public abstract class Item implements StoreItem {
    private String title;
    private BigDecimal price;
    private double rating;
    private int ratingsCount;
    private LocalDateTime releaseDate;

    public Item(String title, BigDecimal price, LocalDateTime releaseDate) {
        setTitle(title);
        setPrice(price);
        this.rating = 0.0;
        this.ratingsCount = 0;
        setReleaseDate(releaseDate);
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public BigDecimal getPrice() {
        DecimalFormat df = new DecimalFormat("#.00");
        String formattedValue = df.format(this.price);
        return new BigDecimal(formattedValue);
    }

    @Override
    public double getRating() {
        return this.rating;
    }

    @Override
    public LocalDateTime getReleaseDate() {
        return this.releaseDate;
    }

    @Override
    public void setTitle(String title) {
        if (title != null && !title.isEmpty()) {
            this.title = title;
        } else {
            this.title = "unknown";
        }
    }

    @Override
    public void setPrice(BigDecimal price) {
        if (price == null) this.price = new BigDecimal(0.00);

        if (price.compareTo(new BigDecimal(0.00)) >= 0) {
            this.price = price;
        } else {
            this.price = new BigDecimal(0.00);
        }
    }

    @Override
    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public void rate(double rating) {
        //Note: here we do not check for rating limit boundaries, because the rating limits can vary
        //depending on the game store the item has been rated into.
        this.rating = this.rating + (rating - this.rating) / ++this.ratingsCount;
    }
}