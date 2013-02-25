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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.durgesh.R;

public class SQDialerActivity extends Activity implements OnItemClickListener, OnItemSelectedListener, OnItemLongClickListener {
    Context context = this;

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        ShortCutAdapter sortcut = new ShortCutAdapter(getShortcutList());
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Short Cut").setAdapter(sortcut, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int selectedNew) {
                dialog.dismiss();

            }

        }).setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> view, View app, int arg2, long arg3) {
                ApplicationInfo appInfo = (ApplicationInfo) app.getTag();
                Intent intent = new Intent();
                ComponentName distantActivity = new ComponentName(appInfo.getPackageName(), appInfo.getClassName());
                intent.setComponent(distantActivity);
                intent.setAction(Intent.ACTION_MAIN);
                context.startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        }).create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.sqdialer);
        GridView gridView = (GridView) findViewById(R.id.sq_dialer);
        gridView.setBackgroundResource(R.drawable.dialer_bg);
        // Instance of ImageAdapter Class

        gridView.setAdapter(new SQDialerAdapter(this));
        gridView.setOnTouchListener(new SQONDialer());
        gridView.setOnItemSelectedListener(this);
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
            // TODO Auto-generated method stub
            return sortcut.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return sortcut.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getDialogView(sortcut, position);
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

    private View getDialogView(final List<ApplicationInfo> sortcut, int position) {
        ApplicationInfo info = sortcut.get(position);
        LinearLayout dialogLayout = new LinearLayout(context);
        ImageView imageView = new ImageView(context);
        // System.out.println(info.);
        imageView.setImageDrawable(info.getAppIcon());
        imageView.setPadding(5, 5, 5,5);
        imageView.setLayoutParams(new GridView.LayoutParams(info.getAppIcon().getBounds().height(), info.getAppIcon().getBounds().height()));
        TextView txt = new TextView(context);
        txt.setText(info.appName);
        dialogLayout.addView(imageView);
        dialogLayout.addView(txt);
        dialogLayout.setTag(info);
        return dialogLayout;
    }
}
