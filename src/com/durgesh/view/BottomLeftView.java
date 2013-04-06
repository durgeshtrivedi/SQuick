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
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.Gravity;

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
    
    public void launchShorcut()
    {
         Intent intent = new Intent();
         intent.setAction(Intent.ACTION_VIEW);
         intent.setData(Uri.parse("content://contacts/people/"));
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
         context.startActivity(intent);
    }
    
}
