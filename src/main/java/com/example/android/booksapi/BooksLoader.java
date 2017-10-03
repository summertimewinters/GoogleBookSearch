package com.example.android.booksapi;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.util.Log;
import java.util.ArrayList;

import static com.example.android.booksapi.BooksDisplay.LOG_TAG;

/**
 * Loader for books
 */

public class BooksLoader extends AsyncTaskLoader<ArrayList<BooksData>> {

    private String mUrl;

    public BooksLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "Forceload Loader");
        forceLoad();
    }

    @Override
    public ArrayList<BooksData> loadInBackground() {
        Log.i(LOG_TAG, "LoadinBackground Loader");
        if (mUrl == null) {
            return null;
        }

        ArrayList<BooksData> books = QueryUtils.fetchBooks(mUrl);
        return books;
    }
}
