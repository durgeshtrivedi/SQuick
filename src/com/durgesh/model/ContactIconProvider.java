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

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Content Provider class for contact-image and implements methods performing file operations.
 * 
 * @author durgesh trivedi
 */
public class ContactIconProvider extends ContentProvider {

    /** Content <code>Uri</code> of file preview provider. */
    public static final Uri BASE_URI = Uri.parse(ContentResolver.SCHEME_CONTENT + "://" + "com.durgesh.quick");

    /** Maximum cache size for application specific cache directory. */
    private static final int MAX_CACHE_SIZE = 50;

    /** Name of the directory where file preview will be stored */
    public static final String DIRECTORY_NAME = "contact-image";

    /** Directory for storing file previews. */
    private File directory;

    /** List of the files for preview. */
    private final List<String> fileList = new LinkedList<String>();

    /*
     * (non-Javadoc)
     * @see android.content.ContentProvider#onCreate()
     */
    @Override
    public boolean onCreate() {
        directory = new File(getContext().getCacheDir(), DIRECTORY_NAME);
        if (!directory.exists()) {
            directory.mkdir();
        }

        File[] files = directory.listFiles();
        Arrays.sort(files, new Comparator<File>() {

            @Override
            public int compare(File o1, File o2) {
                Long d1 = new Long(o1.lastModified());
                Long d2 = new Long(o2.lastModified());
                return d2.compareTo(d1);
            }
        });
        for (File file : files) {
            fileList.add(file.getName());
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * @see android.content.ContentProvider#openFile(android.net.Uri, java.lang.String)
     */
    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        String source = uri.getQueryParameter("file");
        String previewFileName = getDigest(source) + ".png";
        File previewFile = new File(directory, previewFileName);
        int access;
        synchronized (fileList) {
            if (mode.equals("r")) {
                access = ParcelFileDescriptor.MODE_READ_ONLY;
                renovateFile(previewFile);
            } else if (mode.equals("w")) {
                access = ParcelFileDescriptor.MODE_WRITE_ONLY;
                recreateFile(previewFile);
            } else {
                throw new FileNotFoundException();
            }
            fileList.remove(previewFileName);
            fileList.add(0, previewFileName);
            deleteOldFiles();
        }
        return ParcelFileDescriptor.open(previewFile, access);
    }

    /**
     * Changes last modified time of file.
     * 
     * @param file
     *            <code>File</code>
     * @throws FileNotFoundException
     *             file not found exception
     */
    private static void renovateFile(File file) throws FileNotFoundException {
        if (!file.setLastModified(System.currentTimeMillis())) {
            throw new FileNotFoundException();
        }
    }

    /**
     * Deletes existing file and recreate it.
     * 
     * @param file
     *            <code>File</code>
     * @throws FileNotFoundException
     *             file not found exception
     */
    private static void recreateFile(File file) throws FileNotFoundException {
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException ex) {
            throw new FileNotFoundException();
        }
    }

    /**
     * Deletes old files from cache.
     */
    private void deleteOldFiles() {
        while (fileList.size() > MAX_CACHE_SIZE) {
            String fileName = fileList.remove(fileList.size() - 1);
            File fileToDelete = new File(directory, fileName);
            fileToDelete.delete();
        }
    }

    /**
     * Gets string using MD5 Message-Digest Algorithm (MD5 is widely used cryptographic hash function that produces a 128-bit (16-byte) hash value).
     * 
     * @param s
     *            string
     * @return string
     */
    private static String getDigest(String s) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            byte[] bytes = s.getBytes("UTF-8");
            return toHex(digest.digest(bytes));
        } catch (NoSuchAlgorithmException ex) {
            throw new AssertionError(ex);
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }
    }

    /**
     * Converts bytes to hex.
     * 
     * @param bytes
     *            bytes to be converted to hex
     * @return hex string
     */
    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
     * (non-Javadoc)
     * @see android.content.ContentProvider#getType(android.net.Uri)
     */
    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
     * (non-Javadoc)
     * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
     * (non-Javadoc)
     * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int returnValue;
        String source = uri.getQueryParameter("file");
        String fileName = getDigest(source) + ".png";
        File fileToDelete = new File(directory, fileName);
        boolean deleted = fileToDelete.delete();
        if (deleted) {
            fileList.remove(fileName);
            returnValue = 0;
        } else {
            returnValue = -1;
        }
        return returnValue;
    }

    /*
     * (non-Javadoc)
     * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
