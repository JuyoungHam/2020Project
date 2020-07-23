package com.ici.myproject73029.items;

import com.ici.myproject73029.BR;
import com.ici.myproject73029.Constant;

import java.util.ArrayList;
import java.util.Date;

public class Review extends FundamentalItem {
    private String comments;
    private String userId;
    private String itemInfo;
    private String writer;
    Date create_date;
    float rating;

    public ArrayList<String> getWho_liked() {
        return who_liked;
    }

    public void setWho_liked(ArrayList<String> who_liked) {
        this.who_liked = who_liked;
    }

    public int get_number_who_liked() {
        if (who_liked != null) {
            return who_liked.size();
        } else {
            return 0;
        }
    }

    ArrayList<String> who_liked;

    public Review() {
        super();
        setType(Constant.REVIEW);
    }

    public Review(String writer, String title, String comments) {
        super(title);
        this.writer = writer;
        this.comments = comments;
        setType(Constant.REVIEW);
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public void setItemInfo(String itemInfo) {
        this.itemInfo = itemInfo;
        notifyPropertyChanged(BR.review);
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        notifyPropertyChanged(BR.review);
    }

    @Override
    public String getWriter() {
        return writer;
    }

    @Override
    public void setWriter(String writer) {
        this.writer = writer;
        notifyPropertyChanged(BR.review);
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
        notifyPropertyChanged(BR.review);
    }
}