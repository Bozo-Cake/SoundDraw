//package team.cake.sounddraw;
//
//import android.app.Activity;
//import android.view.MotionEvent;
//import android.view.View;
//
//import androidx.constraintlayout.widget.ConstraintLayout;
//
//import java.lang.ref.WeakReference;
//
//public class FingerPolling implements Runnable{
//    boolean run = false;
//    ConstraintLayout drawField;
//    WeakReference<Activity> mee;
//    @Override
//    public void run(Activity pass) {
//        mee = new WeakReference<>(pass);
//        drawField = (ConstraintLayout) mee.get().findViewById(R.id.drawing);
//        Object event;
//        drawField.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final float x = event.getX();
//                final float y = event.getY();
//                return true;
//            }
//        });
//        while (run) {
//            onTouchEvent();
//        }
//    }
//    public void pause() {
//        run = false;
//    }
//    public boolean onTouchEvent(MotionEvent event)
//    {
//        int x = (int)event.getX();
//        int y = (int)event.getY();
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_MOVE:
//            case MotionEvent.ACTION_UP:
//        }
//
//        return false;
//    }
//}
