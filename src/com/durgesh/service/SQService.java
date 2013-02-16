/**Copyright (c) 2013 durgesh trivedi

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
package com.durgesh.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * THe main application Service which run all the time in the background and make the application live .
 * 
 * @author durgesht
 */
public class SQService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        SQGlobals.error("onCreate SoftKeysService");
        ((SQGlobals) getApplication()).bootup();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

}
