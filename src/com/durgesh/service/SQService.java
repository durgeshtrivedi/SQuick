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
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.OrientationEventListener;

import com.durgesh.view.BottomLeftView;
import com.durgesh.view.BottomRightView;
import com.durgesh.view.TopLeftView;
import com.durgesh.view.TopRightView;

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

    private OrientationEventListener sqOrientationListener;

    @Override
    public void onCreate() {
        super.onCreate();
        sqTopLeftView = new TopLeftView(getApplicationContext());
        sqTopRightView = new TopRightView(getApplicationContext());
        sqBottomLeftView = new BottomLeftView(getApplicationContext());
        sqBottomRightView = new BottomRightView(getApplicationContext());
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
        sqTopLeftView.updateViewParameter();
        // Set the position of the Bottom left View
        sqBottomLeftView.updateViewParameter();
        // Set the position of the Top Right View
        sqTopRightView.updateViewParameter();
        // Set the position of the Bottom Right View
        sqBottomRightView.updateViewParameter();

    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
}
