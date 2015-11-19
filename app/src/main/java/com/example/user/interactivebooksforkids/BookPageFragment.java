package com.example.user.interactivebooksforkids;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


/**
 * fragment for a book page
 */
public class BookPageFragment extends Fragment {

    private int book_id = -1;
    private int page = 1;
    private int pages = 0;
    private String[] contents = null;
    private Button back_to_page_list = null;
    private Button next_page_button = null;
    private Button pre_page_button = null;
    private TextView pageContent;
    private TextToSpeech tts;
    private boolean ttsReady = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_page, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        back_to_page_list = (Button) getActivity().findViewById(R.id.back_to_page_list_button);
        next_page_button = (Button) getActivity().findViewById(R.id.next_page_button);
        pre_page_button = (Button) getActivity().findViewById(R.id.pre_page_button);
        back_to_page_list.setOnClickListener(button_click);
        next_page_button.setOnClickListener(button_click);
        pre_page_button.setOnClickListener(button_click);


        Intent intent = getActivity().getIntent();
        book_id = intent.getIntExtra("book", -1);
        page = intent.getIntExtra("page", 1);
        int tmp = getResources().getIdentifier("book" + (book_id + 1) + "_page", "integer", getActivity().getPackageName());
        pages = getResources().getInteger(tmp);

        showBookPage(book_id, page);
        ImageView anim_view = (ImageView) getActivity().findViewById(R.id.read_anim_iv);
        anim_view.setBackgroundResource(R.drawable.read_anim);
        AnimationDrawable animate = (AnimationDrawable) anim_view.getBackground();
        animate.start();

        tts = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    ttsReady = true;
                }
            }
        });

        Button speak = (Button) getActivity().findViewById(R.id.read_text_button);

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ttsReady) {
                    if (!tts.isSpeaking()) {
                        String tmp = pageContent.getText().toString();
                        tts.setLanguage(Locale.US);
                        tts.speak(tmp, TextToSpeech.QUEUE_FLUSH, null);
                    } else
                        tts.stop();
                }
                else
                    Toast.makeText(getActivity().getApplicationContext(), "Your phone does not support text-to-speech :(", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showBookPage(int book_id, int page) {
        int contentArrayId = getResources().getIdentifier("book" + (book_id + 1), "array", getActivity().getPackageName());
        contents = getResources().getStringArray(contentArrayId);
        int imageID = getActivity().getResources().getIdentifier("book" + (book_id + 1) + "_page" + page, "drawable", getActivity().getPackageName());
        ImageView pageImage = (ImageView) getActivity().findViewById(R.id.page_img);
        pageImage.setImageResource(imageID);

        pageContent = (TextView) getActivity().findViewById(R.id.book_page_content);
        pageContent.setText(contents[page-1]);

        Button end_button = (Button) getActivity().findViewById(R.id.button_end_read);
        if(page == pages) {
            next_page_button.setVisibility(View.INVISIBLE);
            end_button.setVisibility(View.VISIBLE);
            end_button.setOnClickListener(button_click);
        }
        else {
            end_button.setVisibility(View.INVISIBLE);
            next_page_button.setVisibility(View.VISIBLE);
        }

        if(page == 1)
            pre_page_button.setVisibility(View.INVISIBLE);
        else {
            pre_page_button.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener button_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(ttsReady && tts.isSpeaking())
                tts.stop();

            int buttonID = v.getId();

            if(buttonID == R.id.pre_page_button) {
                if(page > 1) {
                    --page;
                    showBookPage(book_id, page);
                }
            }
            else if(buttonID == R.id.next_page_button) {
                if(page < contents.length) {
                    ++page;
                    showBookPage(book_id, page);
                }
            }
            else if(buttonID == R.id.back_to_page_list_button) {
                getActivity().finish();
            }
            else if(buttonID == R.id.button_end_read) {
                Intent intent = new Intent(getActivity(), QuizActivity.class);
                intent.putExtra("bookq", book_id);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if(tts != null){
            tts.stop();
            tts.shutdown();
        }
    }
}
