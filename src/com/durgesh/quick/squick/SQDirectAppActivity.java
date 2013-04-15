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

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.durgesh.R;
import com.durgesh.pref.SQPrefs;
import com.durgesh.quick.squick.SQDrawers.ItemClickListener;
import com.durgesh.util.Constants;

/**
 * Display all the shortcuts of apps to launch
 * 
 * @author durgesht
 */
public class SQDirectAppActivity extends SQDrawers implements ItemClickListener {

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View item, int position, long arg3) {
        currentItem = item;
        return launchAppSelector();

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View item, int position, long arg3) {
        Object[] tag = (Object[]) item.getTag();
        if (tag != null && tag[3] != null) {
            Intent intent = (Intent) tag[4];
            String pkg = intent.getStringExtra("PKG");
            if (pkg != null) {
                PackageManager pm = getPackageManager();
                startActivity(pm.getLaunchIntentForPackage(pkg));
            } else {
                startActivity(intent);
            }
            finish();
        }
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        PREFIX = Constants.APPSHORTCUT;
        fillAllDrawerItem(this,0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == Constants.APP) {
            setImage(data.getComponent().getPackageName(), currentItem);
            SQPrefs.setSharedPreferenceApp(this, String.valueOf(getCurrentPosition(currentItem)), data.getComponent().getPackageName());
            //add or update new item in to the drawer
            addItem(this, data);
        }
    }

    /**
     * Set the image
     * 
     * @param info
     *            package info to get app image from pkg manager
     * @param view
     */
    public void setImage(String info, View view) {
        PackageManager pm = getPackageManager();
        try {
            ImageView image = (ImageView) view.findViewById(R.id.shortcut_item_img);
            Drawable icon = pm.getApplicationIcon(info);
            image.setBackgroundDrawable(icon);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public View getView(Object[] tag) {
        LayoutInflater li = LayoutInflater.from(this);
        View itemView = li.inflate(R.layout.drawer_item, null);
        Integer position = (Integer) tag[0];

        String apppkg = SQPrefs.getSharedPrefAppAsStr(this, String.valueOf(position), Constants.DEFAULTURI);
        if (!apppkg.equals(Constants.DEFAULTURI)) {
            setImage(apppkg, itemView);
            // Represent a already existing drawer item
            tag[3] = "DRAWERITEM";
            Intent intent = new Intent();
            intent.putExtra("PKG", apppkg);
            tag[4] = intent;
        } else {
            addDefaultImage(itemView);
        }
        setAnimation(itemView);
        itemView.setTag(tag);
        return itemView;
    }

    /**
     * Launch the application selector dialog
     * 
     * @param item
     */
    private boolean launchAppSelector() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        Intent pickIntent = new Intent(Intent.ACTION_PICK_ACTIVITY);
        pickIntent.putExtra(Intent.EXTRA_INTENT, mainIntent);
        startActivityForResult(pickIntent, selector);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return sqTapListener.gestureDetector.onTouchEvent(event);
    }
}
