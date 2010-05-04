package com.example.TestApp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ActivityScrollbar extends Activity implements OnSeekBarChangeListener  	{
	
	SeekBar seekBar;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d("ActivityScrollbar", "    onCreate");
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.androidscrollbar);   
        
        seekBar = (SeekBar)findViewById(R.id.SeekBar01);
        seekBar.setOnSeekBarChangeListener(this);
       // this.findViewById(id)
        
    }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		Log.d("ActivityScrollbar", "    onProgressChanged");
		
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		Log.d("ActivityScrollbar", "    onStartTrackingTouch");
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		Log.d("ActivityScrollbar", "    onStopTrackingTouch");
		
	}
}
