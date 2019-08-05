package edu.cse.foodie.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import edu.cse.foodie.model.SensorDataObject;

public class AmbientSensorHandler implements SensorEventListener {
    private final float MAX_VALUE;
    private final SensorDataObject dataObject;

    public AmbientSensorHandler(SensorDataObject dataObject, Sensor sensor) {
        this.dataObject = dataObject;
        this.MAX_VALUE = sensor.getMaximumRange();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float reading = event.values[0];
        dataObject.setLux(reading / MAX_VALUE);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
