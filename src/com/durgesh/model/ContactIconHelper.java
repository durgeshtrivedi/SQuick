package com.durgesh.model;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Gets contact image icon using uniform resource locator.
 * 
 * @author durgesh trivedi
 */
public final class ContactIconHelper {

    public static final int MAX_THUMBNAIL_HEIGHT = 48;

    /**
     * Instantiates new <code>ImageHelper</code>.
     */
    private ContactIconHelper() {
    }

    /**
     * Sets Contacts image.
     * 
     * @param fileUri
     *            <code>Uri</code> of file
     * @param bitmap
     *            <code>Bitmap</code> for file
     * @param context
     *            Android <code>Context</code>
     */
    public static void setContactImage(String contactinfo, Bitmap bitmap, Context context) {
        Uri contentUri = getContentUri(contactinfo);

        try {
            OutputStream out = context.getContentResolver().openOutputStream(contentUri);
            try {
                if (bitmap != null) {
                    // make bitmap smaller
                  //  Bitmap scaledBmp = scaleBitmap(bitmap);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, out);

                }
            } finally {
                out.close();
            }
        } catch (IOException ex) {
            Log.e("Error In Set Contact Image ", ex.toString());
        }
    }

    /**
     * Gets Contact Image.
     * 
     * @param fileUri
     *            <code>Uri</code> of file
     * @param context
     *            Android <code>Context</code>
     * @return preview of file
     */
    public static Bitmap getContactImage(String contactinfo, Context context) {
        Bitmap bitmap = null;
        Uri contentUri = getContentUri(contactinfo);
        try {
            InputStream in = context.getContentResolver().openInputStream(contentUri);
            try {
                BitmapFactory.Options bfOptions = new BitmapFactory.Options();
                bfOptions.inDither = false; // Disable Dithering mode
                bfOptions.inPurgeable = true; // Tell to gc that whether it needs free memory, the Bitmap can be cleared
                bfOptions.inInputShareable = true; // Which kind of reference will be used to recover the Bitmap data after being clear, when it will
                                                   // be used in the future
                bfOptions.inTempStorage = new byte[32 * 1024];
                bitmap = BitmapFactory.decodeFileDescriptor(((FileInputStream) in).getFD(), null, bfOptions);
            } finally {
                in.close();
            }
        } catch (IOException ex) {
            Log.i("ImageHelper", "getContactImage", ex);
        }

        return bitmap;
    }

    private static Bitmap scaleBitmap(Bitmap origBitmap) {
        int width = origBitmap.getWidth();
        int height = origBitmap.getHeight();

        // calc aspect ratio of original image
        float aspectRatio = ((float) height / (float) width);
        if (aspectRatio > 1) {
            aspectRatio = 1f / aspectRatio;
        }

        // crop image if it is in portrait mode
        Bitmap croppedBitmap = null;
        boolean hasBeenCropped = false;
        if (width < height) {
            height = (int) (aspectRatio * width);
            croppedBitmap = Bitmap.createBitmap(origBitmap, 0, 0, width, height);
            hasBeenCropped = true;
        } else {
            croppedBitmap = origBitmap;
        }

        // calculate the scale
        float scale = ((float) MAX_THUMBNAIL_HEIGHT) / height;

        // create matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bitmap
        matrix.postScale(scale, scale);

        // recreate the new Bitmap
        Bitmap result = Bitmap.createBitmap(croppedBitmap, 0, 0, width, height, matrix, true);

        if (hasBeenCropped) {
            croppedBitmap.recycle();
        }

        return result;
    }

    /**
     * Gets content <code>Uri</code> of file
     * 
     * @param fileUri
     *            <code>Uri</code> of file
     * @return content <code>Uri</code>
     */
    private static Uri getContentUri(String filename) {
        return ContactIconProvider.BASE_URI.buildUpon().appendQueryParameter("file", filename).build();
    }

    /**
     * Delete contact image.
     * 
     * @param fileUri
     *            <code>Uri</code> of file
     * @param bitmap
     *            <code>Bitmap</code> for file
     * @param context
     *            Android <code>Context</code>
     */
    public static void deleteContactImage(String contactinfo, Context context) {
        int deleted = 0;

        Uri contentUri = getContentUri(contactinfo);
        deleted = context.getContentResolver().delete(contentUri, null, null);
        if (-1 == deleted) {
            Log.w("File Canot be deleted ", "File " + contactinfo + " cannot be found");
        }
    }
}
