package com.example.fletch.mygame;

import android.graphics.Point;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TimingLogger;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imageBall1 = findViewById(R.id.ball1);
        imageBall1.setVisibility(View.INVISIBLE);

        for(int i=1;i<10;i++){
            ImageView ball = new ImageView(this);
            ball.setImageResource(R.drawable.moi);
            ball.setX(i*100);
            ball.setY(i*300);
            ball.setAdjustViewBounds(true);
            ball.setMaxHeight(200);
            ball.setVisibility(View.VISIBLE);
            ConstraintLayout layout = findViewById(R.id.mainLayout);
            layout.addView(ball);
            MoveMyImage move2 = new MoveMyImage();
            move2.execute(ball);
        }

    }

    public class MoveMyImage extends AsyncTask<ImageView, Void, Void> {
        private final int FPS = 40;
        private int directionX = -1;
        private int directionY = -1;

        @Override
        protected Void doInBackground(ImageView... imgs) {
            Timer timer = new Timer();
            final ImageView imageBall1 = imgs[0];
            timer.scheduleAtFixedRate(new TimerTask() {

                public void move(Point screenSize){
                    float x = imageBall1.getX();
                    float y = imageBall1.getY();

                    if (x >= (screenSize.x - imageBall1.getWidth()) || (x <= 0)) {
                        directionX *= -1;
                    }
                    if (y >= (screenSize.y - imageBall1.getHeight()) || (y <= 0)) {
                        directionY *= -1;
                    }

                    imageBall1.setX(x+10*directionX);
                    imageBall1.setY(y+10*directionY);

                }

                @Override
                public void run() {
                    // display.getSize devrait être lu dans Activity.onConfigurationChanged
                    // seulement lors des changements d'orientation
                    TimingLogger timings = new TimingLogger("Gameloops", "run method");
                    Display display = getWindowManager().getDefaultDisplay();
                    Point screenSize = new Point();
                    display.getSize(screenSize);


                    move(screenSize);
                    timings.addSplit("Boucle complétée");

                    /**
                     * La méthode dumpToLog() ne semble fonctionne que lorsqu'un appareil
                     * mobile est connecté.
                     * Il est nécessaire d'exécuter
                     * ./Android/Sdk/platform-tools/adb shell setprop log.tag.Gameloops VERBOSE
                     */
                    timings.dumpToLog();
                }
            }, 0, 1000 / FPS);

            return null;
        }
    }

}

