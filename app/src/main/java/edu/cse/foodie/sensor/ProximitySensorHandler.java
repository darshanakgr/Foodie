package edu.cse.foodie.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import edu.cse.foodie.model.SensorDataObject;

public class ProximitySensorHandler implements SensorEventListener {
    private final SensorDataObject dataObject;

    public ProximitySensorHandler(SensorDataObject dataObject) {
        this.dataObject = dataObject;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        dataObject.setProximity(event.values[0] == 0);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
