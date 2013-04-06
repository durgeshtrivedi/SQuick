/*
 * Copyright (c) 2003 - 2012 Sileria, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package com.sileria.android;

import static android.provider.Settings.Secure.ANDROID_ID;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.sileria.util.Utils;

/**
 * Factory class for getting <code>Context</code> based utility class instances.
 *
 * <strong>IMPORTANT NOTE</strong>: The API must be initialized by calling {@link #init(Context)} method before using any API.</code>
 * Ideal place to initialize the API is in {@link android.app.Application#onCreate()} method as following:
 *
 * <blockquote><pre>
 * public void onCreate () {
 *     Kit.init( getApplicationContext() );
 *
 *     super.onCreate();
 * }
 * </pre></blockquote>
 * <p/>
 *
 * <strong>IMPORTANT NOTE</strong>: The API must be destroyed by calling {@link #destroy()} when the application is exiting.</code>
 * Ideal place to destory the <code>Kit</code> is in {@link android.app.Application#onTerminate()} method as following:
 *
 * <blockquote><pre>
 * public void onTerminate () {
 *     super.onTerminate();
 *
 *     Kit.destroy();
 * }
 * </pre></blockquote>
 *
 * <strong>Subclassing <code>android.app.Application</code></strong>:<br/>
 * 1. In your AndroidManifest.xml put the name attribue for you Application
 *
 * <blockquote><pre>
 * &lt;application android:name=".MyApplication"
 *              android:icon="@drawable/app_icon"
 *              android:label="@string/app_name"&gt;
 * </pre></blockquote>
 *
 * 2. Sub-class {@linkplain android.app.Application}
 * <blockquote><pre>
 * public MyApplication extends Application
 * </pre></blockquote>
 *
 * Alternatively you can also sub-class {@link Application}
 * to extend your own Application class from or simply define
 * the provided {@linkplain Application} into your AndroidManifest.xml:
 *
 * <blockquote><pre>
 * &lt;application android:name="com.sileria.android.Application"
 *              android:icon="@drawable/app_icon"
 *              android:label="@string/app_name"&gt;
 * </pre></blockquote>
 *
 * @author Ahmed Shakil
 * @date Jan 01, 2010
 */
public final class Kit {

	/**
	 * Application Context.
	 */
	private Context ctx;

	/**
	 * Singleting KIT instance.
	 */
	private static Kit instance;

	/**
	 * Emulator flag.
	 */
	private static final boolean EMULATOR 			= Build.MODEL.endsWith( "sdk" );

	/**
	 * Aniqroid TAG.
	 */
	public static String TAG = "Aniqroid";


	/**
	 * Get the singleton instance of the Kit.
	 */
	public static Kit getInstance () {
		if (instance == null)
			throw new IllegalStateException( "Kit.init() has never been called." );

		return instance;
	}

	/**
     * Initialize the <code>Resource</code> object with an application context.
     * <p/>
     * NOTE: Kit MUST be initialized with this method before using any class of the Aniqroid API.
     *
     * @param ctx Make sure to provide application context here.
     */
    public static void init (Context ctx) {
        Resource.init( ctx );
		com.sileria.util.Log.setLogger( new Logger( TAG ) );
		if (instance == null)
			instance = new Kit( ctx );
		else
			instance.ctx = ctx;
    }

	/**
	 * Destory MUST be called when the application is exiting.
	 */
	public static void destroy () {
		if (instance != null) {
			instance.ctx = null;
			instance = null;
		}
	}

	/**
	 * Constructor, private.
	 */
	private Kit (Context ctx) {
		this.ctx = ctx;
	}

	/**
	 * Get the application context provided to the {@link #init(Context)} method.
	 */
	public static Context getAppContext () {
		return getInstance().ctx;
	}

	/**
	 * Return the current configuration that is in effect for this resource
	 * object.  The returned object should be treated as read-only.
	 *
	 * @return The resource's current configuration.
	 */
	public static Configuration getConfiguration () {
		return getInstance().ctx.getResources().getConfiguration();
	}

	/**
	 * Return the current display metrics that are in effect for this resource
	 * object.  The returned object should be treated as read-only.
	 *
	 * @return The resource's current display metrics.
	 */
	public DisplayMetrics getDisplayMetrics () {
		return getInstance().ctx.getResources().getDisplayMetrics();
	}

