package com.example.AndroidScrollBar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class AndroidScrollBar extends Activity {
	
	final int MINIMUM_COR  = 90;
	final int MAXIMUM_COR  = 670;
	final int X_POS        = 70;
	
	int y_cor = MINIMUM_COR;
	int slider_height = 0;
	
	Bitmap bmp;
	Bitmap bmpBg;
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ClassPaint(this));
           
    }
    
    public class ClassPaint extends View
    {   	
    public ClassPaint(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			
		    bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.slider);
		    bmpBg = BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);  
		    slider_height = bmp.getHeight();
			//Log.d("ClassPaint","           constructor");
		}

    @Override
    public void onDraw(Canvas canvas)
    {
    	Log.d("ClassPaint","           getHeight() =" +getHeight());
    	    	
        canvas.drawBitmap(bmpBg, 0, 0, null);  	
        canvas.drawBitmap(bmp, X_POS, y_cor - slider_height/2, null);
    	
    }
    @Override  
   public boolean onTouchEvent(MotionEvent event)
   {
   
	   y_cor = (int) event.getY();
	   Log.d("ClassPaint","           y_cor = "+y_cor);
	   
	   if ((y_cor>= MINIMUM_COR)&&(y_cor <= MAXIMUM_COR))
	   {
		   invalidate();
	   }
	   //invalidate(30,y_cor,30+276,y_cor+71);

	   return true;
   }
    
   }
    
    
    
    
}