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
package com.durgesh.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class SQBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String param =intent.getAction();
        Log.e("superquick_service", "SERVICE SQBootReceiver");
        if (settings.getBoolean("superquick_service", true)) {
            if (TextUtils.equals(Intent.ACTION_BOOT_COMPLETED,param)) {
                context.startService(new Intent(context, SQService.class));
            } else if (TextUtils.equals(Intent.ACTION_SCREEN_OFF,param)) {
                context.stopService(new Intent(context, SQService.class));
                Log.e("superquick_service", "SERVICE STOP");
            } else if (TextUtils.equals(Intent.ACTION_SCREEN_ON,param)) {
                context.startService(new Intent(context, SQService.class));
                Log.e("superquick_service", "SERVICE START");
            }
        }
    }
}
