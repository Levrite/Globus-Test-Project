package com.levrite.globustestproject.Database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Created by Mike on 14.02.2017.
 */

public class DBContentProvider extends ContentProvider {

    private DBHelper databaseHelper;

    private static final int COMPANY = 1;
    private static final int COMPANY_ID = 3;

    private static final String AUTHORITY = "com.levrite.globustestproject.database";
    private static final String BASE_PATH = "database";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/companies";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, BASE_PATH, COMPANY);
        sUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", COMPANY_ID);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DBHelper(getContext());
        return false;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.COMPANY_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        long rowID = 0;

        switch (uriType) {
            case COMPANY:
                rowID = database.insert(DBHelper.COMPANY_TABLE_NAME, null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/#" + rowID);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int rowsDeleted = database.delete(DBHelper.COMPANY_TABLE_NAME, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {

        int newId = contentValues.getAsInteger("newID");
        int currentId = contentValues.getAsInteger("currentID");

        if(s != null){
            updateCurrentItem(newId, currentId);
        } else{
            updatePosition(newId, currentId);
        }


        return 0;
    }

    private void updatePosition(int newID, int currentID) {

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        String updateSQL = "update " + DBHelper.COMPANY_TABLE_NAME + " set " + DBHelper.COMPANY_COLUMN_POSITION + "=" + newID + " where " + DBHelper.COMPANY_COLUMN_POSITION + "=" + currentID;

        database.execSQL(updateSQL);

    }

    private void updateCurrentItem(int newID, int currentID){

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        String updateSQL = "update " + DBHelper.COMPANY_TABLE_NAME + " set " + DBHelper.COMPANY_COLUMN_POSITION + "=" + newID + " where " + DBHelper.COMPANY_COLUMN_ID + "=" + currentID;

        database.execSQL(updateSQL);

    }



}
