package edu.cse.foodie;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;

public class MyJobService extends JobService {
    private String TAG = "MyJobService";
    private static boolean rescheduled;

    @SuppressLint("MissingPermission")
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("BACK_SERVICE", "RUNNING");
        final SoundMeter soundMeter = new SoundMeter();
        final SensorDataObject dataObject = new SensorDataObject();
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        final Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        final Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        final LocationHandler locationHandler = new LocationHandler(dataObject);
        final AmbientSensorHandler ambientSensorHandler = new AmbientSensorHandler(dataObject, lightSensor);
        final ProximitySensorHandler proximitySensorHandler = new ProximitySensorHandler(dataObject);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 6000, 0, locationHandler);
        sensorManager.registerListener(ambientSensorHandler, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(proximitySensorHandler, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        try {
            soundMeter.start();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    double db = soundMeter.getNoiseLevel();
                    dataObject.setNoiceLevel(db);
                    soundMeter.stop();
                    locationManager.removeUpdates(locationHandler);
                    sensorManager.unregisterListener(ambientSensorHandler);
                    sensorManager.unregisterListener(proximitySensorHandler);
                    try {
//                        new RequestHandler(getApplicationContext()).sendUpdate(dataObject);
                        Log.i("BACK_SERVICE", dataObject.toString());
                    } finally {
                        reschedule();
                    }
                }
            }, 10000);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        return true;
    }

    private void reschedule() {
        if (!rescheduled) {
            JobScheduler schedular = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(100, new ComponentName(getPackageName(), MyJobService.class.getName()));
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            builder.setMinimumLatency(60000);
            JobInfo jobInfo = builder.build();
            schedular.schedule(jobInfo);
            Log.i("BACK_SERVICE", "RESCHEDULED");
            rescheduled = true;
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
