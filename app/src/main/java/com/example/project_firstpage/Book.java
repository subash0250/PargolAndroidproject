package com.example.project_firstpage;



public class Book {
    private String id;
    private String title;
    private String author;
    private String language;
    private String gener;
    private  String image;

    public Book(String id, String title, String author, String language, String gener, String image) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.language = language;
        this.gener = gener;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGener() {
        return gener;
    }

    public void setGener(String gener) {
        this.gener = gener;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String gener) {
        this.image = image;
    }
}