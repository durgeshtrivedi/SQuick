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
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.Data;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;

import com.durgesh.R;
import com.durgesh.model.ContactIconHelper;
import com.durgesh.pref.SQPrefs;
import com.durgesh.util.Constants;

/**
 * Constructs shortcut intents.
 */
public class ShortcutIntentBuilder {

    private OnShortcutIntentCreatedListener mListener = null;
    private final Context mContext;
    private int mIconSize;
    private final int mBorderWidth;
    private final int mBorderColor;
    protected Intent contactinfo;
    String directCallorMessage;
    /**
     * This is a hidden API of the launcher in JellyBean that allows us to disable the animation that it would usually do, because it interferes with
     * our own animation for QuickContact
     */
    public static final String INTENT_EXTRA_IGNORE_LAUNCH_ANIMATION = "com.android.launcher.intent.extra.shortcut.INGORE_LAUNCH_ANIMATION";

    /**
     * Listener interface.
     */
    public interface OnShortcutIntentCreatedListener {

        /**
         * Callback for shortcut intent creation.
         * 
         * @param shortcutIntent
         *            resulting shortcut intent.
         */
        void onShortcutIntentCreated(Intent shortcutIntent);
    }

    public ShortcutIntentBuilder(Activity context, OnShortcutIntentCreatedListener listener) {
        mContext = context;
        mListener = listener;
        final Resources r = context.getResources();
        mIconSize = r.getDimensionPixelSize(R.dimen.drawer_item_size);
        mBorderWidth = r.getDimensionPixelOffset(R.dimen.shortcut_icon_border_width);
        mBorderColor = r.getColor(R.color.shortcut_overlay_text_background);
    }

    public void createShortcutIntent(Intent contactinfo, String shortcutSelector) {
        directCallorMessage = shortcutSelector;
        this.contactinfo = contactinfo;
        new PhoneNumberLoadingAsyncTask(contactinfo, shortcutSelector);
    }

    /**
     * An asynchronous task that loads name, photo and other data from the database.
     */
    private abstract class LoadingAsyncTask {
        protected String mDisplayName;
        protected Bitmap mBitmapData;

        public LoadingAsyncTask(Intent contact, String callorMessage) {
            loadData();
        }

        protected abstract void loadData();

    }

    private final class PhoneNumberLoadingAsyncTask extends LoadingAsyncTask {
        private String mPhoneNumber;
        String position;

        public PhoneNumberLoadingAsyncTask(Intent contactinfo, String callorMessage) {
            super(contactinfo, callorMessage);
        }

        @Override
        protected void loadData() {
            // Get current position of item where to update
            position = String.valueOf(contactinfo.getIntExtra(Constants.POSITION, 0));
            if (!readContactFromPref()) {
                mDisplayName = contactinfo.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
                mPhoneNumber = getPhoneNumber(contactinfo);
                if (mPhoneNumber == null || mDisplayName == null || mPhoneNumber.equals("null") || mDisplayName.equals("null")) {
                    mListener.onShortcutIntentCreated(null);
                    return;
                }
                // associate item with type message or call and position
                SQPrefs.setSharedPreference(mContext, directCallorMessage + position, mDisplayName + ',' + mPhoneNumber);
                mBitmapData = (Bitmap) contactinfo.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON);
                // associate image with type message or call and position
                ContactIconHelper.setContactImage(directCallorMessage + position, mBitmapData, mContext);
            }
            // Need to see how to overlay contact name on image
            createPhoneNumberShortcutIntent(mDisplayName, mBitmapData, mPhoneNumber, directCallorMessage);
        }

