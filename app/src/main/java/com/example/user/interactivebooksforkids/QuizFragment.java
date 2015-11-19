package com.example.user.interactivebooksforkids;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;


/**
 * fragment for quiz section
 */
public class QuizFragment extends Fragment {
    private static final String[] levels = {"Newbie", "Expert", "Master"};
    private int cur_lv = 0;
    private int score = 0;
    private int counter = 0; // after 3 correct answer, a random reward will be given
    private TextView score_tv = null;
    private TextView question_tv = null;
    private ImageView question_img = null;
    private ListView option_list = null;
    private Question cur_q = null;
    private Integer[] q_list = null;
    private int q_index = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        score_tv = (TextView) getActivity().findViewById(R.id.question_scrore);
        question_tv = (TextView) getActivity().findViewById(R.id.question_tv);
        question_img = (ImageView) getActivity().findViewById(R.id.question_img);
        option_list = (ListView) getActivity().findViewById(R.id.question_ans);
        Button back = (Button) getActivity().findViewById(R.id.but_back_main_from_quiz);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                int pendingId = 1234;
                PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), pendingId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
                System.exit(0);
            }
        });


        score_tv.setText("Score: " + score + " - Level: " + levels[cur_lv]);
        Intent intent = getActivity().getIntent();
        int book_id = intent.getIntExtra("bookq", 0);
        int num_of_questions = getResources().getIdentifier("book" + (book_id + 1) + "q", "integer", getActivity().getPackageName());
        num_of_questions = getResources().getInteger(num_of_questions);
        q_list = new Integer[num_of_questions];
        for(int i = 0; i< num_of_questions; ++i) {
            q_list[i] = i + 1;
        }

        Collections.shuffle(Arrays.asList(q_list));
        showQuestion(book_id, q_list[0]);
    }

    public void showQuestion(final int book_id, int q_no) {
        cur_q = new Question(getActivity(), book_id, q_no);

        Button help = (Button) getActivity().findViewById(R.id.quiz_help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "The correct answer was " + cur_q.getOptions().get(cur_q.getAnswer()), Toast.LENGTH_SHORT).show();
                int q_list_index = Arrays.asList(q_list).indexOf(q_index);
                if (q_list_index == q_list.length - 1) { //after the quiz is finished, a random reward is given if the score is >= 20
                    Intent intent = new Intent(getActivity(), RewardActivity.class);
                    intent.putExtra("scores", score);
                    intent.putExtra("end", true);
                    startActivity(intent);
                } else {
                    showQuestion(book_id, q_list[q_list_index + 1]);
                }
            }
        });

        question_tv.setText(cur_q.getQuestion());
        if(cur_q.getImg_id() != 0)
            question_img.setImageResource(cur_q.getImg_id());
        else
            question_img.setImageResource(0);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cur_q.getOptions());
        option_list.setAdapter(adapter);

        q_index = q_no;
        option_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int q_list_index = Arrays.asList(q_list).indexOf(q_index);
                if (position == cur_q.getAnswer()) {
                    score += 10;
                    ++counter;
                    if(counter == 2 && cur_lv < levels.length - 1)
                        ++cur_lv;
                    score_tv.setText("Score: " + score + " - Level: " + levels[cur_lv]);
                    Toast.makeText(getActivity().getApplicationContext(), "Correct :)", Toast.LENGTH_SHORT).show();

                    if (counter == 2 && q_list_index != q_list.length - 1) {
                        counter = 0;
                        Intent intent = new Intent(getActivity(), RewardActivity.class);
                        intent.putExtra("scores", score);
                        intent.putExtra("end", false);
                        startActivity(intent);
                    }
                } else
                    Toast.makeText(getActivity().getApplicationContext(), "Incorrect :(", Toast.LENGTH_SHORT).show();

                if (q_list_index == q_list.length - 1) { //after the quiz is finished, a random reward is given if the score is >= 30
                    Intent intent = new Intent(getActivity(), RewardActivity.class);
                    intent.putExtra("scores", score);
                    intent.putExtra("end", true);
                    startActivity(intent);
                } else {
                    showQuestion(book_id, q_list[q_list_index + 1]);
                }
            }
        });


    }

}
