package com.example.TestApp;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.TestApp.PDControlClient.AstPDControlClientListener;

public class AndroidGestureDetector extends Activity implements OnTouchListener,OnClickListener,OnGestureListener,OnDoubleTapListener,AstPDControlClientListener{
	TextView textView = null;
	Button buttonBack;	
	
	//input type
	int rev_command_id       = 0 ;
		
	boolean isOnTouch              = false;
	boolean isOnFling              = false;
	boolean isOnDown               = false;
	boolean isOnScroll             = false;
	boolean isOnDoubleTap          = false;
	boolean isOnLongPress          = false;

	boolean isOnShowPress          = false;
	boolean isOnSingleTapUp        = false;
	boolean isOnDoubleTapEvent     = false;
	boolean isOnSingleTapConfirmed = false;
	
	//fling threshhold
	final int FLING_MIN_DISTANCE   = 40;  //
	final int FLING_MIN_VELOCITY   = 1500; //pix per second

	final int CURSOR_SEND_INTERVAL = 50;
	
	//cursor initial values
	float x_init = 0;
	float y_init = 0;	
	float x_cor  = 0;
	float y_cor  = 0;
	
	float pointsDistance = 0;

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;
	
	//timer for sending data
	Timer GestureTimer                = null;
	GestureTimerTask gestureTimerTask = null;
	Handler              astHandler   = null;		
	
	private GestureDetector gestureDetector;
	private PDControlClient pdControlClient;

public class GestureTimerTask extends TimerTask
{

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub


		//onFling has a higher priority over onTouch
		/*
		if (isOnFling)
		{
			Log.i("AndroidGestureDetector"," 	Send Fling");
			//isOnFling = false;
			
		}		
		else 
			*/
		if (isOnTouch)
		{
			//Log.i("AndroidGestureDetector"," 	Send Cursor");
			
			/*
			if (mode == ZOOM)
			{
				Log.i("AndroidGestureDetector"," 	zoom");
			}
			
			else */
		//	if (mode == DRAG)
			{
				//Log.i("AndroidGestureDetector"," 	drag");
				
				int x = (int)(x_cor - x_init);
				int y = (int)(y_cor - y_init);
				
				byte[] cursorCor = new byte[8];
				
				AstEndian.int32ToByteLE(x,cursorCor,0);
				AstEndian.int32ToByteLE(y,cursorCor,4);
				
				pdControlClient.sendMsg(PDControlClient.MMI_PD_CONTROL_CS_ID_CURSOR_DATA, cursorCor);			
			}

	
			
		}


	}	
}
	
	
/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
	Log.d("AndroidGestureDetector", "####onCreate");
	
    super.onCreate(savedInstanceState);
    setContentView(R.layout.gesture);  
    
    this.findViewById(R.id.RelativeLayout01).setOnTouchListener(this);
    gestureDetector = new GestureDetector(this);
    
    buttonBack = (Button) findViewById(R.id.ButtonBack);
    buttonBack.setOnClickListener(this);
    
    pdControlClient = AndroidUI.pdControlClient;
    pdControlClient.setListener(this);
    
    booleanSetTrue();
	textView = (TextView) this.findViewById(R.id.TextView01);

	astHandler = new Handler(){
	public void handleMessage(Message msg) {
		Intent intent = new Intent();
			intent.setClass(AndroidGestureDetector.this,AndroidUI.class);
			startActivity(intent);
			AndroidGestureDetector.this.finish();
		}
	};


}

@Override
public boolean onTouch(View view, MotionEvent event) {
// TODO Auto-generated method stub
	//Log.d("AndroidGestureDetector", "####onTouch");
	if (isOnTouch)
	{		
		switch(event.getAction()& MotionEvent.ACTION_MASK)
		{
			case MotionEvent.ACTION_DOWN:
			{
				Log.d("onTouch","	down");
				
				mode = DRAG;
				
				//send start
				pdControlClient.sendMsg(PDControlClient.MMI_PD_CONTROL_CS_ID_CURSOR_START, null);
				
				x_init = event.getRawX();
				y_init = event.getRawY();
				
				//set timer
				GestureTimer = new Timer("cursor Timer");
				gestureTimerTask = new GestureTimerTask();
				GestureTimer.scheduleAtFixedRate(gestureTimerTask, 0, CURSOR_SEND_INTERVAL);	
        	
				break;	
			}
				
			case MotionEvent.ACTION_UP:
			{
				Log.d("onTouch","	up");
        	
				GestureTimer.cancel();
				GestureTimer.purge();
				
				//send stop
				pdControlClient.sendMsg(PDControlClient.MMI_PD_CONTROL_CS_ID_CURSOR_STOP, null);
				
				x_init = 0;
				y_init = 0;
				
				x_cor = 0;
				y_cor = 0;
        	
				mode = NONE;
				break;
			}	
			case MotionEvent.ACTION_POINTER_1_UP:
			{
				Log.d("onTouch","	pointer_up");
				//mode = NONE;
				
				break;
			}		
			case MotionEvent.ACTION_POINTER_1_DOWN:
			{
				Log.d("onTouch","	pointer_down");
				
				pointsDistance = spacing(event);
				
				Log.d("onTouch","pointsDistance = "+ pointsDistance);
				
				if (pointsDistance > 80f)
				{
					mode = ZOOM;
				}
        	
				break;
			}
        	
			case MotionEvent.ACTION_MOVE:
			{
				if (mode == DRAG)
				{
					Log.i("onTouch","	drag");
					//send mid point
			
					x_cor = event.getRawX();
					y_cor = event.getRawY();

					textView.setText("[x_cor, y_cor] = [" + x_cor + "," + y_cor+ "]");
				}
				else if (mode == ZOOM)
				{
					Log.i("onTouch","	zoom");
				
					float newDist = spacing(event);
					
					if (newDist > 80f)
					{
						float scale = newDist/pointsDistance;
						//TO DO; send scale
						
						Log.d("ZOOM"," scale = "+scale);
					}
				}
			}
		}
	}
	return gestureDetector.onTouchEvent(event);
}

