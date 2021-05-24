package team.cake.sounddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

//https://stackoverflow.com/questions/13851728/android-how-to-draw-on-view
public class DrawView extends View {
    final String TAG = "DrawView";//For Logs
    Paint paint;
    ArrayList<Float> _x;
    ArrayList<Float> _y;
    private int w, h;
    public DrawView (Context context, ArrayList<Float> x, ArrayList<Float> y) {
        super(context);
        _x = x;
        _y = y;
    }
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int i = 0;
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(3);
        while (i < _x.size() - 1) {
            Log.d(TAG, String.format("Drawing Line %d", i));
            canvas.drawLine(_x.get(i), _y.get(i), _x.get(i + 1), _y.get(i + 1), paint);
            i++;
        }
        //connect to baseline: ~first y point.
        canvas.drawLine(_x.get(_x.size() - 1), _y.get(_y.size() - 1), _x.get(_x.size() - 1), _y.get(0), paint);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        canvas.drawLine(0, _y.get(0), w, _y.get(0), paint);
    }

    //https://stackoverflow.com/questions/6652400/how-can-i-get-the-canvas-size-of-a-custom-view-outside-of-the-ondraw-method
    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        this.w = w;
        this.h = h;
    }
}