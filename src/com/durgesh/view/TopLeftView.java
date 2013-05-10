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
import android.preference.PreferenceManager;
import android.view.Gravity;

import com.durgesh.R;
import com.durgesh.util.Constants;

/**
 * Represent the TopLeftView
 * 
 * @author durgesht
 */
public class TopLeftView extends SQMainVeiw {

    public TopLeftView(Context context) {
        super(context);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String selector = settings.getString("left_bar", context.getResources().getString(R.string.pref_lefbar_title));
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

    public void updateViewParameter() {
        updateView(0, Constants.SCREEN_RATIO, Gravity.LEFT | Gravity.TOP);
    }
}
