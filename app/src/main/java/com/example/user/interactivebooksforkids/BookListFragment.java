package com.example.user.interactivebooksforkids;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


/**
 * Fragment containing the list of all books in a list view
 */
public class BookListFragment extends Fragment {

    // books' names and covers
    private static String[] text_arr = {"Thirteen Ways of Looking at a Blackbird", "The Solar System", "Beach Day", "Music","The Hiccup Mouse"};
    private static Integer[] imgid = {R.drawable.book1_cover, R.drawable.book2_cover, R.drawable.book3_cover, R.drawable. book4_cover, R.drawable.book5_cover};
    private static Integer[] bigimgid = {R.drawable.book1_cover_anim, R.drawable.book2_cover_anim, R.drawable.book3_cover_anim, R.drawable.book4_cover_anim, R.drawable.book5_cover_anim};
    private BookListItemAdapter adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_book_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new BookListItemAdapter(getActivity(), text_arr, imgid, bigimgid);
        ListView list = (ListView) getActivity().findViewById(R.id.bookListView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView sheep = (ImageView) getActivity().findViewById(R.id.sheep_img);
                AnimationDrawable animate = (AnimationDrawable) sheep.getBackground();
                animate.stop();
                sheep.setBackgroundResource(0);


                Intent intent = new Intent(getActivity(), BookPageListActivity.class);
                intent.putExtra("chosen", position);
                startActivity(intent);
                getActivity().finish();
            }
        });

        TextView title = (TextView) getActivity().findViewById(R.id.WelcomeTile);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Callie-Mae.ttf");
        title.setTypeface(font);

        ImageView sheep = (ImageView) getActivity().findViewById(R.id.sheep_img);
        sheep.setBackgroundResource(R.drawable.sheep_animation);
        AnimationDrawable animate = (AnimationDrawable) sheep.getBackground();
        animate.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.clearAll();
        ImageView sheep = (ImageView) getActivity().findViewById(R.id.sheep_img);
        Drawable drawable = sheep.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap.recycle();
        }
    }
}
