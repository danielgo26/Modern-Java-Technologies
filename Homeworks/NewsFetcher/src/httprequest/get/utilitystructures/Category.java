package httprequest.get.utilitystructures;

public enum Category {
    Business("business"),
    Entertainment("entertainment"),
    General("general"),
    Health("health"),
    Science("science"),
    Sports("sports"),
    Technology("technology");

    private final String categoryStringRepresentation;

    Category(String categoryStringRepresentation) {
        this.categoryStringRepresentation = categoryStringRepresentation;
    }

    public static String getCategoryString(Category category) {
        return category.categoryStringRepresentation;
    }
}