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
package com.durgesh.quick.squick;

import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.durgesh.R;
import com.durgesh.pref.SQPrefs;
import com.durgesh.util.Constants;

/**
 * Adapter to hold the list all the applications shortcuts
 * 
 * @author durgesht
 */
public class SQDirectAppAdapter extends BaseAdapter {
    private SQDirectAppActivity context;

    public SQDirectAppAdapter(SQDirectAppActivity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 21;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(context);
        if (itemView == null) {
            itemView = li.inflate(R.layout.shortcut_item, null);
        }
        // Setting position of item from the main list
        itemView.setTag(String.valueOf(position));
        String apppkg = SQPrefs.getSharedPrefAppAsStr(context, String.valueOf(position), Constants.DEFAULTURI);
        if (!apppkg.equals(Constants.DEFAULTURI)) {
            context.setAppShortCuts(apppkg, itemView);
        } else {
            ImageView imageView = (ImageView) itemView.findViewById(R.id.shortcut_item_img);
            imageView.setImageBitmap(((BitmapDrawable) context.getResources().getDrawable(R.drawable.superquick_gray)).getBitmap());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        TranslateAnimation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        anim.setDuration(1500);
        itemView.startAnimation(anim);
        return itemView;
    }
}
