package com.example.IsNetworkAvailable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

public class IsNetworkAvailable extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        HttpTest(IsNetworkAvailable.this);
    }
    
    	    
 /*** check network*/
  public static void HttpTest(final Activity mActivity)
  {
	  Log.d("HttpTest"," -------------------------");
	  if( !isNetworkAvailable( mActivity) )
	  {
		  Log.d("HttpTest"," network is not aviavalable");
		  AlertDialog.Builder builders = new AlertDialog.Builder(mActivity);
    	  builders.setTitle("sorry, There is no Network Available");
    	  LayoutInflater _inflater = LayoutInflater.from(mActivity);
    	  View convertView = _inflater.inflate(R.layout.error,null);
    	  
    	  builders.setView(convertView);
    	  builders.setPositiveButton("sure",  new DialogInterface.OnClickListener(){
    
    		  public void onClick(DialogInterface dialog, int which)
    		  {
    			  mActivity.finish();
    		  }       
    	  });
    	  builders.show();
	  }
	  else
		  Log.d("HttpTest"," --------The network is available-----------------");  
 }   
    
  public static boolean isNetworkAvailable( Activity mActivity ) 
  { 
	  Context context = mActivity.getApplicationContext();
	  ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	  
	  if (connectivity == null) 
	  {   
    	Log.d("isNetworkAvailable"," connectivity is null");
    	return false;
	  } 
	  else 
	  {  
    	Log.d("isNetworkAvailable"," connectivity is not null");
    	
    	NetworkInfo[] info = connectivity.getAllNetworkInfo();   
    	if (info != null) 
    	{   
    		Log.d("isNetworkAvailable"," info is not null");
    		for (int i = 0; i <info.length; i++) 
    		{ 
    			if (info[i].getState() == NetworkInfo.State.CONNECTED)
    			{
    				return true; 
    			}
    			else
    				Log.d("isNetworkAvailable"," info["+i+"] is not connected");
    	     }     
    	 } 
    	else
    		Log.d("isNetworkAvailable"," info is null");
     }   
	  return false;
 }   
    
}