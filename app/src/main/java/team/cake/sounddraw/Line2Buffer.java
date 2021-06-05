package team.cake.sounddraw;

import java.util.ArrayList;

public class Line2Buffer implements Runnable {
    private final String TAG = "Line2Buffer";
    ArrayList<Float> _x;
    ArrayList<Float> _y;
    public Line2Buffer(ArrayList<Float> x, ArrayList<Float> y) {
        _x = new ArrayList<>(x);
        _y = new ArrayList<>(y);
    }

    @Override
    public void run() {
        //Let's make some noise!
    }
}
