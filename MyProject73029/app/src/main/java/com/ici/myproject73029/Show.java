package com.ici.myproject73029;

public class Show {
    private String title;
    private String venue;
    public Show(){}

    public void show(String title, String venue){
        this.title=title;
        this.venue=venue;
    }

    public String getTitle(){
        return title;
    }
    public String getVenue(){
        return venue;
    }
}
