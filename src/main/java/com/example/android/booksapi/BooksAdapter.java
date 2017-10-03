package com.example.android.booksapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Custom books adapter to store books data from RecyclerView format
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
   private Context context;
   private ArrayList<BooksData> booksInput;

    public BooksAdapter(Activity context, ArrayList<BooksData> booksInput) {
        this.context = context;
        this.booksInput = booksInput;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.book_output, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final BooksData booksCurrent = booksInput.get(position);

        viewHolder.titleText.setText(booksCurrent.getTitle());
        viewHolder.authorText.setText(booksCurrent.getAuthor());
        viewHolder.publishedDateText.setText(dateFormatter(booksCurrent.getPublishedDate()));
        viewHolder.descriptionText.setText(booksCurrent.getDescription());
        new DownloadImageTask((ImageView) viewHolder.downloadCoverImage).execute(booksCurrent.getCoverImageURL());
        viewHolder.parentView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(booksCurrent.getInfoURL()));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });
    }

    //Create public method to clear ArrayList
    public ArrayList<BooksData> dataSwap(ArrayList<BooksData> booksDataArrayList) {
        if(booksInput == booksDataArrayList) {
         return null;
        }
        ArrayList<BooksData> oldBooksDataArrayList = booksInput;
        this.booksInput = booksDataArrayList;
        if (booksDataArrayList != null) {
            this.notifyDataSetChanged();
        }
        return oldBooksDataArrayList;
    }

    //Create public method to add all datapoints to adapter
    public void reloadList(ArrayList<BooksData> booksDataArrayList) {
        this.booksInput = booksDataArrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.booksInput.size();
    }

    //Create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText;
        private TextView authorText;
        private TextView publishedDateText;
        private TextView descriptionText;
        private ImageView downloadCoverImage;
        private View parentView;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.parentView = view;
            this.titleText = (TextView) view.findViewById(R.id.output_text_title);
            this.authorText = (TextView) view.findViewById(R.id.output_text_author);
            this.publishedDateText = (TextView) view.findViewById(R.id.output_text_publish_date);
            this.descriptionText = (TextView) view.findViewById(R.id.output_text_description);
            this.downloadCoverImage = (ImageView) view.findViewById(R.id.output_image_cover);
        }
    }

    //For cover image, create method within AsynchTask to pull image data from URL
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        private DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    //Date format method
    private String dateFormatter(String dateRaw) {
        String dateOutput = dateRaw.substring(0, 4);
        return dateOutput;
    }
}
