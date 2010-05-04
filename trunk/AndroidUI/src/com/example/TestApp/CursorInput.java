package com.example.TestApp;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.TestApp.PDControlClient.AstPDControlClientListener;

public class CursorInput extends Activity implements OnTouchListener, OnClickListener,AstPDControlClientListener{
	TextView TextViewCursor = null;
	
	final int MSG_SEND_INTERVAL = 50;
	final int CLICK_THRESHHOLD = 140;
	
	float x_init = 0;
	float y_init = 0;
	
	float x_cor = 0;
	float y_cor = 0;
	
	long downTime = 0;
	long upTime   = 0;
	
	Timer cursorTimer = null;
	CursorTimerTask cursorTimerTask = null;
	Handler              astHandler = null;	
	
	PDControlClient pDControlClient = AndroidUI.pdControlClient;
	
	public class CursorTimerTask extends TimerTask
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			int x = (int)(x_cor - x_init);
			int y = (int)(y_cor - y_init);
			
			
			byte[] cursorCor = new byte[8];
			
			AstEndian.int32ToByteLE(x,cursorCor,0);
			AstEndian.int32ToByteLE(y,cursorCor,4);
			
			pDControlClient.sendMsg(PDControlClient.MMI_PD_CONTROL_CS_ID_CURSOR_DATA, cursorCor);
		
		}		
	}


//	Runnable mUpdateResults;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.cursor);
		
		Log.d("CursorInput","**onCreate");
			
		this.findViewById(R.id.RelativeLayout01).setOnTouchListener(this);
		
		pDControlClient.setListener(this);
		Log.d("CursorInput",Thread.currentThread().getClass().toString());
		
		Button ButtonCursorBack = (Button) findViewById(R.id.ButtonCursorBack);
		ButtonCursorBack.setOnClickListener(this);		
			
		astHandler = new Handler(){
		public void handleMessage(Message msg) {
			
			Intent intent = new Intent();
			intent.setClass(CursorInput.this, AndroidUI.class);
			startActivity(intent);
			CursorInput.this.finish();
		}
		};
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			
			Log.d("CursorInput","----------DOWN-------------");
			downTime = event.getEventTime();
			
			/**send start*/
			pDControlClient.sendMsg(PDControlClient.MMI_PD_CONTROL_CS_ID_CURSOR_START, null);
			
			x_init = event.getRawX();
			y_init = event.getRawY();
			
			/**set timer*/
			cursorTimer = new Timer("cursor Timer");
			cursorTimerTask = new CursorTimerTask();
			cursorTimer.scheduleAtFixedRate(cursorTimerTask, 0, MSG_SEND_INTERVAL);
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			upTime = event.getEventTime();
			
			/**stop timer*/
			cursorTimer.cancel();
			cursorTimer.purge();
			
			if (upTime - downTime < CLICK_THRESHHOLD)
			{
				Log.d("CursorInput","----------CLICK-------------");
				
			}
			else
			{
				Log.d("CursorInput","----------UP-------------");
				/**send stop*/
				pDControlClient.sendMsg(PDControlClient.MMI_PD_CONTROL_CS_ID_CURSOR_STOP, null);

			}
						
			x_init = 0;
			y_init = 0;
			
			x_cor = 0;
			y_cor = 0;
		}
		
		x_cor = event.getRawX();
		y_cor = event.getRawY();
		
		//TextViewCursor.setText(pDControlClient.);
		TextView TextViewCursor = (TextView) this.findViewById(R.id.TextViewCursor);
		TextViewCursor.setText("[x_cor, y_cor] = [" + x_cor + "," + y_cor+ "]");
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if (v.getId() == R.id.ButtonCursorBack)
		{
	    	cursorTimer = null;
	    	cursorTimerTask = null;
	    	astHandler = null;	
	    	pDControlClient = null;
	    	
			Bundle bundle = getIntent().getExtras();
			String returnClassname = bundle.getString("name");	
			Log.d("TextView","  returnClassname = "+returnClassname);
			
			Intent intent = new Intent();
			intent.setClassName(CursorInput.this, returnClassname);
			
			
			startActivity(intent);
			this.finish();
		}
		
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
		Log.d("CursorInput"," 	onDisconnected");
		
		AndroidUI.pdControlClient = null;
		//AndroidUI.pdControlClient.astPDControlClientListener = null;
		//Log.d("CursorInput"," 	astHandler.obtainMessage() = "+astHandler.obtainMessage().toString());
		
		astHandler.sendMessage(astHandler.obtainMessage()); 
	}

	@Override
	public void onDiscovery(byte[] byteRev) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLogin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceive(byte[] byteBuf) {
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

	@Override
	public void onError() {
		// TODO Auto-generated method stub
		
		Log.e("CursorInput"," onError");
		Toast.makeText(this, "Send Data Error", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onReconnectError() {
		// TODO Auto-generated method stub
		
	}

    
    @Override  
    public void onStart()
    {
    	Log.d("CursorInput"," onStart");
    	super.onStart();
    	
    }
    @Override  
    public void onRestart()
    {
    	Log.d("CursorInput"," onRestart");
    	super.onRestart();
    }
    
    @Override  
    public void onResume()
    {
    	Log.d("CursorInput"," onResume");
    	super.onResume();
    	
    	
    	//Log.d("SelectInputType"," socket state = "+astCommClient.commSock);
    }
    
    @Override  
    public void onPause()
    {
    	Log.d("CursorInput"," onPause");
    	super.onPause();
    	
    	Log.d("CursorInput","the activity runs in background");  	
    }
    @Override  
    public void onStop()
    {
    	Log.d("CursorInput"," onStop");
    	Log.d("CursorInput"," activity is invisible: ??");
    	super.onStop();
    	
    	astHandler = null;	
    	pDControlClient = null;
    }
    @Override  
    public void onDestroy()
    {
    	Log.d("CursorInput"," onDestroy");
    	super.onDestroy();
    }

}
