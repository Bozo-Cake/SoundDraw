package team.cake.sounddraw;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

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
        /**
         * Convert a line with effectively random diffX spacing to a equidistant diffX buffer.
         * The amplitude needs to be about +/- 32,540, increasing/decreasing should change volume?
         * The incoming Y scale currently is about -500 to +1000, and will vary from screen to screen.
         * ToDo: Map Y values to appropriate amplitude, and generate additional Y values to evenly distrubute associated X values.
         * Perhaps generate another ArrayList containing the slopes between each Y value, and generate the buffer from that.
        **/
        float xLength = _x.get(_x.size() - 1) - _x.get(0);
        //Let's make some noise!
    }
    private AudioTrack generateTone() {
        int time = 3; //Seconds
        int samplesPerFrame;
        int sizeCount = 600;
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_FLOAT,
                sizeCount, AudioTrack.MODE_STATIC);
        track.write();//finish
        return track;
    }
}