        /**
         * Store the contact info in the shared preference else the drawer view display as shaky because of the time taken to read the contact every
         * time
         * 
         * @return
         */
        boolean readContactFromPref() {
            String info = contactinfo.getStringExtra(Constants.CONTACTINFO);
            if (info != null) {
                String[] contactinfo = info.split(",");
                mDisplayName = contactinfo[0];
                mPhoneNumber = contactinfo[1];
                mBitmapData = ContactIconHelper.getContactImage(directCallorMessage + position, mContext);
                return true;
            }
            return false;
        }
    }

    private String getPhoneNumber(Intent contactinfo) {
        Uri uri = ((Intent) contactinfo.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT)).getData();
        String number = uri.toString();
        // if there is space between numbers remove that
        number = number.replace("%20", "");
        if (directCallorMessage == Intent.ACTION_CALL) {
            if (number.contains("tel:%2B")) {
                number = number.substring(7); // In place of "+" sign has string "%2B" tel:%2B so sub string from 7th position
                number = "+" + number;
            } else {
                number = number.substring(4); // tel: so sub string from 4th position
            }

        } else if (directCallorMessage == Intent.ACTION_SENDTO) {
            if (number.contains("smsto:%2B")) {
                number = number.substring(9); // In place of "+" sign has string "%2B" smsto:%2B so sub string from 9th position
                number = "+" + number;
            } else {
                number = number.substring(6); // smsto: so sub string from 6th position
            }
        }
        return number;
    }

    private void createPhoneNumberShortcutIntent(String displayName, Bitmap bitmap, String phoneNumber, String shortcutAction) {
        Uri phoneUri;
        Intent shortcutIntent;
        if (Intent.ACTION_CALL.equals(shortcutAction)) {
            // Make the URI a direct tel: URI so that it will always continue to work
            phoneUri = Uri.fromParts(Constants.SCHEME_TEL, phoneNumber, null);
            // display name as overlay on contact icon
            bitmap = generatePhoneNumberIcon(bitmap, displayName, R.drawable.badge_action_call);
            shortcutIntent = new Intent(shortcutAction, phoneUri);
        } else {
            phoneUri = Uri.fromParts(Constants.SCHEME_SMSTO, phoneNumber, null);
            // display name as overlay on contact icon
            bitmap = generatePhoneNumberIcon(bitmap, displayName, R.drawable.badge_action_sms);
            shortcutIntent = new Intent(shortcutAction, phoneUri);
        }

        shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, displayName);

        mListener.onShortcutIntentCreated(intent);
    }

    private void drawBorder(Canvas canvas, Rect dst) {
        // Darken the border
        final Paint workPaint = new Paint();
        workPaint.setColor(mBorderColor);
        workPaint.setStyle(Paint.Style.STROKE);
        // The stroke is drawn centered on the rect bounds, and since half will be drawn outside the
        // bounds, we need to double the width for it to appear as intended.
        workPaint.setStrokeWidth(mBorderWidth * 2);
        canvas.drawRect(dst, workPaint);
    }

    /**
     * Generates a phone number shortcut icon. Adds an overlay describing the type of the phone number, and if there is a photo also adds the call
     * action icon.
     */
    private Bitmap generatePhoneNumberIcon(Bitmap photo, String phoneLabel, int actionResId) {
        final Resources r = mContext.getResources();
        final float density = r.getDisplayMetrics().density;

        // Setup the drawing classes
        Bitmap icon = Bitmap.createBitmap(mIconSize, mIconSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(icon);

        // Copy in the photo
        Paint photoPaint = new Paint();
        photoPaint.setDither(true);
        photoPaint.setFilterBitmap(true);
        Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());
        Rect dst = new Rect(0, 0, mIconSize, mIconSize);
        canvas.drawBitmap(photo, src, dst, photoPaint);

        drawBorder(canvas, dst);

        // Create an overlay for the phone number type
        // CharSequence overlay = Phone.getTypeLabel(r, phoneType, phoneLabel);
        CharSequence overlay = phoneLabel;

        if (overlay != null) {
            TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
            textPaint.setTextSize(r.getDimension(R.dimen.shortcut_overlay_text_size));
            textPaint.setColor(r.getColor(R.color.textColorIconOverlay));
            textPaint.setShadowLayer(4f, 0, 2f, r.getColor(R.color.textColorIconOverlayShadow));

            final FontMetricsInt fmi = textPaint.getFontMetricsInt();

            // First fill in a darker background around the text to be drawn
            final Paint workPaint = new Paint();
            workPaint.setColor(Color.BLACK);
            workPaint.setStyle(Paint.Style.FILL);
            final int textPadding = r.getDimensionPixelOffset(R.dimen.shortcut_overlay_text_background_padding);
            final int textBandHeight = (fmi.descent - fmi.ascent) + textPadding * 2;
            dst.set(0 + mBorderWidth, mIconSize - textBandHeight, mIconSize - mBorderWidth, mIconSize - mBorderWidth);
            canvas.drawRect(dst, workPaint);

            final float sidePadding = mBorderWidth;
            overlay = TextUtils.ellipsize(overlay, textPaint, mIconSize - 2 * sidePadding, TruncateAt.END);
            final float textWidth = textPaint.measureText(overlay, 0, overlay.length());
            canvas.drawText(overlay, 0, overlay.length(), (mIconSize - textWidth) / 2, mIconSize - fmi.descent - textPadding, textPaint);
        }
        return icon;
    }

}
