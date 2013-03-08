/**Copyright (c) 2013 Durgesh Trivedi

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.durgesh.pref;

import android.content.Context;
import android.content.SharedPreferences;

public final class SQPrefs {
    private static final int MODE_PRIVATE = Context.MODE_PRIVATE;
    private static final int MODE_MULTI_PROCESS = 4; // since API9 Context.MODE_MULTI_PROCESS;

    private static final String PREFERENCES_KEY = "SQPreference";

    /**
     * Return SharedPreferences
     */
    public static SharedPreferences getSharedPreferences(final Context context) {
        return getSharedPreferences(context, PREFERENCES_KEY);
    }

    /**
     * Return SharedPreferences
     */
    public static SharedPreferences getSharedPreferences(final Context context, final String key) {
        return context.getSharedPreferences(key, MODE_PRIVATE);
    }

    /**
     * Set a Integer value in the preferences
     **/
    public static void setSharedPreference(final Context context, final String key, final int val) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(key, val);
        editor.commit();
    }

    /**
     * Set a Boolean value in the preferences
     **/
    public static void setSharedPreference(final Context context, final String key, final boolean val) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(key, val);
        editor.commit();
    }

    /**
     * Set a String value in the preferences
     **/
    public static void setSharedPreference(final Context context, final String key, final String val) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, val);
        editor.commit();
    }

    /**
     * Return a Integer value from the preferences.
     **/
    public static int getSharedPreferenceAsInt(final Context context, final String key, final int defaultValue) {
        return getSharedPreferences(context).getInt(key, defaultValue);
    }

    /**
     * Return a Boolean value from the preferences.
     **/
    public static boolean getSharedPreferenceAsBool(final Context context, final String key, final boolean defaultValue) {
        return getSharedPreferences(context).getBoolean(key, defaultValue);
    }

    /**
     * Return a String value from the preferences.
     **/
    public static String getSharedPreferenceAsStr(final Context context, final String key, final String defaultValue) {
        return getSharedPreferences(context).getString(key, defaultValue);
    }

}