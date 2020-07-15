package com.ici.myproject73029.items;

import com.ici.myproject73029.Constant;

public class Review extends FundamentalItem {
    private String comments;
    private String userId;
    private String itemInfo;
    private String creator;

    public Review() {
        super();
        setType(Constant.REVIEW);
    }

    public Review(String creator, String title, String comments) {
        super(title);
        this.creator = creator;
        this.comments = comments;
        setType(Constant.REVIEW);
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(String itemInfo) {
        this.itemInfo = itemInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getCreator() {
        return creator;
    }

    @Override
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}