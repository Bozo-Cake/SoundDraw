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
import java.util.HashMap;
import java.util.Set;

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
        //https://developer.android.com/reference/android/view/MotionEvent
        if (event.getAction() == MotionEvent.ACTION_UP) {
            increaseResolution();
            drawLine();
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            T.setText(R.string.pressed);
            wave = new HashMap<Float, Float>();
            wave.put(0f, 0f);
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
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
            //Vibrate if moving too fast
            if (diffX > threshy) {
                if (VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(vibrateTime, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //Depreciated in API 26
                    vibrator.vibrate(vibrateTime);
                }
            }
            //ToDo: if(x > lastX) -> use two individual vectors?
            //https://www.javatpoint.com/collections-in-java
            //Save Everything
            for(int i = 0; i < event.getHistorySize(); i++) {
                wave.put(event.getHistoricalX(i), event.getHistoricalY(i));
            }//Skipped if only one
            wave.put(x, y);//ensure at least one is added


            //Copy log into text editor, save as csv, open in Excel, delete first column.
            //Log.d(TAG, "," + x + "," + y);
            E1.setText(Float.toString(x));
            E2.setText(Float.toString(y));
            return true;
        }

        return false;
    }
    void increaseResolution() {
        return;
    }
    void drawLine() {
        T.setText(String.format("%s%d", getString(R.string.touchToBegin), wave.size()));
        DrawView dv = new DrawView(this, wave);
        dv.setBackgroundColor(Color.WHITE);
        setContentView(dv);
    }
}
class DrawView extends View {
    Paint paint;
    HashMap<Float, Float> _coords;
    public DrawView (Context context, HashMap<Float, Float> coords) {
        super(context);
        _coords = new HashMap<>(coords);
        paint = new Paint();
        paint.setColor(Color.BLUE);
    }
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Set<Float> keys = _coords.keySet();
        float[] lines = new float[keys.size() * 2];
        int pos = 0;
        for (Float i : keys) {
            lines[pos++] = i;
            lines[pos++] = _coords.get(i);
        }
        canvas.drawLines(lines, paint);
    }
}