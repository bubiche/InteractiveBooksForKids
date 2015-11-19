package com.example.user.interactivebooksforkids;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * fragment containing the list of all pages of book
 */
public class BookPageListFragment extends Fragment {

    private static final String[][] teachingPageTitle = {{"Sun", "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto"},
            {"Jazz", "Metal", "Classical", "Pop", "Rock", "Blues", "Folk", "Avant-Garde", "Reggae", "Opera"}};
    private final Integer[] list_anims = {R.drawable.book1_page_list_anim, R.drawable.book2_page_list_anim, R.drawable.book3_page_list_anim, R.drawable.book4_page_list_anim, R.drawable.book5_page_list_anim};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_page_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = getActivity().getIntent();
        int id = intent.getIntExtra("chosen", 0);
        showPageList(id);

        Button back_button = (Button) getActivity().findViewById(R.id.but_back_main);
        back_button.setOnClickListener(back_click);

        ImageView page_anim = (ImageView) getActivity().findViewById(R.id.page_list_anim_iv);
        page_anim.setBackgroundResource(list_anims[id]);
        AnimationDrawable animate = (AnimationDrawable) page_anim.getBackground();
        animate.start();
    }

    // story books has even id
    // books that teaches new concepts has odd id
    public void showPageList(int id) {
        int tmp = getResources().getIdentifier("book" + (id+1) + "_page", "integer", getActivity().getPackageName());
        int pages = getResources().getInteger(tmp);
        for(int i = 1; i <= pages; ++i) {
            int imageID = getActivity().getResources().getIdentifier("book" + (id+1) + "_page" + i, "drawable", getActivity().getPackageName());
            imageOnclickListener listener = new imageOnclickListener(id, i);
            LinearLayout item = new LinearLayout(getActivity());
            item.setOrientation(LinearLayout.VERTICAL);
            item.setPadding(0,0,40,0);
            LinearLayout container = (LinearLayout) getActivity().findViewById(R.id.book_page_scroll_container);
            container.addView(item);

            ImageView image = new ImageView(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(800, 1000);
            image.setLayoutParams(layoutParams);
            image.setImageResource(imageID);
            image.setOnClickListener(listener);
            image.setLayoutParams(new FrameLayout.LayoutParams(400, 400));
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            item.addView(image);

            TextView page_title = new TextView(getActivity());
            page_title.setTextColor(Color.WHITE);
            page_title.setTextSize(30);
            if(id%2 == 0)
                page_title.setText("Page "+i);
            else
                page_title.setText(teachingPageTitle[(id-1)/2][i-1]);
            item.addView(page_title);
        }

    }


    private View.OnClickListener back_click  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*
           LinearLayout container = (LinearLayout) getActivity().findViewById(R.id.book_page_scroll_container);
           for(int i = 0; i < container.getChildCount(); ++i) {
                LinearLayout item = (LinearLayout) container.getChildAt(i);
                for(int j = 0; j < item.getChildCount(); ++j)
                    item.removeView(container.getChildAt(j));
            }
            */

            //Intent intent = new Intent(getActivity(), WelcomeActivity.class);
            //startActivity(intent);
            //getActivity().finish();

            Intent intent = new Intent(getActivity(), WelcomeActivity.class);
            int pendingId = 1234;
            PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), pendingId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
            System.exit(0);

        }
    };

    private final class imageOnclickListener implements View.OnClickListener
    {
        int bookID;
        int page;

        public imageOnclickListener(int bookID, int page) {
            this.bookID = bookID;
            this.page = page;
        }

        @Override
        public void onClick(View v) {
            //if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            //{
                //BookPageFragment book_page = (BookPageFragment) getFragmentManager().findFragmentById(R.id.fragment_bookpage);
                //book_page.showBookPage(bookID, page);
            //}
            //else
            //{
                Intent intent = new Intent(getActivity(), BookPageActivity.class);
                intent.putExtra("book", bookID);
                intent.putExtra("page", page);
                startActivity(intent);
            //}
        }
    };
    /*
    @Override
    public void onDestroy() {
        super.onDestroy();

        unbindDrawables(getActivity().findViewById(R.id.page_list_layout));
        System.gc();
    }

    static void unbindDrawables(View view) {
        try{
            System.out.println("UNBINDING"+view);
            if (view.getBackground() != null) {

                ((BitmapDrawable)view.getBackground()).getBitmap().recycle();


                view.getBackground().setCallback(null);
                view=null;
            }

            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
                ((ViewGroup) view).removeAllViews();
            }

        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    */
}
