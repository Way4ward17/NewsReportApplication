package com.theway4wardacademy.report.Pojo;

public class Post {

    private String msg;
    private String date;
    private String user;
    private String solve;
    private String imageLink;

    public Post(String msg, String date, String user, String solve, String imageLink, String videoLink, String type) {
        this.msg = msg;
        this.date = date;
        this.user = user;
        this.solve = solve;
        this.imageLink = imageLink;
        this.videoLink = videoLink;
        this.type = type;
    }

    private String videoLink;
    private String type;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSolve() {
        return solve;
    }

    public void setSolve(String solve) {
        this.solve = solve;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
