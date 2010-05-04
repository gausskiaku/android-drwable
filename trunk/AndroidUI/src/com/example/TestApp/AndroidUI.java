package com.example.TestApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AndroidUI extends Activity  {
	
	public static PDControlClient pdControlClient;	

	EditText EditTextServerIP;
	EditText EditTextServerPort;

	enum AstInputType
	{
		TEXT_INPUT,
		CURSOR_COORDINATE,
		ACCELERATION
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d("AndroidUI", "####onCreate");
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);   
        
        EditTextServerIP = (EditText )findViewById(R.id.EditTextServerIP);
        EditTextServerPort = (EditText )findViewById(R.id.EditTextServerPort);
        
    	if (pdControlClient == null)
    	{
    		buttonPressedEvent();
    	}
    	else
    	{
    		Log.d("onCreate","pdControlClient exists");
			Intent intent = new Intent(AndroidUI.this,	SelectInputType.class);
			startActivity(intent); 
    	}
        
	}
    
    
    private void buttonPressedEvent()
    {
    	Button ButtonOK = (Button) findViewById(R.id.ButtonOK);
    	ButtonOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				String address = EditTextServerIP.getText().toString();
				int port = 0;
				String stringPort = EditTextServerPort.getText().toString();
				if ((stringPort != null)&&(stringPort.length() != 0))
				{
					port =Integer.parseInt(stringPort);
				}
				else
					Toast.makeText(AndroidUI.this, "IP shoule not be null", Toast.LENGTH_SHORT).show();
				
				if (checkInternetAddress(address,port) == true)
				{
					Intent intent = new Intent(AndroidUI.this,	ActivityScrollbar.class);
					startActivity(intent); 
					
					/*
					pdControlClient = new PDControlClient();

					 if (pdControlClient.connectTo(address, port) == true)
					 {
							Intent intent = new Intent(AndroidUI.this,	SelectInputType.class);
							startActivity(intent); 
					 }
					 else
					 {
						 Log.e("AndroidUI"," Can Not Connect to Server, Pls check the network!");
						 Toast.makeText(AndroidUI.this, "Can Not Connect to Server,Pls check the network!", Toast.LENGTH_SHORT).show(); 
					 }
					 */
				}
				
			}
    	});
    	
    }
   
    public boolean checkInternetAddress(String addr, int port )
    {
    	boolean ret = true;

    	if ((addr == null)||(addr.length() == 0))
    	{
    		ret = false;

    		Toast.makeText(this, "Address can't be null", Toast.LENGTH_SHORT).show();
    	}
    		   		
    	if ((port <0)||(port > 0xFFFF))
    	{
    		ret = false;
    		Toast.makeText(this, "IP is over range", Toast.LENGTH_SHORT).show();
    	}
    	
    	return ret;   	
    }
    
    @Override  
    public void onStart()
    {
    	Log.d("AndroidUI","####onStart");
    	super.onStart();
    	
    }
    
    @Override  
    public void onRestart()
    {
    	Log.d("AndroidUI","####onRestart");
    	super.onRestart();
    }
    
    @Override  
    public void onResume()
    {
    	Log.d("AndroidUI","####onResume");
    	super.onResume();
    	
    }
    
    @Override  
    public void onPause()
    {
    	Log.d("AndroidUI","####onPause");
    	super.onPause();
    	
    	Log.d("AndroidUI","the activity runs in background");  	
    }
    
    @Override  
    public void onStop()
    {
    	Log.d("AndroidUI","####onStop");
    	Log.d("AndroidUI","####activity is invisible: shoulde close the socket ??");
    	super.onStop();
    }
    
    @Override  
    public void onDestroy()
    {
    	Log.d("AndroidUI","####onDestroy");
    	super.onDestroy();
    	
    	pdControlClient.setNull();
    	pdControlClient = null;
    	EditTextServerIP = null;
    	EditTextServerPort = null;
    	
    }


}