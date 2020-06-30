package com.ici.myproject73029;

public class Exhibitions {
    String title;
    String description;
    String creator;
    String url;
    int category;
    String venue;
    String phone;
    String period;


    public Exhibitions(String title, String description) {
        this.title = title;
        this.description = description;
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
}
