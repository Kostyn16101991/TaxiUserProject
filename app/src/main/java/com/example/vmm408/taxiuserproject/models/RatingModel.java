package com.example.vmm408.taxiuserproject.models;

import java.util.Date;

public class RatingModel {
    private String idUserRating;
    private String idUserAuthorRating;
    private Date timeRating;
    private Double starsRating;
    private String commentsRating;

    public void setIdUserRating(String idUserRating) {
        this.idUserRating = idUserRating;
    }

    public String getIdUserRating() {
        return idUserRating;
    }

    public void setIdUserAuthorRating(String idUserAuthorRating) {
        this.idUserAuthorRating = idUserAuthorRating;
    }

    public String getIdUserAuthorRating() {
        return idUserAuthorRating;
    }

    public void setTimeRating(Date timeRating) {
        this.timeRating = timeRating;
    }

    public Date getTimeRating() {
        return timeRating;
    }

    public void setStarsRating(Double starsRating) {
        this.starsRating = starsRating;
    }

    public Double getStarsRating() {
        return starsRating;
    }

    public void setCommentsRating(String commentsRating) {
        this.commentsRating = commentsRating;
    }

    public String getCommentsRating() {
        return commentsRating;
    }
}
