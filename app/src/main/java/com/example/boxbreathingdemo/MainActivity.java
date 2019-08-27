package com.example.boxbreathingdemo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private Button startButton;
    private TextView progressText;
    private ProgressBar levelProgressBar;
    private TextView levelText;
    private TextView levelProgressText;
    private long totalTime;
    private int level = 1;
    private int levelProgress = 0;
    private double levelMultiplier = 1.25;
    private double stepMultiplier = 1.25;
    private int levelMax = 3;
    private CountDownTimer countDownTimer;
    private boolean isCounting = false;
    private int stepProgress = 0;
    private TextView counterText;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);
        startButton = findViewById(R.id.start_button);
        progressText = findViewById(R.id.progress_text);
        levelProgressBar = findViewById(R.id.level_progress_bar);
        levelText = findViewById(R.id.level_text_view);
        levelText.setText(String.valueOf(level));
        counterText = findViewById(R.id.counter_seconds_text);
        counterText.setText(String.format("%s seconds timer", String.valueOf(totalTime / 1000)));
        levelProgressText = findViewById(R.id.level_progress_text);
        levelProgressText.setText(String.format(Locale.ENGLISH, "%d/3", levelProgress));
        startButton.setOnClickListener(this);
        progressBar.setMax((int) totalTime);
        levelProgressBar.setProgress(stepProgress);
        levelProgressBar.setMax(3);
        totalTime = 2000;
        progressBar.setMax(Long.valueOf(totalTime).intValue());

    }

    public void countDown(long totalTime) {
        countDownTimer = new MyCountDownTimer(totalTime, 1);
        countDownTimer.start();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.start_button) {
            if (isCounting) {
                countDownTimer.cancel();
                isCounting = false;
                startButton.setText(R.string.start);
            } else {
                countDown(totalTime);
                isCounting = true;
                startButton.setText(R.string.stop);
            }
        }
    }

    public class MyCountDownTimer extends CountDownTimer {

        MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long timeLeft) {
            int progress = Long.valueOf(progressBar.getMax() - timeLeft).intValue();
            Log.d(TAG, "Progress: " + progress + " GetMax: " + progressBar.getMax() + " TimeLeft: " + timeLeft);
            counterText.setText(String.format(Locale.ENGLISH, "%d seconds timer", totalTime / 1000));
            progressBar.setProgress(progress);
            double percentage = (Integer.valueOf(progress).doubleValue() / Long.valueOf(totalTime).doubleValue()) * 100;
            Log.d(TAG, "Percentage: " + percentage);
            if (percentage <= 25) {
                progressText.setText(R.string.take_breath);
            } else if (percentage <= 50) {
                progressText.setText(R.string.hold);
            } else if (percentage <= 75) {
                progressText.setText(R.string.release);
            } else if (percentage <= 100) {
                progressText.setText(R.string.hold);
            }
        }

        @Override
        public void onFinish() {
            cancel();
            if (level > levelMax) {
                countDownTimer.cancel();
                isCounting = false;
                progressBar.setProgress(0);
                levelProgressBar.setProgress(0);
                level = 1;
                startButton.setText(R.string.start);
            } else if (level < levelMax) {
                if (levelProgress < 3) {
                    progressBar.setProgress(0);
                    progressText.setText("");
                    totalTime = (long) (totalTime * stepMultiplier);
                    levelProgress = levelProgress + 1;
                    progressBar.setMax(Long.valueOf(totalTime).intValue());
                    levelProgressBar.setProgress(levelProgress);
                    levelProgressText.setText(String.format(Locale.ENGLISH, "%d/3", levelProgress));
                    Log.d(TAG, "TotalTime: " + totalTime);
                    countDown(totalTime);
                } else if (levelProgress == 3) {
                    level = level + 1;
                    levelText.setText(String.valueOf(level));
                    totalTime = (long) (totalTime * levelMultiplier);
                    levelProgress = 0;
                    levelProgressBar.setProgress(levelProgress);
                    levelProgressText.setText(String.format(Locale.ENGLISH, "%d/3", levelProgress));
                    countDown(totalTime);
                }
            }
//            progressBar.setProgress(0);
//            progressText.setText("");
//            totalTime = (long) (totalTime * stepMultiplier);
//            levelProgress = levelProgress + 1;
//            progressBar.setMax(Long.valueOf(totalTime).intValue());
//            levelProgressBar.setProgress(levelProgress);
//            levelProgressText.setText(String.format(Locale.ENGLISH, "%d/3", levelProgress));
//            Log.d(TAG, "TotalTime: " + totalTime);
//            if (levelProgress == 3) {
//                if (level == levelMax) {
//                    countDownTimer.cancel();
//                    isCounting = false;
//                    startButton.setText(R.string.start);
//                } else {
//                    level = level + 1;
//                    levelText.setText(String.valueOf(level));
//                    totalTime = (long) (totalTime * levelMultiplier);
//                    levelProgress = 0;
//                    levelProgressBar.setProgress(levelProgress);
//                    levelProgressText.setText(String.format(Locale.ENGLISH, "%d/3", levelProgress));
//                    countDown(totalTime);
//                }
//            }

        }
    }
}
