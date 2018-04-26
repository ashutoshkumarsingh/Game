package com.game.catchme;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.Timer;
import java.util.TimerTask;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private TextView scoreLabel, startLabel;
    private ImageView box, orange, pink, black;
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private boolean action_flag = false;
    private boolean start_flag = false;
    private int boxY;
    private int frameHeight;
    private int boxSize;
    private int screenWidth, screenHeight;
    private int orangeX, orangeY;
    private int pinkX, pinkY;
    private int blackX, blackY;
    private int score = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        box = (ImageView) findViewById(R.id.box);
        orange = (ImageView) findViewById(R.id.orange);
        pink = (ImageView) findViewById(R.id.pink);
        black = (ImageView) findViewById(R.id.black);

        WindowManager wm = getWindowManager();
        Display ds = wm.getDefaultDisplay();
        Point size = new Point();
        ds.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        orange.setX(-80);
        orange.setY(-80);
        pink.setX(-80);
        pink.setY(-80);
        black.setX(-80);
        black.setY(-80);


    }

    public void changePosition() {


        hitCheck();


        orangeX -= 12;
        if (orangeX < 0) {
            orangeX = screenWidth + 20;
            orangeY = (int) Math.floor(Math.random() * (frameHeight - orange.getHeight()));
        }

        orange.setX(orangeX);
        orange.setY(orangeY);


        blackX -= 12;
        if (blackX < 0) {
            blackX = screenWidth + 10;
            blackY = (int) Math.floor(Math.random() * (frameHeight - black.getHeight()));
        }

        black.setX(blackX);
        black.setY(blackY);


        pinkX -= 12;
        if (pinkX < 0) {
            pinkX = screenWidth + 5000;
            pinkY = (int) Math.floor(Math.random() * (frameHeight - pink.getHeight()));
        }

        pink.setX(pinkX);
        pink.setY(pinkY);


        if (action_flag == true) {
            boxY -= 20;
        } else {
            boxY += 20;
        }

        if (boxY < 0)
            boxY = 0;

        if (boxY > frameHeight - boxSize)
            boxY = frameHeight - boxSize;

        box.setY(boxY);

        scoreLabel.setText("Score: " +score);
    }

    private void hitCheck() {

        int orangeCenterX = orangeX + orange.getWidth() / 2;
        int orangeCenterY = orangeY + orange.getHeight() / 2;

        if (0 <= orangeCenterX && orangeCenterX <= boxSize && boxY <= orangeCenterY && orangeCenterY <= boxY + boxSize) {
            // box when not hitting the line

            score += 10;
            orangeX = -10;

        }


        int pinkCenterX = pinkX + pink.getWidth() / 2;
        int pinkCenterY = pinkY + pink.getHeight() / 2;

        if (0 <= pinkCenterX && pinkCenterX <= boxSize && boxY <= pinkCenterY && pinkCenterY <= boxY + boxSize) {
            // box when not hitting the line

            score += 30;
            pinkX = -10;

        }


        int blackCenterX = blackX + black.getWidth() / 2;
        int blackCenterY = blackY + black.getHeight() / 2;

        if (0 <= blackCenterX && blackCenterX <= boxSize && boxY <= blackCenterY && blackCenterY <= boxY + boxSize) {
            // box when not hitting the line

            timer.cancel();
            timer =null;

            Intent intent =new Intent(MainActivity.this,ResultActivity.class);
            intent.putExtra("score",score);
            startActivity(intent);

        }

    }

    public boolean onTouchEvent(MotionEvent motionEvent) {

        if (start_flag == false) {

            start_flag = true;

            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frameLayout.getHeight();

            boxY = (int) box.getY();
            boxSize = box.getHeight();

            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePosition();
                        }
                    });
                }
            }, 0, 20);

        } else {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                action_flag = true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                action_flag = false;
            }
        }


        return true;
    }
}
