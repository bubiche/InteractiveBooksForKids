package com.example.user.interactivebooksforkids;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Question class to store information of a question
 */
public class Question {
    private int correct_answer = 0; // index of correct answer in options
    private ArrayList<String> options = new ArrayList<String>();
    private String question = "";
    private int img_id = 0;

    public Question(Activity context, int book_id, int q_no) {
        int q_arr_id = context.getResources().getIdentifier("book" + (book_id + 1) + "q" + q_no, "array", context.getPackageName());
        String[] q_arr = context.getResources().getStringArray(q_arr_id);
        if(q_arr[0].equals("1")) {
            img_id = context.getResources().getIdentifier("book" + (book_id+1) + "q" + q_no + "_pic", "drawable", context.getPackageName());
        }

        question = q_arr[1];
        for(int i = 2; i < q_arr.length - 1; ++i)
            options.add(q_arr[i]);

        correct_answer = Integer.parseInt(q_arr[q_arr.length -1]) - 1;
    }

    public int getAnswer() {
        return correct_answer;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public int getImg_id() {
        return img_id;
    }

    public String getQuestion() {
        return question;
    }
}
