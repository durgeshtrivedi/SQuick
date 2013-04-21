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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.durgesh.R;
import com.durgesh.model.ContactIconHelper;
import com.durgesh.pref.SQPrefs;
import com.durgesh.quick.squick.SQDrawers.ItemClickListener;
import com.durgesh.quick.squick.ShortcutIntentBuilder.OnShortcutIntentCreatedListener;
import com.durgesh.util.Constants;

/**
 * Class to represent Direct Dial and Direct Message activity
 * 
 * @author durgesht
 */
public class SQDirectDialActivity extends SQDrawers implements ItemClickListener, OnShortcutIntentCreatedListener {
    private final OnShortcutIntentCreatedListener mListener = this;

    @Override
    public void onShortcutIntentCreated(Intent shortcutIntent) {
        if (shortcutIntent == null) {
            noNumberAlert();
        } else {
            setImage(shortcutIntent);
            addItem(this, shortcutIntent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View item, int position, long arg3) {
        currentItem = item;
        return launchContactSelector();

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View item, int position, long arg3) {
        Object[] tag = (Object[]) item.getTag();
        if (tag != null && tag[3] != null) {
            Intent intent = ((Intent) tag[4]).getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        fillAllDrawerItem(this, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            finish();
            return;
        }
        if (requestCode == Constants.MESSAGE || requestCode == Constants.PHONE_CALL) {
            Integer position = (Integer) ((Object[]) currentItem.getTag())[0];
            data.putExtra(Constants.POSITION, position);
            ShortcutIntentBuilder builder = new ShortcutIntentBuilder(this, mListener);
            builder.createShortcutIntent(data, requestCode == Constants.PHONE_CALL ? Intent.ACTION_CALL : Intent.ACTION_SENDTO);
        } else {
            finish();
        }

    }

    /**
     * Alert Dialog to show Phone number is missing
     */
    private void noNumberAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.phoneno_missing).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        // Create the AlertDialog object and return it
        builder.show();
        addDefaultImage(currentItem);
    }

    /**
     * Set Default Image for Empty Contact
     * 
     * @param view
     */
    public void setContactImageDefault(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.shortcut_item_img);
        imageView.setBackgroundResource(R.drawable.ic_contact_picture);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    /**
     * Set Image for Contact
     * 
     * @param view
     */
    public void setImage(Intent intent) {
        ImageView image = (ImageView) currentItem.findViewById(R.id.shortcut_item_img);
        image.setImageBitmap((Bitmap) intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON));
    }

    /**
     * Display the DialogBox having list of shortcuts
     * 
     * @param item
     */
    private boolean launchContactSelector() {
        ComponentName distantActivity;
        if (selector == Constants.PHONE_CALL)
            distantActivity = new ComponentName("com.android.contacts", "alias.DialShortcut");
        else {
            distantActivity = new ComponentName("com.android.contacts", "alias.MessageShortcut");
        }
        Intent intent = new Intent();
        intent.setComponent(distantActivity);
        intent.setAction(Intent.ACTION_PICK);
        intent.setAction(Intent.ACTION_CREATE_SHORTCUT);
        startActivityForResult(intent, selector);
        return true;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return sqTapListener.gestureDetector.onTouchEvent(event);
    }

    public View getView(final Object[] tag) {
        LayoutInflater li = LayoutInflater.from(this);
        View itemView = li.inflate(R.layout.drawer_item, null);
        Integer position = (Integer) tag[0];
        currentItem = itemView;
        // get associate item with type   message or call and position
        String contactinfo = SQPrefs.getSharedPreferenceAsStr(this,PREFIX+String.valueOf(position), Constants.DEFAULTURI);
        if (!contactinfo.equals(Constants.DEFAULTURI)) {
            ShortcutIntentBuilder builder = new ShortcutIntentBuilder(this, new OnShortcutIntentCreatedListener() {
                @Override
                public void onShortcutIntentCreated(Intent shortcutIntent) {
                    setImage(shortcutIntent);
                    // Represent a already existing drawer item
                    tag[3] = Constants.DRAWERITEM;
                    tag[4] = shortcutIntent;
                }
            });
            Intent intent = new Intent();
            intent.putExtra(Constants.POSITION, position);
            intent.putExtra(Constants.CONTACTINFO, contactinfo);
            builder.createShortcutIntent(intent, PREFIX);
        } else {
            addDefaultImage(itemView);
        }
        itemView.setTag(tag);
        setAnimation(itemView);
        return itemView;
    }
}