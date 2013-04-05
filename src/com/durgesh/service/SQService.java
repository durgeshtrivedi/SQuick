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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.OrientationEventListener;
import android.view.WindowManager;

import com.durgesh.view.BottomLeftView;
import com.durgesh.view.BottomRightView;
import com.durgesh.view.SQMainVeiw;
import com.durgesh.view.TopLeftView;
import com.durgesh.view.TopRightView;
import com.sileria.android.Kit;

/**
 * The main application Service which run all the time in the background and make the application live .
 * 
 * @author durgesht
 */
public class SQService extends Service {
    TopLeftView sqTopLeftView;
    TopRightView sqTopRightView;
    BottomLeftView sqBottomLeftView;
    BottomRightView sqBottomRightView;
    boolean leftView, rightView, leftBottomView, rightBottomView;
    Context context;
    private OrientationEventListener sqOrientationListener;
    

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Kit.init(context);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        leftView = settings.getBoolean("checkbox_leftbar", true);
        if (leftView && sqTopLeftView == null) {
            sqTopLeftView = new TopLeftView(context);
        }

        rightView = settings.getBoolean("checkbox_rightbar", true);
        if (rightView && sqTopRightView == null) {
            sqTopRightView = new TopRightView(context);
        }

        leftBottomView = settings.getBoolean("checkbox_leftbottombar", true);
        if (leftBottomView && sqBottomLeftView == null) {
            sqBottomLeftView = new BottomLeftView(context);
        }

        rightBottomView = settings.getBoolean("checkbox_rightbottombar", true);
        if (rightBottomView && sqBottomRightView == null) {
            sqBottomRightView = new BottomRightView(context);
        }

        sqOrientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                initOrientation();
            }
        };
        sqOrientationListener.enable();
        initOrientation();
        
    }

    /**
     * Change the view size when the device Orientation is changed
     */
    public void initOrientation() {
        // Set the position of the Top left View
        if (leftView && sqTopLeftView != null) sqTopLeftView.updateViewParameter();

        // Set the position of the Bottom left View
        if (leftBottomView && sqBottomLeftView != null) sqBottomLeftView.updateViewParameter();

        // Set the position of the Top Right View
        if (rightView && sqTopRightView != null) sqTopRightView.updateViewParameter();

        // Set the position of the Bottom Right View
        if (rightBottomView && sqBottomRightView != null) sqBottomRightView.updateViewParameter();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*
         * TODO Should not remove all the view from the service need to improve the logic here . The better way is to create a service for each view ,
         * so when we disable a particular view only that service is stop and only that view is removed from windows manager all other are unchanged
         * .So need to implement 4 services here
         */
        removeBar(sqTopLeftView);
        removeBar(sqTopRightView);
        removeBar(sqBottomLeftView);
        removeBar(sqBottomRightView);
        sqTopLeftView = null;
        sqTopRightView = null;
        sqBottomLeftView = null;
        sqBottomRightView = null;

    }

    private void removeBar(SQMainVeiw view) {
        if (view != null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(view);
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
}
