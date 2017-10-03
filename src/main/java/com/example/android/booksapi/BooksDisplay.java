package com.example.android.booksapi;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class BooksDisplay extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<BooksData>> {
    public static final String LOG_TAG = BooksDisplay.class.getName();

    //Create global variables
    private String bookURL;
    private TextView emptyTextView;
    private static final int BOOKS_LOADER_ID = 1;
    private BooksAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create RecyclerView
        RecyclerView booksListView = (RecyclerView) findViewById(R.id.list);

        //Set empty text view
        emptyTextView = (TextView) findViewById(R.id.empty_results);

        //Pull the intent with the cleaned user input stored for the correct Books API
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            bookURL = extras.getString("key");
            Log.i(LOG_TAG, "Loader URL " + bookURL);
        }

        //Check for internet connectivity
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        //If network is connected, create LoadManager
        if (isConnected) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOKS_LOADER_ID, null, this).forceLoad();
            Log.i(LOG_TAG, "INIT Loader");
        } else {
            View mProgressBarView = findViewById(R.id.loading_spinner);
            mProgressBarView.setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_connection);
        }

        //Set adapter on books output
        mAdapter = new BooksAdapter(this, new ArrayList<BooksData>());
        booksListView.setAdapter(mAdapter);
        booksListView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Create overrides for three LoaderManager callbacks
    @Override
    public Loader<ArrayList<BooksData>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "OnCreate Loader");
        return new BooksLoader(this, bookURL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<BooksData>> loader, ArrayList<BooksData> booksLoad) {
        Log.i(LOG_TAG, "OnLoadFinished Loader");
        mAdapter.dataSwap(null);

        View mProgressBarView = findViewById(R.id.loading_spinner);
        mProgressBarView.setVisibility(View.GONE);

        if (booksLoad != null && !booksLoad.isEmpty()) {
            mAdapter.reloadList(booksLoad);
        } else {
            emptyTextView.setText(R.string.no_results);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<BooksData>> booksLoad) {
        Log.i(LOG_TAG, "OnLoaderReset Loader");
        mAdapter.dataSwap(null);
    }
}
