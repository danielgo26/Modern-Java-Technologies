package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.util.Locale;

public class TitleItemFilter implements ItemFilter {
    private String title;
    private boolean caseSensitive;

    public TitleItemFilter(String title, boolean caseSensitive) {
        this.setTitle(title);
        this.setCaseSensitive(caseSensitive);
    }

    public String getTitle() {
        return this.title;
    }

    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    @Override
    public boolean matches(StoreItem item) {
        String searched = this.getTitle();
        String searchInto = item.getTitle();

        if (searched == null || searchInto == null)
            return false;

        if (!this.isCaseSensitive()) {
            searched = searched.toLowerCase(Locale.ROOT);
            searchInto = searchInto.toLowerCase(Locale.ROOT);
        }

        return searchInto.contains(searched);
    }
}