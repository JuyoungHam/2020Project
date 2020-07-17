package com.ici.myproject73029.items;

import com.ici.myproject73029.BR;
import com.ici.myproject73029.Constant;

public class Review extends FundamentalItem {
    private String comments;
    private String userId;
    private String itemInfo;
    private String writer;

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

    public void setItemInfo(String itemInfo) {
        this.itemInfo = itemInfo;
        notifyPropertyChanged(BR.review);
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