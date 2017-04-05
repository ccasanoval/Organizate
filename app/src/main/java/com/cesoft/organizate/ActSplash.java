package com.cesoft.organizate;
 
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

////////////////////////////////////////////////////////////////////////////////////////////////////
//TODO: Mejorar imagen de splash!
public class ActSplash extends Activity
{
	private static final int STOPSPLASH1 = 0;
	private static final int STOPSPLASH2 = 1;
	private static final long SPLASHTIME1 = 3500;
	private static final long SPLASHTIME2 = 6000;
   
	private ImageView _splash;

	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.act_splash);
		_splash = (ImageView)findViewById(R.id.splashscreen);
		Message msg = new Message();
		msg.what = STOPSPLASH1;
		CESHandler splashHandler = new CESHandler(this);
		splashHandler.sendMessageDelayed(msg, SPLASHTIME1);
		msg = new Message();
		msg.what = STOPSPLASH2;
		splashHandler.sendMessageDelayed(msg, SPLASHTIME2);

		// Cerrar al clickar
		View.OnClickListener click = new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ActSplash.this.finish();
			}
		};
		_splash.setOnClickListener(click);
		TextView tv = (TextView)findViewById(R.id.splashscreen2);
		tv.setOnClickListener(click);
	}
   
	private static class CESHandler extends Handler
	{
		private ActSplash _win;
		public CESHandler(ActSplash win){_win = win;}
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			//case STOPSPLASH0: setOnClickListener para cerrar ventana y asi no cerrar sin querer...
			case STOPSPLASH1:
				_win._splash.setVisibility(View.GONE);
				break;
			case STOPSPLASH2:
				_win.finish();
				break;
			}
			super.handleMessage(msg);
		}
	}
	/*private Handler splashHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case STOPSPLASH1:
				splash.setVisibility(View.GONE);
				break;
			case STOPSPLASH2:
				ActSplash.this.finish();
				break;
			}
			super.handleMessage(msg);
		}
	};*/
}
