package com.example.AndroidGestureTestApp;

import java.util.ArrayList;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.os.Bundle;
import android.widget.Toast;



public class AndroidGestureTestApp extends Activity implements OnGesturePerformedListener{
	 private GestureLibrary mLibrary;
	 
	 
	 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
                
        mLibrary = GestureLibraries.fromRawResource(this, R.raw.spells);
        
        if (!mLibrary.load())
        {
        	finish();
        }
        
        GestureOverlayView gestureOverlayView = (GestureOverlayView)findViewById(R.id.gestures);        
        gestureOverlayView.addOnGesturePerformedListener(this);
        
        
    }

	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		// TODO Auto-generated method stub
		
		ArrayList<Prediction> predictions = mLibrary.recognize(gesture);		
		if (predictions.size() > 0)
		{
			Prediction prediction = predictions.get(0);
			if (prediction.score > 1.0) {
				// Show the spell
				Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT).show();		
		}
		
		}		
	}
}