package com.marklylebanks.popularmovies;

public class Review {
    private String mContent;
    private String mAuthor;

    public Review(String content, String author){
        mContent = content;
        mAuthor = author;
    }

    public String getContent() {
        return mContent;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
