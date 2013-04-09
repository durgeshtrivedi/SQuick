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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.durgesh.R;
import com.durgesh.pref.SQPrefs;
import com.durgesh.quick.squick.SQDrawers.ItemClickListener;
import com.durgesh.quick.squick.ShortcutIntentBuilder.OnShortcutIntentCreatedListener;
import com.durgesh.util.Constants;

public class SQDirectDialActivity extends SQDrawers implements ItemClickListener, OnShortcutIntentCreatedListener {
    private View currentItem;
    public String contactUri;

    private static int currentPosition;
    private final OnShortcutIntentCreatedListener mListener = this;

    @Override
    public void onShortcutIntentCreated(Uri uri, Intent shortcutIntent) {
        if (shortcutIntent == null) {
            noNumberAlert();
        } else {
            SQPrefs.setSharedPreference(this, String.valueOf(currentPosition), contactUri);
            setContactImage(currentItem, shortcutIntent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View item, int position, long arg3) {
        currentItem = item;
        currentPosition = Integer.parseInt((String) item.getTag());
        return launchContactSelector();

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View item, int position, long arg3) {

        String uri = SQPrefs.getSharedPreferenceAsStr(this, String.valueOf(position), Constants.DEFAULTURI);
        if (!uri.equals(Constants.DEFAULTURI)) {
            ShortcutIntentBuilder builder = new ShortcutIntentBuilder(this, new OnShortcutIntentCreatedListener() {
                @Override
                public void onShortcutIntentCreated(Uri uri, Intent shortcutIntent) {
                    Intent intent = (Intent) shortcutIntent.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
                    intent.getData();
                    startActivity(intent);
                    finish();
                }
            });
            builder.createShortcutIntent(Uri.parse(uri), selector);
        }
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        SQDirectDialAdapter adapter = new SQDirectDialAdapter(this);
        fillAllDrawerItem(adapter);
        setOnItemListener(this);
        openDrawer();

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
        setContactImageDefault(currentItem);
    }

    /**
     * Set Default Image for Empty Contact
     * 
     * @param view
     */
    public void setContactImageDefault(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.shortcut_item_img);
        imageView.setImageBitmap(((BitmapDrawable) this.getResources().getDrawable(R.drawable.ic_contact_picture)).getBitmap());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    /**
     * Set Image for Contact
     * 
     * @param view
     */
    public void setContactImage(View view, Intent intent) {
        String name = intent.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
        ImageView image = (ImageView) view.findViewById(R.id.shortcut_item_img);
        image.setImageBitmap((Bitmap) intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON));
        // TextView text = (TextView) view.findViewById(R.id.shortcut_item_name);
        // text.setText(name);
    }

    /**
     * Display the DialogBox having list of shortcuts
     * 
     * @param item
     */
    private boolean launchContactSelector() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        startActivityForResult(intent, selector);
        return true;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return sqTapListener.gestureDetector.onTouchEvent(event);
    }

}