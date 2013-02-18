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
package com.durgesh.quick;

import com.durgesh.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

public class SQCreator {

    public static int defaultIconSize( Context c ) {
        final Resources resources = c.getResources();
        return( (int) resources.getDimension( android.R.dimen.app_icon_size ) );
    }

    public static int scaledIconSize( Context c, int iconSize, float iconScale ) {
        if( iconSize == 0 ) {
            // default icon size
            iconSize = defaultIconSize( c );
        }
        
        iconSize = (int)(iconSize * iconScale);
        return( iconSize );
    }
    
    
    public static View createView( Context context, int viewSize, float scalefactor, String prefix, ViewGroup root ) {
    
        return createView( context, viewSize, scalefactor, prefix, root, false );
    }
    
    public static View createView( Context context, int viewSize, float scalefactor, String prefix, ViewGroup root,boolean value ) {
        return null;
    }
    
 // this assembles a generic button_container that can be inserted into whatever layout
    public static View createView( Context context, int iconSize, float iconScale, String prefix, ViewGroup root, int[] buttons ) {
        
        iconSize = scaledIconSize( context, iconSize, iconScale );
         Resources appResources =context.getResources();
         root.setBackgroundDrawable( appResources.getDrawable(R.drawable.service_container_background));
         return root;

    }

 // this will return a new drawable scaled to the new size, so you don't have to mutable the source
    public static Drawable resizeImage( Drawable dwawable, int dWidth, int dheight) {
        int width = dwawable.getIntrinsicWidth();
        int height = dwawable.getIntrinsicHeight();

        // catch colors/etc
        if( width < 1 ) {
            width = 1;
        }

        if( height < 1 ) {
            height = 1;
        }

        // if w/h is zero them it means to scale based on the non-zero one and
        // maintain aspect
        if( dWidth == 0 ) {
            dWidth = (int)( (float)dheight * width / height );
        }else if( dheight == 0 ) {
            dheight = (int)( (float)dWidth * height / width );
        }

        Bitmap b;
        if( dwawable instanceof BitmapDrawable ) {
            // I found that the resources are already bitmapdrawables so we can
            // do this,
            // I assume it it's not created from a bitmap like it's a shape or
            // something
            // then this won't work?
            b = ( (BitmapDrawable)dwawable ).getBitmap();
        }else {
            // this was the way more people said to do it, just render the
            // drawable to a canvas
            // backed by your dest bitmap. I assume if you're using a
            // bitmapdrawable
            // then this is slower than just pulling in the drawable backed
            // bitmap
            dwawable.mutate(); // we change the setbounds() so lets not mess with the
            // original
            b = Bitmap.createBitmap( width, height, Config.ARGB_8888 );
            Canvas c = new Canvas( b );
            dwawable.setBounds( 0, 0, width, height );
            dwawable.draw( c );
        }
        
        float scaleWidth = ((float) dWidth) / width;
        float scaleHeight = ((float) dheight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale( scaleWidth, scaleHeight);

        BitmapDrawable ret = new BitmapDrawable( Bitmap.createBitmap(b, 0, 0, width, height, matrix, true) );

        // copy tile mode
        if( dwawable instanceof BitmapDrawable ) {
            ret.setTileModeXY( ( (BitmapDrawable)dwawable ).getTileModeX(), ( (BitmapDrawable)dwawable ).getTileModeY() );
        }
        return ret;
    }

}
