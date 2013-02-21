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
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout.LayoutParams;

import com.durgesh.R;

/**
 * THe main application Service which run all the time in the background and make the application live .
 * 
 * @author durgesht
 */
public class SQService extends Service {
    SQService serviceCotext = this;
    private final int mOffScreenMax = 20;

    private int mScreenWidth;
    private int mScreenHeight;
    private String mOrientation;

    private View sqViewLeft, sqViewRight, sqViewLeftBottom, sqViewRightBottom;

    @Override
    public void onCreate() {
        super.onCreate();
        SQGlobals.error("onCreate SoftKeysService");
        ((SQGlobals) getApplication()).bootup();

        // Get our root
        LayoutInflater appLayout = LayoutInflater.from(this);
        // The main view
        sqViewLeft = appLayout.inflate(R.layout.sqservice, null);
        sqViewRight = appLayout.inflate(R.layout.sqservice, null);
        sqViewLeftBottom = appLayout.inflate(R.layout.sqservice, null);
        sqViewRightBottom = appLayout.inflate(R.layout.sqservice, null);
        // insert the container
        Resources appResources = this.getResources();

        sqViewLeft.setBackgroundColor(Color.LTGRAY);
        sqViewLeft.setOnTouchListener(onTouch);

        sqViewRight.setBackgroundColor(Color.LTGRAY);
        sqViewRight.setOnTouchListener(onTouch);

        sqViewLeftBottom.setBackgroundColor(Color.LTGRAY);
        sqViewLeftBottom.setOnTouchListener(onTouch);

        sqViewRightBottom.setBackgroundColor(Color.LTGRAY);
        sqViewRightBottom.setOnTouchListener(onTouch);

        // applyTransparency( sqView, 1);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(sqViewLeft, makeOverlayParams());
        wm.addView(sqViewRight, makeOverlayParams());
        wm.addView(sqViewLeftBottom, makeOverlayParams());
        wm.addView(sqViewRightBottom, makeOverlayParams());
        initOrientation();
        // wm.addView( mView, makeOverlayParams() );
        // wm.addView( mExtraView, makeOverlayParams() );
    }

    OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    OnLongClickListener onlongClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    };

    @SuppressLint("ShowToast")
    OnTouchListener onTouch = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // Toast.makeText(serviceCotext,"inside Ontouchlistener >>", Toast.LENGTH_LONG).show();
            return false;
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

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

    private WindowManager.LayoutParams makeOverlayParams() {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        // save screen width/height
        Display display = wm.getDefaultDisplay();
        mScreenWidth = display.getWidth();
        mScreenHeight = display.getHeight();
        int displayHeight = mScreenHeight * 25 / 100;
        int displayWidth=20;
        return new WindowManager.LayoutParams(displayWidth, displayHeight, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
        // in adjustWindowParams system overlay windows are stripped of focus/touch events
        // WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
    }

    public void initOrientation() {
        // init x/y of buttons and save screen width/heigth
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        // save screen width/height
        Display display = wm.getDefaultDisplay();
        mScreenWidth = display.getWidth();
        mScreenHeight = display.getHeight();

        mOrientation = "portrait";
        if (mScreenWidth > mScreenHeight) {
            mOrientation = "landscape";
        }
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) sqViewLeft.getLayoutParams();
        params.y = mScreenHeight/5;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        wm.updateViewLayout(sqViewLeft, params);

        WindowManager.LayoutParams paramsLB = (WindowManager.LayoutParams) sqViewLeftBottom.getLayoutParams();
        paramsLB.y = mScreenHeight/4;
        paramsLB.gravity = Gravity.LEFT;
        wm.updateViewLayout(sqViewLeftBottom, paramsLB);

        WindowManager.LayoutParams paramsR = (WindowManager.LayoutParams) sqViewRight.getLayoutParams();
        paramsR.x = mScreenWidth;
        paramsR.y = mScreenHeight/5;
        paramsR.gravity = Gravity.TOP;
        wm.updateViewLayout(sqViewRight, paramsR);

        WindowManager.LayoutParams paramsRB = (WindowManager.LayoutParams) sqViewRightBottom.getLayoutParams();
        paramsRB.x = mScreenWidth;
        paramsRB.y = mScreenHeight/4;
        wm.updateViewLayout(sqViewRightBottom, paramsRB);
    }
}
