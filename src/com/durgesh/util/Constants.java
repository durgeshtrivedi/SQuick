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

public class Constants {
    public static final String MIME_TYPE_VIDEO_CHAT = "vnd.android.cursor.item/video-chat-address";

    public static final String SCHEME_TEL = "tel";
    public static final String SCHEME_SMSTO = "smsto";
    public static final String SCHEME_MAILTO = "mailto";
    public static final String SCHEME_IMTO = "imto";
    public static final String SCHEME_SIP = "sip";
    public static final int DO_NOTHING = 0;
    public static final int PHONE_CALL = 1;
    public static final int MESSAGE = 2;
    public static final int CONTACT = 3;
    public static final int APP = 4;
    public static final String SUPERQUICK="superquick";
    public static final String DEFAULTURI = "NOURI";
    public static final String CONTACTPACKAGE="com.android.contact";

    /**
     * Log tag for performance measurement. To enable: adb shell setprop log.tag.ContactsPerf VERBOSE
     */
    public static final String PERFORMANCE_TAG = "ContactsPerf";

    /**
     * Log tag for enabling/disabling StrictMode violation log. To enable: adb shell setprop log.tag.ContactsStrictMode DEBUG
     */
    public static final String STRICT_MODE_TAG = "ContactsStrictMode";
}
