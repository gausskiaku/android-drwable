package com.example.AndroidAccelerometer;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;



public class AccelerometerManager {
 
    private static Sensor sensor;
    private static SensorManager sensorManager;
    private static AccelerometerListener listener;
 
    /** indicates whether or not Accelerometer Sensor is supported */
    private static Boolean supported;
    /** indicates whether or not Accelerometer Sensor is running */
    private static boolean running = false;
 
    public interface AccelerometerListener {
   	 
    	public void onAccelerationChanged(float x, float y, float z);
     
    	public void onShake(float force);
     
    }
    
    /**
     * Returns true if the manager is listening to orientation changes
     */
    public static boolean isListening() {
        Log.d("AccelerometerManager","isListening************************");        	
        return running;
    }
 
    /**
     * Unregisters listeners
     */
    public static void stopListening() {
        running = false;
        Log.d("AccelerometerManager","stopListening************************");        
        try {
            if (sensorManager != null && sensorEventListener != null) {
                sensorManager.unregisterListener(sensorEventListener);
            }
        } catch (Exception e) {}
    }
 
    /**
     * Returns true if at least one Accelerometer sensor is available
     */
    public static boolean isSupported() {
        Log.d("isSupported","isSupported************************");    	
        if (supported == null) {
            if (AndroidAccelerometer.getContext() != null) {
                sensorManager = (SensorManager) AndroidAccelerometer.getContext().
                        getSystemService(Context.SENSOR_SERVICE);
                List<Sensor> sensors = sensorManager.getSensorList(
                        Sensor.TYPE_ACCELEROMETER);
                supported = new Boolean(sensors.size() > 0);
            } else {
                supported = Boolean.FALSE;
            }
        }
        return supported;
    }

    public static void startListening(
            AccelerometerListener accelerometerListener) {
        sensorManager = (SensorManager) AndroidAccelerometer.getContext().
                getSystemService(Context.SENSOR_SERVICE);
        Log.d("AccelerometerManager","startListening************************");           
        List<Sensor> sensors = sensorManager.getSensorList(
                Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0) {
            sensor = sensors.get(0);
            running = sensorManager.registerListener(
                    sensorEventListener, sensor, 
                    SensorManager.SENSOR_DELAY_GAME);
            listener = accelerometerListener;
            Log.d("AccelerometerManager","startListening************sensro size************"+sensors.size());              
        }
    }

    private static SensorEventListener sensorEventListener = 
        new SensorEventListener() {
  
        private float x = 0;
        private float y = 0;
        private float z = 0;
        
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.d("onAccuracyChanged","onAccuracyChanged************************");  
        }
 
        public void onSensorChanged(SensorEvent event) {
 
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            Log.d("AccelerometerManager","onSensorChanged************************");    

            // trigger change event
            listener.onAccelerationChanged(x, y, z);
        }
    };	
}
