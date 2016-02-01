package com.cesoftware.Organizate;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.IntentService;
import android.content.Intent;

import com.cesoftware.Organizate.models.Aviso;
import com.cesoftware.Organizate.models.Objeto;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

////////////////////////////////////////////////////////////////////////////////////////////////////
/// Created by Cesar_Casanova on 27/01/2016
////////////////////////////////////////////////////////////////////////////////////////////////////
//TODO: Si no hay avisos en bbdd quitar servicio, solo cuando se añada uno, activarlo
//TODO: hacer que el servicio arranque con el sistema, fallará si no llamas a SugarContext.init() ????
public class CesService extends IntentService
{
	private static final long DELAY_LOAD = 2*60*1000;//TODO: ampliar tras debug
	private static final long DELAY_CHECK = 2*60*1000;
	private ArrayList<Aviso> _lista = new ArrayList<>();

	//______________________________________________________________________________________________
	public CesService()
	{
		super("Organizate");
		//SugarContext.init(this);
	}

	//______________________________________________________________________________________________
	@Override
	protected void onHandleIntent(Intent workIntent)
	{
		try
		{
			SugarContext.init(this);

			//String dataString = workIntent.getDataString();
			long tmLoad = System.currentTimeMillis();
			long tmCheck = System.currentTimeMillis();
			while(true)
			{
System.err.println("CesService:onHandleIntent:looping------------");
				Thread.sleep(DELAY_CHECK/2);
				if(tmLoad + DELAY_LOAD < System.currentTimeMillis())
				{
					cargarLista();
					tmLoad = System.currentTimeMillis();
				}
				if(tmCheck + DELAY_CHECK < System.currentTimeMillis())
				{
					checkAvisos();
					tmCheck = System.currentTimeMillis();
				}
			}
		}
		catch(InterruptedException e){System.err.println("CesService:onHandleIntent:------------");}
	}

	//______________________________________________________________________________________________
	private void cargarLista()
	{
System.err.println("CesService----cargarLista----");
		try
		{
			_lista.clear();
			Iterator<Aviso> it = Aviso.getActivos();
			while(it.hasNext())
				_lista.add(it.next());
System.err.println("CesService---------------------cargarLista:"+_lista.size());
		}
		catch(Exception e)
		{
System.err.println("CesService---------------------cargarLista:e:"+e);
			//_lista.clear();
			//_lista = new ArrayList<>();
		}
	}

	//______________________________________________________________________________________________
	private void checkAvisos()
	{
System.err.println("CesService-------checkAvisos----1");
		if(_lista == null || _lista.size() == 0)return;
		for(Aviso a : _lista)
		{
			Calendar now = Calendar.getInstance();
System.err.println("CesService-------checkAvisos----"+now);

			byte[] aMeses = a.getMeses();
			byte[] aDiasMes = a.getDiasMes();
			byte[] aDiasSemana = a.getDiasSemana();
			byte[] aHoras = a.getHoras();
			byte[] aMinutos = a.getMinutos();
System.err.println("CesService-------checkAvisos----5 m:"+now.get(Calendar.MONTH)+1);
			if(aMeses.length > 0 && aMeses[0] != Aviso.TODO)
			{
				boolean b = false;
				for(byte mes : aMeses)
					if(b = now.get(Calendar.MONTH)+1 == mes)
						break;
				if(!b)return;
			}
System.err.println("CesService-------checkAvisos----6 dm:"+now.get(Calendar.DAY_OF_MONTH));
			if(aDiasMes.length > 0 && aDiasMes[0] != Aviso.TODO)
			{
				boolean b = false;
				for(byte diaMes : aDiasMes)
					if(b = now.get(Calendar.DAY_OF_MONTH) == diaMes)
						break;
				if(!b)return;
			}
System.err.println("CesService-------checkAvisos----7 ds:"+now.get(Calendar.DAY_OF_WEEK));
			if(aDiasSemana.length > 0 && aDiasSemana[0] != Aviso.TODO)
			{
				boolean b = false;
				for(byte diaSemana : aDiasSemana)
					if(b = now.get(Calendar.DAY_OF_WEEK) == diaSemana)
						break;
				if(!b)return;
			}
System.err.println("CesService-------checkAvisos----8 hor:"+now.get(Calendar.HOUR_OF_DAY)+" : "+now.get(Calendar.HOUR));
			//TODO: hacer algo para que si no especifico la hora o minuto no se avisa cada 5 min...
			//TODO: hacer algo para desactivar la alrma: On Off & Off in this day...
			if(aHoras.length > 0 && aHoras[0] != Aviso.TODO)
			{
				boolean b = false;
				for(byte hora : aHoras)
					if(b = now.get(Calendar.HOUR_OF_DAY) == hora)
						break;
				if(!b)return;
			}
System.err.println("CesService-------checkAvisos----9 min:"+now.get(Calendar.MINUTE));
			if(aMinutos.length > 0 && aMinutos[0] != Aviso.TODO)//TODO: Mover esto a Aviso::IsDueTime()
			{
				boolean b = false;
				for(byte minuto : aMinutos)
					if(b = (now.get(Calendar.MINUTE)-2 <= minuto && now.get(Calendar.MINUTE)+2 >= minuto) )
						break;
					else System.err.println("CesService-------checkAvisos----PPP:"+now.get(Calendar.MINUTE)+":::"+minuto);
				if(!b)return;
			}

System.err.println("CesService-------checkAvisos----ACTIVA EL AVISO*****************************************************" + a.getTexto());

			/*Intent intent = new Intent(getBaseContext(), ActSplash.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getApplication().startActivity(intent);*/
			Intent intent = new Intent(getBaseContext(), ActAvisoDlg.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("aviso", a.getTexto());
			getApplication().startActivity(intent);
		}
	}
}
