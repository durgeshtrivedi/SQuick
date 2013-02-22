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

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.durgesh.R;
import com.durgesh.view.BottomLeftView;
import com.durgesh.view.BottomRightView;
import com.durgesh.view.TopLeftView;
import com.durgesh.view.TopRightView;

/**
 * The main application Service which run all the time in the background and make the application live .
 * 
 * @author durgesht
 */
public class SQService extends Service implements OnGestureListener {
    SQService serviceCotext = this;
    TopLeftView sqTopLeftView;
    TopRightView sqTopRightView;
    BottomLeftView sqBottomLeftView;
    BottomRightView sqBottomRightView;

    private OrientationEventListener sqOrientationListener;

    @Override
    public void onCreate() {
        super.onCreate();
        sqTopLeftView = new TopLeftView(this);
        sqTopRightView = new TopRightView(this);
        sqBottomLeftView = new BottomLeftView(this);
        sqBottomRightView = new BottomRightView(this);
        sqOrientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                initOrientation();
            }
        };
        sqOrientationListener.enable();

        initOrientation();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
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
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

}
