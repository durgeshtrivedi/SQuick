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

import com.durgesh.util.Constants;

import android.content.Context;
import android.widget.Toast;


public class BottomRightView extends SQMainVeiw {

    public BottomRightView(Context context) {
        super(context,Constants.APP);
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
        updateView(1, SQ_BOTTOM_VIEW_POSITION_RATIO, -10);

    }
}
