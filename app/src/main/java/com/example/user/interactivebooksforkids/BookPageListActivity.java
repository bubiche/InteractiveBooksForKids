package com.example.user.interactivebooksforkids;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.Random;


public class BookPageListActivity extends Activity {

    private static final int[] colors = {0xff3498db, 0xff8e44ad, 0xff1abc9c, 0xff2c3e50, 0xfff1c40f, 0xffd35400, 0xffe74c3c, 0xff95a5a6};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_page_list);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.page_list_layout);
        Random random = new Random();
        int colorIndex = random.nextInt(colors.length);
        layout.setBackgroundColor(colors[colorIndex]);
    }
}
