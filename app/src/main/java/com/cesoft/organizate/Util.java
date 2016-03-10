package com.cesoft.organizate;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;

import com.cesoft.organizate.models.AvisoAbs;

import java.util.HashMap;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Created by Cesar_Casanova on 02/03/2016
////////////////////////////////////////////////////////////////////////////////////////////////////
public class Util
{
	//______________________________________________________________________________________________
	// NOTIFICATION UTILS
	//______________________________________________________________________________________________
	public static void playNotificacion(Context c)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		if(prefs.getBoolean("notifications_new_message", true))//true o false ha de coincidir con lo que tengas en pref_notificacion.xml
		{
System.err.println("-----------------------------Ding Dong!!!!!!!!!");
			String sound = prefs.getString("notifications_new_message_ringtone", "");
			if( ! sound.isEmpty())
				playSound(c, Uri.parse(sound));

			if(prefs.getBoolean("notifications_new_message_vibrate", true))
				vibrate(c);
		}
	}

	//______________________________________________________________________________________________
	private static void playSound(Context c, Uri sound)
	{
		if(sound == null)
			sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(c, sound);
		r.play();
	}
	//______________________________________________________________________________________________
	private static void vibrate(Context c)
	{
        Vibrator vibrator = (Vibrator)c.getSystemService(Context.VIBRATOR_SERVICE);
		//long pattern[]={0,200,100,300,400};//pattern for vibration (mili seg ?)
		// 0 = start vibration with repeated count, use -1 if you don't want to repeat the vibration
		//vibrator.vibrate(pattern, -1);
		vibrator.vibrate(1000);//1seg
		//vibrator.cancel();
    }

	//______________________________________________________________________________________________
	// NOTIFICATION
	//______________________________________________________________________________________________
	public static void showAviso(Context c, String sTitulo, AvisoAbs a, Intent intent)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		if(prefs.getBoolean("notifications_new_message_type", true))
		{
			showNotificacionDlg(c, a);
		}
		else
		{
			showNotificacion(c, sTitulo, a.getTexto(), intent);
		}
	}
	//______________________________________________________________________________________________
	private static void showNotificacion(Context c, String titulo, String texto, Intent intent)
	{
		PowerManager pm = (PowerManager)c.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
		wakeLock.acquire();
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(c)
				.setSmallIcon(android.R.drawable.ic_menu_mylocation)//R.mipmap.ic_launcher)
				.setContentTitle(titulo)
				.setContentText(texto)
				.setDefaults(Notification.DEFAULT_ALL)
				.setContentIntent(PendingIntent.getActivity(c, 0, intent, 0))
				.setAutoCancel(false);
		NotificationManager notificationManager = (NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notificationBuilder.build());
		wakeLock.release();
	}
	//______________________________________________________________________________________________
	private static void showNotificacionDlg(Context c, Parcelable p)
	{
		Intent intent = new Intent(c, ActAvisoDlg.class);//getBaseContext()
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(p.getClass().getName(), p);//.getTexto()
		c.startActivity(intent);//getApplication()
		//Util.playNotificacion(c);Aqui no funcionaria
	}


	//______________________________________________________________________________________________
	/// TEXT TO SPEECH
	//______________________________________________________________________________________________
	private static TextToSpeech tts = null;
	public static void hablar(final Context c, String texto)
	{
		if(tts == null)
		tts = new TextToSpeech(c.getApplicationContext(), new TextToSpeech.OnInitListener()
		{
   			@Override
   			public void onInit(int status)
			{
				if(status != TextToSpeech.ERROR)
					tts.setLanguage(c.getResources().getConfiguration().locale);//new Locale("es", "ES");Locale.forLanguageTag("ES")
			}
		});
		//tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);//DEPRECATED
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
    		ttsGreater21(c, tts, texto);
		else
    		ttsUnder20(tts, texto);

	}
	//______________________________________________________________________________________________
	@SuppressWarnings("deprecation")
	private static void ttsUnder20(TextToSpeech tts, String texto)
	{
		//System.err.println("------ttsUnder20");
    	HashMap<String, String> map = new HashMap<>();
    	map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
    	tts.speak(texto, TextToSpeech.QUEUE_FLUSH, map);
	}
	//______________________________________________________________________________________________
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private static void ttsGreater21(Context c, TextToSpeech tts, String texto)
	{
		//System.err.println("------ttsGreater21");
    	String utteranceId=c.hashCode() + "";
    	tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
	}
}
