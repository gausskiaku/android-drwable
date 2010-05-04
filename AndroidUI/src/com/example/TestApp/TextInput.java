package com.example.TestApp;

import java.io.UnsupportedEncodingException;
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
import android.widget.EditText;

import com.example.TestApp.PDControlClient.AstPDControlClientListener;



public class TextInput extends Activity implements OnClickListener,AstPDControlClientListener{
	Button ButtonTextSend, ButtonTextback;
	EditText EditText01;
	String returnClassname = null;
	
	Handler astHandler;
	PDControlClient pdControlClient;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.text);
		
		pdControlClient = AndroidUI.pdControlClient;
		pdControlClient.setListener(this);
		Log.d("TextInput","**onCreate");
		onButtonClickedEvent();
				
		astHandler = new Handler(){		
		public void handleMessage(Message msg) {
							
			Intent intent = new Intent();
			intent.setClass(TextInput.this, AndroidUI.class);
			startActivity(intent);
			TextInput.this.finish();	
		}
		};
	}

	
	private void onButtonClickedEvent()
	{
		Log.d("TextInput","**onButtonClickedEvent");
		
		EditText01 = (EditText) findViewById(R.id.EditText01);
		
		ButtonTextSend = (Button) findViewById(R.id.ButtonTextSend);
		ButtonTextback = (Button) findViewById(R.id.ButtonTextback);
		
		ButtonTextSend.setOnClickListener(this);
		ButtonTextback.setOnClickListener(this);
	}

	@Override
	public void onClick(View src) {
		// TODO Auto-generated method stub
		Log.d("TextView","**onClick");
		switch(src.getId())
		{
			case R.id.ButtonTextSend:
				sendInputText();
				break;
			case R.id.ButtonTextback:
			{
				Bundle bundle = getIntent().getExtras();
				String returnClassname = bundle.getString("name");	
				Log.d("TextView","  returnClassname = "+returnClassname);
				
				Intent intent = new Intent();
				intent. setClassName(TextInput.this, returnClassname);
				this.startActivity(intent);
				finish();
				break;		
			}
		}	
	}
	
	private void sendInputText()
	{
		
		if ((EditText01.getText().toString() != null)){
			//send start???////////////////////////////////////////
			pdControlClient.sendMsg(PDControlClient.MMI_PD_CONTROL_CS_ID_COMMAND,null);
			Log.d("TextInput","**start has been sent");
			
			//send body
			byte[] byteString = null;
			try {
				byteString = EditText01.getText().toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}					
			AndroidUI.pdControlClient.sendMsg(PDControlClient.MMI_PD_CONTROL_CS_ID_TEXT_DATA, byteString);

			Log.d("TextInput","**body has been sent");
			//send stop
			AndroidUI.pdControlClient.sendMsg(PDControlClient.MMI_PD_CONTROL_CS_ID_TEXT_STOP,null);
			Log.d("TextInput","**stop has been sent");	
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
		Log.d("TextInput"," 	onDisconnected");
		
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
