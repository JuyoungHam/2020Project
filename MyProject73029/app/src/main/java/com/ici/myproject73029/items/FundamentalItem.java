package com.ici.myproject73029.items;

public class FundamentalItem {
    private String poster;
    private String title;
    private String description;
    private String creator;
    private String url;
    private int category;
    private String venue;
    private String phone;
    private String period;
    private int type;
    private int favorite_count;

    public FundamentalItem() {
    }

    public FundamentalItem(String title) {
    this.title = title;
    }

    public FundamentalItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public FundamentalItem(String title, String description, String poster) {
        this.title = title;
        this.description = description;
        this.poster = poster;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
