package com.example.TestApp;

import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class MySimpleGesture extends SimpleOnGestureListener{

	  public boolean onDoubleTap(MotionEvent e) {  
		  Log.i("MySimpleGesture", "onDoubleTap");  
		  return false;
		//return super.onDoubleTap(e);  
		} 
	
	  public boolean onDown(MotionEvent e) {  
		  Log.i("MySimpleGesture", "onDown");  
		  return super.onDown(e);  
		  }  	
	  
	  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {  
		  Log.i("MySimpleGesture", "onFling");  
		  return super.onFling(e1, e2, velocityX, velocityY);  
		  } 
	  
	  public void onLongPress(MotionEvent e) {  
		  Log.i("MySimpleGesture", "onLongPress");  
		  super.onLongPress(e);  
		  }  	  
	  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {  
		 Log.i("MySimpleGesture", "onScroll");  
		  return super.onScroll(e1, e2, distanceX, distanceY);  
		  }    
	  public void onShowPress(MotionEvent e) {  
		  Log.i("MySimpleGesture", "onShowPress");  
		  super.onShowPress(e);  
		  }  
	  
	  public boolean onSingleTapConfirmed(MotionEvent e) {  
		  Log.i("MySimpleGesture", "onSingleTapConfirmed");  
		  return super.onSingleTapConfirmed(e);  
		  }  
	  
	public boolean onSingleTapUp(MotionEvent e) {  
		  Log.i("MySimpleGesture", "onSingleTapUp");  
		  return super.onSingleTapUp(e);  
		  }  	  
}
