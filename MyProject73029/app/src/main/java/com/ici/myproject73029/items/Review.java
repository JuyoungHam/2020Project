package com.ici.myproject73029.items;

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
    }

    public Review(String title, String comments) {
        super(title);
        this.comments = comments;
    }
}