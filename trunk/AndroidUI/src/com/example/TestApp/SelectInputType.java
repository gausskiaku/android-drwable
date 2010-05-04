package com.example.TestApp;

import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.TestApp.PDControlClient.AstPDControlClientListener;

public class SelectInputType extends Activity implements OnClickListener,AstPDControlClientListener{
	
	Button ButtonCursor, ButtonTextInput,  ButtonAccelerometer,ButtonGestureDetector,ButtonInputDisconnect;
	Handler              astHandler = null;	
	
	PDControlClient  pdControlClient= AndroidUI.pdControlClient;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d("SelectInputType", " onCreate");
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputmethod);
		
       buttonPressedEvent();
       pdControlClient.setListener(this);

		astHandler = new Handler(){
			public void handleMessage(Message msg) {
				
				Intent intent = new Intent();
				intent.setClass(SelectInputType.this, AndroidUI.class);
				startActivity(intent);
				SelectInputType.this.finish();
			}
			};
    }
  
    public void buttonPressedEvent()
    {
    	ButtonCursor = (Button) findViewById(R.id.ButtonCursor);
    	ButtonTextInput = (Button) findViewById(R.id.ButtonTextInput);
    	ButtonAccelerometer = (Button) findViewById(R.id.ButtonAccelerometer);
    	ButtonGestureDetector = (Button) findViewById(R.id.ButtonGestureDetector);
    	ButtonInputDisconnect = (Button) findViewById(R.id.ButtonInputDisconnect);
    	  	
    	ButtonCursor.setOnClickListener(this);  
    	ButtonTextInput.setOnClickListener(this);  
    	ButtonAccelerometer.setOnClickListener(this); 
    	ButtonGestureDetector.setOnClickListener(this); 
    	ButtonInputDisconnect.setOnClickListener(this);     
    	
    }
    
   
    /*
    @Override  
    public void onStart()
    {
    	Log.d("SelectInputType"," onStart");
    	super.onStart();
    	
    }
    @Override  
    public void onRestart()
    {
    	Log.d("SelectInputType"," onRestart");
    	super.onRestart();
    }
    
    @Override  
    public void onResume()
    {
    	Log.d("SelectInputType"," onResume");
    	super.onResume();
    	
    	
    	//Log.d("SelectInputType"," socket state = "+astCommClient.commSock);
    }
    
    @Override  
    public void onPause()
    {
    	Log.d("SelectInputType"," onPause");
    	super.onPause();
    	
    	Log.d("SelectInputType","the activity runs in background");  	
    }
    @Override  
    public void onStop()
    {
    	Log.d("SelectInputType"," onStop");
    	Log.d("SelectInputType"," activity is invisible: shoulde close the socket ??");
    	super.onStop();
    }
    @Override  
    public void onDestroy()
    {
    	Log.d("SelectInputType"," onDestroy");
    	super.onDestroy();
    }
*/
    
	@Override
	public void onClick(View src) {
		// TODO Auto-generated method stub
		
		Log.d("SelectInputType","####onClick");
		Class<?> activityClass = null;
		
		switch(src.getId())
		{		
		case R.id.ButtonCursor:
			Log.d("SelectInputType","--cursor");			
			activityClass = CursorInput.class;	
			break;
		case R.id.ButtonTextInput:
			
			Log.d("SelectInputType","--text");			
			activityClass = TextInput.class;	
			break;
		case R.id.ButtonAccelerometer:
			Log.d("SelectInputType","--AccelerationInput");			
			activityClass = AccelerationInput.class;	
			break;
		case R.id.ButtonGestureDetector:
			Log.d("SelectInputType","--AndroidGestureDetector");			
			activityClass = AndroidGestureDetector.class;	
			break;			
		case R.id.ButtonInputDisconnect:
		{
			PDControlClient pDControlClient = AndroidUI.pdControlClient;
			pDControlClient.disconnectSocket();
						
			AndroidUI.pdControlClient = null;
			activityClass = AndroidUI.class;
			break;
			
		}

	}
	
		if (activityClass != null)
		{
			Intent intent = new Intent();
			intent.setClassName(this, activityClass.getName());
			//intent.setClass(this, activityClass);
			
			Bundle bundle=new Bundle();
			bundle.putString("name", "com.example.TestApp.SelectInputType");
			
			intent.putExtras(bundle);
			this.startActivity(intent);
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
		Log.d("SelectInputType"," 	onDisconnected");
		
		
		AndroidUI.pdControlClient = null;
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
		
	}

	@Override
	public void onReconnectError() {
		// TODO Auto-generated method stub
		
	}
}
