package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Article {
    private int id; // 번호
    private String title; // 제목
    private String body; // 내용
    private String regDate; // 등록 날짜
    private int view; // 조회수
    private int hit; // 추천수
    private ArrayList<Comments> comments;

    public void addComment(String comment, String commentRegDate) {
        Comments newComment = new Comments(comment, commentRegDate);
        comments.add(newComment);
    }

    public ArrayList<Comments> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comments> comments) {
        this.comments = comments;
    }

    public Article() {
    }

    public Article(int id, String title, String body, int view, int hit, String regDate) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.regDate = regDate;
        this.comments = new ArrayList<>();
        this.view = view;
        this.hit = hit;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getBody() {

        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void increaceHit() {
        this.hit++;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public void increaseView() {
        this.view++;
    }

    public int getView() {
        return view;
    }

    public void setView(int views) {
        this.view = views;
    }
}

class Comments {
    String comments;
    String commentsRegDate;

    public Comments() {

    }

    public String getCommentsRegDate() {
        return commentsRegDate;
    }

    public void setCommentsRegDate(String commentsRegDate) {
        this.commentsRegDate = commentsRegDate;
    }

    public Comments(String comments, String commentsRegDate) {
        this.comments = comments;
        this.commentsRegDate = commentsRegDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}