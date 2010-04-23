package com.example.AndroidGestureDetector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;


public class AndroidGestureDetector extends Activity implements OnTouchListener
//, android.view.GestureDetector.OnGestureListener 
{  
     private GestureDetector mGestureDetector;  
     
     public AndroidGestureDetector() {  
    	        // mGestureDetector = new GestureDetector(this);  
    	         mGestureDetector = new GestureDetector(new MySimpleGesture());    	         
    	         
    	      }  
     public void onCreate(Bundle savedInstanceState) {  
         super.onCreate(savedInstanceState);  
         setContentView(R.layout.main);  
         
         Log.d("AndroidGestureDetector","onCreate");
         this.findViewById(R.id.RelativeLayout01).setOnTouchListener(this);
          
         mGestureDetector.setIsLongpressEnabled(true);  
         
         Button button = (Button)findViewById(R.id.Button01);
         button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(AndroidGestureDetector.this, MySimpleGesture.class);
				startActivity(intent);
			}});
         
     }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		Log.d("AndroidGestureDetector","onTouch");
		return mGestureDetector.onTouchEvent(event);
	}
	/*
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		         Log.i("MyGesture", "onDown");  
		         Toast.makeText(this, "onDown", Toast.LENGTH_SHORT).show();  
		         return true;  
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2,
			float velocityX, float velocityY) {
		// TODO Auto-generated method stub
		Log.d("AndroidGestureDetector","onFling");
		Toast.makeText(this, "onFling", Toast.LENGTH_LONG).show();  
		return true;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.d("AndroidGestureDetector","onLongPress");
		Toast.makeText(this, "onLongPress", Toast.LENGTH_LONG).show();  
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2,
			float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		Log.d("AndroidGestureDetector","onScroll");
	     Toast.makeText(this, "onScroll", Toast.LENGTH_LONG).show();  
		return true;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.d("AndroidGestureDetector","onShowPress");
		Toast.makeText(this, "onShowPress", Toast.LENGTH_SHORT).show();  
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.d("AndroidGestureDetector","onSingleTapUp");
		Toast.makeText(this, "onSingleTapUp", Toast.LENGTH_SHORT).show();  
		return true;
	} 
	*/
}
