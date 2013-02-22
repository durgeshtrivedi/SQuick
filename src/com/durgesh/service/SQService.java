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

    private int sqScreenWidth;
    private int sqScreenHeight;
    private static final int SQ_VIEW_WIDTH = 20;
    private static final int SQ_VIEW_HEIGHT = 25;

    // Position TOP view LEFT and RIGHT
    private static final int SQ_TOP_VIEW_POSITION_RATIO = 5;
    private static final int SQ_BOTTOM_VIEW_POSITION_RATIO = 4;

    private OrientationEventListener sqOrientationListener;

    private View sqViewLeft, sqViewRight, sqViewLeftBottom, sqViewRightBottom;

    @Override
    public void onCreate() {
        super.onCreate();
        sqTopLeftView = new TopLeftView(this);
        sqTopRightView = new TopRightView(this);
        sqBottomLeftView = new BottomLeftView(this);
        sqBottomRightView = new BottomRightView(this);

        sqViewLeft = sqTopLeftView.inflateView();

        sqViewRight = sqTopRightView.inflateView();

        sqViewLeftBottom = sqBottomLeftView.inflateView();
        sqViewRightBottom = sqBottomRightView.inflateView();

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
     * Set the Transparency for the view
     */
    private void applyTransparency(View v, int amount) {
        // apply transparency, is there a better way?
        float transparency = amount;
        float finalAlpha = (100f - transparency) / 100f;

        Animation alpha = new AlphaAnimation(finalAlpha, finalAlpha);
        alpha.setDuration(0);
        alpha.setFillAfter(true);

        // need to create an animation controller since its empty by default and the animation doesn't work
        ((ViewGroup) v).setLayoutAnimation(new LayoutAnimationController(alpha, 0));
    }

   
    /**
     * Change the view size when the device Orientation is changed
     */
    public void initOrientation() {
        // Set the position of the Top left View
        updateViewParameter(sqViewLeft, 0, SQ_TOP_VIEW_POSITION_RATIO, Gravity.LEFT | Gravity.TOP);

        // Set the position of the Bottom left View
        updateViewParameter(sqViewLeftBottom, 0, SQ_BOTTOM_VIEW_POSITION_RATIO, Gravity.LEFT);

        // Set the position of the Top Right View
        updateViewParameter(sqViewRight, 1, SQ_TOP_VIEW_POSITION_RATIO, Gravity.TOP);

        // Set the position of the Bottom Right View
        updateViewParameter(sqViewRightBottom, 1, SQ_BOTTOM_VIEW_POSITION_RATIO, -10);

    }

    /**
     * update the View with Screen orientation
     * 
     * @param sqView
     *            view to display
     * @param xAxis
     *            position of view on xAxis in case of Gravity LEFT it is not required thats why come as 0
     * @param ration
     *            give a ration to display the view at particular position(height) with screen height
     * @param gravity
     *            Gravity of the view to display on the screen
     */
    private void updateViewParameter(View sqView, int xAxis, int ration, int gravity) {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        // save screen width/height
        Display display = wm.getDefaultDisplay();
        sqScreenWidth = display.getWidth();
        sqScreenHeight = display.getHeight();
        // To give View the size according to Screen size take ration from screen size
        int displayHeight = sqScreenHeight * SQ_VIEW_HEIGHT / 100;
        WindowManager.LayoutParams paramsRB = (WindowManager.LayoutParams) sqView.getLayoutParams();
        paramsRB.x = xAxis == 0 ? 0 : sqScreenWidth;
        paramsRB.y = sqScreenHeight / ration;
        paramsRB.width = SQ_VIEW_WIDTH;
        paramsRB.height = displayHeight;
        paramsRB.gravity = gravity != -10 ? gravity : Gravity.NO_GRAVITY;
        wm.updateViewLayout(sqView, paramsRB);
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
