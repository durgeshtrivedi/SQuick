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
    public String contactUri;
    private final OnShortcutIntentCreatedListener mListener = this;

    @Override
    public void onShortcutIntentCreated(Uri uri, Intent shortcutIntent) {
        if (shortcutIntent == null) {
            noNumberAlert();
        } else {
            setImage(currentItem, shortcutIntent);
            //update the drawer item with new item  
            SQPrefs.setSharedPreference(this, String.valueOf(getCurrentPosition(currentItem)), contactUri);
            //add or update new item in to the drawer
            addItem(this,shortcutIntent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View item, int position, long arg3) {
        currentItem = item;
        return launchContactSelector();

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View item, int position, long arg3) {
        Object[] tag =(Object[])item.getTag();
           if(tag!=null && tag[3]!=null){
               Intent intent = ((Intent)tag[4]).getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
               startActivity(intent);
               finish();
           }
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        PREFIX = Constants.DIRECTCALLMSG;
        fillAllDrawerItem(this,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
        case Constants.PHONE_CALL:
        case Constants.MESSAGE: {
            contactUri = data.getData().toString();
            ShortcutIntentBuilder builder = new ShortcutIntentBuilder(this, mListener);
            builder.createShortcutIntent(data.getData(), requestCode);
            break;
        }

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
    public void setImage(View view, Intent intent) {
        ImageView image = (ImageView) view.findViewById(R.id.shortcut_item_img);
        image.setImageBitmap((Bitmap) intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON));
    }

    /**
     * Display the DialogBox having list of shortcuts
     * 
     * @param item
     */
    private boolean launchContactSelector() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, selector);
        return true;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return sqTapListener.gestureDetector.onTouchEvent(event);
    }

    public View getView(final Object[] tag) {
        final View currentitem;
        LayoutInflater li = LayoutInflater.from(this);
        View itemView = li.inflate(R.layout.drawer_item, null);
        Integer position = (Integer) tag[0];
        currentitem = itemView;
        String uri = SQPrefs.getSharedPreferenceAsStr(this, String.valueOf(position), Constants.DEFAULTURI);
        if (!uri.equals(Constants.DEFAULTURI)) {
            ShortcutIntentBuilder builder = new ShortcutIntentBuilder(this, new OnShortcutIntentCreatedListener() {
                @Override
                public void onShortcutIntentCreated(Uri uri, Intent shortcutIntent) {
                    setImage(currentitem, shortcutIntent);
                    // Represent a already existing drawer item
                    tag[3] = "DRAWERITEM";
                    tag[4]=shortcutIntent;
                }
            });
            builder.createShortcutIntent(Uri.parse(uri), selector);
        } else {
            addDefaultImage(currentitem);
        }
        itemView.setTag(tag);
        setAnimation(itemView);
        return itemView;
    }
}