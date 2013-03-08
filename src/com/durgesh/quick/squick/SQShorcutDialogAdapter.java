/**
  Copyright (c) 2013 Durgesh Trivedi

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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.durgesh.R;

/**
 * Adapter to represent the list of shortcuts of app
 * 
 * @author durgesht
 */
class SQShorcutDialogAdapter extends BaseAdapter {
    Context context;
    final List<ApplicationInfo> sortcut;

    public SQShorcutDialogAdapter(Context context) {
        this.context = context;
        sortcut = getShortcutList();
        
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

    /**
     * Get the list of available shortcuts for apps
     * 
     * @return
     */
    private List<ApplicationInfo> getShortcutList() {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_CREATE_SHORTCUT, null);

        List<ApplicationInfo> appList = new ArrayList<SQShorcutDialogAdapter.ApplicationInfo>();
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