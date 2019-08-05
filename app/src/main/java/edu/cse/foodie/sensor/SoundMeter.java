package edu.cse.foodie.sensor;

import android.media.MediaRecorder;

import java.io.IOException;

public class SoundMeter {

    private MediaRecorder mRecorder = null;

    public void start() throws IOException {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            mRecorder.prepare();
            mRecorder.start();
            mRecorder.getMaxAmplitude();
        }
    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double getNoiseLevel() {
        if (mRecorder != null) {
            int amplitude = mRecorder.getMaxAmplitude();
            return (20 * Math.log10(amplitude / 0.1));
        } else {
            return 0;
        }
    }
}
