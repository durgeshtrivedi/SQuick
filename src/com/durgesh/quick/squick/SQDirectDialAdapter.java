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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.durgesh.R;
import com.durgesh.pref.SQPrefs;
import com.durgesh.quick.squick.ShortcutIntentBuilder.OnShortcutIntentCreatedListener;
import com.durgesh.util.Constants;

public class SQDirectDialAdapter extends BaseAdapter {
    private SQDirectDialActivity context;

    public SQDirectDialAdapter(SQDirectDialActivity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 16;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        final View currentitem;
        LayoutInflater li = LayoutInflater.from(context);
        if (itemView == null) {
            itemView = li.inflate(R.layout.shortcut_item, null);
        }
        currentitem = itemView;
        String uri = SQPrefs.getSharedPreferenceAsStr(context, String.valueOf(position), Constants.DEFAULTURI);
        if (!uri.equals(Constants.DEFAULTURI )) {
            ShortcutIntentBuilder builder = new ShortcutIntentBuilder(context, new OnShortcutIntentCreatedListener() {

                @Override
                public void onShortcutIntentCreated(Uri uri, Intent shortcutIntent) {
//                    String name = shortcutIntent.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
//                    ImageView image = (ImageView) currentitem.findViewById(R.id.shortcut_item_img);
//                    image.setImageBitmap((Bitmap) shortcutIntent.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON));
//                    TextView text = (TextView) currentitem.findViewById(R.id.shortcut_item_name);
//                    text.setText(name);
                    if (shortcutIntent == null) {
                        context.setContactImageDefault(currentitem);

                    } else {
                        context.setContactImage(currentitem, shortcutIntent);
                    }
                }
            });
            builder.createShortcutIntent(Uri.parse(uri), context.selector);
        } else {
            ImageView imageView = (ImageView) currentitem.findViewById(R.id.shortcut_item_img);
            imageView.setImageBitmap(((BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_contact_picture)).getBitmap());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        return itemView;
    }
}
