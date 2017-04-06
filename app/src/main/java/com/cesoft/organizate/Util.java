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
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;

import com.cesoft.organizate.models.AvisoAbs;
import com.cesoft.organizate.util.Log;

import java.util.HashMap;
import java.util.Locale;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Created by Cesar_Casanova on 02/03/2016
////////////////////////////////////////////////////////////////////////////////////////////////////
public class Util
{
	private static final String TAG = Util.class.getSimpleName();
	//______________________________________________________________________________________________
	// NOTIFICATION UTILS
	//______________________________________________________________________________________________
	public static void playNotificacion(Context c)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		if(prefs.getBoolean("notifications_new_message", true))//true o false ha de coincidir con lo que tengas en pref_notificacion.xml
		{
Log.e(TAG,"-----------------------------Ding Dong!!!!!!!!!");
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
		if(r != null)r.play();
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
	private static void showLights(Context c, int color)
	{
		NotificationManager notif = (NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);
		notif.cancel(1); // clear previous notification
		final Notification notification = new Notification();
		notification.ledARGB = color;
		notification.ledOnMS = 1000;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notif.notify(1, notification);
	}

	//______________________________________________________________________________________________
	// NOTIFICATION
	//______________________________________________________________________________________________
	public static void showAviso(Context c, String sTitulo, AvisoAbs a, Intent intent)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		if(prefs.getBoolean("notifications_new_message_type", true))
		{
			showNotificacionDlg(c, a, intent);
		}
		else
		{
			showNotificacion(c, sTitulo, a, intent);
		}
	}
	//______________________________________________________________________________________________
	private static void showNotificacion(Context c, String titulo, AvisoAbs a, Intent intent)
	{
		PowerManager pm = (PowerManager)c.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
		wakeLock.acquire();
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(c)
				.setSmallIcon(android.R.drawable.ic_menu_mylocation)//R.mipmap.ic_launcher)
				.setLargeIcon(android.graphics.BitmapFactory.decodeResource(c.getResources(), R.mipmap.ic_launcher))
				.setContentTitle(titulo)
				.setContentText(a.getTexto())
				.setContentIntent(PendingIntent.getActivity(c, a.getId().hashCode(), intent, PendingIntent.FLAG_ONE_SHOT))
				.setAutoCancel(true)
				.setDefaults(Notification.DEFAULT_ALL)
				//.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
        		//.setSound(Uri.parse(""))
				;
		NotificationManager notificationManager = (NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(a.getId().hashCode(), notificationBuilder.build());
		wakeLock.release();
	}
	//______________________________________________________________________________________________
	private static void showNotificacionDlg(Context c, AvisoAbs a, Intent intent)
	{
		Intent i = new Intent(c, ActAvisoDlg.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("aviso", a);
		i.putExtra("intent", intent);
		c.startActivity(i);
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
				//if(status != TextToSpeech.ERROR)
				if(status == TextToSpeech.SUCCESS)
					tts.setLanguage(Locale.getDefault());//new Locale("es", "ES");Locale.forLanguageTag("ES")
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
		//Log.e(TAG,"------ttsUnder20");
    	HashMap<String, String> map = new HashMap<>();
    	map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
    	tts.speak(texto, TextToSpeech.QUEUE_FLUSH, map);
	}
	//______________________________________________________________________________________________
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private static void ttsGreater21(Context c, TextToSpeech tts, String texto)
	{
		//Log.e(TAG,"------ttsGreater21");
    	String utteranceId=c.hashCode() + "";
		//if(status == TextToSpeech.SUCCESS) {
    	tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
	}

	//______________________________________________________________________________________________
	/// CONFIG
	//______________________________________________________________________________________________
	public static boolean isAutoArranque(Context c)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
Log.e(TAG,"isAutoArranque------"+prefs.getBoolean("is_auto_arranque", true));
		return prefs.getBoolean("is_auto_arranque", true);
	}


	public static String formatLatLon(double lat, double lon)
	{
		 return String.format("%.5f/%.5f", lat, lon);//Locale.ENGLISH,
	}
}
