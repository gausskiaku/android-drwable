package com.example.AndroidLifeCycle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

class foo {
	String _name;
	public foo(String name) {
		Log.d(_name, "============constructor");
		_name = name; 
    }
    protected void finalize()
    {
		Log.d(_name, "============finalize");
    }
}

public class AndroidLifeCycle extends Activity {
    /** Called when the activity is first created. */
	static foo foo1;
	foo foo2;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		Log.d("AndroidLifeCycle", "============onCreate");
		foo1 = new foo("foo1");
		foo2 = new foo("foo2");
    }
    
    @Override
	public void onDestroy() {
		Log.d("AndroidLifeCycle", "============onDestroy");
		super.onDestroy();
	}
        
    @Override
	public void onPause() {
		Log.d("AndroidLifeCycle", "============onPause");
		super.onPause();
	}
    
    @Override
	public void onResume() {
		Log.d("AndroidLifeCycle", "============onResume");
		super.onResume();
	}

    @Override
	public void onStart() {
		Log.d("AndroidLifeCycle", "============onStart");
		super.onStart();
	}

    @Override
	public void onStop() {
		Log.d("AndroidLifeCycle", "============onStop");
		super.onStop();
	}
}