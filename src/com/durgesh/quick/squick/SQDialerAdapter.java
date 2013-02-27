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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.durgesh.R;

public class SQDialerAdapter extends BaseAdapter {
    private Context context;
    public SQDialerAdapter(SQDialerActivity sqDialerActivity) {
        // TODO Auto-generated constructor stub
        this.context =sqDialerActivity;
        
    }
    // Keep all Images in array
    public Integer[] dialerPosition = {
            R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher,R.drawable.ic_launcher
    };
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dialerPosition.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return dialerPosition[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        
        LayoutInflater li = LayoutInflater.from(context);
        if (itemView == null) {
            itemView = li.inflate(R.layout.shortcut_item, null);
        }
        ImageView imageView = (ImageView) itemView.findViewById(R.id.shortcut_item_img);
        imageView.setImageResource(dialerPosition[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
       // imageView.setLayoutParams(new GridView.LayoutParams(40, 40));
        return itemView;
    }
    
    
    /**
     * Listener for close button on MDV thumbnail view to show Discard, Save dialog box before closing modified document.
     */
    class SQDialerListener implements View.OnClickListener {

        @Override
        public void onClick(View imageButton) {
            imageButton.getTag();
            

        }
    }
}
