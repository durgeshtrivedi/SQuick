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

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.WindowManager;

import com.durgesh.R;

/**
 * Abstract Class to create the View of the app and to detect the fling on the View
 * 
 * @author durgesht
 */
public abstract class SQMainVeiw extends View implements OnTouchListener {
    public Context context;
    public View sqView;
    private int sqScreenWidth;
    private int sqScreenHeight;
    private static final int SQ_VIEW_WIDTH = 20;
    private static final int SQ_VIEW_HEIGHT = 25;

    // Position TOP view LEFT and RIGHT
    public final int SQ_TOP_VIEW_POSITION_RATIO = 5;
    public final int SQ_BOTTOM_VIEW_POSITION_RATIO = 4;

    public SQMainVeiw(Context context) {
        super(context);
        this.context = context;
        inflateView();
    }

    private final GestureDetector gdt = new GestureDetector(new GestureListener());

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        gdt.onTouchEvent(event);
        return true;
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 6;
        private static final int SWIPE_THRESHOLD_VELOCITY = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                onRightToLeft();
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                onLeftToRight();
                return true;
            }
            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                onBottomToTop();
                return true;
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                onTopToBottom();
                return true;
            }
            return false;
        }
    }

    public abstract void onRightToLeft();

    public abstract void onLeftToRight();

    public abstract void onBottomToTop();

    public abstract void onTopToBottom();

    /**
     * update the view position on the screen
     */
    public abstract void updateViewParameter();

    private void inflateView() {
        LayoutInflater appLayout = LayoutInflater.from(context);
        sqView = appLayout.inflate(R.layout.sqservice, null);
        sqView.setBackgroundColor(Color.LTGRAY);
        sqView.setOnTouchListener(this);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.addView(sqView, makeOverlayParams());
    }

    /**
     * Set the layout parameter for the SQView
     * 
     * @return {@link WindowManager.LayoutParams}
     */
    private WindowManager.LayoutParams makeOverlayParams() {
        return new WindowManager.LayoutParams(0, 0, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
        // in adjustWindowParams system overlay windows are stripped of focus/touch events
        // WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
    }

    /**
     * update the View with Screen orientation
     * 
     * @param xAxis
     *            position of view on xAxis in case of Gravity LEFT it is not required thats why come as 0
     * @param ration
     *            give a ration to display the view at particular position(height) with screen height
     * @param gravity
     *            Gravity of the view to display on the screen
     */
    protected void updateView(int xAxis, int ration, int gravity) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

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
     * return the Current view
     * 
     * @return
     */
    public View getView() {
        return sqView;
    }

}
