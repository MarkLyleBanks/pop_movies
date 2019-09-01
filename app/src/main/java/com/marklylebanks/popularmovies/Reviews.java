package com.marklylebanks.popularmovies;

public class Reviews {
    private String mContent;
    private String mAuthor;

    Reviews(String content, String author){
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
