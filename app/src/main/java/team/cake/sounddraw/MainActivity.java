package team.cake.sounddraw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    TextView T;
    EditText E1, E2;
    float x = 0f;
    float y = 0f;
    boolean touching;
    HashMap<Float, Float> wave;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        touching = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        E1 = (EditText) findViewById(R.id.editText);
        E2 = (EditText) findViewById(R.id.editText2);
        T=(TextView)findViewById(R.id.textview);
        T.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                T.setText("Released: Touch to get co-ordinates\n" + wave.size());
            }
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                T.setText("Pressed");
                wave = new HashMap<Float, Float>();
            }
            final float x = event.getX();
            final float y = event.getY();
            wave.put(x,y);
            float lastXAxis = x;
            float lastYAxis = y;
            E1.setText(Float.toString(lastXAxis));
            E2.setText(Float.toString(lastYAxis));
            return true;
        });
        //T.onTouchEvent()
        //T.onTouch()
    }
}