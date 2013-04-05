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
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ViewAnimator;

import com.durgesh.R;
import com.durgesh.util.Constants;

public class BottomLeftView extends SQMainVeiw {

    public BottomLeftView(Context context) {
        super(context);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String selector = settings.getString("left_bottom_bar", context.getResources().getString(R.string.pref_leftbottombar_title));
        viewSelector(selector);
    }

    @Override
    public void onRightToLeft() {
        launchShorcut();
    }

    @Override
    public void onLeftToRight() {
      launchShorcut();
    }

    @Override
    public void onBottomToTop() {
     launchShorcut();
    }

    @Override
    public void onTopToBottom() {

      launchShorcut();
       
    }

    @Override
    public void updateViewParameter() {
        updateView(0, Constants.SQ_BOTTOM_VIEW_POSITION_RATIO, Gravity.LEFT);

    }
    
//    public void showDrawer()
//    {
//        View  newView;
//        LayoutInflater li = LayoutInflater.from(context);
//            newView = li.inflate(R.layout.sqleftdrawer, null);
//        windowsmanger = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        newView.setBackgroundColor(Color.LTGRAY);
//      //  newView.setOnTouchListener(this);
//        ViewAnimator mViewAnimator;
//        mViewAnimator = new ViewAnimator(context);
//       
//        mViewAnimator.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.pull_in_from_left));
//        mViewAnimator.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.push_out_to_left));            
//        mViewAnimator.addView(newView);
//        mViewAnimator.showNext();
//        // bringToFront(mViewAnimator);
//       
//        windowsmanger.addView(mViewAnimator, makeOverlayParams());
//       // overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
//        
//    }
    
}
