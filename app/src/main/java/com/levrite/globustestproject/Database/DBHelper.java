package com.levrite.globustestproject.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.levrite.globustestproject.Model.Company;

import java.util.ArrayList;

/**
 * Created by Mike on 13.02.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "database";
    private static final int DATABASE_VERSION = 1;

    public static final String COMPANY_TABLE_NAME = "company";
    public static final String COMPANY_COLUMN_ID = "_id";
    public static final String COMPANY_COLUMN_NAME = "name";
    public static final String COMPANY_COLUMN_POSITION = "position";

    private static final String CREATE_COMPANY_TABLE =
            "create table " + COMPANY_TABLE_NAME + " (" + COMPANY_COLUMN_ID + " integer primary key, "
            + COMPANY_COLUMN_NAME + " text, " + COMPANY_COLUMN_POSITION
            + " integer not null" + ");";


    public DBHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_COMPANY_TABLE);

        sqLiteDatabase.execSQL("insert into company values(0,'Harman', 0);");
        sqLiteDatabase.execSQL("insert into company values(1, 'Mera', 1);");
        sqLiteDatabase.execSQL("insert into company values(2, 'Globus', 2);");
        sqLiteDatabase.execSQL("insert into company values(3, 'Intel', 3);");
        sqLiteDatabase.execSQL("insert into company values(4, 'Yandex', 4);");
        sqLiteDatabase.execSQL("insert into company values(5, 'Telma', 5);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
