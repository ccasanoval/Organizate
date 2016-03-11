package com.cesoft.organizate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.cesoft.organizate.models.AvisoAbs;


////////////////////////////////////////////////////////////////////////////////////////////////////
// Created by Cesar_Casanova on 01/02/2016
//TODO: Mejorar aspecto: Añadir icono... color de fondo, etc... rounded corners => manifest : android:theme="@android:style/Theme.Dialog"
//TODO: A veces salta dos veces????
////////////////////////////////////////////////////////////////////////////////////////////////////
public class ActAvisoDlg extends Activity
{
	private static final int CLOSE_DLG = 0;
	private static final long CLOSE_TIME = 60*1000;

	private AvisoAbs _a;
	private Intent _i;

	//______________________________________________________________________________________________
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
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

		txtAviso.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ActAvisoDlg.this.startActivity(_i);
				ActAvisoDlg.this.finish();
			}
		});

		//------------------------------------------------------------------------------------------
		try
		{
			_a = getIntent().getParcelableExtra("aviso");
			txtAviso.setText(_a.getTexto());
			_i = getIntent().getParcelableExtra("intent");
		}
		catch(Exception e)
		{
			System.err.println("ActAvisoDlg:onCreate:e:"+e);
			this.finish();
		}
		//------------------------------------------------------------------------------------------
	}

	//______________________________________________________________________________________________
	@Override
	protected void onResume()
	{
		super.onResume();
		Util.playNotificacion(this);
	}

	//______________________________________________________________________________________________
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
