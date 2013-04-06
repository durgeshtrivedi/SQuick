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
import android.preference.PreferenceManager;

import com.durgesh.R;
import com.durgesh.quick.squick.SQDirectAppActivity;
import com.durgesh.util.Constants;


public class BottomRightView extends SQMainVeiw {

    public BottomRightView(Context context) {
        super(context);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String selector = settings.getString("right_bottom_bar", context.getResources().getString(R.string.pref_rightbottombar_title));
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
        updateView(1, Constants.SQ_BOTTOM_VIEW_POSITION_RATIO, -10);

    }
    
    public void launchShorcut()
    {
        Intent dialerActivity = new Intent(context, SQDirectAppActivity.class);
        dialerActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        dialerActivity.putExtra(Constants.SUPERQUICK, shortcutSelector);
        context.startActivity(dialerActivity);
    }
    
}
