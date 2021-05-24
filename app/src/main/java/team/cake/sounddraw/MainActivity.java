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
    final float vibThresh = 6f;
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
        }
        //OnMove
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //A motion event seems to have 0 - 4 coordinates in each.
            Float lastX = event.getX();
            Float lastY = event.getY();
            Float prevX = lastX;
            Float prevY = lastY;
            int waveSize = xWave.size();
            if(waveSize > 0) {
                prevX = xWave.get(waveSize - 1);
                prevY = yWave.get(waveSize - 1);
            }
            else {
                prevX = lastX;
                prevY = lastY;
            }
            float diffX = lastX - prevX;
            //If drawing backtracks: Can't do this for sound waves.
            if (diffX < 0) {
                lastX = prevX;
            }
            //Save Everything
            int sz = event.getHistorySize();
            for(int i = 0; i < sz; i++) {
                //If drawing backtracks: Can't do this for sound waves.
                float x = event.getHistoricalX(i);
                float y = event.getHistoricalY(i);
                if (diffX < 0) {
                    lastX = prevX;
                }
                xWave.add(event.getHistoricalX(i));
                yWave.add(event.getHistoricalY(i));
            }//Skips the last one; manually add the last one.
            xWave.add(lastX);
            yWave.add(lastY);

            //Vibrate if moving too fast
            if (diffX > vibThresh) {
                if (VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(vibrateTime, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //Depreciated in API 26
                    vibrator.vibrate(vibrateTime);
                }
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
//https://stackoverflow.com/questions/13851728/android-how-to-draw-on-view
class DrawView extends View {
    final String TAG = "DrawView";//For Logs
    Paint paint;
    ArrayList<Float> _x;
    ArrayList<Float> _y;
    public DrawView (Context context, ArrayList<Float> x, ArrayList<Float> y) {
        super(context);
        _x = x;
        _y = y;
        paint = new Paint();
        paint.setColor(Color.BLUE);
    }
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int i = 0;
        while (i < _x.size() - 1) {
            Log.d(TAG, String.format("Drawing Line %d", i));
            canvas.drawLine(_x.get(i), _y.get(i), _x.get(i + 1), _y.get(i + 1), paint);
            i++;
        }
    }
}