	/**
	 * Get the default display.
	 */
	public static Display getDisplay () {
		return ((WindowManager)getSystemService( Context.WINDOW_SERVICE )).getDefaultDisplay();
	}

	/**
	 * Calculates the diagonal display size in inches.
	 * NOTE: intended use of this method is to be called once in the beginning
	 * of your app. Donot frequently call this method since it is slightly
	 * expense to perform calculations every time.
	 */
	public static double calcDisplaySize () {
		DisplayMetrics dm = new DisplayMetrics();
		getDisplay().getMetrics( dm );

		double width = dm.widthPixels / dm.xdpi;
		double height = dm.heightPixels / dm.ydpi;

		double size = Math.sqrt( width * width + height * height );
		return Math.round( size * 10 ) / 10.;
	}

	/**
	 * Get default shared preferences.
	 */
	public static SharedPreferences getPreferences () {
		return PreferenceManager.getDefaultSharedPreferences( getInstance().ctx );
	}

	/**
	 * Get shared preferences with specified file name.
	 */
	public static SharedPreferences getPreferences (String name) {
		return getInstance().ctx.getSharedPreferences( name, Context.MODE_PRIVATE );
	}

	/**
	 * Convenience method to get the system service.
	 * @param service system service name
	 */
	public static Object getSystemService (String service) {
		return getInstance().ctx.getSystemService( service );
	}

	/**
	 * Convenience method to check whether a system service is supported on the platform.
	 * @param service system service name
	 */
	public static boolean hasSystemService (String service) {
		return getSystemService( service ) != null;
	}

	/**
	 * Convenience method to get the 64-bit number (as a hex string) that is randomly
	 * generated on the device's first boot and should remain constant for the lifetime
	 * of the device (which may change after a factory reset).
	 *
	 * @see android.provider.Settings.Secure#ANDROID_ID
	 */
	public static String getAndroidId () {
		return Settings.Secure.getString( getInstance().ctx.getContentResolver(), ANDROID_ID );
	}

