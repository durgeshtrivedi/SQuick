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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.durgesh.R;

public class SQDirectDialAdapter extends BaseAdapter {
    private Context context;

    public SQDirectDialAdapter(Context context) {
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

        LayoutInflater li = LayoutInflater.from(context);
        if (itemView == null) {
            itemView = li.inflate(R.layout.shortcut_item, null);
        }
        ImageView imageView = (ImageView) itemView.findViewById(R.id.shortcut_item_img);
        imageView.setImageBitmap(((BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_contact_picture)).getBitmap());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return itemView;
    }
}
