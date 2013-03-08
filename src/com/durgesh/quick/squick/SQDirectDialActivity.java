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
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.durgesh.R;
import com.durgesh.quick.squick.SQShorcutDialogAdapter.ApplicationInfo;
import com.durgesh.quick.squick.ShortcutIntentBuilder.OnShortcutIntentCreatedListener;

public class SQDirectDialActivity extends Activity implements OnItemClickListener, OnItemLongClickListener, OnShortcutIntentCreatedListener {
    GridView dialogGridView;
    View currentItem;
    private static final int PHONE_CALL = 1;
    private final OnShortcutIntentCreatedListener mListener = this;

    @Override
    public void onShortcutIntentCreated(Uri uri, Intent shortcutIntent) {
        String name = shortcutIntent.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
        ImageView image = (ImageView) currentItem.findViewById(R.id.shortcut_item_img);
        image.setImageBitmap((Bitmap) shortcutIntent.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON));
        TextView text = (TextView) currentItem.findViewById(R.id.shortcut_item_name);
        text.setText(name);
        startActivity((Intent) shortcutIntent.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View item, int position, long arg3) {
        currentItem = item;
        return createShorsutsDialog();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View item, int arg2, long arg3) {

    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.shortcuts);
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
        if (requestCode == PHONE_CALL) {
            ShortcutIntentBuilder builder = new ShortcutIntentBuilder(this, mListener);
            builder.createPhoneNumberShortcutIntent(((Intent) data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT)).getData());
        }
    }  
    
   
    /**
     * display the DialogBox having list of shortcuts
     * 
     * @param item
     */
    private boolean createShorsutsDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        GridView dialogLayout = (GridView) li.inflate(R.layout.shortcut_dlg_grid, null);
        dialogLayout.setBackgroundColor(Color.WHITE);
        dialogLayout.setAdapter(new SQShorcutDialogAdapter(this));
        final AlertDialog dialog = new AlertDialog.Builder(this).setTitle(R.string.shortcut_dlg_name).setView(dialogLayout).create();
        dialogLayout.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, final View app, int position, long arg3) {
                dialog.dismiss();
                ApplicationInfo appInfo = (ApplicationInfo) app.getTag();
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
        lp.width =450;
        lp.height = LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        return true;
    }

    
}
