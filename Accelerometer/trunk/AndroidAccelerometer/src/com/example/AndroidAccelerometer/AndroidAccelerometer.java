package com.example.AndroidAccelerometer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.AndroidAccelerometer.AccelerometerManager.AccelerometerListener;

public class AndroidAccelerometer extends Activity implements AccelerometerListener {
 
    private static Context CONTEXT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        CONTEXT = this;
        
        Log.d("AndroidAccelerometer","onCreate************************");
        
        Button ButtonQuit =(Button)findViewById(R.id.ButtonQuit);
        ButtonQuit.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}});
    }
 
    protected void onResume() {
        super.onResume();
        if (AccelerometerManager.isSupported()) {
            Log.d("AndroidAccelerometer","onResume************************");          	
            AccelerometerManager.startListening(this);
        
        }
    }
 
    protected void onDestroy() {
        super.onDestroy();
        if (AccelerometerManager.isListening()) {
        	AccelerometerManager.stopListening();
        }
 
    }
 
    public static Context getContext() {
        return CONTEXT;
    }

    public void onAccelerationChanged(float x, float y, float z) {
    	
        ((TextView) findViewById(R.id.x)).setText(String.valueOf(x));
        ((TextView) findViewById(R.id.y)).setText(String.valueOf(y));
        ((TextView) findViewById(R.id.z)).setText(String.valueOf(z));
    }

	@Override
	public void onShake(float force) {
		// TODO Auto-generated method stub
	Log.d("onShake","onShake***************************************");	
	}
 
}