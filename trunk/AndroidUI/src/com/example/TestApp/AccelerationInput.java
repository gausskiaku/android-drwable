package com.example.TestApp;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.TestApp.AccelerometerManager.AccelerometerListener;
import com.example.TestApp.PDControlClient.AstPDControlClientListener;

public class AccelerationInput extends Activity implements OnClickListener,AccelerometerListener, AstPDControlClientListener{

	final int MSG_SEND_INTERVAL = 50;
	
	static Context context;
	PDControlClient pDControlClient = AndroidUI.pdControlClient;	
	
	static Timer accelerationTimer = null;
	AccelerationTimerTask accelerationTimerTask = null;
	Handler              astHandler = null;	
	
	float acc_x = 0;
	float acc_y = 0;	
	float acc_z = 0;
	
	public class AccelerationTimerTask extends TimerTask
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			byte[] accValue = new byte[12];
			
			AstEndian.int32ToByteLE((int)acc_x,accValue,0);
			AstEndian.int32ToByteLE((int)acc_y,accValue,4);
			AstEndian.int32ToByteLE((int)acc_z,accValue,8);			
			
			//send acc
			pDControlClient.sendMsg(PDControlClient.MMI_PD_CONTROL_CS_ID_ACC_DATA, accValue);		
		}
		
	}
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac);
		context = this;
		
		Log.d("AccelerationInput", "onCreate");
		
		pDControlClient.setListener(this);	
		((Button) findViewById(R.id.ButtonAcBack)).setOnClickListener(this);
			
		astHandler = new Handler(){
			public void handleMessage(Message msg) {

				if (AccelerometerManager.isListening()) {
					AccelerometerManager.stopListening();
				}
				
				Intent intent = new Intent(AccelerationInput.this, AndroidUI.class);
				startActivity(intent);
				AccelerationInput.this.finish();
			}
			};
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d("AccelerationInput", "onResume");
		
		if (AccelerometerManager.isSupported()) {

			AccelerometerManager.startListening(this);			
			//send start
			pDControlClient.sendMsg(PDControlClient.MMI_PD_CONTROL_CS_ID_ACC_START, null);

			/**set timer*/
			accelerationTimer = new Timer("cursor Timer");
			accelerationTimerTask = new AccelerationTimerTask();
			accelerationTimer.scheduleAtFixedRate(accelerationTimerTask, 0, MSG_SEND_INTERVAL);
		}
	}
	

    
    @Override  
    public void onStart()
    {
    	Log.d("AccelerationInput"," onStart");
    	super.onStart();
    	
    }
    @Override  
    public void onRestart()
    {
    	Log.d("AccelerationInput"," onRestart");
    	super.onRestart();
    }
    
    
    @Override  
    public void onPause()
    {
    	Log.d("AccelerationInput"," onPause");
    	super.onPause();
    	
    	Log.d("AccelerationInput","the activity runs in background");  	
    }
    @Override  
    public void onStop()
    {
    	Log.d("AccelerationInput"," onStop");
    	Log.d("AccelerationInput"," activity is invisible: shoulde close the socket ??");
    	super.onStop();
    	
    	
		if (accelerationTimer != null)
		{
			accelerationTimer .cancel();
			accelerationTimer .purge();
		}
    }

	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("AccelerationInput", "onDestroy");
		
		if (AccelerometerManager.isListening()) {
			AccelerometerManager.stopListening();
		
			this.finish();
		}
	}
	
	public static Context getContext()
	{
		return context;
	}

	@Override
	public void onAccelerationChanged(float x, float y, float z) {
		// TODO Auto-generated method stub
		
		((TextView) findViewById(R.id.x)).setText(String.valueOf(x));
		((TextView) findViewById(R.id.y)).setText(String.valueOf(y));
		((TextView) findViewById(R.id.z)).setText(String.valueOf(z));	
		
		acc_x = x;
		acc_y = y;
		acc_z = z;
	}

	@Override
	public void onShake(float force) {
		// TODO Auto-generated method stub
		
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
		Log.d("AccelerationInput"," 	onDisconnected");
		accelerationTimer.cancel();
		accelerationTimer.purge();
		astHandler.sendMessage(astHandler.obtainMessage()); 
		AndroidUI.pdControlClient = null;
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		accelerationTimer.cancel();
		accelerationTimer.purge();
		
		//send stop
		pDControlClient.sendMsg(PDControlClient.MMI_PD_CONTROL_CS_ID_ACC_STOP, null);
				
		Bundle bundle = getIntent().getExtras();
		String returnClassname = bundle.getString("name");	
		Log.d("TextView","  returnClassname = "+returnClassname);
		
		Intent intent = new Intent();
		intent.setClassName(AccelerationInput.this,returnClassname);
		startActivity(intent);
		AccelerationInput.this.finish();
	}
}
