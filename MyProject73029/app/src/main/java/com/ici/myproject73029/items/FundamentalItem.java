package com.ici.myproject73029.items;

import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;

import java.util.ArrayList;

public class FundamentalItem extends BaseObservable {
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
    private ArrayList<String> tag;

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

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
        notifyPropertyChanged(BR.FundamentalItem);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        notifyPropertyChanged(BR.FundamentalItem);
    }

    public String getWriter() {
        return creator;
    }

    public void setWriter(String writer) {
        this.creator = writer;
        notifyPropertyChanged(BR.FundamentalItem);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.FundamentalItem);
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
        notifyPropertyChanged(BR.FundamentalItem);
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
        notifyPropertyChanged(BR.FundamentalItem);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.FundamentalItem);
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
        notifyPropertyChanged(BR.FundamentalItem);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.FundamentalItem);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.FundamentalItem);
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
        notifyPropertyChanged(BR.FundamentalItem);
    }
}
