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
package com.durgesh.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

import com.durgesh.R;
import com.durgesh.quick.squick.SQDirectAppActivity;
import com.durgesh.quick.squick.SQDirectDialActivity;
import com.durgesh.util.Constants;

/**
 * Abstract Class to create the View of the app and to detect the fling on the View
 * 
 * @author durgesht
 */
public abstract class SQMainVeiw extends View implements OnTouchListener {
    public Context context;
    public SQMainVeiw sqView;
    private int sqScreenWidth;
    private int sqScreenHeight;
    private static float barSize = 15;
    private static final int SQ_VIEW_HEIGHT = 25;

    int shortcutSelector;
    WindowManager windowsmanger;

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

        private static final int SWIPE_MIN_DISTANCE = 3;
        private static final int SWIPE_THRESHOLD_VELOCITY = 10;

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
            return true;
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

    /**
     * Launch the shortcut selector base on the view on which the finger is swap
     */
    void launchShorcut() {
        switch (shortcutSelector) {
        case Constants.PHONE_CALL:
        case Constants.MESSAGE: {
            Intent dialerActivity = new Intent(context, SQDirectDialActivity.class);
            dialerActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            dialerActivity.putExtra(Constants.SUPERQUICK, shortcutSelector);
            context.startActivity(dialerActivity);
            break;
        }

        case Constants.CONTACT: {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("content://contacts/people/"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            context.startActivity(intent);
        }
            break;
        case Constants.APP: {
            Intent dialerActivity = new Intent(context, SQDirectAppActivity.class);
            dialerActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            dialerActivity.putExtra(Constants.SUPERQUICK, shortcutSelector);
            context.startActivity(dialerActivity);
            break;
        }
        }

    }

    private void inflateView() {
        windowsmanger = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        sqView = this;
        sqView.setBackgroundColor(Color.LTGRAY);
        sqView.setOnTouchListener(this);
        windowsmanger.addView(sqView, makeOverlayParams());
    }

    /**
     * Set the layout parameter for the View
     * 
     * @return {@link WindowManager.LayoutParams}
     */
    private WindowManager.LayoutParams makeOverlayParams() {
        return new WindowManager.LayoutParams(0, 0, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
    }

    /**
     * Update the View with Screen orientation
     * 
     * @param xAxis
     *            position of view on xAxis in case of Gravity LEFT it is not required thats why come as 0
     * @param ration
     *            give a ration to display the view at particular position(height) with screen height
     * @param gravity
     *            Gravity of the view to display on the screen
     */
    protected void updateView(int xAxis, int ration, int gravity) {
        // save screen width/height
        Display display = windowsmanger.getDefaultDisplay();
        sqScreenWidth = display.getWidth();
        sqScreenHeight = display.getHeight();
        // To give View the size according to Screen size take ration from screen size
        int displayHeight = sqScreenHeight * SQ_VIEW_HEIGHT / 100;
        WindowManager.LayoutParams paramsRB = (WindowManager.LayoutParams) sqView.getLayoutParams();
        paramsRB.x = xAxis == 0 ? 0 : sqScreenWidth;
        paramsRB.y = sqScreenHeight / ration;
        applyScaling(paramsRB);
        paramsRB.height = displayHeight;
        paramsRB.gravity = gravity != -10 ? gravity : Gravity.NO_GRAVITY;
        applyTransparency(paramsRB);
        windowsmanger.updateViewLayout(sqView, paramsRB);

    }

    /**
     * Set the Transparency for the view
     */
    private void applyTransparency(WindowManager.LayoutParams params) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        int transparency = settings.getInt("service_transparency", 0);
        float finalAlpha = (100f - transparency) / 100f;
        params.alpha = finalAlpha;
    }

    /**
     * Apply the scaling on the Bar
     * 
     * @param params
     */
    private void applyScaling(WindowManager.LayoutParams params) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String size = settings.getString("service_size", "medium");
        float buttonMult = 1;
        if (size.equals("huge")) {
            buttonMult = 2f;
        } else if (size.equals("large")) {
            buttonMult = 1.5f;
        } else if (size.equals("medium")) {
            // regular size for the system
            buttonMult = 1;
        } else if (size.equals("small")) {
            buttonMult = 0.75f;
        } else if (size.equals("tiny")) {
            buttonMult = 0.5f;
        }
        params.width = (int) (barSize * buttonMult);
    }

    public void viewSelector(String selector) {
        if (selector.equals(context.getResources().getString(R.string.pref_lefbar_title))) {
            shortcutSelector = Constants.PHONE_CALL;
        } else if (selector.equals(context.getResources().getString(R.string.pref_rightbare_title))) {
            shortcutSelector = Constants.MESSAGE;
        } else if (selector.equals(context.getResources().getString(R.string.pref_leftbottombar_title))) {
            shortcutSelector = Constants.CONTACT;
        } else if (selector.equals(context.getResources().getString(R.string.pref_rightbottombar_title))) {
            shortcutSelector = Constants.APP;
        }
    }

    /**
     * Return the Current view
     * 
     * @return
     */
    public SQMainVeiw getView() {
        return this;
    }

    /**
     * Return the Current view
     * 
     * @return
     */
    public SQMainVeiw setViewNull() {
        return sqView = null;
    }
}
