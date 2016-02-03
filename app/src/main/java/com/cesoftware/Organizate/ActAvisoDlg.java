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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.cesoftware.Organizate.models.Aviso;

/**
 * Created by Cesar_Casanova on 01/02/2016
 */
//TODO: Make noise? => Settings
//TODO:Settings dialog: Avisos? Run at Startup? Widget Time change? Aviso noise? Aviso time to desapear? Desactivar por (hoy|hora|...)
//TODO: Private but free git host? gitHub make ur code public...
////////////////////////////////////////////////////////////////////////////////////////////////////
public class ActAvisoDlg extends Activity
{
	private static final int CLOSE_DLG = 0;
	private static final long CLOSE_TIME = 60*1000;

	private Aviso _a;

	//______________________________________________________________________________________________
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_aviso_dlg);

		// Automatic close
		Message msg = new Message();
		msg.what = CLOSE_DLG;
		CESHandler dlgHandler = new CESHandler(this);
		dlgHandler.sendMessageDelayed(msg, CLOSE_TIME);

		// Fields
		TextView txtAviso = (TextView)findViewById(R.id.txtAviso);
		Switch swtActivo = (Switch)findViewById(R.id.bActivo);
		swtActivo.setChecked(true);
		swtActivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				_a.setActivo(isChecked);
				_a.save();
				ActAvisoDlg.this.finish();
			}
		});
swtActivo.setVisibility(View.INVISIBLE);
		Button btnDesactivar = (Button)findViewById(R.id.btnDesactivar);
		btnDesactivar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				System.err.println("----------DESACTIVADO POR HOY");
				_a.desactivarPorHoy();
				ActAvisoDlg.this.finish();
			}
		});
		ImageButton btnCerrar = (ImageButton)findViewById(R.id.btnCerrar);
		btnCerrar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ActAvisoDlg.this.finish();
			}
		});

		//------------------------------------------------------------------------------------------
		try
		{
			//String sAviso = this.getIntent().getStringExtra("aviso");//this.getIntent().getExtras().getString("aviso");
			_a = this.getIntent().getParcelableExtra("aviso");
			txtAviso.setText(_a.getTexto());
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
	}

}
