package com.example.android.booksapi;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * First activity to collect books input
 */

public class BooksInput extends AppCompatActivity {

    //Create String variable for user input
    public String bookQueryInputString;
    private final String BOOK_QUERY_RAW = "https://www.googleapis.com/books/v1/volumes?";

    // Set the content of the activity to use the book input layout file
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_input);
    }

    // Upon button click, create a new intent to open the Books Display
    public void booksDisplayClick(View view) {
        EditText bookQueryInput = (EditText) findViewById(R.id.input_books_search);
        bookQueryInputString = bookQueryInput.getText().toString();

        if (bookQueryInputString.isEmpty()) {
            Context context = getApplicationContext();
            CharSequence text = getString(R.string.no_input);
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, text, duration).show();
        } else {
            Intent booksDisplayIntent = new Intent(BooksInput.this, BooksDisplay.class);
            booksDisplayIntent.putExtra("key", convertBookQueryInput());
            startActivity(booksDisplayIntent);
        }
    }

    //URI builder to be adjusted for search input and settings preferences
    private String convertBookQueryInput() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String bookReturnCount = sharedPrefs.getString(getString(R.string.settings_books_results_number_key),
                getString(R.string.settings_books_results_number_default));
        Uri baseURI = Uri.parse(BOOK_QUERY_RAW);
        Uri.Builder uriBuilder = baseURI.buildUpon();
        uriBuilder.appendQueryParameter("q", bookQueryInputString);
        uriBuilder.appendQueryParameter("maxResults", bookReturnCount);
        return uriBuilder.toString();
    }

    //Create new options main inflator
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