	/**
	 * Browse specified url.
	 *
	 * @param url web address
	 */
	public static void browse (Context ctx, String url) {
		ctx.startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse( url ) ) );
	}

	/**
	 * Start the email client with the email <code>to</code> prefilled.
	 * @param ctx calling activity
	 * @param to email to address
	 */
	public static void email (Context ctx, String to) {
		email( ctx, null, null, to );
	}

	/**
	 * Start the email client with the all fields filled as specified.
	 * @param ctx calling activity
	 * @param subject subject line (can be null)
	 * @param message msg text (can be null)
	 * @param to email to address (cannot be null)
	 */
	public static void email (Context ctx, String subject, String message, String ... to) {

		String addr = to == null || to.length == 0 ? "" : to[0];
		Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "mailto:" + addr ) );

		if (to != null)
			intent.putExtra(Intent.EXTRA_EMAIL, to);

		if (subject != null)
			intent.putExtra(Intent.EXTRA_SUBJECT, subject);

		if (message != null)
			intent.putExtra(Intent.EXTRA_TEXT, message );

		ctx.startActivity( intent );
	}

	/**
	 * Start the send client with the all fields filled as specified.
	 * @param ctx calling activity
	 * @param subject subject line (can be null)
	 * @param message msg text (can be null)
	 */
	public static void send (Context ctx, String subject, String message) {

		Intent intent = new Intent( Intent.ACTION_SEND );
		intent.setType( "text/plain" );

		if (subject != null)
			intent.putExtra( Intent.EXTRA_SUBJECT, subject );

		if (message != null)
			intent.putExtra( Intent.EXTRA_TEXT, message );

		ctx.startActivity( intent );
	}

	/**
	 * Start the send client with the all fields filled as specified.
	 * @param ctx calling activity
	 * @param subject subject line (can be null)
	 * @param message msg text (can be null)
	 * @param chooserTitle Title to show for picker
	 */
	public static void send (Context ctx, String subject, String message, String chooserTitle) {

		Intent intent = new Intent( Intent.ACTION_SEND );
		intent.setType( "text/plain" );

		if (subject != null)
			intent.putExtra( Intent.EXTRA_SUBJECT, subject );

		if (message != null)
			intent.putExtra( Intent.EXTRA_TEXT, message );

		ctx.startActivity( Intent.createChooser( intent, chooserTitle ) );
	}

	/**
	 * Start the send client with the all fields filled as specified and matching the pkg regex provided.
	 * @param ctx calling activity
	 * @param subject subject line (can be null)
	 * @param message msg text (can be null)
	 * @param chooserTitle Title to show for picker
	 * @param pkgRegex regular expression to look through available packages
	 */
	public static void send (Context ctx, String subject, String message, String chooserTitle, String pkgRegex) {
		Intent intent = createChooser( ctx, subject, message, chooserTitle, pkgRegex );
		if (intent != null)
			ctx.startActivity( intent );
	}

	/**
	 * Create a share intent with specified pkg regex.
	 * @param ctx calling activity
	 * @param subject subject line (can be null)
	 * @param message msg text (can be null)
	 * @param chooserTitle Title to show for picker
	 * @param pkgRegex regular expression to look through available packages
	 */
	public static Intent createChooser (Context ctx, String subject, String message, String chooserTitle, String pkgRegex) {

		Intent shareIntent = new Intent( android.content.Intent.ACTION_SEND );
		shareIntent.setType( "text/plain" );
		List<ResolveInfo> resInfoList = ctx.getPackageManager().queryIntentActivities( shareIntent, 0 );

		if (resInfoList.isEmpty()) return null;

		List<Intent> apps = new ArrayList<Intent>();
		for (ResolveInfo ri : resInfoList) {

			if (ri.activityInfo.packageName.matches( pkgRegex )) {
				Intent in = new Intent( Intent.ACTION_SEND ).setType( "text/plain" ).setPackage( ri.activityInfo.packageName );

				if (subject != null)
					in.putExtra( Intent.EXTRA_SUBJECT, subject );

				if (message != null)
					in.putExtra( Intent.EXTRA_TEXT, message );

				apps.add( in );
			}
		}

		if (apps.isEmpty()) return null;

		return Intent.createChooser( apps.remove( 0 ), chooserTitle )
				.putExtra( Intent.EXTRA_INITIAL_INTENTS, apps.toArray( new Parcelable[apps.size()] ) );
	}

	/**
	 * Checks if certain intent is available.
	 */
	public static boolean isIntentAvailable (Intent intent) {
		final PackageManager mgr = instance.ctx.getPackageManager();
		List<ResolveInfo> list = mgr.queryIntentActivities( intent, PackageManager.MATCH_DEFAULT_ONLY );
		return !Utils.isEmpty( list );
	}

	/**
	 * Call the phone number on the device.
	 *
	 * @param number phone number to call
	 */
	public static void call (Context ctx, String number) {
		ctx.startActivity( new Intent( Intent.ACTION_CALL, Uri.parse( "tel:" + Utils.defaultIfNull( number, Utils.EMPTY_STRING ) ) ) );
	}

	/**
	 * Show the phone dial pad with the dialed number but not call.
	 *
	 * @param number phone number to dial
	 */
	public static void dial (Context ctx, String number) {
		ctx.startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse( "tel:" + Utils.defaultIfNull( number, Utils.EMPTY_STRING ) ) ) );
	}

	/**
	 * Start the SMS client with the phone <code>number</code> prefilled.
	 * @param ctx calling activity
	 * @param number phone number to sms to
	 */
	public static void sms (Context ctx, String number) {
		ctx.startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + Utils.defaultIfNull( number, Utils.EMPTY_STRING ) ) ) );
	}

	/**
	 * Starts the SMS client to specified number.
	 * @param ctx calling activity
	 * @param number phone number to sms to
	 * @param message message to send
	 */
	public static void sms (Context ctx, String number, String message) {
		Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + Utils.defaultIfNull( number, Utils.EMPTY_STRING ) ) );
		if (message != null)
			intent.putExtra("sms_body", message );

		ctx.startActivity( intent );
	}

	/**
	 * Show specified lat/lon in google maps.
	 *
	 * @param lat latitude
	 * @param lon longitude
	 */
	public static void map (Context ctx, double lat, double lon) {
		Uri uri = Uri.parse("geo:"+lat+","+lon);
		ctx.startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
	}

	/**
	 * Launch the directions intent.
	 *
	 * @param lat latitude
	 * @param lon longitude
	 */
	public static void directions (Context ctx, double lat, double lon) {
		//Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
		//Uri.parse("google.navigation:q=an+address+city");

		Intent intent = new Intent( android.content.Intent.ACTION_VIEW,
				Uri.parse( "http://maps.google.com/maps?f=d&daddr="
					+ lat + "," + lon ) );
		intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		ctx.startActivity( intent );
	}

	/**
	 * Hide keyboard.
	 */
	public static void hideKeyboard (Activity activity) {
		InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		View view = activity.getCurrentFocus();
		if (mgr != null && view != null) {
			mgr.hideSoftInputFromWindow( view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS );
		}
	}

	/**
	 * Check the current network state to be connected or connecting.
	 */
	public static boolean isConnectedOrConnecting() {
		ConnectivityManager cm = (ConnectivityManager)getSystemService( Context.CONNECTIVITY_SERVICE );
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	/**
	 * Check the current network state to be connected.
	 */
	public static boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager)getSystemService( Context.CONNECTIVITY_SERVICE );
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnected();
	}

	/**
	 * Check the current network connection is available.
	 */
	public static boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager)getSystemService( Context.CONNECTIVITY_SERVICE );
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isAvailable();
	}

	/**
	 * Check if the current network is available and connected.
	 */
	public static boolean isNetworkOnline() {
		ConnectivityManager cm = (ConnectivityManager)getSystemService( Context.CONNECTIVITY_SERVICE );
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isAvailable() && netInfo.isConnected();
	}

	/**
	 * Check if GPS location provider is enabled.
	 */
	public static boolean isGPSLocationEnabled () {

		final LocationManager mgr = (LocationManager)Kit.getSystemService( Context.LOCATION_SERVICE );
		try {
			return mgr.isProviderEnabled( LocationManager.GPS_PROVIDER );
		}
		catch (Exception e) {
			Log.d( TAG, e.getLocalizedMessage() );
			return false;
		}
	}

	/**
	 * Check if Network location provider is enabled.
	 */
	public static boolean isNetworkLocationEnabled () {

		final LocationManager mgr = (LocationManager)Kit.getSystemService( Context.LOCATION_SERVICE );
		try {
			return mgr.isProviderEnabled( LocationManager.NETWORK_PROVIDER );
		}
		catch (Exception e) {
			Log.d( TAG, e.getLocalizedMessage() );
			return false;
		}
	}

	/**
	 * Check if any of the GPS or Network location provider is enabled.
	 */
	public static boolean isAnyLocationEnabled () {

		final LocationManager mgr = (LocationManager)Kit.getSystemService( Context.LOCATION_SERVICE );
		try {
			// check for existing providers
			List<String> providers = mgr.getAllProviders();
			if (Utils.isEmpty( providers ))
				return false;

			// scan the list for the specified provider
			for (String provider : providers) {
				if (LocationManager.GPS_PROVIDER.equals( provider )
						|| "passive".equals( provider )
						|| LocationManager.NETWORK_PROVIDER.equals( provider )) {
					if (mgr.isProviderEnabled( provider ))
						return true;
				}
			}
		}
		catch (Throwable e) {
			Log.d( TAG, e.getLocalizedMessage() );
			return false;
		}

		return false;
	}

	/**
	 * Check to see if the specified <code>provider</code> is listed
	 * amongst the supported location providers.
	 *
	 * @param provider location provider name
	 *
	 * @return <code>true</code> if location provider is supported; otherwise <code>false</code>
	 */
	public static boolean isProviderSupported (String provider) {

		final LocationManager mgr = (LocationManager)Kit.getSystemService( Context.LOCATION_SERVICE );

		try {
			List<String> providers = mgr.getAllProviders();

			// scan the list for the specified provider
			for (String prov : providers)
				if (provider.equals( prov ))
					return true;
		}
		catch (Throwable e) {
			// Can throw a SecurityException
			return false;
		}

		return false;
	}

	/**
	 * Returns true when running over emulator.
	 */
	public static boolean isEmulator () {
		return EMULATOR;
	}
}
