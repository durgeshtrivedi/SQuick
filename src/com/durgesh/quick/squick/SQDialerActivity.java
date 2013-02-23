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
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.durgesh.R;
import com.durgesh.view.SQMainVeiw;

public class SQDialerActivity extends Activity {
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.sqdialer);
        GridView gridView = (GridView) findViewById(R.id.sq_dialer);
        gridView.setBackgroundResource(R.drawable.dialer_bg);
        // Instance of ImageAdapter Class

        gridView.setAdapter(new SQDialerAdapter(this));
        gridView.setOnTouchListener(new SQONDialer());
    }
    
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getIntExtra(SQMainVeiw.RESULT_CODE_FINISH_ACTIVITY, SQMainVeiw.RESULT_CODE_FINISH) != SQMainVeiw.RESULT_CODE_FINISH) {
            finish();
        }

    }

    final class SQONDialer implements OnItemClickListener, OnTouchListener {
        @Override
        public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
               // finish();
                return true;
            }
            return false;
        }
    }

}