public void translateInput(byte[] byte_rev)
{
	Log.d("AndroidGestureDetector", "**translateInput");
	
	booleanSetFalse();
	Class<?> activityClass = null;
	
	int magic = AstEndian.byteLEToInt32LE(byte_rev,0) ;
	rev_command_id = AstEndian.byteLEToInt32LE(byte_rev,4) ;
	//int length = AstEndian.byteLEToInt32LE(byte_rev,8) ;
		
	if (magic == AstCommClient.COMM_CLIENT_MESSAGE_MAGIC)
	{
		Log.d("AndroidGestureDetector", "	Magic is Correct");
		switch(rev_command_id )
		{		
		case PDControlClient.MMI_PD_CONTROL_CS_ID_0:
			Log.d("AndroidGestureDetector", "	MMI_PD_CONTROL_CS_ID_0");
			activityClass = TextInput.class;	
			//astOnScroll();

			break;
		case PDControlClient.MMI_PD_CONTROL_CS_ID_1:
			Log.d("AndroidGestureDetector", "	MMI_PD_CONTROL_CS_ID_1");
			break;
		default :
			break;
		}
	}
	else
		Log.e("AndroidGestureDetector", "	Magic is Wrong");
	
	
	if (activityClass != null)
	{
		Intent intent = new Intent();
		intent.setClass(this, activityClass);
		
		Bundle bundle=new Bundle();
		bundle.putString("name", "com.example.TestApp.AndroidGestureDetector");
		intent.putExtras(bundle);
		this.startActivity(intent);
	}
}
	
private void booleanSetTrue()
{
	isOnTouch              = true;
	isOnFling              = true;
	//isOnDown               = true;
	//isOnScroll             = true;
	isOnDoubleTap          = true;
	isOnLongPress          = true;

	isOnShowPress          = true;
	//isOnSingleTapUp        = true;
	//isOnDoubleTapEvent     = true;
	isOnSingleTapConfirmed = true;
}	
	
	
private void booleanSetFalse()
{	
	isOnTouch              = false;
	isOnFling              = false;
	isOnDown               = false;
	isOnScroll             = false;
	isOnDoubleTap          = false;
	isOnLongPress          = false;

	isOnShowPress          = false;
	isOnSingleTapUp        = false;
	isOnDoubleTapEvent     = false;
	isOnSingleTapConfirmed = false;
}

private float spacing(MotionEvent event) {
	   float x = event.getX(0) - event.getX(1);
	   float y = event.getY(0) - event.getY(1);
	   return FloatMath.sqrt(x * x + y * y);
	}

@Override
public void onClientTimeout() {
	// TODO Auto-generated method stub
	
}

@Override
public void onConnectError() {
	// TODO Auto-generated method stub
	
}

@Override
public void onConnectNonBlock(Socket commSock, AstCommClient client) {
	// TODO Auto-generated method stub
	
}

@Override
public void onConnected() {
	// TODO Auto-generated method stub
	
}

@Override
public void onDisconnected() {
	// TODO Auto-generated method stub
	
	
	AndroidUI.pdControlClient = null;
	astHandler.sendMessage(astHandler.obtainMessage()); 
}

@Override
public void onDiscovery(byte[] byteRev) {
	// TODO Auto-generated method stub
	
}

@Override
public void onError() {
	// TODO Auto-generated method stub
	
}

@Override
public void onLogin() {
	// TODO Auto-generated method stub
	
}

