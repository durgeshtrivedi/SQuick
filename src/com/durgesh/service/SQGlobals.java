/**Copyright (c) 2013 durgesh trivedi

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Application;
import android.content.Intent;
import android.content.res.AssetManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class SQGlobals extends Application {
    private RootContext mRootContext = null;
    private String android_id = null;
    private boolean mBooted = false;

    public SQGlobals() {
        // TODO Auto-generated constructor stub
    }

    // interface to root context class
    public RootContext getRootContext() throws Exception {
        if (mRootContext == null) {
            // set up env and run the context
            String wd = getFilesDir().getAbsolutePath();
            File jar = new File(wd + "/RemoteContext.jar");
            if (true) {
                AssetManager m = getResources().getAssets();
                InputStream in = m.open("input/RemoteContext.jar");
                FileOutputStream out = new FileOutputStream(jar);
                int read;
                byte[] b = new byte[4 * 1024];
                while ((read = in.read(b)) != -1) {
                    out.write(b, 0, read);
                }
                out.close();
                in.close();
            }

            if (android_id == null) {
                // to run in the emulator
                // adb shell
                // # mkdir /data/tmp
                // # cat /system/bin/sh > /data/tmp/su
                // # chmod 6755 /data/tmp/su
                // # mount -oremount,suid /dev/block/mtdblock1 /data
                error("Detected emulator");
                mRootContext = new RootContext("/data/tmp/su", wd);
            } else {
                mRootContext = new RootContext("su", wd);
            }
        }

        return (mRootContext);
    }

    /**
     * this used to be oncreate, but we can't have it there since we register for a boot receiver now since the only entry points are the receiver and
     * keys, we'll run this from their oncreate or whatever and it will only run once then we'll flag it so we don't do it again
     */
    public void bootup() {
        if (!mBooted) {
            // warn if we don't notice some binaries we need
            for (String name : new String[] { "/system/bin/su" }) {
                File check = new File(name);
                try {
                    if (!check.exists()) {
                        Toast.makeText(this, "Failed to find file: " + name + ", SoftKeys may not function", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Unable to check for file: " + name, Toast.LENGTH_LONG).show();
                }

            }

            android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

            // init the shell
            try {
                getRootContext();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to initialize root context", Toast.LENGTH_LONG);
            }

            // restartService();
            // initNotifications();
            mBooted = true;
        }
    }

    public void restartService() {
        // start the service
        this.stopService(new Intent(this, SQService.class));
        // SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences( this );
        // if( settings.getBoolean( "service", true ) ) {
        // this.startService( new Intent( this, SQService.class ) );
        // }
        this.startService(new Intent(this, SQService.class));
    }

    public class RootContext {
        Process p;
        OutputStream o;

        RootContext(String shell, String workingDir) throws Exception {
            // Log.d( "SoftKeys.RootContext", "Starting shell: '" + shell + "'" );
            p = Runtime.getRuntime().exec(shell);
            o = p.getOutputStream();

            // spawn our context
            system("export CLASSPATH=" + workingDir + "/RemoteContext.jar");
            system("exec app_process " + workingDir + " co.durgesh.android.RemoteContext");
        }

        private void system(String cmd) throws Exception {
            // Log.d( "SoftKeys.RootContext", "Running command: '" + cmd + "'" );
            o.write((cmd + "\n").getBytes("ASCII"));
        }

        // slightly renamed since we're not running system("cmd") anymore but
        // RootContext commands
        public void runCommand(String cmd) throws Exception {
            system(cmd);
        }

        public void close() throws Exception {
            // Log.d( "SoftKeys.RootContext", "Destroying shell" );
            o.flush();
            o.close();
            p.destroy();
        }
    }

    public static void error(String message) {
        Log.e("SQSerive >>>>>>", message);
    }
}
