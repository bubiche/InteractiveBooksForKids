package com.example.user.interactivebooksforkids;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void close_big_cover(View view) {
        ImageView cover_big = (ImageView) findViewById(R.id.book_cover_big);
        AnimationDrawable animate = (AnimationDrawable) cover_big.getBackground();
        animate.stop();
        cover_big.setBackgroundResource(0);

        LinearLayout big_cover = (LinearLayout) findViewById(R.id.big_cover_layout);
        big_cover.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageView cover_big = (ImageView) findViewById(R.id.book_cover_big);
        Drawable drawable = cover_big.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap.recycle();
        }
    }
}
