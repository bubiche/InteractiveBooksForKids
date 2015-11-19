package com.example.user.interactivebooksforkids;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Adapter for the List View containing the list of books
 */
public class BookListItemAdapter extends ArrayAdapter<String> {

    private Activity context;
    private String[] itemname;
    private Integer[] imgid;
    private Integer[] imgid_big;

    public BookListItemAdapter(Activity context, String[] itemname, Integer[] imgid, Integer[] imgid_big) {
        super(context, R.layout.book_list_item, itemname);

        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;
        this.imgid_big = imgid_big;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.book_list_item, null,true);

        // display books' names and covers
        TextView text = (TextView) rowView.findViewById(R.id.list_row_text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.list_row_image);
        text.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);

        // set separate onclick listener for the image view (let user see the cover in a bigger size)
        imageView.setOnClickListener(new imageOnclickListener(position));
        return rowView;
    };

    private final class imageOnclickListener implements View.OnClickListener
    {
        int bookID;

        public imageOnclickListener(int bookID) {
            this.bookID = bookID;
        }

        @Override
        public void onClick(View v) {
            /*
            ImageView cover_big = (ImageView) context.findViewById(R.id.book_cover_big);
            cover_big.setImageResource(imgid_big[bookID]);
            LinearLayout big_cover_container = (LinearLayout) context.findViewById(R.id.big_cover_layout);
            big_cover_container.setVisibility(View.VISIBLE);
            */


            ImageView cover_big = (ImageView) context.findViewById(R.id.book_cover_big);
            cover_big.setBackgroundResource(imgid_big[bookID]);
            AnimationDrawable animate = (AnimationDrawable) cover_big.getBackground();
            animate.start();
            LinearLayout big_cover_container = (LinearLayout) context.findViewById(R.id.big_cover_layout);
            big_cover_container.setVisibility(View.VISIBLE);

        }

    };

    public void clearAll() {
        itemname = new String[1];
        imgid = new Integer[1];
        imgid_big = new Integer[1];
        notifyDataSetChanged();
    }
}
