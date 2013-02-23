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
import android.view.Gravity;
import android.widget.Toast;


public class BottomLeftView extends SQMainVeiw {

    public BottomLeftView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onRightToLeft() {
        Toast mToast = Toast.makeText(context.getApplicationContext(), "Long Press BottomLeftView", Toast.LENGTH_SHORT);
        mToast.show();

    }

    @Override
    public void onLeftToRight() {
        Toast mToast = Toast.makeText(context.getApplicationContext(), "Long Press BottomLeftView", Toast.LENGTH_SHORT);
        mToast.show();

    }

    @Override
    public void onBottomToTop() {
        Toast mToast = Toast.makeText(context.getApplicationContext(), "Long Press BottomLeftView", Toast.LENGTH_SHORT);
        mToast.show();

    }

    @Override
    public void onTopToBottom() {
        Toast mToast = Toast.makeText(context.getApplicationContext(), "Long Press BottomLeftView", Toast.LENGTH_SHORT);
        mToast.show();

    }

    @Override
    public void updateViewParameter() {
        updateView(0, SQ_BOTTOM_VIEW_POSITION_RATIO, Gravity.LEFT);

    }
}
