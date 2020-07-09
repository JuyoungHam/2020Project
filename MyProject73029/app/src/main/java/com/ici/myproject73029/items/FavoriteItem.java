package com.ici.myproject73029.items;

public class FavoriteItem {
    private String title;
    private String poster;

    public FavoriteItem(){}

    public FavoriteItem(String title){
        this.title=title;
    }
    public String getTitle(){
        return title;
    }
    public String getPoster(){
        return poster;
    }
}
