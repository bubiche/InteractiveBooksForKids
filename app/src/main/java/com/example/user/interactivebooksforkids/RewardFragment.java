package com.example.user.interactivebooksforkids;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;


/**
 * fragment for the reward after quiz
 */
public class RewardFragment extends Fragment {
    private static final int REQ_CODE_TAKE_PICTURE = 90210;
    private ImageView rewardImg = null;
    private final Integer[] rewardImgList = {R.drawable.reward1, R.drawable.reward2, R.drawable.reward3, R.drawable.reward4, R.drawable.reward5};
    private final Integer[] rewardSongList = {R.raw.song1, R.raw.song2, R.raw.song3, R.raw.song4, R.raw.song5};
    private Random rand = new Random();
    private MediaPlayer mediaPlayer = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reward, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rewardImg = (ImageView) getActivity().findViewById(R.id.rewardImage);
        Intent intent = getActivity().getIntent();
        int score = intent.getIntExtra("scores", 0);
        boolean done = intent.getBooleanExtra("end", false);
        Button back_to_main = (Button) getActivity().findViewById(R.id.back_to_main_from_reward);
        Button cam = (Button) getActivity().findViewById(R.id.camera_button);
        back_to_main.setOnClickListener(new View.OnClickListener() {
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

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(picIntent, REQ_CODE_TAKE_PICTURE);
            }
        });
        Button back_to_q = (Button) getActivity().findViewById(R.id.back_to_q);
        back_to_q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        if(done) {
            back_to_q.setVisibility(View.INVISIBLE);
            back_to_main.setVisibility(View.VISIBLE);
            PackageManager packageManager = getActivity().getPackageManager();
            if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA))
                cam.setVisibility(View.VISIBLE);
        }
        else {
            back_to_q.setVisibility(View.VISIBLE);
        }

        if(score >= 20)
            showReward();
        else
            consolate();
    }

    public void showReward() {
        int rewardIndex = rand.nextInt(rewardImgList.length);
        rewardImg.setImageResource(rewardImgList[rewardIndex]);
        mediaPlayer = MediaPlayer.create(getActivity(), rewardSongList[rewardIndex]);
        mediaPlayer.setLooping(true); // Set looping
        mediaPlayer.setVolume(100, 100);
        mediaPlayer.start();

        Button play_pause = (Button) getActivity().findViewById(R.id.pause_play_button);
        play_pause.setVisibility(View.VISIBLE);
        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                else
                    mediaPlayer.start();
            }
        });
    }

    public void consolate() {
        rewardImg.setImageResource(R.drawable.consolation);
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.lose);
        mediaPlayer.setVolume(100, 100);
        mediaPlayer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.stop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == REQ_CODE_TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            Bitmap bmp = (Bitmap) intent.getExtras().get("data");
            WallpaperManager myWallpaperManage = WallpaperManager.getInstance(getActivity().getApplicationContext());
            try {
                myWallpaperManage.setBitmap(bmp);
            } // try is compulsory because there is an exception to handle
            catch (IOException e) {
                Toast.makeText(getActivity(), "Cannot set image as wallpaper", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
