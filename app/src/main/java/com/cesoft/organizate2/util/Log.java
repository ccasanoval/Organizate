package com.cesoft.organizate2.util;

import com.cesoft.organizate2.BuildConfig;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Created by booster-bikes on 09/02/2017.
//
public class Log
{
	public static void e(String TAG, String msg)
	{
		if(BuildConfig.DEBUG) android.util.Log.e(TAG, msg);
	}
	public static void e(String TAG, String msg, Throwable t)
	{
		if(BuildConfig.DEBUG) android.util.Log.e(TAG, msg, t);
	}
}
