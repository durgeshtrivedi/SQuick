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
package com.durgesh.quick;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.durgesh.pref.SQPreference;
import com.durgesh.service.SQService;

/**
 * Main class of the application to launch the SuperQuick service the purpose of the class is just to launch the service
 * 
 * @author durgesht
 */
public class SuperQuick extends Activity {

    private final static int PREFERENCE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restartService();
        startActivityForResult(new Intent(this, SQPreference.class), PREFERENCE);

    }

    public void restartService() {
        // start the service
        stopService(new Intent(this, SQService.class));
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        if (settings.getBoolean("superquick_service", true)) {
            startService(new Intent(this, SQService.class));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PREFERENCE) {
            restartService();
            finish();
        }
    }

}
