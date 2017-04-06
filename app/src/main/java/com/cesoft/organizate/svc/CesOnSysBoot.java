package com.cesoft.organizate.svc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cesoft.organizate.util.Util;
import com.cesoft.organizate.util.Log;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Created by Cesar_Casanova on 03/02/2016
////////////////////////////////////////////////////////////////////////////////////////////////////
public class CesOnSysBoot extends BroadcastReceiver
{
 	@Override
	public void onReceive(Context context, Intent intent)
	{
Log.e("CesOnSysBoot", "------------------onReceive : action="+intent.getAction()+":::"+ Util.isAutoArranque(context));
		if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED) && Util.isAutoArranque(context))
		{
			Intent serviceIntent = new Intent(context, CesServiceAviso.class);
			context.startService(serviceIntent);
		}
	}
}
