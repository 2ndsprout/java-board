package org.example;

import java.util.ArrayList;

public class Customer {
    String id;
    String password;
    String nickname;
    ArrayList<Article> articleList = new ArrayList<>();

    public Customer() {
    }

    public void addArtcleList(Article article){
        articleList.add(article);
    }

    public ArrayList<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(ArrayList<Article> articleList) {
        this.articleList = articleList;
    }

    public Customer(String id, String password, String nickname) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.articleList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
