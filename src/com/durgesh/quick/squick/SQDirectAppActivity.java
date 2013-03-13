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
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.durgesh.R;
import com.durgesh.pref.SQPrefs;
import com.durgesh.util.Constants;

/**
 * Display all the shortcuts of apps to launch
 * 
 * @author durgesht
 */
public class SQDirectAppActivity extends Activity implements OnItemClickListener, OnItemLongClickListener {
    private View currentItem;
    public int selector;
    private static int currentPosition;

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View item, int position, long arg3) {
        currentItem = item;
        currentPosition = position;
        return launchAppSelector();

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View item, int position, long arg3) {
        String apppkg = SQPrefs.getSharedPrefAppAsStr(this, String.valueOf(position), Constants.DEFAULTURI);
        if (!apppkg.equals(Constants.DEFAULTURI)) {
            PackageManager pm = getPackageManager();
            ApplicationInfo appInfo;
            try {
                appInfo = pm.getApplicationInfo(apppkg, PackageManager.PERMISSION_GRANTED);
                ComponentName distantActivity = new ComponentName(appInfo.packageName, appInfo.name);
                Intent intent = new Intent();
                intent.setComponent(distantActivity);
                intent.setAction(Intent.ACTION_MAIN);
                startActivity(intent);
                finish();
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.shortcuts);
        selector = getIntent().getIntExtra(Constants.SUPERQUICK, Constants.DO_NOTHING);
        GridView gridView = (GridView) findViewById(R.id.shortcut_grid);
        gridView.setAdapter(new SQDirectAppAdapter(this));
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == Constants.APP) {
            SQPrefs.setSharedPreferenceApp(this, String.valueOf(currentPosition), data.getComponent().getPackageName());
            getAppIntent(data.getComponent().getPackageName(), currentItem);
        }
    }

    public void getAppIntent(String info, View view) {
        PackageManager pm = getPackageManager();
        ApplicationInfo appInfo;
        try {
            appInfo = pm.getApplicationInfo(info, PackageManager.PERMISSION_GRANTED);
            ImageView image = (ImageView) view.findViewById(R.id.shortcut_item_img);
            Drawable icon = pm.getApplicationIcon(info);
            Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
            image.setImageBitmap(bitmap);
            TextView text = (TextView) view.findViewById(R.id.shortcut_item_name);
            text.setText(appInfo.loadLabel(pm));
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        // return new Intent().setComponent(info).setAction(Intent.ACTION_MAIN);
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
        // rename Main to your class or activity
        startActivityForResult(pickIntent, selector);
        return true;

    }
}
