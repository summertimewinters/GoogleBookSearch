package com.example.android.booksapi;

/**
 * Custom class to store information about books
 */

public class BooksData {

    //Create text and image variables to store book information
    private String mTitle;
    private String mAuthor;
    private String mPublishedDate;
    private String mDescription;
    private String mCoverImageURL;
    private String mInfoURL;

    //Create object to capture book data
    public BooksData(String title, String author, String publishedDate, String description, String coverImageURL, String infoURL) {
        mTitle = title;
        mAuthor = author;
        mPublishedDate = publishedDate;
        mDescription = description;
        mCoverImageURL = coverImageURL;
        mInfoURL = infoURL;
    }

    //Create methods to call book data
    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getCoverImageURL() {
        return mCoverImageURL;
    }

    public String getInfoURL() {
        return mInfoURL;
    }
}
