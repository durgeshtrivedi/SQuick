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

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;

import com.durgesh.R;
import com.durgesh.pref.SQPrefs;
import com.durgesh.util.Constants;

/**
 * Constructs shortcut intents.
 */
public class ShortcutIntentBuilder {

    private static final String[] CONTACT_COLUMNS = { Contacts.DISPLAY_NAME, Contacts.PHOTO_ID, };
    private static final int CONTACT_DISPLAY_NAME_COLUMN_INDEX = 0;
    private static final int CONTACT_PHOTO_ID_COLUMN_INDEX = 1;
    private static final String[] PHOTO_COLUMNS = { Photo.PHOTO, };
    private static final int PHOTO_PHOTO_COLUMN_INDEX = 0;
    private static final String PHOTO_SELECTION = Photo._ID + "=?";
    private OnShortcutIntentCreatedListener mListener = null;
    private final Context mContext;
    private int mIconSize;
    private final int mBorderWidth;
    private final int mBorderColor;
    private final String PREFIX = "CONTACT";

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
         * @param uri
         *            the original URI for which the shortcut intent has been created.
         * @param shortcutIntent
         *            resulting shortcut intent.
         */
        void onShortcutIntentCreated(Uri uri, Intent shortcutIntent);
    }

    public ShortcutIntentBuilder(Context context, OnShortcutIntentCreatedListener listener) {
        mContext = context;
        mListener = listener;
        final Resources r = context.getResources();
        mIconSize = r.getDimensionPixelSize(R.dimen.drawer_item_size);
        mBorderWidth = r.getDimensionPixelOffset(R.dimen.shortcut_icon_border_width);
        mBorderColor = r.getColor(R.color.shortcut_overlay_text_background);
    }

    public void createShortcutIntent(Uri dataUri, int shortcutSelector) {
        switch (shortcutSelector) {
        case Constants.PHONE_CALL:
            new PhoneNumberLoadingAsyncTask(dataUri, shortcutSelector).execute();
            break;
        case Constants.MESSAGE:
            new PhoneNumberLoadingAsyncTask(dataUri, shortcutSelector).execute();
            break;
        }
    }

    /**
     * An asynchronous task that loads name, photo and other data from the database.
     */
    private abstract class LoadingAsyncTask extends AsyncTask<Void, Void, Void> {
        protected Uri mUri;
        protected String mDisplayName;
        protected byte[] mBitmapData;
        protected long mPhotoId;
        int directCallorMessage;

        public LoadingAsyncTask(Uri uri, int callorMessage) {
            mUri = uri;
            directCallorMessage = callorMessage;
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadData();
            loadPhoto();
            return null;
        }

        protected abstract void loadData();

        private void loadPhoto() {
            if (mPhotoId == 0) {
                return;
            }

            ContentResolver resolver = mContext.getContentResolver();
            Cursor cursor = resolver.query(Data.CONTENT_URI, PHOTO_COLUMNS, PHOTO_SELECTION, new String[] { String.valueOf(mPhotoId) }, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        mBitmapData = cursor.getBlob(PHOTO_PHOTO_COLUMN_INDEX);
                    }
                } finally {
                    cursor.close();
                }
            }
        }
    }

    private final class PhoneNumberLoadingAsyncTask extends LoadingAsyncTask {
        private String mPhoneNumber;
        private int mPhoneType;
        private String mPhoneLabel;

        public PhoneNumberLoadingAsyncTask(Uri uri, int callorMessage) {
            super(uri, callorMessage);
        }

        @Override
        protected void loadData() {
            if (!readContactFromPref()) {
                Cursor cursor = mContext.getContentResolver().query(mUri, null, ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1", null, null);
                while (cursor.moveToNext()) {
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        mDisplayName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        mPhotoId = phones.getLong(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));
                        mPhoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        mPhoneType = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        mPhoneLabel = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
                        phones.close();
                        break;
                    }
                    cursor.close();
                    break;
                }
                SQPrefs.setSharedPreference(mContext, mUri.toString(), mDisplayName + ',' + mPhoneNumber + ',' + mPhotoId);
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mPhoneNumber == null) {
                mListener.onShortcutIntentCreated(null, null);
                return;
            }

            if (directCallorMessage == Constants.PHONE_CALL)
                createPhoneNumberShortcutIntent(mUri, mDisplayName, mBitmapData, mPhoneNumber, mPhoneType, mPhoneLabel, Intent.ACTION_CALL);
            else {
                createPhoneNumberShortcutIntent(mUri, mDisplayName, mBitmapData, mPhoneNumber, mPhoneType, mPhoneLabel, Intent.ACTION_SENDTO);
            }
        }

        /**
         * Store the contact info in the shared preference else the drawer view display as shaky because of the time taken to read the contact every
         * time
         * 
         * @return
         */
        boolean readContactFromPref() {
            String contactdata = SQPrefs.getSharedPreferenceAsStr(mContext, mUri.toString(), Constants.DEFAULTURI);
            if (contactdata != Constants.DEFAULTURI) {
                String[] contactinfo = contactdata.split(",");
                mDisplayName = contactinfo[0];
                mPhoneNumber = contactinfo[1];
                mPhotoId = Long.valueOf(contactinfo[2]);
                return true;
            }
            return false;
        }
    }

    private Bitmap getPhotoBitmap(byte[] bitmapData, String shortcutAction) {
        Bitmap bitmap;
        if (bitmapData != null) {
            bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length, null);
        } else if (shortcutAction.equals(Intent.ACTION_CALL)) {
            bitmap = ((BitmapDrawable) mContext.getResources().getDrawable(R.drawable.contact_picture_call)).getBitmap();
        } else {
            bitmap = ((BitmapDrawable) mContext.getResources().getDrawable(R.drawable.contact_picture_msg)).getBitmap();
        }
        return bitmap;
    }

    private void createPhoneNumberShortcutIntent(Uri uri, String displayName, byte[] bitmapData, String phoneNumber, int phoneType,
            String phoneLabel, String shortcutAction) {
        Bitmap bitmap = getPhotoBitmap(bitmapData, shortcutAction);

        Uri phoneUri;
        Intent shortcutIntent;
        if (Intent.ACTION_CALL.equals(shortcutAction)) {
            // Make the URI a direct tel: URI so that it will always continue to work
            phoneUri = Uri.fromParts(Constants.SCHEME_TEL, phoneNumber, null);
            // bitmap = generatePhoneNumberIcon(bitmap, phoneType, phoneLabel, R.drawable.badge_action_call);
            // setting display name in place for phonelabel
            bitmap = generatePhoneNumberIcon(bitmap, phoneType, displayName, R.drawable.badge_action_call);
            shortcutIntent = new Intent(shortcutAction, phoneUri);
        } else {
            phoneUri = Uri.fromParts(Constants.SCHEME_SMSTO, phoneNumber, null);
            // bitmap = generatePhoneNumberIcon(bitmap, phoneType, phoneLabel, R.drawable.badge_action_sms);
            // setting display name in place for phonelabel
            bitmap = generatePhoneNumberIcon(bitmap, phoneType, displayName, R.drawable.badge_action_sms);
            shortcutIntent = new Intent(shortcutAction, phoneUri);
        }

        shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, displayName);

        mListener.onShortcutIntentCreated(uri, intent);
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
    private Bitmap generatePhoneNumberIcon(Bitmap photo, int phoneType, String phoneLabel, int actionResId) {
        final Resources r = mContext.getResources();
        final float density = r.getDisplayMetrics().density;
        Bitmap phoneIcon = ((BitmapDrawable) r.getDrawable(actionResId)).getBitmap();

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
            workPaint.setColor(mBorderColor);
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

        // Draw the phone action icon as an overlay
        src.set(0, 0, phoneIcon.getWidth(), phoneIcon.getHeight());
        int iconWidth = icon.getWidth();
        dst.set(iconWidth - ((int) (20 * density)), -1, iconWidth, ((int) (19 * density)));
        dst.offset(-mBorderWidth, mBorderWidth);
        canvas.drawBitmap(phoneIcon, src, dst, photoPaint);
        return icon;
    }

}
