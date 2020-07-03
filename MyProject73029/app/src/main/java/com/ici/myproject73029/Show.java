package com.ici.myproject73029;

import com.google.firebase.Timestamp;

public class Show {
    private String title;
    private String venue;
    private String poster;
    private Timestamp endDate,startDate;

    public String getTitle() {
        return title;
    }

    public String getVenue() {
        return venue;
    }
    public Timestamp getendDate(){
        return endDate;
    }
    public Timestamp getStartDate(){
        return startDate;
    }

    public void show(String title, String venue) {
        this.title = title;
        this.venue = venue;
    }
}

