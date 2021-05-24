package team.cake.sounddraw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    TextView T;
    EditText E1, E2;

    final String TAG = "MAIN";//For Logs
    final float vibThresh = 10f;
    final int vibrateTime = 100;//ms
    Vibrator vibrator;
    ArrayList<Float> xWave;
    ArrayList<Float> yWave;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        E1 = findViewById(R.id.editText);
        E2 = findViewById(R.id.editText2);
        T = findViewById(R.id.textview);
        T.setOnTouchListener(this::onTouch);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private boolean onTouch(View v, MotionEvent event) {
        //https://developer.android.com/reference/android/view/MotionEvent
        //OnRelease
        if (event.getAction() == MotionEvent.ACTION_UP) {
            drawLine();
            increaseResolution();
        }
        //OnTouch
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            T.setText(R.string.pressed);
            //wave = new HashMap<Float, Float>();
            //wave.put(0f, 0f);
            xWave = new ArrayList<>();
            yWave = new ArrayList<>();
            xWave.add(event.getX());
            yWave.add(event.getY());
        }
        //OnMove
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //A motion event seems to have 0 - 4 coordinates in each.
            Float lastX = event.getX();
            Float lastY = event.getY();
            //If prev* don't update, you'll see a line from top corner to first touch.
            Float prevX = 0f;
            Float prevY = 0f;
            float diffX;
            float diffY;
            int waveSize = xWave.size();
            //Update prev* values after at least one entry is saved.
            if(waveSize > 0) {//Should always be true because first entry is saved in OnTouch
                prevX = xWave.get(waveSize - 1);
                prevY = yWave.get(waveSize - 1);
            }

            int sz = event.getHistorySize();
            //Save Everything
            boolean tooFast = false;
            for(int i = 0; i < sz; i++) {
                float x = event.getHistoricalX(i);
                float y = event.getHistoricalY(i);

                diffX = x - prevX;
                //If drawing backtracks, keep largest X value for each new Y
                //Sound Waves must pass vertical line test
                if (diffX < 0) {
                    x = prevX;
                    y = prevY;
                }
                else {
                    xWave.add(x);
                    yWave.add(y);
                }
                if (diffX > vibThresh) {
                    tooFast = true;
                }

                prevX = x;
                prevY = y;
            }//Skips the last one; manually add the last one.
            //No Backtracking
            diffX = lastX - prevX;
            if (diffX < 0) {
                lastX = prevX;
                lastY = prevY;
            }
            else {
                xWave.add(lastX);
                yWave.add(lastY);
            }
            if(diffX > vibThresh) {
                tooFast = true;
            }

            //Vibrate if moving too fast
            if (tooFast) {
                if (VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(vibrateTime, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //Depreciated in API 26
                    vibrator.vibrate(vibrateTime);
                }
                tooFast = false;
            }

            E1.setText(Float.toString(lastX));
            E2.setText(Float.toString(lastY));
            return true;
        }

        return false;
    }
    void increaseResolution() {
        return;
    }
    @SuppressLint("DefaultLocale")
    void drawLine() {
        T.setText(String.format("%s%d", getString(R.string.touchToBegin), xWave.size()));
        DrawView dv = new DrawView(this, xWave, yWave);
        dv.setBackgroundColor(Color.WHITE);
        setContentView(dv);
    }
}