package com.cesoft.organizate;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.cesoft.organizate.util.Log;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of the list of settings.<p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
////////////////////////////////////////////////////////////////////////////////////////////////////
//TODO: Utilizar sugar para guardar config, asi puedes exportar todo de una vez?
public class ActConfig extends PreferenceActivity //AppCompatActivity
{
	private static final String TAG = ActConfig.class.getSimpleName();
	/*@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if(id == android.R.id.home)
		{
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onHeaderClick(Header header, int position)
	{
		super.onHeaderClick(header, position);
		if(header.id == R.id.send_by_email)
		{
			setResult(Activity.RESULT_OK, new Intent());
			finish();
		}
	}*/

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setupActionBar();

		/*Preference export = findPreference("send_by_email");
		if(export != null)
		export.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
		{
			public boolean onPreferenceClick(Preference preference)
			{
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				//i.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
				i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) +" : Export");
				i.putExtra(Intent.EXTRA_TEXT   , "body of email");
				try
				{
    				startActivity(Intent.createChooser(i, "Send mail..."));
				}
				catch (android.content.ActivityNotFoundException ex)
				{
    				//Toast.makeText(MyActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});*/

		/*setContentView(R.layout.act_config);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new MyPreferenceFragment()).commit();

        layout : act_config.xml
        <?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.v7.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_height="?attr/actionBarSize"
        android:layout_width="match_parent"
        android:minHeight="?attr/actionBarSize"
        android:background="?attr/colorPrimary"
        app:theme="@style/AppTheme.AppBarOverlay"
        app:popupTheme="@style/AppTheme.PopupOverlay"/>

    <FrameLayout
        android:id="@+id/content_frame"
        android:layout_below="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

</RelativeLayout>
        */
	}
	/*public static class MyPreferenceFragment extends PreferenceFragment
	{
    	@Override
    	public void onCreate(final Bundle savedInstanceState)
		{
        	super.onCreate(savedInstanceState);
        	addPreferencesFromResource(R.xml.pref_headers);//pref_general
    	}
	}*/
	/**
	 * {@inheritDoc}
	 */
	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target)
	{
		loadHeadersFromResource(R.xml.pref_headers, target);
	}

	private void setupActionBar()
	{
		ActionBar actionBar = getActionBar();
		if(actionBar != null)
		{
			// Show the Up button in the action bar.
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onIsMultiPane()
	{
		return isXLargeTablet(this);
	}
	// Helper method to determine if the device has an extra-large screen. For example, 10" tablets are extra-large.
	private static boolean isXLargeTablet(Context context)
	{
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	// A preference value change listener that updates the preference's summary to reflect its new value.
	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener()
	{
		@Override
		public boolean onPreferenceChange(Preference preference, Object value)
		{
			String stringValue = value.toString();
			if(preference instanceof ListPreference)
			{
				// For list preferences, look up the correct display value in the preference's 'entries' list.
				ListPreference listPreference = (ListPreference)preference;
				int index = listPreference.findIndexOfValue(stringValue);
				// Set the summary to reflect the new value.
				preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
			}
			else if(preference instanceof RingtonePreference)
			{
				// For ringtone preferences, look up the correct display value using RingtoneManager.
				if(TextUtils.isEmpty(stringValue))
				{
					// Empty values correspond to 'silent' (no ringtone).
					preference.setSummary(R.string.pref_ringtone_silent);
				}
				else
				{
					Ringtone ringtone = RingtoneManager.getRingtone(preference.getContext(), Uri.parse(stringValue));
					if(ringtone == null)
					{
						// Clear the summary if there was a lookup error.
						preference.setSummary(null);
					}
					else
					{
						// Set the summary to reflect the new ringtone display name.
						String name = ringtone.getTitle(preference.getContext());
						preference.setSummary(name);
					}
				}
			}
			else
			{
				// For all other preferences, set the summary to the value's simple string representation.
				preference.setSummary(stringValue);
			}
			return true;
		}
	};

	// Binds a preference's summary to its value. More specifically, when the preference's value is changed, its summary (line of text below the preference title)
	// is updated to reflect the value. The summary is also immediately updated upon calling this method. The exact display format is dependent on the type of preference.
	private static void bindPreferenceSummaryToValue(Preference preference)
	{
		try
		{
			// Set the listener to watch for value changes.
			preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
			// Trigger the listener immediately with the preference's current value.
			sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
		}
		catch(Exception e){
			Log.e(TAG, "bindPreferenceSummaryToValue:e:"+e);}//Para secciones que no hacen nada, como 'sobre esta app...'
	}

	// This method stops fragment injection in malicious applications. Make sure to deny any unknown fragments here.
	protected boolean isValidFragment(String fragmentName)
	{
		return PreferenceFragment.class.getName().equals(fragmentName) || GeneralPreferenceFragment.class.getName().equals(fragmentName) || DataSyncPreferenceFragment.class.getName().equals(fragmentName) || NotificationPreferenceFragment.class.getName().equals(fragmentName);
	}

	//--------------------------------------- SOBRE ESTA APP ---------------------------------------
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class GeneralPreferenceFragment extends PreferenceFragment
	{
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_general);
			setHasOptionsMenu(true);

			try{
				PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
				Preference customPref = findPreference("version");//Look at pref_general.xml
				customPref.setSummary(String.format(getString(R.string.app_vers), pInfo.versionName));
			}catch(Exception e){Log.e(TAG, "GeneralPreferenceFragment:onCreate:e:"+e);}

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences to their values.
			// When their values change, their summaries are updated to reflect the new value, per the Android Design guidelines.
			//bindPreferenceSummaryToValue(findPreference("example_text"));
			//bindPreferenceSummaryToValue(findPreference("example_list"));
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item)
		{
			int id = item.getItemId();
			if(id == android.R.id.home)
			{
				startActivity(new Intent(getActivity(), ActConfig.class));
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
	}

	//--------------------------------------- NOTIFICACIONES ---------------------------------------
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class NotificationPreferenceFragment extends PreferenceFragment
	{
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_notification);
			setHasOptionsMenu(true);
			// Bind the summaries of EditText/List/Dialog/Ringtone preferences to their values.
			// When their values change, their summaries are updated to reflect the new value, per the Android Design guidelines.
			bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item)
		{
			int id = item.getItemId();
			if(id == android.R.id.home)
			{
				startActivity(new Intent(getActivity(), ActConfig.class));
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
	}

	//--------------------------------------- DATOS ---------------------------------------
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class DataSyncPreferenceFragment extends PreferenceFragment
	{
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_data_sync);
			setHasOptionsMenu(true);
			// Bind the summaries of EditText/List/Dialog/Ringtone preferences to their values.
			// When their values change, their summaries are updated to reflect the new value, per the Android Design guidelines.
			//bindPreferenceSummaryToValue(findPreference("sync_frequency"));
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item)
		{
			int id = item.getItemId();
			if(id == android.R.id.home)
			{
				startActivity(new Intent(getActivity(), ActConfig.class));
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
	}
}