@Override
public void onReceive(byte[] byteBuf) {
	// TODO Auto-generated method stub
	
	translateInput(byteBuf);
	Log.d("AndroidGestureDetector", "####onReceive");
}

@Override
public void onReconnectError() {
	// TODO Auto-generated method stub
	
}

@Override
public void onRelogin() {
	// TODO Auto-generated method stub
	
}

@Override
public void onServerTimeout() {
	// TODO Auto-generated method stub
	
}

//This will be triggered immediately for every down event. All other events should be preceded by this.
@Override
public boolean onDown(MotionEvent e) {
	// TODO Auto-generated method stub
	if (isOnDown)
	{
		Log.i("AndroidGestureDetector", "	onDown");
		
	}
	
	
	return false;
}

////notified when a long press occurs with the initial on down MotionEvent  that trigged it.
@Override
public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,	float velocityY) {
		// TODO Auto-generated method stub

		if (e1.getX()-e2.getX()>FLING_MIN_DISTANCE && Math.abs(velocityX)> FLING_MIN_VELOCITY)
		{
			Log.i("AndroidGestureDetector", "		Fling Left");
			
			//pdControlClient.sendMsg(PDControlClient.MMI_PD_CONTROL_CS_ID_FLING_DATA, cursorCor);
		}
		else if (e2.getX()-e1.getX()>FLING_MIN_DISTANCE && Math.abs(velocityX)> FLING_MIN_VELOCITY)
		{
			Log.i("AndroidGestureDetector", "		Fling Right");

		
		}
		else if (e1.getY()-e2.getY()>FLING_MIN_DISTANCE && Math.abs(velocityY)> FLING_MIN_VELOCITY)
		{
			Log.i("AndroidGestureDetector", "		Fling Up");

		}
		else if (e2.getY()-e1.getY()>FLING_MIN_DISTANCE && Math.abs(velocityY)> FLING_MIN_VELOCITY)
		{
			Log.i("AndroidGestureDetector", "		Fling Down");

		}


		return false;
	}

//Notified when a long press occurs with the initial on down MotionEvent  that trigged it.
@Override
public void onLongPress(MotionEvent e) {
	// TODO Auto-generated method stub
	if (isOnLongPress)
	{
		Log.i("AndroidGestureDetector", "	onLongPress");
		
		//send Start
		
		//send body
		
		//send stop
	}
}


//Notified when a scroll occurs with the initial on down MotionEvent  and the current move MotionEvent.
@Override
public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
	// TODO Auto-generated method stub
	if (isOnScroll)
	{
		Log.i("AndroidGestureDetector", "	onScroll");
		//Log.d("AndroidGestureDetector","		e2X = "+e2.getX());
		//Log.d("AndroidGestureDetector","		e1X = "+e1.getX());	
	}

	return false;
}

//triggered by performing motion down event,and not performed a move or up yet.Used to highlight an element or sth else.
@Override
public void onShowPress(MotionEvent e) {
	// TODO Auto-generated method stub
	if (isOnShowPress)
	{
		Log.i("AndroidGestureDetector", "	onShowPress");
	}
	
}

//Notified when a tap occurs with the up MotionEvent  that triggered it.
@Override
public boolean onSingleTapUp(MotionEvent e) {
	// TODO Auto-generated method stub
	if (isOnSingleTapUp)
	{
		Log.i("AndroidGestureDetector", "	onSingleTapUp");
	}
	
	return false;
}

@Override
public boolean onDoubleTap(MotionEvent event) {
	// TODO Auto-generated method stub
	
	if (isOnDoubleTap)
	{
		Log.i("AndroidGestureDetector", "	onDoubleTap: onDoubleTap");
		
		//send Start
		
		//send body
		
		//send stop
	}	

	return false;
}

@Override
public boolean onDoubleTapEvent(MotionEvent event) {
	// TODO Auto-generated method stub
	if (isOnDoubleTapEvent)
	{
		Log.i("AndroidGestureDetector", "	onDoubleTapEvent");

	}

	return false;
}


//only be called after the detector is confident that the user's first tap is not followed by a second tap leading to a double-tap gesture.
@Override
public boolean onSingleTapConfirmed(MotionEvent e) {
	// TODO Auto-generated method stub
	
	if (isOnSingleTapConfirmed)
	{
		Log.i("AndroidGestureDetector", "	onSingleTapConfirmed");
		
		//send Start

		//send body
		
		//send stop

	}

	return false;
}

@Override
public void onClick(View view) {
	// TODO Auto-generated method stub
	if (view.getId() == R.id.ButtonBack)
	{
		
		Intent intent = new Intent();
		intent.setClass(AndroidGestureDetector.this, SelectInputType.class);
		startActivity(intent);
		this.finish();
	}	
}
 
@Override  
public void onDestroy()
{
	Log.d("AndroidGestureDetector"," onDestroy");
	super.onDestroy();
	
	
}
}
