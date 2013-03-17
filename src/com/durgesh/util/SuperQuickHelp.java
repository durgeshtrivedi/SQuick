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
package com.durgesh.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.durgesh.R;

public class SuperQuickHelp extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpdoc);

        String url = "file:///android_asset/";

        Intent i = getIntent();
        if (i != null) {
            Bundle ex = i.getExtras();
            String type = ex.getString("type");
            if (type.equals("sqhelp")) {
                url += "sqhelp.html";
            }else if(type.equals("disclaimer")){
                url += "disclaimer.html";
            }
        }

        WebView mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(false);
        mWebView.setWebViewClient(new ViewClient());
        mWebView.loadUrl(url);
    }

    public void closeHelp(View v) {
        this.finish();
    }

    /* this fixes the browser opening when you click links */
    public class ViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("file://")) {
                return false;
            } else {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            }
        }
    }
}
