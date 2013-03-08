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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.durgesh.R;
import com.durgesh.quick.squick.ShortcutIntentBuilder.OnShortcutIntentCreatedListener;

public class SQDialerActivity extends Activity implements OnItemClickListener, OnItemLongClickListener, OnShortcutIntentCreatedListener {
    Activity context = this;
    GridView dialogGridView;
    View currentItem;
    Bitmap shortcutIcon;
    private static final int PHONE_CALL = 1;

    @Override
    public void onShortcutIntentCreated(Uri uri, Intent shortcutIntent) {
        String name = shortcutIntent.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
        ImageView image = (ImageView) currentItem.findViewById(R.id.shortcut_item_img);
        image.setImageBitmap((Bitmap) shortcutIntent.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON));
        // image.setImageBitmap(shortcutIcon);
        TextView text = (TextView) currentItem.findViewById(R.id.shortcut_item_name);
        text.setText(name);
        startActivity((Intent) shortcutIntent.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT));
    }

    private final OnShortcutIntentCreatedListener mListener = this;

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View item, int arg2, long arg3) {
        ShortCutAdapter sortcut = new ShortCutAdapter(getShortcutList());
        LayoutInflater li = LayoutInflater.from(this);
        GridView dialogLayout = (GridView) li.inflate(R.layout.shortcut_dlg_grid, null);
        currentItem = item;
        final AlertDialog dialog = new AlertDialog.Builder(this).setTitle(R.string.shortcut_dlg_name).setView(dialogLayout).create();
        dialogLayout.setAdapter(sortcut);
        dialogLayout.setBackgroundColor(Color.WHITE);
        dialogLayout.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, final View app, int arg2, long arg3) {
                dialog.dismiss();

                ApplicationInfo appInfo = (ApplicationInfo) app.getTag();
                // Intent intent = new Intent(Intent.ACTION_PICK_ACTIVITY,ContactsContract.Contacts.CONTENT_URI);
                ComponentName distantActivity = new ComponentName(appInfo.getPackageName(), appInfo.getClassName());
                Intent intent = new Intent();
                intent.setComponent(distantActivity);
                intent.setAction(Intent.ACTION_PICK_ACTIVITY);
                intent.setAction(Intent.ACTION_CREATE_SHORTCUT);
                startActivityForResult(intent, PHONE_CALL);
            }
        });

        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = 450;
        lp.height = LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View item, int arg2, long arg3) {

    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.shortcuts);
        GridView gridView = (GridView) findViewById(R.id.shortcut_grid);
        gridView.setAdapter(new SQDialerAdapter(this));
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
    }

    final class SQONDialer implements OnTouchListener, OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // finish();
                return true;
            }
            return false;
        }
    }

    final class ShortCutAdapter extends BaseAdapter {

        final List<ApplicationInfo> sortcut;

        public ShortCutAdapter(final List<ApplicationInfo> sortcuts) {
            sortcut = sortcuts;
        }

        @Override
        public int getCount() {
            return sortcut.size();
        }

        @Override
        public Object getItem(int position) {
            return sortcut.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getDialogGridView(convertView, sortcut, position);
        }

    }

    public List<ApplicationInfo> getShortcutList() {
        PackageManager pm = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_CREATE_SHORTCUT, null);

        List<ApplicationInfo> appList = new ArrayList<SQDialerActivity.ApplicationInfo>();
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED);

        for (ResolveInfo rInfo : list) {
            if (rInfo.activityInfo.targetActivity != null) {
                ApplicationInfo appInfo = new ApplicationInfo();
                appInfo.setAppName(rInfo.loadLabel(pm));
                appInfo.setPackageName(rInfo.activityInfo.applicationInfo.packageName);
                appInfo.setAppIcon(rInfo.loadIcon(pm));
                appInfo.setClassName(rInfo.activityInfo.targetActivity);
                String pkg = rInfo.activityInfo.applicationInfo.packageName;
                if (pm.getLaunchIntentForPackage(pkg) == null) {
                    continue;
                }
                appList.add(appInfo);
            }
        }

        return appList;
    }

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

    private View getDialogGridView(View dialogLayout, final List<ApplicationInfo> sortcut, int position) {

        ApplicationInfo info = sortcut.get(position);
        LayoutInflater li = LayoutInflater.from(context);
        if (dialogLayout == null) {
            dialogLayout = li.inflate(R.layout.shortcut_dlg_item, null);
        }
        ImageView imageView = (ImageView) dialogLayout.findViewById(R.id.app_icon);
        imageView.setImageDrawable(info.getAppIcon());
        TextView textView = (TextView) dialogLayout.findViewById(R.id.app_name);
        textView.setText(info.getAppName());
        dialogLayout.setTag(info);
        return dialogLayout;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == PHONE_CALL) {
            ShortcutIntentBuilder builder = new ShortcutIntentBuilder(this, mListener);
            builder.createPhoneNumberShortcutIntent(((Intent) data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT)).getData());
        }
    }
}
