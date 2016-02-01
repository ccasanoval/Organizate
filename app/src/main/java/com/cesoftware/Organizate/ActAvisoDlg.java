package com.cesoftware.Organizate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Cesar_Casanova on 01/02/2016
 */
//TODO:switch que permita desactivar termporalmente el aviso => Temporal? fecha_aviso?
////////////////////////////////////////////////////////////////////////////////////////////////////
public class ActAvisoDlg extends Activity
{
	private static final int CLOSE_DLG = 0;
	private static final long CLOSE_TIME = 1*60*1000;
	private String _sAviso="";

	//______________________________________________________________________________________________
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_aviso_dlg);

		Message msg = new Message();
		msg.what = CLOSE_DLG;
		CESHandler dlgHandler = new CESHandler(this);
		dlgHandler.sendMessageDelayed(msg, CLOSE_TIME);

		//------------------------------------------------------------------------------------------
		try
		{
			_sAviso = this.getIntent().getStringExtra("aviso");//this.getIntent().getExtras().getString("aviso");
		}
		catch(Exception e)
		{
			System.err.println("ActAvisoDlg:onCreate:ERROR:"+e);
			this.finish();
		}
		//------------------------------------------------------------------------------------------
	}

	//______________________________________________________________________________________________
	//TODO: do the same in splash? yes
	static class CESHandler extends Handler
	{
		private ActAvisoDlg _win;
		public CESHandler(ActAvisoDlg win){_win = win;}
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case CLOSE_DLG:
				_win.finish();
				break;
			}
			super.handleMessage(msg);
		}
	};

}
