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

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.durgesh.quick.squick.ShortcutIntentBuilder.OnShortcutIntentCreatedListener;
import com.durgesh.util.Constants;

public class SQDirectDialActivity extends Activity implements OnItemClickListener, OnItemLongClickListener, OnShortcutIntentCreatedListener {
    private View currentItem;
    public int selector;
    private static int currentPosition;
    private final OnShortcutIntentCreatedListener mListener = this;

    @Override
    public void onShortcutIntentCreated(Uri uri, Intent shortcutIntent) {
        String name = shortcutIntent.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
        ImageView image = (ImageView) currentItem.findViewById(R.id.shortcut_item_img);
        image.setImageBitmap((Bitmap) shortcutIntent.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON));
        TextView text = (TextView) currentItem.findViewById(R.id.shortcut_item_name);
        text.setText(name);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View item, int position, long arg3) {
        currentItem = item;
        currentPosition = position;
        return launchContactSelector();

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View item, int position, long arg3) {

        String uri = SQPrefs.getSharedPreferenceAsStr(this, String.valueOf(position), Constants.DEFAULTURI);
        ShortcutIntentBuilder builder = new ShortcutIntentBuilder(this, new OnShortcutIntentCreatedListener() {
            @Override
            public void onShortcutIntentCreated(Uri uri, Intent shortcutIntent) {
                startActivity((Intent) shortcutIntent.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT));
            }
        });
        builder.createShortcutIntent(Uri.parse(uri), selector);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.shortcuts);
        selector = getIntent().getIntExtra(Constants.SUPERQUICK, Constants.DO_NOTHING);
        GridView gridView = (GridView) findViewById(R.id.shortcut_grid);
        gridView.setAdapter(new SQDirectDialAdapter(this));
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {

        case Constants.PHONE_CALL: {
            SQPrefs.setSharedPreference(this, String.valueOf(currentPosition), ((Intent) data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT))
                    .getData().toString());
            ShortcutIntentBuilder builder = new ShortcutIntentBuilder(this, mListener);
            builder.createShortcutIntent(((Intent) data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT)).getData(), requestCode);
            break;
        }
        case Constants.MESSAGE: {
            SQPrefs.setSharedPreference(this, String.valueOf(currentPosition), ((Intent) data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT))
                    .getData().toString());
            ShortcutIntentBuilder builder = new ShortcutIntentBuilder(this, mListener);
            builder.createShortcutIntent(((Intent) data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT)).getData(), requestCode);
            break;
        }
        case Constants.CONTACT: {
            SQPrefs.setSharedPreference(this, String.valueOf(currentPosition), ((Intent) data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT))
                    .getData().toString());
            ShortcutIntentBuilder builder = new ShortcutIntentBuilder(this, mListener);
            builder.createShortcutIntent(((Intent) data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT)).getData(), requestCode);
            break;
        }
        default: {
//            SQPrefs.setSharedPreference(this, String.valueOf(currentPosition), ((Intent) data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT))
//                    .getData().toString());
//            ShortcutIntentBuilder builder = new ShortcutIntentBuilder(this, mListener);
//            builder.createShortcutIntent(((Intent) data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT)).getData(), requestCode);
        }
        }
    }

    /**
     * display the DialogBox having list of shortcuts
     * 
     * @param item
     */
    private boolean launchContactSelector() {
        ApplicationInfo appInfo = getContactActivity();
        ComponentName distantActivity = new ComponentName(appInfo.getPackageName(), appInfo.getClassName());
        Intent intent = new Intent();
        intent.setComponent(distantActivity);
        intent.setAction(Intent.ACTION_PICK_ACTIVITY);
        intent.setAction(Intent.ACTION_CREATE_SHORTCUT);
        startActivityForResult(intent, selector);
        return true;

    }

    /**
     * Get the list of available shortcuts for apps
     * 
     * @return
     */
    private ApplicationInfo getContactActivity() {
        PackageManager pm = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_CREATE_SHORTCUT, null);
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED);
        ApplicationInfo appInfo = new ApplicationInfo();
        for (ResolveInfo rInfo : list) {
            if (rInfo.activityInfo.applicationInfo.packageName!=null  && rInfo.activityInfo.applicationInfo.packageName.contains(Constants.CONTACTPACKAGE)) {
                appInfo.setAppName(rInfo.loadLabel(pm));
                appInfo.setPackageName(rInfo.activityInfo.packageName);
                appInfo.setAppIcon(rInfo.loadIcon(pm));
                appInfo.setClassName(rInfo.activityInfo.targetActivity);
                break;
            }
        }

        return appInfo;
    }

    /**
     * Class to store the Appinfo like Icon, name App activity
     * 
     * @author durgesht
     */
    class ApplicationInfo {
        private Drawable appIcon;
        private String activityName;
        private String packageName;
        private CharSequence appName;
        private String className;

        public Drawable getAppIcon() {
            return appIcon;
        }

        public void setAppIcon(Drawable appIcon) {
            this.appIcon = appIcon;
        }

        public String getActivityName() {
            return activityName;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String class1) {
            this.className = class1;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        public CharSequence getAppName() {
            return appName;
        }

        public void setAppName(CharSequence charSequence) {
            this.appName = charSequence;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

    }

}
