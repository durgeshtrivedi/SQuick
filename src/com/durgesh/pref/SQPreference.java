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

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

import com.durgesh.R;

/**
 * Display Preference Screen of the App
 * 
 * @author durgesht
 */
public class SQPreference extends PreferenceActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        String ver = "unknown";
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.durgesh", PackageManager.GET_META_DATA);
            ver = info.versionName;
        } catch (Exception e) {
        }

        Preference version = findPreference("pref_version");
        version.setSummary(getString(R.string.pref_version_summary, ver));
        findPreference("pref_help").setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                triggerHelp();
                return false;
            }
        });

        final Preference sqservice = findPreference("service_size");
        sqservice.setDependency("superquick_service");
        final Preference tranparency = findPreference("service_transparency");
        tranparency.setDependency("superquick_service");
        final Preference orderSize = findPreference("bar_order_size");
        orderSize.setDependency("superquick_service");

        CheckBoxPreference checkboxservice = (CheckBoxPreference) findPreference("superquick_service");
        CheckBoxPreference checkboxleftbar = (CheckBoxPreference) findPreference("checkbox_leftbar");
        CheckBoxPreference checkboxrightbar = (CheckBoxPreference) findPreference("checkbox_rightbar");
        CheckBoxPreference checkboxleftbottombar = (CheckBoxPreference) findPreference("checkbox_leftbottombar");
        CheckBoxPreference checkboxrightbottombar = (CheckBoxPreference) findPreference("checkbox_rightbottombar");
        if (!checkboxleftbar.isChecked() && !checkboxrightbar.isChecked() && !checkboxleftbottombar.isChecked()
                && !checkboxrightbottombar.isChecked()) {
            checkboxservice.setChecked(false);
        }

        final Preference leftbar = findPreference("left_bar");
        leftbar.setTitle(leftbar.getSharedPreferences().getString("left_bar", getResources().getString(R.string.pref_lefbar_title)));
        leftbar.setDependency("checkbox_leftbar");
        leftbar.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof String) {
                    String title = (String) newValue;
                    leftbar.setTitle(title);
                }
                return true;
            }
        });

        final Preference rightbar = findPreference("right_bar");
        rightbar.setTitle(rightbar.getSharedPreferences().getString("right_bar", getResources().getString(R.string.pref_rightbare_title)));
        rightbar.setDependency("checkbox_rightbar");
        rightbar.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof String) {
                    String title = (String) newValue;
                    rightbar.setTitle(title);
                }
                return true;
            }
        });

        final Preference leftbottombar = findPreference("left_bottom_bar");
        leftbottombar.setDependency("checkbox_leftbottombar");
        leftbottombar.setTitle(leftbottombar.getSharedPreferences().getString("left_bottom_bar",
                getResources().getString(R.string.pref_leftbottombar_title)));

        leftbottombar.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof String) {
                    String title = (String) newValue;
                    leftbottombar.setTitle(title);
                }
                return true;
            }
        });

        final Preference rightbottombar = findPreference("right_bottom_bar");
        rightbottombar.setDependency("checkbox_rightbottombar");
        rightbottombar.setTitle(rightbottombar.getSharedPreferences().getString("right_bottom_bar",
                getResources().getString(R.string.pref_rightbottombar_title)));

        rightbottombar.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof String) {
                    String title = (String) newValue;
                    rightbottombar.setTitle(title);
                }
                return true;
            }
        });

    }

    private void triggerHelp() {
        // Intent intent = new Intent(this, HelpDoc.class);
        // intent.putExtra("type", "help");

        // startActivity(intent);
    }

}
