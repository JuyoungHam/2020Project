package com.ici.myproject73029.items;

import com.ici.myproject73029.Constant;

public class Review extends FundamentalItem {
    private String comments;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Review() {
        super();
        setType(Constant.REVIEW);
    }

    public Review(String title, String comments) {
        super(title);
        this.comments = comments;
    }
}