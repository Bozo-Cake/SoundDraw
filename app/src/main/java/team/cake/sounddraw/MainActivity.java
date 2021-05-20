package team.cake.sounddraw;

import android.annotation.SuppressLint;
import android.content.Context;
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
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    TextView T;
    EditText E1, E2;

    final String TAG = "MAIN";
    final float threshy = 5f;
    final int vibrateTime = 100;//ms
    boolean touching;
    Vibrator vibrator;
    HashMap<Float, Float> wave;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        touching = false;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        E1 = (EditText) findViewById(R.id.editText);
        E2 = (EditText) findViewById(R.id.editText2);
        T = (TextView)findViewById(R.id.textview);
        T.setOnTouchListener(this::onTouch);//End OnTouchEvent()
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            evaluateHistory();
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            T.setText(R.string.pressed);
            wave = new HashMap<Float, Float>();
            wave.put(0f, 0f);
        }
        final float x = event.getX();
        final float y = event.getY();
        float diffX = 0;
        float diffY = 0;
        int sz = event.getHistorySize();
        Log.d(TAG, String.valueOf(sz));
        //A motion event seems to have 0 - 4 coordinates in each.

        //MakeShift: Only triggers if moving fast enough to record a few coordinates per event
        if (sz > 1) {
            float lX = event.getHistoricalX(sz - 1);
            float lY = event.getHistoricalY(sz - 1);
            diffX = x - lX;
            diffY = y - lY;
            Log.d(TAG,String.format("Diff X:Y -> %f:%f", diffX,diffY));
        }

        //ToDo: if(x > lastX)
        //Save Everything
        for(int i = 0; i < event.getHistorySize(); i++) {
            wave.put(event.getHistoricalX(i), event.getHistoricalY(i));
        }//Skipped if only one
        wave.put(x, y);//ensure at least one is added

        //Calculate Resolution

        //Log.d(TAG, diffX + ":" + diffY);
        if (diffX > threshy) {
            if (VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(vibrateTime, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //Depreciated in API 26
                vibrator.vibrate(vibrateTime);
            }
        }

        //Log.d(TAG, x + ", " + y);
        E1.setText(Float.toString(x));
        E2.setText(Float.toString(y));
        return true;
    }
    void evaluateHistory() {
        T.setText(String.format("%s%d", getString(R.string.touchToBegin), wave.size()));
    }
}